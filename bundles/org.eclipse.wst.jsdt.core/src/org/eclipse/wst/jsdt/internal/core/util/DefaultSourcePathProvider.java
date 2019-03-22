/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.JavaScriptCore;

public abstract class DefaultSourcePathProvider {
	public DefaultSourcePathProvider() {
	}

	public IIncludePathEntry[] getDefaultSourcePaths(IProject p) {
		return new IIncludePathEntry[]{JavaScriptCore.newSourceEntry(p.getFullPath())};
	}
}
