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
package org.eclipse.wst.jsdt.js.grunt.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.js.common.build.system.Task;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.grunt.GruntPlugin;
import org.eclipse.wst.jsdt.js.grunt.internal.util.GruntVisitor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class GruntFileContentProvider implements ITreeContentProvider, IResourceChangeListener {
	
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

		    this.viewer =  viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
	 */
	@Override
	public Object[] getChildren(Object parentNode) {
		Object[] children = null;
		ArrayList<Task> tasks = new ArrayList<>();
		if (parentNode instanceof IFile) {
			try {
				JavaScriptUnit unit = ASTUtil.getJavaScriptUnit((IFile) parentNode);
				GruntVisitor visitor = new GruntVisitor();
				unit.accept(visitor);
				children = visitor.getTasks().toArray();
				for (Object o : children) {
					tasks.add(new Task(o.toString(), (IFile) parentNode, false));
				}
			} catch (JavaScriptModelException e) {
				GruntPlugin.logError(e, e.getMessage());
			}
			return tasks.toArray();
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
		return false;
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
