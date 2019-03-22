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
package org.eclipse.wst.jsdt.internal.core.search;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.internal.compiler.env.AccessRestriction;

/**
 * A <code>IRestrictedAccessTypeRequestor</code> collects search results from a <code>searchAllTypeNames</code>
 * query to a <code>SearchBasicEngine</code> providing restricted access information when a type is accepted.
 * @see org.eclipse.wst.jsdt.core.search.TypeNameRequestor
 */
public interface IRestrictedAccessBindingRequestor {

	public boolean acceptBinding(int type, int modifiers, char[] packageName, char[] simpleBindingName, String path, AccessRestriction access);

	public String getFoundPath();

	public void reset();

	public void setExcludePath(String excludePath);

	public ArrayList getFoundPaths();

}
