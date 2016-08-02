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
package org.eclipse.wst.jsdt.chromium.debug.js.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.js.JSDebuggerPlugin;
import org.eclipse.wst.jsdt.chromium.debug.js.Messages;
import org.eclipse.wst.jsdt.chromium.debug.js.launchers.WIPLauncher;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ChromiumUtil;
import org.eclipse.wst.jsdt.chromium.debug.js.util.LaunchConfigurationUtil;
import org.eclipse.wst.jsdt.chromium.debug.js.util.WorbenchResourceUtil;
import org.eclipse.wst.jsdt.core.runtime.IJSRunner;
import org.eclipse.wst.jsdt.core.runtime.JSRunnerConfiguration;
import org.eclipse.wst.jsdt.launching.ExecutionArguments;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
	
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
		
		String projectName = configuration.getAttribute(LaunchConstants.ATTR_APP_PROJECT, (String) null);
		String resourceRelativePath = configuration.getAttribute(LaunchConstants.ATTR_APP_PROJECT_RELATIVE_PATH, (String) null);
		String url = configuration.getAttribute(LaunchConstants.ATTR_CHROMIUM_URL, (String) null);
		String debugContainerUrl = configuration.getAttribute(LaunchConstants.ATTR_BASE_URL, (String) null);
		
		int port = Integer.parseInt(configuration.getAttribute(LaunchParams.CHROMIUM_DEBUG_PORT,
				String.valueOf(LaunchConstants.DEFAULT_PORT)));
		
		try {
			IProject project = WorbenchResourceUtil.getProject(projectName);
			
			IJSRunner runner = LaunchConfigurationUtil.getJSRunner(configuration, mode);

			if (runner == null) {
				throw new CoreException(new Status(IStatus.ERROR, JSDebuggerPlugin.PLUGIN_ID,
						Messages.LAUNCH_CONFIGURATION_NO_RUNNER_FOUND_ERROR));
			}
			
			// Environment variables
			String[] envp= LaunchConfigurationUtil.getEnvironment(configuration);
			
			// Program arguments
			String pgmArgs = LaunchConfigurationUtil.getProgramArguments(configuration);
			
			// Chrome / Chromium arguments
			String chromiumArgs = LaunchConfigurationUtil.getChromiumArguments(configuration);

			if (mode.equals(ILaunchManager.DEBUG_MODE)) {
				
				chromiumArgs += "--remote-debugging-port=" + port + " "; //$NON-NLS-1$ //$NON-NLS-2$
				chromiumArgs += "--user-data-dir=" + ChromiumUtil.getChromiumUserDataDir() + " "; //$NON-NLS-1$ //$NON-NLS-2$
				chromiumArgs += "--disable-component-extensions-with-background-pages "; //$NON-NLS-1$
				chromiumArgs += url;
				
			}
			
			ExecutionArguments execArgs = new ExecutionArguments(chromiumArgs, pgmArgs);
			JSRunnerConfiguration runConfig = new JSRunnerConfiguration(project.getLocation().toOSString());
			runConfig.setProgramArguments(execArgs.getProgramArgumentsArray());
			runConfig.setEnvironment(envp);
			runConfig.setJSRuntimeArguments(execArgs.getVMArgumentsArray());
			runConfig.setWorkingDirectory(project.getLocation().toOSString());

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}
			
			// done the verification phase
			monitor.worked(1);
			
			IProcess chromiumProcess = runner.run(runConfig, launch, monitor);
			
			if (chromiumProcess == null) return;
			
			// Attaching WIP debugger process
			WIPLauncher.launch(project, resourceRelativePath, debugContainerUrl, port);

		} finally {
			monitor.done();
		}
	}

}
