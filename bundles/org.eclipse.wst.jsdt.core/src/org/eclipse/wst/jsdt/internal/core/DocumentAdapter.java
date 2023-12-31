/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.wst.jsdt.core.IBuffer;

/*
 * Adapts an IBuffer to IDocument
 */
public class DocumentAdapter extends Document {

	private IBuffer buffer;

	public DocumentAdapter(IBuffer buffer) {
		super(buffer.getContents());
		this.buffer = buffer;
	}

	public void set(String text) {
		super.set(text);
		this.buffer.setContents(text);
	}

	public void replace(int offset, int length, String text) throws BadLocationException {
		super.replace(offset, length, text);
		this.buffer.replace(offset, length, text);
	}

}
