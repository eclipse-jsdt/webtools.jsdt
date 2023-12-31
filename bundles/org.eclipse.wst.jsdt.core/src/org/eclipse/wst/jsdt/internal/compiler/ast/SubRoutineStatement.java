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
import org.eclipse.wst.jsdt.core.ast.ISubRoutineStatement;


/**
 * Extra behavior for statements which are generating subroutines
 * @deprecated
 */
public abstract class SubRoutineStatement extends Statement implements ISubRoutineStatement {

	 

	 
 
 
 


 
	public abstract boolean isSubRoutineEscaping();

	public int getASTType() {
		return IASTNode.SUB_ROUTINE_STATEMENT;
	
	}
}
