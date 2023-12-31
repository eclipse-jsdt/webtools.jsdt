/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.grunt.internal.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.wst.jsdt.js.cli.core.CLI;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.grunt.GruntPlugin;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntConstants;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration conf, String arg1, ILaunch arg2, IProgressMonitor monitor) throws CoreException {
		String projectName = conf.getAttribute(GruntConstants.PROJECT, (String) null);
		String dirPath = conf.getAttribute(GruntConstants.DIR, (String) null);
		String commandName = conf.getAttribute(GruntConstants.COMMAND, (String) null);
		String parameters = conf.getAttribute(GruntConstants.PARAMETERS, (String) null);
		
		IProject project = WorkbenchResourceUtil.getProject(projectName);
		if (project != null && project.exists()) {
			IPath dir = (dirPath == null) ? project.getLocation() : new Path(dirPath);
			CLICommand command = generateCLICommand(commandName, parameters);
			launchGrunt(project, dir, command, monitor);
		}	
	}
	

	private void launchGrunt(IProject project, IPath dir, CLICommand command, IProgressMonitor monitor) {
		try {
			new CLI(project, dir, command).execute(monitor);
		} catch (CoreException e) {
			GruntPlugin.logError(e);
		}
	}
	
	protected CLICommand generateCLICommand(String commandName, String parameters) {
		// ANSI color codes are not supported.
		String[] options = {GruntConstants.GRUNT_NO_COLOR};
		return new CLICommand(GruntConstants.GRUNT, commandName, parameters, options);
	}

}
