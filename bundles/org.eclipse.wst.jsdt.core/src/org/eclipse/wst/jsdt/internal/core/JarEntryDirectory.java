/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.IJarEntryResource;

public class JarEntryDirectory extends JarEntryResource {
	private IJarEntryResource[] children;

	public JarEntryDirectory(String simpleName) {
		super(simpleName);
	}

	public JarEntryResource clone(Object newParent) {
		JarEntryDirectory dir = new JarEntryDirectory(this.simpleName);
		dir.setParent(newParent);
		int length = this.children.length;
		if (length > 0) {
			IJarEntryResource[] newChildren = new IJarEntryResource[length];
			for (int i = 0; i < length; i++) {
				JarEntryResource child = (JarEntryResource) this.children[i];
				newChildren[i] = child.clone(dir);
			}
			dir.setChildren(newChildren);
		}
		return dir;
	}

	public IJarEntryResource[] getChildren() {
		return this.children;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(new byte[0]);
	}

	public boolean isFile() {
		return false;
	}

	public void setChildren(IJarEntryResource[] children) {
		this.children = children;
	}

	public String toString() {
		return "JarEntryDirectory["+getEntryName()+"]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
