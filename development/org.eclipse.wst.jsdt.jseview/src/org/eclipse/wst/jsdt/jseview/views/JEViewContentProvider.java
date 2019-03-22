/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.jseview.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class JEViewContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object element) {
		if (element instanceof JEAttribute)
			return ((JEAttribute) element).getChildren();
		return JEAttribute.EMPTY;
	}

	public Object getParent(Object element) {
		if (element instanceof JEAttribute)
			return ((JEAttribute) element).getParent();
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof JEAttribute)
			return ((JEAttribute) element).getChildren().length > 0;
		return false;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof JEAttribute)
			return ((JEAttribute) inputElement).getChildren();
		return JEAttribute.EMPTY;
	}

	public void dispose() {
		// do nothing
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

}
