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
package org.eclipse.wst.jsdt.internal.ui.text.correction;

import java.util.Arrays;

public class SimilarElement {

	private final int fKind;
	private final String fName;
	private final String[] fTypesParameters;
	private final int fRelevance;

	public SimilarElement(int kind, String name, int relevance) {
		this(kind, name, null, relevance);
	}

	public SimilarElement(int kind, String name, String[] typesParameters, int relevance) {
		fKind= kind;
		fName= name;
		fTypesParameters= typesParameters;
		fRelevance= relevance;
	}

	/**
	 * Gets the kind.
	 * @return Returns a int
	 */
	public int getKind() {
		return fKind;
	}

	/**
	 * Gets the parameter types.
	 * @return Returns a int
	 */
	public String[] getTypesParameter() {
		return fTypesParameters;
	}

	/**
	 * Gets the name.
	 * @return Returns a String
	 */
	public String getName() {
		return fName;
	}

	/**
	 * Gets the relevance.
	 * @return Returns a int
	 */
	public int getRelevance() {
		return fRelevance;
	}

	/* (non-Javadoc)
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof SimilarElement) {
			SimilarElement elem= (SimilarElement) obj;
			return fName.equals(elem.fName) && fKind == elem.fKind && Arrays.equals(fTypesParameters, elem.fTypesParameters);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return fName.hashCode() + fKind;
	}
}
