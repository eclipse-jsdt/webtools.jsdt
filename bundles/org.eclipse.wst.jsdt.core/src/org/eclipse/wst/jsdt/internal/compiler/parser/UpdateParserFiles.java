/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.parser;

import java.io.IOException;

public class UpdateParserFiles {


	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			printUsage();
			return;
		}
		Parser.buildFilesFromLPG(args[0], args[1]);
	}

	public static void printUsage() {
		System.out.println("Usage: UpdateParserFiles <path to javadcl.java> <path to javahdr.java>"); //$NON-NLS-1$
		System.out.println("e.g. UpdateParserFiles c:/javadcl.java c:/javahdr.java"); //$NON-NLS-1$
	}
}
