/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.chromium.ConnectionLogger;
import org.eclipse.wst.jsdt.chromium.debug.core.model.BreakpointSynchronizer;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ConnectionLoggerImpl;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ConsolePseudoProcess;
import org.eclipse.wst.jsdt.chromium.debug.core.model.DebugTargetImpl;
import org.eclipse.wst.jsdt.chromium.debug.core.model.IPredefinedSourceWrapProvider;
import org.eclipse.wst.jsdt.chromium.debug.core.model.JavascriptVmEmbedder;
import org.eclipse.wst.jsdt.chromium.debug.core.model.JavascriptVmEmbedderFactory;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.core.model.NamedConnectionLoggerFactory;
import org.eclipse.wst.jsdt.chromium.debug.core.model.SourceWrapSupport;
import org.eclipse.wst.jsdt.chromium.debug.core.model.VProjectWorkspaceBridge;
import org.eclipse.wst.jsdt.chromium.debug.core.model.WorkspaceBridge;
import org.eclipse.wst.jsdt.chromium.debug.ui.listeners.JavaScriptChangeListener;
import org.eclipse.wst.jsdt.chromium.debug.ui.listeners.LaunchTerminateListener;
import org.eclipse.wst.jsdt.chromium.util.Destructable;
import org.eclipse.wst.jsdt.chromium.util.DestructingGuard;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;

/**
 * Mimic {@link LaunchTypeBase} for V8 debugger launch
 * 
 * @author "Gorkem Ercan (gercan)"
 * @author "Ilya Buziuk (ibuziuk)"
 */
final public class NodeDebugConnector {
	private static final String PROJECT_EXPLORER_ID = "org.eclipse.ui.navigator.ProjectExplorer"; //$NON-NLS-1$
	private final ILaunchConfiguration configuration;
	private final ILaunch launch;

	public NodeDebugConnector(ILaunchConfiguration configuration, final ILaunch launch) {
		this.configuration = configuration;
		this.launch = launch;
	}

	boolean attach() throws CoreException {

		String host = configuration.getAttribute(NodeConstants.ATTR_HOST_FIELD, NodeConstants.DEFAULT_HOST);
		int port = Integer.parseInt(
				configuration.getAttribute(NodeConstants.ATTR_PORT_FIELD, String.valueOf(NodeConstants.DEFAULT_PORT)));

		NamedConnectionLoggerFactory consoleFactory = NO_CONNECTION_LOGGER_FACTORY;

		if (configuration.getAttribute(NodeConstants.ATTR_ADD_NETWORK_CONSOLE_FIELD, false)) {
			consoleFactory = new NamedConnectionLoggerFactory() {
				public ConnectionLogger createLogger(String title) {
					return createConsoleAndLogger(launch, true, title);
				}
			};
		}

		SourceWrapSupport sourceWrapSupport = createSourceWrapSupportFromConfig(configuration);

		JavascriptVmEmbedder.ConnectionToRemote remoteServer = JavascriptVmEmbedderFactory.connectToStandalone(host,
				port, consoleFactory);

		remoteServer.selectVm();

		DestructingGuard destructingGuard = new DestructingGuard();
		Destructable lauchDestructor = new Destructable() {
			public void destruct() {
				if (!launch.hasChildren()) {
					DebugPlugin.getDefault().getLaunchManager().removeLaunch(launch);
				}
			}
		};
		try {

			destructingGuard.addValue(lauchDestructor);

			WorkspaceBridge.Factory bridgeFactory = new VProjectWorkspaceBridge.FactoryImpl(configuration.getName());

			final DebugTargetImpl target = new DebugTargetImpl(launch, bridgeFactory, sourceWrapSupport,
					BreakpointSynchronizer.Direction.MERGE);

			Destructable targetDestructor = new Destructable() {
				public void destruct() {
					try {
						target.terminate();
					} catch (DebugException e) {
						NodePlugin.logError(e);
					}
				}
			};

			destructingGuard.addValue(targetDestructor);

			boolean attached = DebugTargetImpl.attach(target, remoteServer, destructingGuard,
					OPENING_VIEW_ATTACH_CALLBACK, new NullProgressMonitor());

			if (!attached) {
				// Cancel pressed.
				return false;
			}

			launch.addDebugTarget(target);

			destructingGuard.discharge();
			
			addListeners(launch);
			
			return true;

		} finally {
			destructingGuard.doFinally();
		}
	}
	
	private void addListeners(ILaunch launch) {
		IResourceChangeListener listener = new JavaScriptChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
		DebugPlugin.getDefault().addDebugEventListener(new LaunchTerminateListener(launch, listener));
	}
	

	private static ConnectionLogger createConsoleAndLogger(final ILaunch launch, final boolean addLaunchToManager,
			final String title) {
		final ConsolePseudoProcess.Retransmitter consoleRetransmitter = new ConsolePseudoProcess.Retransmitter();

		// This controller is responsible for creating ConsolePseudoProcess only
		// on logStarted call. Before this ConnectionLoggerImpl with all it fields should stay
		// garbage-collectible, because connection may not even start.
		ConnectionLoggerImpl.LogLifecycleListener consoleController = new ConnectionLoggerImpl.LogLifecycleListener() {
			private final AtomicBoolean alreadyStarted = new AtomicBoolean(false);

			public void logClosed() {
				consoleRetransmitter.processClosed();
			}

			public void logStarted(ConnectionLoggerImpl connectionLogger) {
				boolean res = alreadyStarted.compareAndSet(false, true);
				if (!res) {
					throw new IllegalStateException();
				}
				new ConsolePseudoProcess(launch, title, consoleRetransmitter,
						connectionLogger.getConnectionTerminate());
				consoleRetransmitter.startFlushing();
				if (addLaunchToManager) {
					// Active the launch (again if it has already been removed)
					DebugPlugin.getDefault().getLaunchManager().addLaunch(launch);
				}
			}
		};

		return new ConnectionLoggerImpl(consoleRetransmitter, consoleController);
	}
	
	private static final NamedConnectionLoggerFactory NO_CONNECTION_LOGGER_FACTORY = new NamedConnectionLoggerFactory() {
		public ConnectionLogger createLogger(String title) {
			return null;
		}
	};

	private static SourceWrapSupport createSourceWrapSupportFromConfig(ILaunchConfiguration config)
			throws CoreException {
		List<IPredefinedSourceWrapProvider.Entry> entries = LaunchParams.PredefinedSourceWrapperIds
				.resolveEntries(config);
		List<SourceWrapSupport.Wrapper> wrappers = new ArrayList<SourceWrapSupport.Wrapper>(entries.size());
		for (IPredefinedSourceWrapProvider.Entry en : entries) {
			wrappers.add(en.getWrapper());
		}
		return new SourceWrapSupport(wrappers);
	}
	
	/**
	 * Brings up the "Project Explorer" view in the active workbench window.
	 */
	private static final Runnable OPENING_VIEW_ATTACH_CALLBACK = new Runnable() {
		public void run() {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
					if (window == null) {
						if (workbench.getWorkbenchWindowCount() == 1) {
							window = workbench.getWorkbenchWindows()[0];
						}
					}
					if (window != null) {
						try {
							window.getActivePage().showView(PROJECT_EXPLORER_ID);
						} catch (PartInitException e) {
							// ignore
						}
					}
				}
			});
		}
	};
	
}