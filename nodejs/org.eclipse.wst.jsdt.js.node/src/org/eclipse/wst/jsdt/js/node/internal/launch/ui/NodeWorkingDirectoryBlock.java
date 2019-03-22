/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.launch.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;


/**
 * Node working directory block for node application launch configuration
 * 
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeWorkingDirectoryBlock extends WorkingDirectoryBlock {

	public NodeWorkingDirectoryBlock(String workingDirectoryAttribteName) {
		super(workingDirectoryAttribteName);
	}

	@Override
	protected IProject getProject(ILaunchConfiguration launchConfiguration) throws CoreException {
		String projectName = NodeConstants.EMPTY;
		try {
			projectName = launchConfiguration.getAttribute(NodeConstants.ATTR_APP_PROJECT,
					NodeConstants.EMPTY);
		} catch (CoreException e) {
			NodePlugin.logError(e, e.getLocalizedMessage());
		}
		if (!projectName.equals(NodeConstants.EMPTY)) {
			IStatus status = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
        	if(status.isOK()){
    			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    			if (project != null && project.isAccessible()) {
    				return project;
    			}
        	}
		}
		return null;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		setLaunchConfiguration(configuration);
		try {
			String wd = configuration.getAttribute(NodeConstants.ATTR_WORKING_DIRECTORY,
					NodeConstants.EMPTY);
			setDefaultWorkingDir();
			if (!wd.equals(NodeConstants.EMPTY)) {
				setOtherWorkingDirectoryText(wd);
			}
		} catch (CoreException e) {
			NodePlugin.logError(e, e.getLocalizedMessage());
		}
	}
}
