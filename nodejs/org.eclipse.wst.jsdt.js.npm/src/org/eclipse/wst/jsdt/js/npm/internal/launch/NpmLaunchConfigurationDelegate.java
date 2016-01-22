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
package org.eclipse.wst.jsdt.js.npm.internal.launch;

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
import org.eclipse.wst.jsdt.js.cli.core.CLI;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.npm.NpmPlugin;
import org.eclipse.wst.jsdt.js.npm.internal.Messages;
import org.eclipse.wst.jsdt.js.npm.internal.NpmConstants;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NpmLaunchConfigurationDelegate  implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor monitor) throws CoreException {
		String projectName = configuration.getAttribute(NpmConstants.LAUNCH_PROJECT, (String) null);
		String dirPath = configuration.getAttribute(NpmConstants.LAUNCH_DIR, (String) null);
		String commandName = configuration.getAttribute(NpmConstants.LAUNCH_COMMAND, (String) null);
		
		IProject project = WorkbenchResourceUtil.getProject(projectName);
		if (project != null && project.exists()) {
			IPath dir = (dirPath == null) ? project.getLocation() : new Path(dirPath);
			CLICommand command = generateCommand(commandName);
			launchNpm(project, dir, command, monitor);
		}
	}
	
	private void launchNpm(IProject project, IPath dir, CLICommand command, IProgressMonitor monitor) {
		try {
			 new CLI(project, dir).execute(command, monitor);
		} catch (CoreException e) {
			NpmPlugin.logError(e);
			ErrorDialog.openError(Display.getDefault().getActiveShell(), Messages.NpmLaunchError_Title,
					Messages.NpmLaunchError_Message, e.getStatus());
		}
	}
	
	private CLICommand generateCommand(String commandName) {
		return new CLICommand(NpmConstants.NPM, commandName, null, null);	
	}
	
}

