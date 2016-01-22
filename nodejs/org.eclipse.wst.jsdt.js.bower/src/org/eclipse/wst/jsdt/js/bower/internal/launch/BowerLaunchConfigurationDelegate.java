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
package org.eclipse.wst.jsdt.js.bower.internal.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.js.bower.BowerPlugin;
import org.eclipse.wst.jsdt.js.bower.internal.BowerConstants;
import org.eclipse.wst.jsdt.js.bower.internal.Messages;
import org.eclipse.wst.jsdt.js.cli.core.CLI;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor monitor) throws CoreException {
		String projectName = configuration.getAttribute(BowerConstants.LAUNCH_PROJECT, (String) null);
		String dirPath = configuration.getAttribute(BowerConstants.LAUNCH_DIR, (String) null);
		String commandName = configuration.getAttribute(BowerConstants.LAUNCH_COMMAND, (String) null);
		
		IProject project = WorkbenchResourceUtil.getProject(projectName);
		if (project != null && project.exists()) {
			IPath dir = (dirPath == null) ? project.getLocation() : new Path(dirPath);
			CLICommand command = generateCommand(commandName);
			launchBower(project, dir, command, monitor);
		}
	}
	
	private void launchBower(IProject project, IPath dir, CLICommand command, IProgressMonitor monitor) {
		try {
			 new CLI(project, dir).execute(command, monitor);
		} catch (CoreException e) {
			BowerPlugin.logError(e);
			ErrorDialog.openError(Display.getDefault().getActiveShell(), Messages.BowerLaunchError_Title,
					Messages.BowerLaunchError_Message, e.getStatus());
		}
	}
	
	private CLICommand generateCommand(String commandName) {
		return new CLICommand(BowerConstants.BOWER, commandName, null, null);
	}
	
}
