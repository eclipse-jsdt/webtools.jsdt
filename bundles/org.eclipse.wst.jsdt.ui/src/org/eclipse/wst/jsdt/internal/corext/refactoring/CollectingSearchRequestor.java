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
package org.eclipse.wst.jsdt.internal.corext.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;

/**
 * Collects the results returned by a <code>SearchEngine</code>.
 */
public class CollectingSearchRequestor extends SearchRequestor {
	private ArrayList fFound;

	public CollectingSearchRequestor() {
		fFound= new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.search.SearchRequestor#acceptSearchMatch(org.eclipse.wst.jsdt.core.search.SearchMatch)
	 */
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		fFound.add(match);
	}

	/**
	 * @return a List of {@link SearchMatch}es (not sorted)
	 */
	public List/*<SearchMatch>*/ getResults() {
		return fFound;
	}
}


