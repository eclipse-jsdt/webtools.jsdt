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
import org.eclipse.wst.jsdt.core.ast.IJsDocReturnStatement;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

/**
 * 
 *@deprecated
 */
public class JavadocReturnStatement extends ReturnStatement implements IJsDocReturnStatement {

	public JavadocReturnStatement(int s, int e) {
		super(null, s, e);
		this.bits |= (ASTNode.InsideJavadoc | ASTNode.Empty);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Statement#resolve(org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public void resolve(BlockScope scope) {
		MethodScope methodScope = scope.methodScope();
		MethodBinding methodBinding = null;
		TypeBinding methodType =
			(methodScope.referenceContext instanceof AbstractMethodDeclaration)
				? ((methodBinding = ((AbstractMethodDeclaration) methodScope.referenceContext).binding) == null
					? null
					: methodBinding.returnType)
				: TypeBinding.VOID;
		if (methodType == null || methodType == TypeBinding.VOID) {
			scope.problemReporter().javadocUnexpectedTag(this.sourceStart, this.sourceEnd);
		} else if ((this.bits & ASTNode.Empty) != 0) {
			scope.problemReporter().javadocEmptyReturnTag(this.sourceStart, this.sourceEnd, scope.getDeclarationModifiers());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.Statement#printStatement(int, java.lang.StringBuffer)
	 */
	public StringBuffer printStatement(int tab, StringBuffer output) {
		printIndent(tab, output).append("return"); //$NON-NLS-1$
		if ((this.bits & ASTNode.Empty) == 0)
			output.append(' ').append(" <not empty>"); //$NON-NLS-1$
		return output;
	}

	/* (non-Javadoc)
	 * Redefine to capture javadoc specific signatures
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode#traverse(org.eclipse.wst.jsdt.internal.compiler.ASTVisitor, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	/* (non-Javadoc)
	 * Redefine to capture javadoc specific signatures
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode#traverse(org.eclipse.wst.jsdt.internal.compiler.ASTVisitor, org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	public int getASTType() {
		return IASTNode.JSDOC_RETURN_STATEMENT;
	
	}
}
