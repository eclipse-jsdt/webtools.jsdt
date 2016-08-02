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
package org.eclipse.wst.jsdt.chromium.debug.js.launchers;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams.PredefinedSourceWrapperIds;
import org.eclipse.wst.jsdt.chromium.debug.core.util.MementoFormat;
import org.eclipse.wst.jsdt.chromium.debug.js.JSDebuggerPlugin;
import org.eclipse.wst.jsdt.chromium.debug.js.launch.LaunchConstants;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class WIPLauncher {

	public static void launch(IProject project, String resourceRelativePath, String url, int port) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(LaunchConstants.WIP_LAUNCH_CONFIGURATION_TYPE_ID);
		
		String projectName = project.getName();

		ILaunchConfigurationWorkingCopy chromiumLaunch = type.newInstance(project, projectName);
		
		chromiumLaunch.setAttribute(LaunchParams.CHROMIUM_DEBUG_HOST, LaunchConstants.LOCALHOST);
		chromiumLaunch.setAttribute(LaunchParams.CHROMIUM_DEBUG_PORT, port);

		chromiumLaunch.setAttribute(LaunchParams.ADD_NETWORK_CONSOLE, false);
		
		chromiumLaunch.setAttribute(LaunchParams.BREAKPOINT_SYNC_DIRECTION, LaunchConstants.MERGE);
		chromiumLaunch.setAttribute(LaunchParams.SOURCE_LOOKUP_MODE, LaunchConstants.EXACT_MATCH);
		chromiumLaunch.setAttribute(LaunchParams.PredefinedSourceWrapperIds.CONFIG_PROPERTY, encode(LaunchConstants.PREDEFIENED_WRAPPERS));
		
		chromiumLaunch.setAttribute(LaunchParams.ATTR_APP_PROJECT, projectName);
		chromiumLaunch.setAttribute(LaunchParams.ATTR_APP_PROJECT_RELATIVE_PATH, resourceRelativePath);
		chromiumLaunch.setAttribute(LaunchParams.ATTR_BASE_URL, url);
		
		chromiumLaunch.setAttribute(LaunchParams.WIP_BACKEND_ID, LaunchConstants.ATTR_WIP_BACKEND_CURRENT_DEV);
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				try {
					// FIXME ugly / need to implement similar approach to Node.js Debugger
					Thread.sleep(1500);
					DebugUITools.launch(chromiumLaunch, ILaunchManager.DEBUG_MODE);
				} catch (InterruptedException e) {
					JSDebuggerPlugin.logError(e, e.getMessage());
				}
			}
		});
	}
	
	
	/**
	 * Encoding predefined source wrappers via {@link MementoFormat} for correct
	 * decoding in {@link PredefinedSourceWrapperIds}
	 */
	public static String encode(List<String> wrappers) {
		StringBuilder output = new StringBuilder();
		Collections.sort(wrappers);
		for (String wrapper : wrappers) {
			output.append(wrapper.length());
			output.append('(').append(wrapper).append(')');
		}
		return output.toString();
	}

}
