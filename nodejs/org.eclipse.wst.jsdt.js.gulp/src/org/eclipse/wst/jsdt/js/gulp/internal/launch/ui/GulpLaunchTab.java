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
package org.eclipse.wst.jsdt.js.gulp.internal.launch.ui;

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
import org.eclipse.wst.jsdt.js.gulp.GulpPlugin;
import org.eclipse.wst.jsdt.js.gulp.internal.GulpConstants;
import org.eclipse.wst.jsdt.js.gulp.internal.Messages;
import org.eclipse.wst.jsdt.js.gulp.internal.ui.ImageResource;
import org.eclipse.wst.jsdt.js.gulp.util.GulpVisitor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GulpLaunchTab extends GenericBuildSystemTab {
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setWarningMessage(null);
		
		String buildFilePath = buildFileText.getText();
		File file = WorkbenchResourceUtil.getFile(buildFilePath);
		if (file == null) {
			setErrorMessage(Messages.GulpLaunchTab_ErrorNotExist);
			return false;
		}
		
		// Check if the entered task exists in the Gulpfile, display warning if it does not.
		try {
			String task = tasksCommbo.getText();
			if (task.isEmpty()) {
				return true;
			}
			IFile ifile = WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());
			List<String> availTasks = Arrays.asList(getTaskNames(
					ASTUtil.getTasks(buildFilePath, new GulpVisitor(ifile))));

			if (!availTasks.contains(task)) {
				setWarningMessage(Messages.GulpLaunchTab_WarningTaskNotExist);
			}
		} catch (JavaScriptModelException e) {
			GulpPlugin.logError(e, e.getMessage());
		}

		return true;
	}
		
	@Override
	public Image getImage() {
		//DESIGN-735 Need to create icon for JavaScript Build Systems
		return ImageResource.getImage(ImageResource.IMG_GULPFILE);
	}
	
	
	@Override
	public void initializeFrom(ILaunchConfiguration lc) {
		String buildFileLocation = "";  //$NON-NLS-1$
		try {
			buildFileLocation = lc.getAttribute(GulpConstants.BUILD_FILE, (String) null);
			buildFileText.setText(buildFileLocation != null ? buildFileLocation : ""); //$NON-NLS-1$

			File file = WorkbenchResourceUtil.getFile(buildFileLocation);
			IFile ifile = null;
			if (file != null) {
				ifile = WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());
			}

			Set<ITask> tasks = ASTUtil.getTasks(buildFileLocation, new GulpVisitor(ifile));
			List<String> tasknames = Arrays.asList(getTaskNames(tasks));
			updateTasks(getTaskNames(tasks));
			String task = lc.getAttribute(GulpConstants.COMMAND, (String) null);
			if (task != null && !task.isEmpty()) {
				tasksCommbo.setText(task);
			} else {
				if (tasknames.contains(GulpConstants.DEFAULT_COMMAND)) {
					tasksCommbo.setText(GulpConstants.DEFAULT_COMMAND);
				} else {
					tasksCommbo.setText("");
				}
			}

			String params = lc.getAttribute(GulpConstants.PARAMETERS, (String) null);
			if (params != null) {
				parametersText.setText(params);
			} else {
				parametersText.setText("");
			}
		} catch (CoreException e) {
			GulpPlugin.logError(e, e.getMessage());
		}		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy wc) {
		IFile buildFile = getBuildFile();
		if (buildFile != null) {
			IProject project = buildFile.getProject();
			wc.setAttribute(GulpConstants.BUILD_FILE, buildFile.getLocation().toOSString());
			wc.setAttribute(GulpConstants.PROJECT, project.getName());
			wc.setAttribute(GulpConstants.DIR, buildFile.getParent().getLocation().toOSString());
			wc.setAttribute(GulpConstants.COMMAND, tasksCommbo.getText());
			wc.setAttribute(GulpConstants.PARAMETERS, parametersText.getText());
		}
	}
	
	@Override
	protected String[] getTasksFromFile(IFile file) throws JavaScriptModelException {
		Set<ITask> tasks = ASTUtil.getTasks(file.getLocation().toOSString(), new GulpVisitor(file));
		return getTaskNames(tasks);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy wc) {		
	}
	
}
