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
package org.eclipse.wst.jsdt.internal.core.search;

import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;

public abstract class AbstractSearchScope implements IJavaScriptSearchScope {

/* (non-Javadoc)
 * Process the given delta and refresh its internal state if needed.
 * Returns whether the internal state was refreshed.
 */
public abstract void processDelta(IJavaScriptElementDelta delta);

public boolean shouldExclude(String container, String resourceName) {
	return false;
}
}
