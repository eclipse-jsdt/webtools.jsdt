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
package org.eclipse.wst.jsdt.debug.internal.crossfire;

import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.JSON;

/**
 * Helper class for common tracing functions
 * 
 * @since 1.0
 */
public class Tracing {

	public static final String PRINTABLE_LINE_FEED = "\\\\r\\\\n"; //$NON-NLS-1$
	
	/**
	 * Writes the string to system out cleaning it of control chars before printing it
	 * 
	 * @param string
	 */
	public static void writeString(String string) {
		String s = string.replaceAll(JSON.LINE_FEED, PRINTABLE_LINE_FEED);
		s = s.replaceAll("\r", "\\\\r");  //$NON-NLS-1$//$NON-NLS-2$
		s = s.replaceAll("\n", "\\\\n");  //$NON-NLS-1$//$NON-NLS-2$
		System.out.println("[CROSSFIRE]" + s); //$NON-NLS-1$
	}
	
}
