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
package org.eclipse.wst.jsdt.js.grunt.internal.launch.shortcut;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.launch.LaunchConfigurationAutoFill;
import org.eclipse.wst.jsdt.js.grunt.GruntPlugin;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntConstants;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntTask;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntLaunch implements ILaunchShortcut {
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			element.toString();
			if (element instanceof ITask) {
				ITask task = (ITask) element;
				launch(task, mode);
				// Launch for Gruntfile.js
			} else if (element instanceof IFile) {
				launch(createDefaultTask((IFile) element), mode);
			}
		}
	}
	
	@Override
	public void launch(IEditorPart arg0, String arg1) {	
	}
	
	protected void launch(ITask task, String mode) {
		try {
			IFile buildFile = task.getBuildFile();
			ILaunchConfigurationType gruntLaunchConfiguraionType = DebugPlugin.getDefault().getLaunchManager()
					.getLaunchConfigurationType(GruntConstants.LAUNCH_CONFIGURATION_ID); 
			
			// Check if configuration already exists
			ILaunchConfiguration[] configurations = DebugPlugin.getDefault()
					.getLaunchManager().getLaunchConfigurations(gruntLaunchConfiguraionType);
			
			// Get only configurations for this Gruntfile.
			ILaunchConfiguration[] existingConfigurations = LaunchConfigurationAutoFill
					.getAllLaunchConfigurations(configurations, task, GruntConstants.BUILD_FILE);

			for (ILaunchConfiguration conf : existingConfigurations) {
				if (conf.getAttribute(GruntConstants.COMMAND, (String) null).equals(task.getName())) {
					DebugUITools.launch(conf, mode);
					return;
				}
			}

			// No configuration found; create a new one.
			if (buildFile != null){
				IProject project = buildFile.getProject();	
				ILaunchConfigurationWorkingCopy newConfiguration = createEmptyLaunchConfiguration(
						project.getName() + " - " + task.getName() + " [" + buildFile.getName() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				newConfiguration.setAttribute(GruntConstants.BUILD_FILE, buildFile.getLocation().toOSString());
				newConfiguration.setAttribute(GruntConstants.PROJECT, project.getName());
				newConfiguration.setAttribute(GruntConstants.DIR, buildFile.getParent().getLocation().toOSString());
				newConfiguration.setAttribute(GruntConstants.COMMAND, task.getName());
				newConfiguration.doSave();
				DebugUITools.launch(newConfiguration, mode);				
			}
		} catch (CoreException e) {
			GruntPlugin.logError(e, e.getMessage());
		}
	}

	private ILaunchConfigurationWorkingCopy createEmptyLaunchConfiguration(String namePrefix) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager
				.getLaunchConfigurationType(GruntConstants.LAUNCH_CONFIGURATION_ID);
		ILaunchConfigurationWorkingCopy launchConfiguration = launchConfigurationType.newInstance(null,
				launchManager.generateLaunchConfigurationName(namePrefix));
		return launchConfiguration;
	}
	
	private ITask createDefaultTask(IFile resource) {
		return new GruntTask("", resource, true, null); //$NON-NLS-1$
	}
	
}
