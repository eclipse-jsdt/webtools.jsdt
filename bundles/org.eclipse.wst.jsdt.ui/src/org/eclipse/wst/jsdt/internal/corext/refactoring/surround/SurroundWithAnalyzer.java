/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.surround;

import java.util.List;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BodyDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.Message;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.Statement;
import org.eclipse.wst.jsdt.core.dom.SuperConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.VariableDeclaration;
import org.eclipse.wst.jsdt.internal.corext.dom.ASTNodes;
import org.eclipse.wst.jsdt.internal.corext.dom.Selection;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.wst.jsdt.internal.corext.refactoring.base.JavaStatusContext;
import org.eclipse.wst.jsdt.internal.corext.refactoring.util.CodeAnalyzer;

public class SurroundWithAnalyzer extends CodeAnalyzer {
	
	private VariableDeclaration[] fLocals;

	public SurroundWithAnalyzer(IJavaScriptUnit cunit, Selection selection) throws JavaScriptModelException {
		super(cunit, selection, false);
	}
	
	public Statement[] getSelectedStatements() {
		if (hasSelectedNodes()) {
			return (Statement[])internalGetSelectedNodes().toArray(new Statement[internalGetSelectedNodes().size()]);
		} else {
			return new Statement[0];
		}
	}
	
	public VariableDeclaration[] getAffectedLocals() {
		return fLocals;
	}
	
	public BodyDeclaration getEnclosingBodyDeclaration() {
		ASTNode node= getFirstSelectedNode();
		if (node == null)
			return null;
		return (BodyDeclaration)ASTNodes.getParent(node, BodyDeclaration.class);
	}
	
	protected boolean handleSelectionEndsIn(ASTNode node) {
		return true;
	}
	
	public void endVisit(JavaScriptUnit node) {
		postProcessSelectedNodes(internalGetSelectedNodes());
		BodyDeclaration enclosingNode= null;
		superCall: {
			if (getStatus().hasFatalError())
				break superCall;
			if (!hasSelectedNodes()) {
				ASTNode coveringNode= getLastCoveringNode();
				if (coveringNode instanceof Block) {
					Block block= (Block)coveringNode;
					Message[] messages= ASTNodes.getMessages(block, ASTNodes.NODE_ONLY);
					if (messages.length > 0) {
						invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_compile_errors, 
							JavaStatusContext.create(getCompilationUnit(), block)); 
						break superCall;
					}
				}
				invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_doesNotCover); 
				break superCall;
			}
			enclosingNode= (BodyDeclaration)ASTNodes.getParent(getFirstSelectedNode(), BodyDeclaration.class);
			if (!(enclosingNode instanceof FunctionDeclaration) && !(enclosingNode instanceof Initializer)) {
				invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_doesNotContain);  
				break superCall;
			}
			if (!onlyStatements()) {
				invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_onlyStatements); 
			}
			fLocals= LocalDeclarationAnalyzer.perform(enclosingNode, getSelection());
		}
		super.endVisit(node);
	}
	
	public void endVisit(SuperConstructorInvocation node) {
		if (getSelection().getEndVisitSelectionMode(node) == Selection.SELECTED) {
			invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_cannotHandleSuper, JavaStatusContext.create(fCUnit, node)); 
		}
		super.endVisit(node);
	}
	
	public void endVisit(ConstructorInvocation node) {
		if (getSelection().getEndVisitSelectionMode(node) == Selection.SELECTED) {
			invalidSelection(RefactoringCoreMessages.SurroundWithTryCatchAnalyzer_cannotHandleThis, JavaStatusContext.create(fCUnit, node)); 
		}
		super.endVisit(node);
	}
	
	protected void postProcessSelectedNodes(List selectedNodes) {
		if (selectedNodes == null || selectedNodes.size() == 0)
			return;
		if (selectedNodes.size() == 1) {
			ASTNode node= (ASTNode)selectedNodes.get(0);
			if (node instanceof Expression && node.getParent() instanceof ExpressionStatement) {
				selectedNodes.clear();
				selectedNodes.add(node.getParent());
			}
		}
	}
	
	private boolean onlyStatements() {
		ASTNode[] nodes= getSelectedNodes();
		for (int i= 0; i < nodes.length; i++) {
			if (!(nodes[i] instanceof Statement))
				return false;
		}
		return true;
	}	

}
