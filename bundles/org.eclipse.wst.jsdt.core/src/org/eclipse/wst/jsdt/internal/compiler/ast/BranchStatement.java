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
import org.eclipse.wst.jsdt.core.ast.IBranchStatement;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
/**
 * 
 *@deprecated
 */
public abstract class BranchStatement extends Statement implements IBranchStatement {

	public char[] label;
	public SubRoutineStatement[] subroutines;
	public int initStateIndex = -1;

/**
 * BranchStatement constructor comment.
 */
public BranchStatement(char[] label, int sourceStart,int sourceEnd) {
	this.label = label ;
	this.sourceStart = sourceStart;
	this.sourceEnd = sourceEnd;
}

public void resolve(BlockScope scope) {
	// nothing to do during name resolution
}
public int getASTType() {
	return IASTNode.BRANCH_STATEMENT;

}
}
