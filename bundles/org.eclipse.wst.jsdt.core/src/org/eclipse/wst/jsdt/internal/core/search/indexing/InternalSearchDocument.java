/*******************************************************************************
 * Copyright (c) 2004, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.search.indexing;

import org.eclipse.wst.jsdt.internal.core.index.Index;

/**
 * Internal search document implementation
 */
public class InternalSearchDocument {
	Index index;
	private String containerRelativePath;
	/*
	 * Hidden by API SearchDocument subclass
	 */
	public void addIndexEntry(char[] category, char[] key) {
		if (this.index != null)
			index.addIndexEntry(category, key, getContainerRelativePath());
	}
	private String getContainerRelativePath() {
		if (this.containerRelativePath == null)
			this.containerRelativePath = this.index.containerRelativePath(getPath());
		return this.containerRelativePath;
	}
	/*
	 * Hidden by API SearchDocument subclass
	 */
	public void removeAllIndexEntries() {
		if (this.index != null)
			index.remove(getContainerRelativePath());
	}
	/*
	 * Hidden by API SearchDocument subclass
	 */
	public String getPath() {
		return null; // implemented by subclass
	}
}
