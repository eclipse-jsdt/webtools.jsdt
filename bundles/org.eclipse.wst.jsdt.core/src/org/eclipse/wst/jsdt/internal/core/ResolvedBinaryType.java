/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.core.JavaScriptModelException;

/**
 * Handle representing a binary type that is resolved.
 * The uniqueKey contains the genericTypeSignature of the resolved type. Use BindingKey to decode it.
 */
public class ResolvedBinaryType extends BinaryType {

	private String uniqueKey;

	/*
	 * See class comments.
	 */
	public ResolvedBinaryType(JavaElement parent, String name, String uniqueKey) {
		super(parent, name);
		this.uniqueKey = uniqueKey;
	}

	public String getFullyQualifiedParameterizedName() throws JavaScriptModelException {
		return getFullyQualifiedParameterizedName(getFullyQualifiedName(), this.uniqueKey);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.BinaryType#getKey()
	 */
	public String getKey() {
		return this.uniqueKey;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.core.BinaryType#isResolved()
	 */
	public boolean isResolved() {
		return true;
	}

	/**
	 * @private Debugging purposes
	 */
	protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
		super.toStringInfo(tab, buffer, info, showResolvedInfo);
		if (showResolvedInfo) {
			buffer.append(" {key="); //$NON-NLS-1$
			buffer.append(this.uniqueKey);
			buffer.append("}"); //$NON-NLS-1$
		}
	}

	public JavaElement unresolved() {
		SourceRefElement handle = new BinaryType(this.parent, this.name);
		handle.occurrenceCount = this.occurrenceCount;
		return handle;
	}
}
