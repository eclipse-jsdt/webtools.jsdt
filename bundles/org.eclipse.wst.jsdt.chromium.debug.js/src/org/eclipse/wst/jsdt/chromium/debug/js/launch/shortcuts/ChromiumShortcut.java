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
package org.eclipse.wst.jsdt.chromium.debug.js.launch.shortcuts;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.js.JSDebuggerPlugin;
import org.eclipse.wst.jsdt.chromium.debug.js.launch.LaunchConstants;
import org.eclipse.wst.jsdt.chromium.debug.js.util.ChromiumUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class ChromiumShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		try {
			Object objSelected = ((IStructuredSelection) selection).getFirstElement();
			if (objSelected instanceof IResource) {
				launch((IResource) objSelected, mode);
			}
		} catch (CoreException | IOException e) {
			JSDebuggerPlugin.logError(e, e.getMessage());
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			IEditorInput editorInput = editor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) editorInput).getFile();
				launch(file, mode);
			}
		} catch (CoreException | IOException e) {
			JSDebuggerPlugin.logError(e, e.getMessage());
		}
	}

	private void launch(IResource resource, String mode) throws CoreException, IOException {
		IProject project = resource.getProject();
		String projectName = project.getName();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager
				.getLaunchConfigurationType(LaunchConstants.CHROMIUM_LAUNCH_TYPE_ID);
		ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(project, projectName);

		workingCopy.setAttribute(LaunchConstants.ATTR_APP_PROJECT, projectName);
		workingCopy.setAttribute(LaunchConstants.ATTR_APP_PROJECT_RELATIVE_PATH,
				resource.getProjectRelativePath().toOSString());
		workingCopy.setAttribute(LaunchConstants.ATTR_CHROMIUM_URL, resource.getLocation().toOSString());
		
		workingCopy.setAttribute(LaunchConstants.ATTR_BASE_URL, "file://" + resource.getParent().getLocation().toOSString()); //$NON-NLS-1$
		
		workingCopy.setAttribute(LaunchParams.CHROMIUM_DEBUG_PORT, String.valueOf(ChromiumUtil.getRandomOpenPort()));
		
		DebugUITools.launch(workingCopy, mode);
	}

}
