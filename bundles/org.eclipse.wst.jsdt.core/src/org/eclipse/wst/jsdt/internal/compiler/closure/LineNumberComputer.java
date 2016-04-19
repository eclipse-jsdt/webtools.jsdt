/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation 
 **/
package org.eclipse.wst.jsdt.internal.compiler.closure;

import java.util.ArrayList;

/**
 * Helper for computing line number table.
 * 
 * @author Gorkem Ercan
 *
 */
public final class LineNumberComputer {

	public static int[] computeLineTable(String source) {
		ArrayList<Integer> lineEndOffsets = new ArrayList<Integer>();
		for (int index = 0; index < source.length(); index++) {
			char ch = source.charAt(index);
			if (isLineTerminator(ch)) {
				if (index + 1 < source.length() && ch == '\r' && source.charAt(index + 1) == '\n') {
					index++;
				}
				lineEndOffsets.add(index);
			}
		}
		return toIntArray(lineEndOffsets);
	}

	private static int[] toIntArray(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	private static boolean isLineTerminator(char ch) {
		switch (ch) {
			case '\n' : // Line Feed
			case '\r' : // Carriage Return
			case '\u2028' : // Line Separator
			case '\u2029' : // Paragraph Separator
				return true;
			default :
				return false;
		}
	}

}
