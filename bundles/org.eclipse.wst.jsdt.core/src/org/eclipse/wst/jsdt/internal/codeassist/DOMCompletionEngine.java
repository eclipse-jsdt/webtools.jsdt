/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.codeassist;



import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.CompletionContext;
import org.eclipse.wst.jsdt.core.CompletionProposal;
import org.eclipse.wst.jsdt.core.CompletionRequestor;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.ForInStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.Statement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.internal.codeassist.impl.Keywords;

/**
 * A completion engine that use {@link JavaScriptUnit} to create completions.
 * 
 */
public class DOMCompletionEngine {

	private class Scope{
		ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>(); 
	}
	
	/**
	 * Visit the AST tree following the scope for the position. 
	 * Marks all the variables, functions etc on the way for code completion.
	 */
	protected class ScopedCodeAssistVisitor extends HierarchicalASTVisitor {
		
		private int fPosition;
		private Stack<Scope> scopes = new Stack<DOMCompletionEngine.Scope>();
		
		public ScopedCodeAssistVisitor(int position) {
			fPosition= position;
		}
		
		private boolean isInside(ASTNode node) {
			int start= node.getStartPosition();
			int end= start + node.getLength();
			return start <= fPosition && fPosition < end;
		}
		
		public boolean visit(JavaScriptUnit node){
			// Push the global scope.
			scopes.push(new Scope());
			return true;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.codeassist.HierarchicalASTVisitor#endVisit(org.eclipse.wst.jsdt.core.dom.JavaScriptUnit)
		 */
		public void endVisit(JavaScriptUnit node) {
			//Send all the proposals from all scopes
			for (Scope scope : scopes) {
				for (CompletionProposal proposal : scope.proposals) {
					requestor.accept(proposal);
				}
			}
			super.endVisit(node);
		}
		
		public boolean visit(FunctionDeclaration node) {
			final boolean inside = isInside(node);
			if (node.getStartPosition() < fPosition && node.getName() != null) {
				final String nodeName = node.getName().toString();
				CompletionProposal proposal = createProposal(CompletionProposal.METHOD_REF, nodeName, nodeName+"()");
				List parameters = node.parameters();
				if(parameters != null ){
					char[][] parameterNames = new char[parameters.size()][];
					for(int i = 0; i< parameters.size(); i++){
						SingleVariableDeclaration decl =(SingleVariableDeclaration)parameters.get(i);
						parameterNames[i] =decl.getName().toString().toCharArray(); 
					}
					proposal.setParameterNames(parameterNames);
				}
				scopes.peek().proposals.add(proposal);
			}
			if(inside){
				Block body= node.getBody();
				if (body != null) {
					// New function scope
					scopes.push(new Scope());
					body.accept(this);
				}
				visitBackwards(node.parameters());
			}
			return false;
		}
		
		public boolean visit(Initializer node) {
			return isInside(node);
		}		
		
		public boolean visit(Statement node) {
			return isInside(node);
		}
		
		public boolean visit(Block node) {
			if (isInside(node)) {
				visitBackwards(node.statements());
				createKeywordProposal( Keywords.DO);
				createKeywordProposal( Keywords.FOR);
				createKeywordProposal( Keywords.IF);
				createKeywordProposal(Keywords.RETURN);
				createKeywordProposal(Keywords.SWITCH);
				createKeywordProposal(Keywords.THROW);
				createKeywordProposal(Keywords.TRY);
				createKeywordProposal(Keywords.WHILE);
				createKeywordProposal(Keywords.VAR);
				createKeywordProposal(Keywords.FUNCTION);
				createKeywordProposal(Keywords.DELETE);
				createKeywordProposal(Keywords.TYPEOF);
				CompletionProposal proposal = createProposal(CompletionProposal.KEYWORD, Keywords.IF, Keywords.IF);
			}
			return false;
		}
				
		public boolean visit(VariableDeclaration node) {
			if (node.getStartPosition() < fPosition) {
				if(node.getName() != null){
					final String nodeName = node.getName().toString();
					CompletionProposal proposal = createProposal(CompletionProposal.LOCAL_VARIABLE_REF,nodeName,nodeName );
					scopes.peek().proposals.add(proposal);
				}
			}
			return true;
		}
		
		public boolean visit(VariableDeclarationStatement node) {
			visitBackwards(node.fragments());
			return false;
		}		
		
		public boolean visit(VariableDeclarationExpression node) {
			visitBackwards(node.fragments());
			return false;
		}

		public boolean visit(CatchClause node) {
			if (isInside(node)) {
				node.getBody().accept(this);
				node.getException().accept(this);
			}
			return false;			
		}
		
		public boolean visit(ForStatement node) {
			if (isInside(node)) {
				node.getBody().accept(this);
				visitBackwards(node.initializers());
			}
			return false;
		}	

		public boolean visit(ForInStatement node) {
			if (isInside(node)) {
				node.getBody().accept(this);
				node.getIterationVariable().accept(this);
			}
			return false;
		}	

		public boolean visit(TypeDeclarationStatement node) {
			if (node.getStartPosition() + node.getLength() < fPosition) {
				IBinding binding = node.getDeclaration().getName().resolveBinding();
				if(binding != null){
					CompletionProposal proposal = CompletionProposal.create(CompletionProposal.LOCAL_VARIABLE_REF, fPosition);
					proposal.setCompletion(binding.getName().toCharArray());
					requestor.accept(proposal);
				}
				return false;
			}
			return isInside(node);
		}
		
		private void visitBackwards(List<ASTNode> list) {
			for (int i= list.size() - 1; i >= 0; i--) {
				ASTNode curr= list.get(i);
				if (curr.getStartPosition() <  fPosition) {
					curr.accept(this);
				}
			}			
		}
	
	}
	
	
	private final CompletionRequestor requestor;
	private final IJavaScriptProject project;
	private ScopedCodeAssistVisitor visitor;

	/**
	 * 
	 */
	public DOMCompletionEngine(CompletionRequestor aRequestor, IJavaScriptProject aProject) {
		this.requestor = aRequestor;
		this.project = aProject;
	}
	
	public void complete(final JavaScriptUnit unit, final int position, final int offset){
		visitor = new ScopedCodeAssistVisitor(position);
		CompletionContext context = new CompletionContext();
		context.setOffset(position - offset);
		context.setTokenKind(CompletionContext.TOKEN_KIND_UNKNOWN);
		this.requestor.acceptContext(context);
		unit.accept(visitor);
	}
	
	
	private CompletionProposal createKeywordProposal(char[] keyword){
		return createProposal(CompletionProposal.KEYWORD, keyword, keyword);
	}
	private CompletionProposal createProposal(final int kind, final char[] name, final char[] completion){
		final CompletionProposal proposal = CompletionProposal.create(kind, visitor.fPosition);
		proposal.setName(name);
		proposal.setCompletion(completion);
		visitor.scopes.peek().proposals.add(proposal);
		return proposal;		
	}
	private CompletionProposal createProposal(final int kind, final String name, final String completion){
		if(name == null || completion == null ){
			throw new IllegalArgumentException("Completion or Name is missing"); //$NON-NLS-1$
		}
		return createProposal(kind, name.toCharArray(), completion.toCharArray());
	}
}
