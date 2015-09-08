/*******************************************************************************
 * Copyright (c) 2015 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * - Mickael Istria (Red Hat Inc.(
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.importer;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.wizards.datatransfer.ProjectConfigurator;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.core.util.ConvertUtility;

public class JSDTProjectNature implements ProjectConfigurator {
	
	private final static String FILE_EXTENSION = ".js";

	private final static class JavaScriptResourceExistsFinder implements IResourceVisitor {
		private boolean hasJSFile;
		private Set<IPath> ignoredDirectories;
		
		public JavaScriptResourceExistsFinder(Set<IPath> ignoredDirectories) {
			this.ignoredDirectories = ignoredDirectories;
		}
		
		@Override
		public boolean visit(IResource resource) throws CoreException {
			if (this.ignoredDirectories != null) {
				for (IPath ignoredDirectory : this.ignoredDirectories) {
					if (ignoredDirectory.isPrefixOf(resource.getLocation())) {
						return false;
					}
				}
			}
			
			this.hasJSFile = this.hasJSFile || (resource.getType() == IResource.FILE && resource.getName().endsWith(FILE_EXTENSION));
			return !this.hasJSFile; 
		}
		
		public boolean hasJavaFile() {
			return this.hasJSFile;
		}
		
	}
	
	private final static class JavaScriptResourceFinder implements IResourceVisitor {
		private Set<IContainer> mostLikelySourceFolders =  new HashSet<IContainer>();
		private Set<IPath> ignoredDirectories;
		
		public JavaScriptResourceFinder(Set<IPath> ignoredDirectories) {
			this.ignoredDirectories = ignoredDirectories;
		}
		
		@Override
		public boolean visit(final IResource resource) throws CoreException {
			if (this.ignoredDirectories != null) {
				for (IPath ignoredDirectory : this.ignoredDirectories) {
					if (ignoredDirectory.isPrefixOf(resource.getLocation())) {
						return false;
					}
				}
			}
			
			if (resource.getType() == IResource.FILE && resource.getName().endsWith(FILE_EXTENSION)) {
				this.mostLikelySourceFolders.add(resource.getParent());
			} else {
				return true;
			}
			
			return false; // don't visit a file
		}
		
		public Set<IContainer> getSourceFolders() {
			Set<IContainer> res = new HashSet<IContainer>();
			res.addAll(this.mostLikelySourceFolders);
			for (IContainer item : this.mostLikelySourceFolders) {
				boolean alreadyContainsAParent = false;
				Set<IContainer> childrenOfItem = new HashSet<IContainer>();
				for (IContainer other : res) {
					if (item.getFullPath().isPrefixOf(other.getFullPath())) {
						childrenOfItem.add(other);
					} else if (other.getFullPath().isPrefixOf(item.getFullPath())) {
						alreadyContainsAParent = true;
					}
				}
				res.removeAll(childrenOfItem);
				if (!alreadyContainsAParent) {
					res.add(item);
				}
			}
			return res;
		}
	}

	@Override
	public boolean canConfigure(IProject project, Set<IPath> ignoredDirectories, IProgressMonitor monitor) {
		JavaScriptResourceExistsFinder javaResourceFinder = new JavaScriptResourceExistsFinder(ignoredDirectories);
		try {
			project.accept(javaResourceFinder);
		} catch (CoreException ex) {
			Activator.getDefault().getLog().log(new Status(
					IStatus.ERROR,
					Activator.PLUGIN_ID,
					ex.getMessage(),
					ex));
			return false;
		}
		return javaResourceFinder.hasJavaFile();
	}

	@Override
	public IWizard getConfigurationWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configure(IProject project, Set<IPath> ignoredDirectories, IProgressMonitor monitor) {
		try {
			new ConvertUtility(project).configure(monitor);
			IJavaScriptProject jsProject = (IJavaScriptProject) project.getNature(JavaScriptCore.NATURE_ID);
			// TODO exclude ignored Directories from source folders ?
		} catch (Exception ex) {
			Activator.getDefault().getLog().log(new Status(
					IStatus.ERROR,
					Activator.PLUGIN_ID,
					ex.getMessage(),
					ex));
		}
	}

	@Override
	public boolean shouldBeAnEclipseProject(IContainer container, IProgressMonitor monitor) {
		return false;
	}
	
	@Override
	public Set<IFolder> getDirectoriesToIgnore(IProject project, IProgressMonitor monitor) {
		return null; // JSDT doesn't create "rubbish" directories
	}

	@Override
	public Set<File> findConfigurableLocations(File root, IProgressMonitor monitor) {
		// No easy way to detect project directories just by finding .js file in a dir
		return Collections.EMPTY_SET;
	}
}
