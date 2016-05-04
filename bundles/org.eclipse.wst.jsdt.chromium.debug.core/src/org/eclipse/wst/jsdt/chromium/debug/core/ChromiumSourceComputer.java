// Copyright (c) 2010, 2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
//     Ilya Buziuk <ilyabuziuk@gmail.com> - Bug 490626 Debugging should happen in JSDT editor if the project exists in workspace

package org.eclipse.wst.jsdt.chromium.debug.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.ContainerSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.FolderSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;

/**
 * A source path computer implementation that provides
 * {@link VProjectSourceContainer} as a default source files container for
 * V8/Chrome debug sessions and {@link SourceNameMapperContainer} for projects available in the workspace
 */
public class ChromiumSourceComputer implements ISourcePathComputerDelegate {
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {
		List<ISourceContainer> containers = new ArrayList<ISourceContainer>();

		String projectName = configuration.getAttribute(LaunchParams.ATTR_APP_PROJECT, (String) null);
		String path = configuration.getAttribute(LaunchParams.ATTR_APP_PROJECT_RELATIVE_PATH, (String) null);
		
		if (projectName != null && path != null) {
			IProject project = getProject(projectName);
			if (project != null && project.isAccessible()) {
				IPath resourcePath = new Path(path);
				IResource resource = project.getFile(resourcePath);
				if (resource.isAccessible()) {
					IContainer container = resource.getParent();
					String mapping = container.getLocation().toOSString();
					// {@link SourceNameMapperContainer} for projects available in the workspace
					SourceNameMapperContainer workspaceConatiner = createWorkspaceSourceContainer(container, mapping);
					containers.add(workspaceConatiner);
				}
			}
		}

		// Default source files container for V8/Chrome debug sessions 
		// contains files which are not available in the workspace e.g. node.js
		containers.add(new VProjectSourceContainer());

		return containers.toArray(new ISourceContainer[containers.size()]);
	}

	private IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	private SourceNameMapperContainer createWorkspaceSourceContainer(IContainer container, String path) {
		ContainerSourceContainer sourceContainer = null;
		if (container instanceof IProject) {
			sourceContainer = new ProjectSourceContainer((IProject) container, true);
		} else if (container instanceof IFolder) {
			sourceContainer = new FolderSourceContainer((IFolder) container, true);
		}

		// Using absolute path as a mapping prefix for project
		return new SourceNameMapperContainer(path + System.getProperty("file.separator"), //$NON-NLS-1$
				sourceContainer);
	}
}
