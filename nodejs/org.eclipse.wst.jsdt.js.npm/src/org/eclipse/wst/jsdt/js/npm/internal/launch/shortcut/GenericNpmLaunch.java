/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
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
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.wst.jsdt.js.cli.core.CLI;
import org.eclipse.wst.jsdt.js.cli.core.CLICommand;
import org.eclipse.wst.jsdt.js.npm.NpmPlugin;
import org.eclipse.wst.jsdt.js.npm.internal.Messages;
import org.eclipse.wst.jsdt.js.npm.internal.NpmConstants;
import org.eclipse.wst.jsdt.js.npm.util.NpmUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public abstract class GenericNpmLaunch implements ILaunchShortcut {
	
	protected abstract CLICommand getCLICommand();
	
	protected abstract String getCommandName();

	protected abstract String getLaunchName();
	
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			 Object element = ((IStructuredSelection)selection).getFirstElement();
			 if (element != null && element instanceof IResource) {
				try {
					IResource selectedResource = (IResource) element;
					launchNpm(selectedResource);
				} catch (CoreException e) {
					NpmPlugin.logError(e);
				}
			 }
		}
	}
	
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput editorInput = editor.getEditorInput();
		IFile file = ResourceUtil.getFile(editorInput);
		if (file != null && file.exists() && NpmConstants.PACKAGE_JSON.equals(file.getName())) {
			try {
				launchNpm(file);
			} catch (CoreException e) {
				NpmPlugin.logError(e);
			}
		}
	}
	
	private void launchNpm(IResource resource) throws CoreException {
		try {
			 new CLI(resource.getProject(), getWorkingDirectory(resource)).execute(getCLICommand(), null);
		} catch (CoreException e) {
			NpmPlugin.logError(e);
			ErrorDialog.openError(Display.getDefault().getActiveShell(), Messages.NpmLaunchError_Title,
					Messages.NpmLaunchError_Title, e.getStatus());
		}
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
