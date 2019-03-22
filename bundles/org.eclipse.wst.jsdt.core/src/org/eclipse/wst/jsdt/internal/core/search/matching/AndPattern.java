/*******************************************************************************
* Copyright (c) 2016 Red Hat, Inc. 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* 	Contributors:
* 		 Red Hat Inc. - initial API and implementation and/or initial documentation
*******************************************************************************/

package org.eclipse.wst.jsdt.internal.core.search.matching;

import org.eclipse.wst.jsdt.core.search.SearchPattern;

public class AndPattern extends IntersectingPattern {

	private final SearchPattern leftPattern;
	private final SearchPattern rightPattern;
	private SearchPattern currentPattern;

	public AndPattern(SearchPattern leftPattern, SearchPattern rightPattern) {
		super(0, 0);
		this.currentPattern = leftPattern;
		this.leftPattern = leftPattern;
		this.rightPattern = rightPattern;
	}
	
	protected boolean hasNextQuery() {
		if (this.currentPattern == leftPattern) {
			this.currentPattern = rightPattern;
			return true;
		}
		return false;
	}

	protected void resetQuery() {
		this.currentPattern = leftPattern;
	}

}
