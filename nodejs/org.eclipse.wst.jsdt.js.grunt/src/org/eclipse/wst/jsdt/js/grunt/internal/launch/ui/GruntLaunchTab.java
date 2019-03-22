/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.grunt.internal.launch.ui;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.launch.ui.GenericBuildSystemTab;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;
import org.eclipse.wst.jsdt.js.grunt.GruntPlugin;
import org.eclipse.wst.jsdt.js.grunt.internal.GruntConstants;
import org.eclipse.wst.jsdt.js.grunt.internal.Messages;
import org.eclipse.wst.jsdt.js.grunt.internal.ui.ImageResource;
import org.eclipse.wst.jsdt.js.grunt.util.GruntVisitor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntLaunchTab extends GenericBuildSystemTab {
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setWarningMessage(null);
		
		String buildFilePath = buildFileText.getText();
		File file = WorkbenchResourceUtil.getFile(buildFilePath);
		if (file == null) {
			setErrorMessage(Messages.GruntLaunchTab_ErrorNotExist);
			return false;
		}
		
		// Check if the selected task exists in the Gruntfile, display warning if it does not.
		try {
			String task = tasksCommbo.getText();
			if (task.isEmpty()) {
				return true;
			}
			String rootTask = task.split(":")[0];
			IFile ifile =  WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());

			List<String> availTasks = Arrays.asList(getTaskNames(
					ASTUtil.getTasks(buildFilePath, new GruntVisitor(ifile))));

			if (!availTasks.contains(rootTask)) {
				setWarningMessage(Messages.GruntLaunchTab_WarningTaskNotExist);
			}

		} catch (JavaScriptModelException e) {
			GruntPlugin.logError(e, e.getMessage());
		}

		return true;
	}
		
	@Override
	public Image getImage() {
		//DESIGN-735 Need to create icon for JavaScript Build Systems
		return ImageResource.getImage(ImageResource.IMG_GRUNTFILE);
	}
	
	
	@Override
	public void initializeFrom(ILaunchConfiguration lc) {
		String buildFileLocation = "";  //$NON-NLS-1$
		try {
			buildFileLocation = lc.getAttribute(GruntConstants.BUILD_FILE, (String) null);
			buildFileText.setText(buildFileLocation != null ? buildFileLocation : ""); //$NON-NLS-1$
			
			File file = WorkbenchResourceUtil.getFile(buildFileLocation);
			IFile ifile = null;
			if (file != null) {
				ifile = WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());
			}
			
			// If launch config has a defined task, use that, if not use 'default'. If there is 
			// no default, leave task field blank.
			String task = lc.getAttribute(GruntConstants.COMMAND, (String) null);
			Set<ITask> tasks = ASTUtil.getTasks(buildFileLocation, new GruntVisitor(ifile));
			List<String> tasknames = Arrays.asList(getTaskNames(tasks));
			updateTasks(getTaskNames(tasks));
			if (task != null && !task.isEmpty()) {
				tasksCommbo.setText(task);
			} else {
				if (tasknames.contains(GruntConstants.DEFAULT_COMMAND)) {
					tasksCommbo.setText(GruntConstants.DEFAULT_COMMAND);
				} else {
					tasksCommbo.setText("");
				}
			}

			String params = lc.getAttribute(GruntConstants.PARAMETERS, (String) null);
			if (params != null) {
				parametersText.setText(params);
			} else {
				parametersText.setText("");
			}
		} catch (CoreException e) {
			GruntPlugin.logError(e, e.getMessage());
		}		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy wc) {
		IFile buildFile = getBuildFile();
		if (buildFile != null) {
			IProject project = buildFile.getProject();
			wc.setAttribute(GruntConstants.BUILD_FILE, buildFile.getLocation().toOSString());
			wc.setAttribute(GruntConstants.PROJECT, project.getName());
			wc.setAttribute(GruntConstants.DIR, buildFile.getParent().getLocation().toOSString());
			wc.setAttribute(GruntConstants.COMMAND, tasksCommbo.getText());
			wc.setAttribute(GruntConstants.PARAMETERS, parametersText.getText());
		}
	}
	
	@Override
	protected String[] getTasksFromFile(IFile file) throws JavaScriptModelException {
		Set<ITask> tasks = ASTUtil.getTasks(file.getLocation().toOSString(), new GruntVisitor(file));
		return getTaskNames(tasks);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
	}
	
}
