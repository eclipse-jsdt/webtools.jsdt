/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. we
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.js.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.js.JSDebuggerPlugin;
import org.eclipse.wst.jsdt.chromium.debug.js.Messages;
import org.eclipse.wst.jsdt.chromium.debug.js.launch.LaunchConstants;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ChromiumUtil;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ExceptionUtil;
import org.eclipse.wst.jsdt.chromium.debug.js.util.WorbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumHandler extends AbstractHandler {
	private static final String WEBAPP = "webapp"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			String url = ChromiumUtil.guessUrl();
			IProject project = ChromiumUtil.guessProject();
			
			if (url == null) {
				throw new CoreException(new Status(IStatus.ERROR, JSDebuggerPlugin.PLUGIN_ID, Messages.ERROR_UNABLE_TO_DETECT_LAUNCH_URL));
			}
					
			if (project == null || !project.isAccessible()) {
				throw new CoreException(new Status(IStatus.ERROR, JSDebuggerPlugin.PLUGIN_ID, Messages.ERROR_UNABLE_TO_DETECT_DEBUGGING_PROJECT));
			}
			
			String projectName = project.getName();
			
			// FIXME: Bug 499566 - Need to improve mapping logic between base url and js files in workspace
			IContainer webapp = WorbenchResourceUtil.findFolder(project, WEBAPP);
			
			if (webapp == null || !webapp.isAccessible()) {
				throw new CoreException(new Status(IStatus.ERROR, JSDebuggerPlugin.PLUGIN_ID, Messages.ERROR_UNABLE_TO_DETECT_WEBAPP_FOLDER));
			}
			
			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(LaunchConstants.CHROMIUM_LAUNCH_TYPE_ID);
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(project, projectName);

			workingCopy.setAttribute(LaunchConstants.ATTR_APP_PROJECT, projectName);
			workingCopy.setAttribute(LaunchConstants.ATTR_APP_PROJECT_RELATIVE_PATH, webapp.getProjectRelativePath().toOSString());
			workingCopy.setAttribute(LaunchConstants.ATTR_CHROMIUM_URL, url);
			workingCopy.setAttribute(LaunchConstants.ATTR_BASE_URL, url);
			workingCopy.setAttribute(LaunchParams.CHROMIUM_DEBUG_PORT, String.valueOf(ChromiumUtil.getRandomOpenPort()));

			DebugUITools.launch(workingCopy, ILaunchManager.DEBUG_MODE);
		} catch (CoreException | IOException e) {
			JSDebuggerPlugin.logError(e, e.getMessage());
			ExceptionUtil.showErrorDialog(e);
		}

		return null;
	}

}
