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

package org.eclipse.wst.jsdt.internal.ui.text.spelling.engine;

/**
 * Interface of hashers to compute the phonetic hash for a word.
 *
 * 
 */
public interface IPhoneticHashProvider {

	/**
	 * Returns the phonetic hash for the word.
	 *
	 * @param word
	 *                  The word to get the phonetic hash for
	 * @return The phonetic hash for the word
	 */
	public String getHash(String word);

	/**
	 * Returns an array of characters to compute possible mutations.
	 *
	 * @return Array of possible mutator characters
	 */
	public char[] getMutators();
}
