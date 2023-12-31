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
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.INumberLiteral;
/**
 * 
 *@deprecated
 */
public abstract class NumberLiteral extends Literal implements INumberLiteral {

	public char[] source;
	
	public NumberLiteral(char[] token, int s, int e) {
		
		this(s,e) ;
		source = token ;
	}
	
	public NumberLiteral(int s, int e) {
		super (s,e) ;
	}
	
	public boolean isValidJavaStatement(){

		return false ;
	}
	
	public char[] source(){

		return source;
	}
	public int getASTType() {
		return IASTNode.NUMBER_LITERAL;
	
	}
}
