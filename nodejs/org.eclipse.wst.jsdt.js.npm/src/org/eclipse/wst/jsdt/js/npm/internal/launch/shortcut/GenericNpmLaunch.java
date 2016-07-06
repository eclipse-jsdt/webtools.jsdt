/*******************************************************************************
 * Copyright (c) 2015, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.internal.launch.shortcut;

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
import org.eclipse.wst.jsdt.js.npm.NpmPlugin;
import org.eclipse.wst.jsdt.js.npm.internal.NpmConstants;
import org.eclipse.wst.jsdt.js.npm.internal.NpmScriptTask;
import org.eclipse.wst.jsdt.js.npm.util.NpmUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public abstract class GenericNpmLaunch implements ILaunchShortcut {

	protected abstract String getCommandName();

	@Override
	public void launch(ISelection selection, String mode) {
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			 Object element = ((IStructuredSelection)selection).getFirstElement();
			 if (element instanceof IResource) {
				IResource selectedResource = (IResource) element;
				launch(selectedResource, mode);
			 } else if (element instanceof NpmScriptTask) {
				 NpmScriptTask scriptTask = (NpmScriptTask) element;
				launch(scriptTask.getParent(), mode, scriptTask.getName());
			 }
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		IFile file = ResourceUtil.getFile(editorInput);
		if (file != null && file.exists() && NpmConstants.PACKAGE_JSON.equals(file.getName())) {
			launch(file, mode);
		}
	}

	private void launch(final IResource resource, final String mode) {
		launch(resource, mode, null);
	}

	private void launch(final IResource resource, final String mode, final String subCommand) {
		try {
			IProject project = resource.getProject();
			IPath workingDirectory = getWorkingDirectory(resource);
			if (project.exists() && workingDirectory != null) {
				String projectName = project.getName();
				ILaunchConfigurationType npmLaunchType = DebugPlugin.getDefault().getLaunchManager()
						.getLaunchConfigurationType(NpmConstants.LAUNCH_CONFIGURATION_ID);
				ILaunchConfigurationWorkingCopy npmLaunch = npmLaunchType.newInstance(null, generateLaunchName(projectName));
				npmLaunch.setAttribute(NpmConstants.LAUNCH_COMMAND, getCommandName());
				npmLaunch.setAttribute(NpmConstants.LAUNCH_PROJECT, projectName);
				npmLaunch.setAttribute(NpmConstants.LAUNCH_DIR, workingDirectory.toOSString());
				npmLaunch.setAttribute(NpmConstants.LAUNCH_SUBCOMMAND, subCommand);
				DebugUITools.launch(npmLaunch, mode);
			}
		} catch (CoreException e) {
			NpmPlugin.logError(e, e.getMessage());
		}
	}

	private String generateLaunchName(String projectName) {
		return projectName + " [" + NpmConstants.NPM + " " + getCommandName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	protected IPath getWorkingDirectory(IResource resource) throws CoreException {
		IPath workingDir = null;
		if (resource != null && resource.exists()) {
			if (resource.getType() == IResource.FILE && NpmConstants.PACKAGE_JSON.equals(resource.getName())) {
				workingDir = resource.getParent().getLocation();
			} else if (resource.getType() == IResource.FOLDER) {
				workingDir = resource.getLocation();
			} else if (resource.getType() == IResource.PROJECT) {
				IProject project = (IProject) resource;
				IFile file = project.getFile(NpmConstants.PACKAGE_JSON);
				if (file.exists()) {
					workingDir = resource.getLocation();
				} else {
					// Trying to find package.json file ignoring "node_modules" (default modules dir that can not be changed)
					workingDir = NpmUtil.getNpmWorkingDir(project, NpmConstants.NODE_MODULES);
				}
			}
		}
		return workingDir;
	}

}
