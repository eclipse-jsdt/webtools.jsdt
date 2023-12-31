/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.bower.internal.launch.shortcut;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.wst.jsdt.js.bower.BowerPlugin;
import org.eclipse.wst.jsdt.js.bower.internal.BowerConstants;
import org.eclipse.wst.jsdt.js.bower.util.BowerUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public abstract class GenericBowerLaunch implements ILaunchShortcut {
	protected abstract String getCommandName();

	@Override
	public void launch(ISelection selection, String mode) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			 Object element = ((IStructuredSelection)selection).getFirstElement();
			 if (element instanceof IResource) {
				 IResource selectedResource = (IResource) element;
				 launch(selectedResource, mode);
			 }
		}
	}
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		IFile file = ResourceUtil.getFile(editorInput);
		if (file != null && file.exists() && BowerConstants.BOWER_JSON.equals(file.getName())) {
			launch(file, mode);
		}
	}
	
	private void launch(final IResource resource, String mode) {
		try {
			IProject project = resource.getProject();
			IPath workingDirectory = getWorkingDirectory(resource);
			if (project.exists() && workingDirectory != null) {
				String projectName = project.getName();
				ILaunchConfigurationType bowerLaunchType = DebugPlugin.getDefault().getLaunchManager()
						.getLaunchConfigurationType(BowerConstants.LAUNCH_CONFIGURATION_ID);
				ILaunchConfigurationWorkingCopy bowerLaunch = bowerLaunchType.newInstance(null, generateLaunchName(projectName));
				bowerLaunch.setAttribute(BowerConstants.LAUNCH_COMMAND, getCommandName());
				bowerLaunch.setAttribute(BowerConstants.LAUNCH_PROJECT, projectName);
				bowerLaunch.setAttribute(BowerConstants.LAUNCH_DIR, workingDirectory.toOSString());	
				DebugUITools.launch(bowerLaunch, mode);
			}
		} catch (CoreException e) {
			BowerPlugin.logError(e, e.getMessage());
		}
	}
	
	private String generateLaunchName(String projectName) {
		return projectName + " [" + BowerConstants.BOWER + " " + getCommandName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private IPath getWorkingDirectory(final IResource resource) throws CoreException {
		IPath workingDir = null;
		if (resource != null && resource.exists()) {
			if (resource.getType() == IResource.FILE && BowerConstants.BOWER_JSON.equals(resource.getName())) {
				workingDir = resource.getParent().getLocation();
			} else if (resource.getType() == IResource.FOLDER) {
				workingDir = resource.getLocation();
			} else if (resource.getType() == IResource.PROJECT) {
				IProject project = (IProject) resource;
				IFile file = project.getFile(BowerConstants.BOWER_JSON);
				if (file.exists()) {
					workingDir = resource.getLocation();
				} else {
					try {
						workingDir = getWorkingDirectory(project);
					} catch (IOException e) {
						BowerPlugin.logError(e);
					}
				}
			}
		}
		return workingDir;
	}
	
	/**
	 * Detects working directory for bower execution depending on .bowerrc file
	 * @throws CoreException 
	 * @throws UnsupportedEncodingException
	 * @see <a href="http://bower.io/docs/config/">Bower Configuration</a>
	 */
	private IPath getWorkingDirectory(final IProject project) throws CoreException, UnsupportedEncodingException {
		IPath workingDir = null;
		IFile bowerrc = BowerUtil.getBowerrc(project);
		if (bowerrc != null) {
			IContainer parent = bowerrc.getParent();
			if (parent.exists() && parent.findMember(BowerConstants.BOWER_JSON) != null) {
				workingDir = parent.getLocation();
			} else {
				String directoryName = BowerUtil.getDirectoryName(bowerrc);
				directoryName = (directoryName != null) ? directoryName : BowerConstants.BOWER_COMPONENTS;
				workingDir = BowerUtil.getBowerWorkingDir(project, directoryName);
			}
		} else {
			// Trying to find bower.json file ignoring "bower_components"
			// (default components directory)
			workingDir = BowerUtil.getBowerWorkingDir(project, BowerConstants.BOWER_COMPONENTS);
		}
		return workingDir;
	}
	
}
