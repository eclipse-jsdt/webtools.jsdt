/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core;

/**
 * Utility class with common text handling functions in it
 * 
 * @since 1.0
 */
public final class TextUtils {

	private TextUtils() {/*no instantiation*/}

	/**
	 * Shortens the given string to be no longer than the given threshold. Any removed characters are 
	 * replaced with <code>...</code>.<br><br>
	 * For example: <code>shortenText("this is a string to shorten text", 8)</code> would result in the string:
	 * <code>"thi...xt"</code>
	 * 
	 * @param string
	 * @param threshold TODO
	 * @return the given string shortened to be no longer than the given threshold
	 */
	public static String shortenText(String string, int threshold) {
		int length = string.length(); 
		if (length > threshold) {
			int chomp = length - threshold + 3;
			int begin = Math.round(threshold/2)-1;
			StringBuffer buff = new StringBuffer();
			buff.append(string.substring(0, begin)).append("...").append(string.substring(begin+chomp)); //$NON-NLS-1$
			return buff.toString();
		}
		return string;
	}
}
