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
import org.eclipse.wst.jsdt.core.ast.IEmptyStatement;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
/**
 * 
 *@deprecated
 */
public class EmptyStatement extends Statement implements IEmptyStatement {

	public EmptyStatement(int startPosition, int endPosition) {
		this.sourceStart = startPosition;
		this.sourceEnd = endPosition;
	}

	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
		return flowInfo;
	}

	// Report an error if necessary
	public boolean complainIfUnreachable(FlowInfo flowInfo, BlockScope scope, boolean didAlreadyComplain) {

		// before 1.4, empty statements are tolerated anywhere
		if (scope.compilerOptions().complianceLevel < ClassFileConstants.JDK1_4) {
			return false;
		}
		return super.complainIfUnreachable(flowInfo, scope, didAlreadyComplain);
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {
		return printIndent(tab, output).append(';');
	}

	public void resolve(BlockScope scope) {
		if ((bits & IsUsefulEmptyStatement) == 0) {
			scope.problemReporter().superfluousSemicolon(this.sourceStart, this.sourceEnd);
		} else {
			scope.problemReporter().emptyControlFlowStatement(this.sourceStart, this.sourceEnd);
		}
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	public int getASTType() {
		return IASTNode.EMPTY_STATEMENT;
	
	}

}

