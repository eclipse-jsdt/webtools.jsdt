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

package org.eclipse.wst.jsdt.internal.corext.util;

import java.util.LinkedHashMap;

/**
 * 
 */
public class LRUMap extends LinkedHashMap {
	
	private static final long serialVersionUID= 1L;
	private final int fMaxSize;
	
	public LRUMap(int maxSize) {
		super(maxSize, 0.75f, true);
		fMaxSize= maxSize;
	}
	
	protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
		return size() > fMaxSize;
	}
}
