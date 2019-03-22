/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.refactoring.nls.search;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;

public class CompilationUnitEntry implements IAdaptable {
	
	private final String fMessage;
	private final IJavaScriptUnit fCompilationUnit;

	public CompilationUnitEntry(String message, IJavaScriptUnit compilationUnit) {
		fMessage= message;
		fCompilationUnit= compilationUnit;
	}

	public String getMessage() {
		return fMessage;
	}

	public IJavaScriptUnit getCompilationUnit() {
		return fCompilationUnit;
	}
	
	public Object getAdapter(Class adapter) {
		if (IJavaScriptUnit.class.equals(adapter))
			return getCompilationUnit();
		return null;
	}

}
