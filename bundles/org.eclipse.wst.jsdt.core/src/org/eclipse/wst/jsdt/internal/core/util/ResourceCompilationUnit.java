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
package org.eclipse.wst.jsdt.internal.core.util;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;

/**
 * An IJavaScriptUnit that retrieves its contents using an IFile
 */
public class ResourceCompilationUnit extends CompilationUnit {

	protected IFile file;

	public ResourceCompilationUnit(IFile file, URI location) {
		super(null/*no contents*/, location == null ? file.getFullPath().toString() : location.getPath(), null/*encoding is used only when retrieving the contents*/);
		this.file = file;
	}

	public char[] getContents() {
		if (this.contents != null)
			return this.contents;   // answer the cached source

		// otherwise retrieve it
		try {
			return Util.getResourceContentsAsCharArray(this.file);
		} catch (CoreException e) {
			return CharOperation.NO_CHAR;
		}
	}
}
