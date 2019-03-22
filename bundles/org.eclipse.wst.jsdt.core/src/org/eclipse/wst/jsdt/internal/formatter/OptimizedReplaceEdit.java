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
package org.eclipse.wst.jsdt.internal.formatter;

public class OptimizedReplaceEdit {

	int offset;
	int length;
	String replacement;

	public OptimizedReplaceEdit(int offset, int length, String replacement) {
		this.offset = offset;
		this.length = length;
		this.replacement = replacement;
	}

	public String toString() {
		return "(" + this.offset + ", length " + this.length + " :>" + this.replacement + "<"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
	}
}
