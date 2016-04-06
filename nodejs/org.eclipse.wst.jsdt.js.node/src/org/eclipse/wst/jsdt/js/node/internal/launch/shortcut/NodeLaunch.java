/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.launch.shortcut;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.common.json.objects.PackageJson;
import org.eclipse.wst.jsdt.js.node.common.util.PackageJsonUtil;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;
import org.eclipse.wst.jsdt.js.node.internal.util.LaunchConfigurationUtil;

/**
 * Launch configuration shortcut for node application
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeLaunch implements ILaunchShortcut{
	@Override
	public void launch(ISelection selection, String mode) {
		try {
			Object objSelected = ((IStructuredSelection) selection).getFirstElement();
			if (objSelected instanceof IFile) {
				launchFile((IFile) objSelected, mode);
			} else  if (objSelected instanceof IContainer) {
                launchContainer((IContainer) objSelected, mode);
            }
		} catch (CoreException e) {
			NodePlugin.logError(e.getLocalizedMessage());
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		try {
			IEditorInput editorInput = editor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) editorInput).getFile();
				launchFile(file, mode);
			}
		} catch (CoreException e) {
			NodePlugin.logError(e.getLocalizedMessage());
		}
	}

	private void launchContainer(IContainer container, String mode) throws CoreException {
	    String path = container.getFullPath().toString();
	    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
	    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(NodeConstants.LAUNCH_CONFIGURATION_TYPE_ID);
	    ILaunchConfiguration configuration = createLaunchConfiguration(type, path, container);
	    launchHelper(configuration, mode);
	}

	protected void launchHelper(ILaunchConfiguration configuration, String mode){
	    String mainScript = null;
		try {
			mainScript = configuration.getAttribute(NodeConstants.ATTR_APP_PATH, NodeConstants.EMPTY);
		    if (mainScript != null && mainScript.equals(NodeConstants.EMPTY)) {
		        Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		        String groupId = DebugUITools.getLaunchGroup(configuration, mode).getIdentifier();
		        DebugUITools.openLaunchConfigurationDialogOnGroup(shell,new StructuredSelection(configuration), groupId);
		    }
		    if (mainScript != null && !mainScript.equals(NodeConstants.EMPTY)) {
		        DebugUITools.launch(configuration, mode);
		    }
		} catch (CoreException e) {
			NodePlugin.logError(e.getLocalizedMessage());
		}
	}

	private void launchFile(IFile file, String mode) throws CoreException {
		String path = ResourcesPlugin.getWorkspace().getRoot().findMember(file.getFullPath().toString()).getLocation()
				.toOSString();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(NodeConstants.LAUNCH_CONFIGURATION_TYPE_ID);
		ILaunchConfiguration configuration = createLaunchConfiguration(type, path, file);
		launchHelper(configuration, mode);
	}

	private ILaunchConfiguration createLaunchConfiguration(ILaunchConfigurationType type, String path, IResource file)
			throws CoreException {
		IFile configFile = null;
		if(file.getType() == IResource.PROJECT || file.getType() == IResource.FOLDER){
			path = NodeConstants.EMPTY;
			PackageJson packageJson = PackageJsonUtil.readPackageJsonFromIResource(file);
			if(packageJson.getMain() != null){
				IFile mainFile = file.getProject().getFile(packageJson.getMain());
				if(mainFile != null && mainFile.isAccessible()){
					configFile = mainFile;
				}
			}
			if(configFile != null) {
				path = ResourcesPlugin.getWorkspace().getRoot().findMember(configFile.getFullPath()).getLocation().toOSString();
			}
		}
    	else if (file.getType() == IResource.FILE){
    		configFile = (IFile) file;
    	}

		ILaunchConfiguration config = LaunchConfigurationUtil.getExistingLaunchConfiguration(configFile,
				type, NodeConstants.ATTR_APP_PATH);
		if (config != null) {
			return config;
		}

		String configName = NodeConstants.EMPTY;
		if(configFile!=null) {
			configName = configFile.getFullPath().toString();
			if (configName.startsWith("/")) {
				configName = configName.substring(1, configName.length());
			}
		} else {
    		configName = file.getProject().getName();
    	}

		// Make sure the configuration name is unique
		configName = DebugPlugin.getDefault().getLaunchManager().generateLaunchConfigurationName(configName);

		IContainer container = null;
		ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(container, configName);
		workingCopy.setAttribute(NodeConstants.ATTR_APP_PATH, path);
		workingCopy.setAttribute(NodeConstants.ATTR_APP_PROJECT, file.getProject().getLocation().toOSString());
		return workingCopy.doSave();
	}
}
