/*******************************************************************************
 * Copyright (c) 2016, 1027 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.npm.internal.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.Location;
import org.eclipse.wst.jsdt.js.npm.NpmPlugin;
import org.eclipse.wst.jsdt.js.npm.PackageJson;
import org.eclipse.wst.jsdt.js.npm.internal.NpmScriptTask;
import org.eclipse.wst.jsdt.js.npm.util.NpmUtil;

/**
 * Scans the package.json file and extracts the names of the available scripts
 * to run.
 *
 * @author Shane Bryzak
 *
 */
public class NpmScriptContentProvider implements ITreeContentProvider, IResourceChangeListener {

	private Viewer viewer;
	private IResource resource;

	protected static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public void dispose() {
		if (resource != null) {
			resource.getWorkspace().removeResourceChangeListener(this);
			resource = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (resource != null) {
			resource.getWorkspace().removeResourceChangeListener(this);
		}

		resource = (IResource) newInput;

		if (resource != null) {
			resource.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
		}

		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
	 */
	@Override
	public Object[] getChildren(Object parentNode) {
		if (parentNode instanceof IFile) {
			try {
				Set<ITask> tasks = new HashSet<ITask>();
				
				PackageJson packageJson = NpmUtil.parsePackageJsonFile((IFile) parentNode);

				if (packageJson != null && packageJson.getScripts() != null) {
					for (String scriptName : packageJson.getScripts().keySet()) {
						tasks.add(
								new NpmScriptTask(((IFile) parentNode), scriptName, null, false, new Location(0, 1)));
					}
				}
				
				return tasks.toArray();
			} catch (Exception ex) {
				NpmPlugin.logError(ex, ex.getMessage());
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(Object)
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof IFile;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			return ((List<?>) inputElement).toArray();
		}
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}
		return EMPTY_ARRAY;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}
}
