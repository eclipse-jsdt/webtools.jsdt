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

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.internal.compiler.env.AccessRuleSet;

/**
 * Collects the resource paths reported by a client to this search requestor.
 */
public class PathCollector extends IndexQueryRequestor {

	/* a set of resource paths */
	public HashSet paths = new HashSet(5);

	/* (non-Javadoc)
	 * @seeIndexQueryRequestor#acceptIndexMatch(IndexRecord, SearchParticipant, SearchPattern)
	 */
	public boolean acceptIndexMatch(String documentPath, SearchPattern indexRecord, SearchParticipant participant, AccessRuleSet access) {
		paths.add(documentPath);
		return true;
	}

	/**
	 * Returns the paths that have been collected.
	 */
	public String[] getPaths() {
		String[] result = new String[this.paths.size()];
		int i = 0;
		for (Iterator iter = this.paths.iterator(); iter.hasNext();) {
			result[i++] = (String)iter.next();
		}
		return result;
	}
}
