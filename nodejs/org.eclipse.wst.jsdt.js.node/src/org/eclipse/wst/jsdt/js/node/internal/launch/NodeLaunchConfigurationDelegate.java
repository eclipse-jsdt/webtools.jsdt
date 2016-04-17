/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.launch;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.JSRunnerConfiguration;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;
import org.eclipse.wst.jsdt.js.node.internal.util.LaunchConfigurationUtil;
import org.eclipse.wst.jsdt.js.node.runtime.NodeJsRuntimeType;
import org.eclipse.wst.jsdt.launching.ExecutionArguments;

/**
 * Launch configuration delegate for node application
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeLaunchConfigurationDelegate extends LaunchConfigurationDelegate {
    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.
     * eclipse.debug.core.ILaunchConfiguration, java.lang.String,
     * org.eclipse.debug.core.ILaunch,
     * org.eclipse.core.runtime.IProgressMonitor)
     */

    @Override
	public void launch(ILaunchConfiguration configuration, final String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(NLS.bind("{0}...", new String[]{configuration.getName()}), 3); //$NON-NLS-1$
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}

		try {
			String project = configuration.getAttribute(NodeConstants.ATTR_APP_PROJECT, NodeConstants.EMPTY);
			String mainTypeName = configuration.getAttribute(NodeConstants.ATTR_APP_PATH, NodeConstants.EMPTY);
			IJSRunner runner = getJSRunner(configuration, mode);

			File workingDir = verifyWorkingDirectory(configuration);
			String workingDirName = null;
			if (workingDir != null) {
				workingDirName = workingDir.getAbsolutePath();
			}

			// Environment variables
			String[] envp= getEnvironment(configuration);

			// Program & VM arguments
			String pgmArgs = getProgramArguments(configuration);
			String nodeArgs = getNodeArguments(configuration);
			if (mode.equals(ILaunchManager.DEBUG_MODE)) {
				String debugPort = configuration.getAttribute(NodeConstants.ATTR_PORT_FIELD,
						String.valueOf(NodeConstants.DEFAULT_PORT));
				Boolean isBreakEnable = configuration.getAttribute(NodeConstants.ATTR_BREAK_FIELD, true);

				// Adding the "debug" flag first
				if (isBreakEnable) {
					nodeArgs = "--debug-brk=" + debugPort + " " + nodeArgs; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					nodeArgs = "--debug=" + debugPort + " " + nodeArgs; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			ExecutionArguments execArgs = new ExecutionArguments(nodeArgs, pgmArgs);

			// Create VM config
			JSRunnerConfiguration runConfig = new JSRunnerConfiguration(mainTypeName);
			runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
			runConfig.setEnvironment(envp);
			runConfig.setJSRuntimeArguments(execArgs.getVMArgumentsArray());
			runConfig.setWorkingDirectory(workingDirName);

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}

			// done the verification phase
			monitor.worked(1);

			// Launch the configuration - 1 unit of work
			runner.run(runConfig, launch, monitor);
			
			// Launch Chromium V8 
			if(mode.equals(ILaunchManager.DEBUG_MODE)){
			    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(NodeConstants.CHROMIUM_LAUNCH_CONFIGURATION_TYPE_ID);
				IContainer container = null;
				final ILaunchConfigurationWorkingCopy chromiumLaunch = type.newInstance(container, configuration.getName());

				chromiumLaunch.setAttribute(NodeConstants.CHROMIUM_DEBUG_HOST,
						configuration.getAttribute(NodeConstants.ATTR_HOST_FIELD, NodeConstants.DEFAULT_HOST));

				chromiumLaunch.setAttribute(NodeConstants.CHROMIUM_DEBUG_PORT, Integer.parseInt(configuration
						.getAttribute(NodeConstants.ATTR_PORT_FIELD, String.valueOf(NodeConstants.DEFAULT_PORT))));

				chromiumLaunch.setAttribute(NodeConstants.ADD_NETWORK_CONSOLE,
						configuration.getAttribute(NodeConstants.ATTR_ADD_NETWORK_CONSOLE_FIELD, false));

				chromiumLaunch.setAttribute(NodeConstants.BREAKPOINT_SYNC_DIRECTION, NodeConstants.MERGE);

				chromiumLaunch.setAttribute(NodeConstants.SOURCE_LOOKUP_MODE, NodeConstants.EXACT_MATCH);
				
				chromiumLaunch.setAttribute(NodeConstants.ATTR_APP_PROJECT, project);
				
				
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						DebugUITools.launch(chromiumLaunch, mode);
					}
				});

			}

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}
		}
		finally {
			monitor.done();
		}

    }

	public IJSRunner getJSRunner(ILaunchConfiguration configuration, String mode) throws CoreException {
		IJSRuntimeInstall runtimeInstall = verifyJSRuntimeInstall(configuration);
		IJSRunner runner = runtimeInstall.getJSRunner(mode);
		if (runner == null) {
			return null;
		}
		return runner;
	}

	public IJSRuntimeInstall verifyJSRuntimeInstall(ILaunchConfiguration configuration) throws CoreException {
		return getJSRuntimeInstall(configuration);
	}

	public IJSRuntimeInstall getJSRuntimeInstall(ILaunchConfiguration configuration) throws CoreException {
		// As for now, always run using the default runtime install for Node.js 
		return JSRuntimeManager.getDefaultRuntimeInstall(NodeJsRuntimeType.NODE_JS_RUNTIME_TYPE_ID);
	}

	public File verifyWorkingDirectory(ILaunchConfiguration configuration)
				throws CoreException {
		File workingPath = null;
		String workingDirectory = configuration.getAttribute(NodeConstants.ATTR_WORKING_DIRECTORY, NodeConstants.EMPTY);
		if (workingDirectory.equals(NodeConstants.EMPTY)) {
			workingDirectory = configuration.getAttribute(NodeConstants.ATTR_APP_PROJECT, NodeConstants.EMPTY);
		}
		if (workingDirectory.length() > 0){
			workingDirectory = LaunchConfigurationUtil.resolveValue(workingDirectory);
			if(workingDirectory != null){
				workingPath = new File(workingDirectory);
			}
		}
		return workingPath;
	}

	public String[] getEnvironment(ILaunchConfiguration configuration) throws CoreException {
		return DebugPlugin.getDefault().getLaunchManager().getEnvironment(configuration);
	}

	public String getProgramArguments(ILaunchConfiguration configuration)
				throws CoreException {
		String appArguments = configuration.getAttribute(NodeConstants.ATTR_APP_ARGUMENTS, NodeConstants.EMPTY);
		return VariablesPlugin.getDefault().getStringVariableManager()
				.performStringSubstitution(appArguments);
	}

	public String getNodeArguments(ILaunchConfiguration configuration) throws CoreException {
		String nodeArguments = configuration.getAttribute(NodeConstants.ATTR_NODE_ARGUMENTS, NodeConstants.EMPTY);
		String args = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(nodeArguments);
		return args;
	}
}
