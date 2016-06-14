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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.variables.VariablesPlugin;
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
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodeLaunch implements ILaunchShortcut{
	@Override
	public void launch(ISelection selection, String mode) {
		try {
			Object objSelected = ((IStructuredSelection) selection).getFirstElement();
			if (objSelected instanceof IResource) {
				launchHelper((IResource) objSelected, mode);
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
				launchHelper(file, mode);
			}
		} catch (CoreException e) {
			NodePlugin.logError(e.getLocalizedMessage());
		}
	}

	protected void launchHelper(IResource file, String mode) throws CoreException {
	    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
	    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(NodeConstants.LAUNCH_CONFIGURATION_TYPE_ID);
		ILaunchConfiguration configuration = createLaunchConfiguration(type, file);
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

	private ILaunchConfiguration createLaunchConfiguration(ILaunchConfigurationType type, IResource file)
			throws CoreException {
		String path = NodeConstants.EMPTY;
		IFile configFile = null;
		if(file.getType() == IResource.PROJECT || file.getType() == IResource.FOLDER){
			PackageJson packageJson = PackageJsonUtil.readPackageJsonFromIResource(file);
			if(packageJson.getMain() != null){
				IFile mainFile = file.getProject().getFile(packageJson.getMain());
				if(mainFile != null && mainFile.isAccessible()){
					configFile = mainFile;
				}
			}
			if (configFile == null) {
				IProject project = file.getProject();
				// need to reuse the launch for the project if it has been already generated
				ILaunchConfiguration launch = LaunchConfigurationUtil.getLaunchByName(project.getName(), type);
				if (launch != null) {
					return launch;
				}
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
			// Get relative path
			path = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", //$NON-NLS-1$
					configFile.getFullPath().toString());

			// Get configuration name
			configName = configFile.getFullPath().toString();
			if (configName.startsWith("/")) { //$NON-NLS-1$
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
		workingCopy.setAttribute(NodeConstants.ATTR_APP_PROJECT, file.getProject().getName());
		workingCopy.setAttribute(NodeConstants.ATTR_APP_PROJECT_RELATIVE_PATH, file.getProjectRelativePath().toOSString());
		workingCopy.setMappedResources(getResource(file.getProject().getName()));
		LaunchConfigurationUtil.addSourceLookupAttr(workingCopy);
	
		return workingCopy.doSave();
	}
	
    private IResource[] getResource(String projectName){
        if (projectName.length() > 0) {
        	IStatus status = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
        	if(status.isOK()){
            	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            	return new IResource[] {project}; 
        	}
        }
		return null;
    }

}
