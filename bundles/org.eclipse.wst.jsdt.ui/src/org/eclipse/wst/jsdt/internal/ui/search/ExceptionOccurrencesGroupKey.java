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

package org.eclipse.wst.jsdt.internal.ui.search;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;

public class ExceptionOccurrencesGroupKey extends JavaElementLine {
	private boolean fIsException;
	
	/**
	 * @param element either an IJavaScriptUnit or an IClassFile
	 */
	public ExceptionOccurrencesGroupKey(IJavaScriptElement element, int line, String lineContents, boolean isException) {
		super(element, line, lineContents);
		fIsException= isException;
	}

	public boolean isException() {
		return fIsException;
	}

	public void setException(boolean isException) {
		fIsException= isException;
	}
}
