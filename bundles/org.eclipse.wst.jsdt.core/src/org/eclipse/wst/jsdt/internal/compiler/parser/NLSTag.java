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
package org.eclipse.wst.jsdt.internal.compiler.parser;

public class NLSTag {

	public int start;
	public int end;
	public int lineNumber;
	public int index;

	public NLSTag(int start, int end, int lineNumber, int index) {
		this.start = start;
		this.end = end;
		this.lineNumber = lineNumber;
		this.index = index;
	}

	public String toString() {
		return "NLSTag(" + this.start + "," + this.end + "," + this.lineNumber + ")"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
	}
}
