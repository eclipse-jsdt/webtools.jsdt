/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.refactoring.nls.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

public class FileEntry implements IAdaptable {

	private IFile fPropertiesFile;
	private String fMessage;

	public FileEntry(IFile propertiesFile, String message) {
		fPropertiesFile= propertiesFile;
		fMessage= message;
	}
	
	public IFile getPropertiesFile() {
		return fPropertiesFile;
	}

	public String getMessage() {
		return fMessage;
	}
	
	public String toString() {
		return fMessage;
	}
	
	public Object getAdapter(Class adapter) {
		if (IResource.class.equals(adapter))
			return fPropertiesFile;
		return null;
	}
}
