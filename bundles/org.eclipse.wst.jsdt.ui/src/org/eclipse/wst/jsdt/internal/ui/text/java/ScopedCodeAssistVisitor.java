/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.stream.Collectors;

import org.eclipse.wst.jsdt.core.CompletionProposal;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Assignment.Operator;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.DefaultASTVisitor;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.ForInStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;

/**
 * Visit the AST following the scope for the position.
 * Marks all the variables, functions etc on the way for code completion.
 */
@SuppressWarnings("nls")
public class ScopedCodeAssistVisitor extends DefaultASTVisitor {

	IdentifierProposal currentIdentifier;
	IdentifierProposal objectLiteralFieldParent;
	int filePosition;
	boolean global;
	public Stack<Scope> scopes = new Stack<Scope>();
	List<IdentifierProposal> identifiers = new ArrayList<IdentifierProposal>();

	public class Scope {
		ArrayList<IdentifierProposal> proposals = new ArrayList<IdentifierProposal>();
	}

	public List<IdentifierProposal> getIdentifiers() {
		return identifiers;
	}

	public List<IdentifierProposal> getIdentifiers(String key) {
		List<IdentifierProposal> relevantProposals = getIdentifiers();
		return getIdentifiers(key, relevantProposals);
	}

	public void clearIdentifierProposals() {
		this.scopes.clear();
	}

	public ScopedCodeAssistVisitor(int position) {
		filePosition = position;
	}

	/**
	 * Determines whether the file position is inside the given ASTNode.
	 */
	private boolean isInside(ASTNode node) {
		int start = node.getStartPosition();
		int end = start + node.getLength();

		return (start <= filePosition) && (filePosition < end);
	}

	public boolean visit(JavaScriptUnit node) {
		// Push the global scope.
		scopes.push(new Scope());
		return true;
	}

	public boolean visit(Assignment node) {
		Operator operator = node.getOperator();
		if (operator.equals(Operator.ASSIGN)) {
			String leftHandSide = node.toString().split("=")[0];
			String name;
			if (leftHandSide.contains(".")) {
				name = node.toString().split("\\.")[0];
			} else {
				name = leftHandSide;
			}

			// Check if this identifier has previously been declared. If it has not been declared,
			// then we add it as a globally defined identifier.
			IdentifierProposal previouslyDeclaredProposal = identifierPreviouslyDeclared(name);

			if (previouslyDeclaredProposal == null) {
				IdentifierProposal proposal = new IdentifierProposal(name);
				proposal.setIsGlobal(true);
				addIdentifier(proposal, scopes.get(0).proposals);
				currentIdentifier = proposal;
			} else {
				currentIdentifier = previouslyDeclaredProposal;
			}
		}

		return true;
	}

	public void endVisit(Assignment node) {
		currentIdentifier = null;
	}

	public IdentifierProposal addParent(String expression) {
		String[] splitExpression = expression.split("\\.");
		IdentifierProposal parent = null, root = null;

		for (String identifier : splitExpression) {
			if (parent == null) {
				parent = new IdentifierProposal(identifier);
				parent.updateScope(scopes);
				root = parent;
				addIdentifier(parent, scopes.peek().proposals);
			} else {
				IdentifierProposal field = new IdentifierProposal(identifier);
				parent.addField(field);
				parent = field;
			}
		}

		return root;
	}

	public void endVisit(FieldAccess node) {
		IdentifierProposal field = new IdentifierProposal(node.getName().toString());
		IdentifierProposal parent = findLastFieldFromExpression(node.getExpression());

		if (parent == null) {
			parent = addParent(node.getExpression().toString());
		}

		if ((parent != null) && !identifierExists(field.getName(), parent.getFields())) {
			addIdentifier(field, parent.getFields());
			field.addParent(parent);
			currentIdentifier = field;
		}
	}

	public void endVisit(FunctionInvocation node) {
		// Remove the parenthesis (e.g. one.two() becomes one.two).
		String nodeName = node.toString().substring(0, node.toString().length() - 2);

		IdentifierProposal identifier = getIdentifier(nodeName);

		if (identifier != null) {
			identifier.setType(IdentifierType.FUNCTION);
		}
	}

	public boolean visit(VariableDeclarationFragment node) {
		IdentifierProposal proposal = new IdentifierProposal(node.getName().getIdentifier());
		proposal.updateScope(scopes);
		addIdentifier(proposal, scopes.peek().proposals);
		currentIdentifier = proposal;
		return true;
	}

	public void endVisit(VariableDeclarationFragment node) {
		currentIdentifier = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.codeassist.HierarchicalASTVisitor#endVisit(org.eclipse.wst.jsdt.core.dom.JavaScriptUnit)
	 */
	public void endVisit(JavaScriptUnit node) {
		identifiers.addAll(scopes.peek().proposals);
		scopes.pop();
		super.endVisit(node);
	}

	public boolean visit(FunctionDeclaration node) {
		Expression methodName = node.getMethodName();
		IdentifierProposal proposal = null;
		List<String> parameterNames = new ArrayList<String>();
		if (currentIdentifier == null) {
			String name;

			if (methodName != null) {
				name = methodName.toString();
				proposal = new IdentifierProposal(name);
				proposal.updateScope(scopes);
				addIdentifier(proposal, scopes.peek().proposals);
			}
		} else {
			proposal = currentIdentifier;
		}

		if (proposal != null) {
			proposal.setType(IdentifierType.FUNCTION);
			parameterNames = (List<String>) node.parameters().stream().map(k -> ((SingleVariableDeclaration) k).getName().toString()).collect(Collectors.toList());
			proposal.setParameters(parameterNames);
			proposal.setJSdoc(node.getJavadoc());
		}

		Block body = node.getBody();
		if (body != null) {
			// New function scope
			scopes.push(new Scope());
			for (String parameterName : parameterNames) {
				IdentifierProposal parameterProposal = new IdentifierProposal(parameterName);
				addIdentifier(parameterProposal, scopes.peek().proposals);
			}
			body.accept(this);
		}
		visitBackwards(node.parameters());

		// Doesn't recognize anything inside function body.
		return false;
	}

	public void endVisit(FunctionDeclaration node) {
		if (isInside(node)) {
			identifiers.addAll(scopes.peek().proposals);
		}
		scopes.pop();
	}

	public boolean visit(Initializer node) {
		return isInside(node);
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

	public boolean visit(ObjectLiteralField node) {
		IdentifierProposal field = new IdentifierProposal(node.getFieldName().toString());
		IdentifierProposal parent = currentIdentifier;
		objectLiteralFieldParent = parent;

		if ((parent != null) && !identifierExists(field.getName(), parent.getFields())) {
			addIdentifier(field, parent.getFields());
			field.addParent(parent);
			currentIdentifier = field;
		}

		return true;
	}

	public void endVisit(ObjectLiteralField node) {
		currentIdentifier = objectLiteralFieldParent;
	}

	public boolean visit(TypeDeclarationStatement node) {
		if ((node.getStartPosition() + node.getLength()) < filePosition) {
			IBinding binding = node.getDeclaration().getName().resolveBinding();
			if (binding != null) {
				CompletionProposal proposal = CompletionProposal.create(CompletionProposal.LOCAL_VARIABLE_REF, filePosition);
				proposal.setCompletion(binding.getName().toCharArray());
			}
			return false;
		}
		return isInside(node);
	}

	private void visitBackwards(List<ASTNode> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			ASTNode curr = list.get(i);
			curr.accept(this);
		}
	}

//	private void printIdentifierTree() {
//		List<IdentifierProposal> rootProposals = getIdentifiers();
//		for (IdentifierProposal identifier : rootProposals) {
//			System.out.println("> " + identifier.getName());
//			printIdentifierTree(1, identifier.getFields());
//		}
//	}
//
//	private void printIdentifierTree(int indent, List<IdentifierProposal> proposals) {
//		for (IdentifierProposal identifier : proposals) {
//			System.out.println("> " + new String(new char[indent]).replace("\0", "    ") + identifier.getName());
//			printIdentifierTree(indent + 1, identifier.getFields());
//		}
//	}


	/**
	 * Finds the identifier of the last field in a chain of field accessess.
	 * For example, if the expression is 'one.two.three.four', this method would find the identifier
	 * that corresponds to 'four'.
	 * @param fieldExpression The expression.
	 * @return The correspondning identifier.
	 */
	private IdentifierProposal findLastFieldFromExpression(Expression fieldExpression) {
		String rootId, parentId;

		if (fieldExpression.toString().contains(".")) {
			String[] splitExpression = fieldExpression.toString().split("\\.");
			rootId = splitExpression[0];
			parentId = splitExpression[splitExpression.length - 1];
			IdentifierProposal root = getIdentifierFromScopes(rootId);
			if (root == null) {
				return null;
			}
			return findField(parentId, root);
		} else {
			return getIdentifierFromScopes(fieldExpression.toString());
		}
	}

	/**
	 * Gets an identifier from the relevant scopes. Starts with most recently pushed scope.
	 * @param identifierName
	 * @return
	 */
	private IdentifierProposal getIdentifierFromScopes(String identifierName) {
		ListIterator<Scope> reverseScopesIterator = scopes.listIterator(scopes.size());
		while(reverseScopesIterator.hasPrevious()) {
			Scope scope = reverseScopesIterator.previous();
			for (IdentifierProposal proposal : scope.proposals) {
				if (proposal.getName().equals(identifierName)) {
					return proposal;
				}
			}
		}

		return null;
	}

	/**
	 * Finds the field in an identifier's field tree with the root node root. If the root node's tree
	 * does not contain an identifier with the given field name, this method returns null.
	 * @param fieldName The name of the field being searched for.
	 * @param root The root node that this search is run on.
	 * @return True if a field exists with the given name. Null otherwise.
	 */
	private IdentifierProposal findField(String fieldName, IdentifierProposal root) {
		if (root.getName().equals(fieldName)) {
			return root;
		}

		for (IdentifierProposal fieldProposal : root.getFields()) {
			IdentifierProposal found = findField(fieldName, fieldProposal);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
	/**
	 * Adds the given identifier proposal to the list of sibling identifiers. If the proposal is already declared in the
	 * given siblings then nothing happens.
	 * @param proposal
	 * @param siblingIdentifiers
	 */
	private void addIdentifier(IdentifierProposal proposal, List<IdentifierProposal> siblingIdentifiers) {
		if (!identifierExists(proposal.getName(), siblingIdentifiers)) {
			siblingIdentifiers.add(proposal);
		}
	}

	/**
	 * Returns whether an identifier exists in sibling identifiers with the same name as the given identifier name.
	 * @param identifierName The given identifier name to which all identifiers are compared.
	 * @param siblingIdentifiers The sibling identifiers.
	 * @return True if an identifier exists with the same name in the sibling identifiers.
	 */
	private boolean identifierExists(String identifierName, List<IdentifierProposal> siblingIdentifiers) {
		return siblingIdentifiers.stream().filter(k -> k.getName().equals(identifierName)).collect(Collectors.toList()).size() > 0;
	}

	/**
	 * Check if an identifier has previously been declared in the current scope or before the current scope.
	 * @param candidateProposalName The name of the identifier being searched for.
	 * @return The identifier matching identifier, if found. Null if not found.
	 */
	private IdentifierProposal identifierPreviouslyDeclared(String candidateProposalName) {
		for (Scope scope : scopes) {
			for (IdentifierProposal p : scope.proposals) {
				if (candidateProposalName.equals(p.getName())) {
					return p;
				}
			}
		}

		return null;
	}

	private List<IdentifierProposal> getIdentifiers(String key, List<IdentifierProposal> proposals) {
		if (key.contains(".")) {
			String[] splitString = key.split("\\.", 2);
			String firstIdentifier = splitString[0];
			String rest = splitString[1];
			for (IdentifierProposal identifier : proposals) {
				if (identifier.getName().equals(firstIdentifier)) {
					return getIdentifiers(rest, identifier.getFields());
				}
			}
			return Collections.emptyList();
		} else {
			return proposals.stream().filter(k -> k.getName().startsWith(key) || k.getCamelCaseName().startsWith(key)).collect(Collectors.toList());
		}
	}

	private IdentifierProposal getIdentifier(String key) {
		List<IdentifierProposal> relevantProposals = getCurrentIdentifiers();
		return getIdentifier(key, relevantProposals);
	}

	private IdentifierProposal getIdentifier(String key, List<IdentifierProposal> proposals) {
		if (key.contains(".")) {
			String[] splitString = key.split("\\.", 2);
			String firstIdentifier = splitString[0];
			String rest = splitString[1];
			for (IdentifierProposal identifier : proposals) {
				if (identifier.getName().equals(firstIdentifier)) {
					return getIdentifier(rest, identifier.getFields());
				}
			}
		} else {
			List<IdentifierProposal> matches = proposals.stream().filter(k -> k.getName().equals(key)).collect(Collectors.toList());
			if (!matches.isEmpty()) {
				return matches.get(0);
			}
		}
		return null;
	}

	private List<IdentifierProposal> getCurrentIdentifiers() {
		List<IdentifierProposal> scopedIdentifiers = new ArrayList<IdentifierProposal>();
		for (Scope scope : scopes) {
			scopedIdentifiers.addAll(0, scope.proposals);
		}
		return scopedIdentifiers;
	}

}