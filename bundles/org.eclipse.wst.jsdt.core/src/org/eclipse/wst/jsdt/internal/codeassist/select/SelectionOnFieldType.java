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
package org.eclipse.wst.jsdt.internal.codeassist.select;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;

public class SelectionOnFieldType extends FieldDeclaration {
	public SelectionOnFieldType(TypeReference type) {
		super();
		this.sourceStart = type.sourceStart;
		this.sourceEnd = type.sourceEnd;
		this.type = type;
		this.name = CharOperation.NO_CHAR;
	}
	public StringBuffer printStatement(int tab, StringBuffer output) {
		return type.print(tab, output).append(';');
	}
}
