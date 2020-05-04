/*******************************************************************************
 * Copyright (c) 2016, 2020 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;

/**
 * Class/Function scope. Contains declared classes, functions and variables.
 *
 * @since 2.0
 *
 */
public class Scope {

	/**
	 * arguments token
	 */
	private static final String ARGUMENTS = "arguments"; //$NON-NLS-1$

	/**
	 * Declarations contained in this scope
	 */
	private final Map<String, IDeclaration> declarations = new LinkedHashMap<>();

	/**
	 * Parent scope
	 */
	private final Scope parent;

	/**
	 * Scope depth. Root scope's depth is 0
	 */
	protected final int depth;

	/**
	 * Scope root in AST
	 */
	protected final ASTNode rootNode;

	/**
	 * Special 'arguments' variable
	 */
	private VariableDeclaration arguments;

	/**
	 * counter for declarations and references contained in the scope
	 */
	private int referenceCount = 0;


	Scope(Scope parent, ASTNode rootNode) {
		this.parent = parent;
		this.rootNode = rootNode;
		this.depth = (parent == null ? 0 : parent.depth + 1);
	}

	public int getDepth() {
		return depth;
	}

	public ASTNode getRootNode() {
		return rootNode;
	}

	public Scope getParentScope() {
		return parent;
	}

	public Scope getParent() {
		return parent;
	}

	public Collection<IDeclaration> getDeclarations() {
		return declarations.values();
	}

	/**
	 * Return global (highest) scope
	 *
	 * @return global (highest) scope
	 */
	public Scope getGlobalScope() {
		Scope result = this;
		while (result.getParent() != null) {
			result = result.getParent();
		}
		return result;
	}

	/**
	 * Creates variable declaration and puts it into scope.
	 *
	 * @param name
	 *            variable name
	 * @param node
	 *            variable node in the AST
	 * @param varKind
	 *            kind of variable
	 * @return newly created declaration
	 */
	IDeclaration declareVariable(String name, SimpleName node, VariableDeclaration.VariableKind varKind) {
		VariableDeclaration declaration = new VariableDeclaration(node, this, registerReference(), varKind);
		declarations.put(name, declaration);
		return declaration;
	}

	/**
	 * Creates function declaration and puts it into scope.
	 *
	 * @param name
	 *            function name
	 * @param node
	 *            function node in the AST
	 * @return newly created declaration
	 */
	IDeclaration declareFunction(String name, SimpleName node) {
		FunctionDeclaration declaration = new FunctionDeclaration(node, this, registerReference());
		declarations.put(name, declaration);
		return declaration;
	}

	/**
	 * Creates class declaration and puts it into scope.
	 *
	 * @param name
	 *            class name
	 * @param node
	 *            class node in the AST
	 * @return newly created declaration
	 */
	IDeclaration declareClass(String name, SimpleName node) {
		ClassDeclaration declaration = new ClassDeclaration(node, this, registerReference());
		declarations.put(name, declaration);
		return declaration;
	}

	/**
	 * Returns symbol declared in this exact scope. Do not examines parent
	 * scope.
	 *
	 * @param name
	 *            name of the symbol to find
	 * @return found declaration or <code>null</code> if none found
	 */
	public IDeclaration getOwnDeclaration(String name) {
		return declarations.get(name);
	}

	/**
	 * Returns symbol declared in this or upper scope.
	 *
	 * @param name
	 *            name of the symbol to find
	 * @return found declaration or <code>null</code> if none found
	 */
	public IDeclaration getDeclaration(String name) {
		Scope scope = this;
		while (scope != null) {
			IDeclaration var = scope.declarations.get(name);
			if (var != null) {
				return var;
			}
			boolean plain = (scope.isFunctionScope() || scope.isFunctionBlockScope()) && !scope.isArrowFunctionScope();
			if (ARGUMENTS.equals(name) && plain) {
				return scope.getArgumentsVariable();
			}
			// Recurse up the parent Scope
			scope = scope.parent;
		}
		return null;
	}

	/**
	 * Returns (creating if necessary) special 'arguments' variable
	 * declaration
	 *
	 * @return special 'arguments' variable declaration
	 */
	public VariableDeclaration getArgumentsVariable() {
		if (arguments == null) {
			arguments = VariableDeclaration.createArgumentsDeclaration(this);
		}
		return arguments;
	}

	/**
	 * Checks if given symbol is declared in this ore parens scope(s)
	 *
	 * @param name
	 *            name to find
	 * @param recurse
	 *            whether to look in parent scope or not
	 * @return <code>true</code> if symbol is declared
	 */
	public boolean isDeclared(String name, boolean recurse) {
		Scope scope = this;
		while (true) {
			if (scope.declarations.containsKey(name)) {
				return true;
			}

			// In ES6, a separate "function parameter scope" is created above
			// the function block scope to handle default parameters.
			// Since nothing in the function block scope is allowed to shadow
			// the variables in the function scope, two scopes are treated as
			// one in this method.
			if (scope.isFunctionBlockScope() || (scope.parent != null && recurse)) {
				scope = scope.parent;
				continue;
			}
			return false;
		}
	}

	/**
	 * Return number of declarations in this scope
	 *
	 * @return
	 */
	public int getDeclarationsCount() {
		return declarations.size();
	}

	/**
	 * Returns whether this is the global scope.
	 */
	public boolean isGlobal() {
		return parent == null;
	}

	/**
	 * Returns whether this is a local scope (i.e. not the global scope).
	 */
	public boolean isLocal() {
		return parent != null;
	}

	/**
	 * Check if scope is block
	 *
	 * @return <code>true</code> if scope is block
	 */
	public boolean isBlockScope() {
		switch (getRootNode().getNodeType()) {
			case ASTNode.BLOCK : {
				if (getRootNode().getParent() == null || getRootNode().getParent().getParent() == null || getRootNode().getParent().getNodeType() == ASTNode.CATCH_CLAUSE) {
					return false;
				}
				return true;
			}
			case ASTNode.FOR_STATEMENT :
			case ASTNode.FOR_IN_STATEMENT :
			case ASTNode.FOR_OF_STATEMENT :
			case ASTNode.SWITCH_STATEMENT :
			case ASTNode.TYPE_DECLARATION :
				return true;
		}
		return false;
	}

	/**
	 * Check if scope is function block scope
	 *
	 * @return <code>true</code> if scope is function block scope
	 */
	public boolean isFunctionBlockScope() {
		return isBlockScope() && parent != null && (parent.getRootNode().getNodeType() == ASTNode.FUNCTION_DECLARATION_STATEMENT || parent.getRootNode().getNodeType() == ASTNode.FUNCTION_DECLARATION);
	}

	/**
	 * Check if scope is function scope
	 *
	 * @return <code>true</code> if scope is function scope
	 */
	public boolean isFunctionScope() {
		return getRootNode().getNodeType() == ASTNode.FUNCTION_DECLARATION;
	}

	/**
	 * Check if scope is arrow function scope
	 *
	 * @return <code>true</code> if scope is arrow function scope
	 */
	public boolean isArrowFunctionScope() {
		ASTNode grandparent = NodeUtil.grandParent(getRootNode());
		return (grandparent != null && grandparent.getNodeType() == ASTNode.ARROW_FUNCTION_EXPRESSION);
	}

	/**
	 * If a var were declared in this scope, return the scope it would be
	 * hoisted to.
	 */
	public Scope getHoistScope() {
		Scope current = this;
		while (current != null) {
			if (current.isFunctionScope() || current.isFunctionBlockScope() || current.isGlobal()) {
				return current;
			}
			current = current.parent;
		}
		return null;
	}

	/**
	 * Returns AST node of the function declaration if this scope corresponds
	 * to function declaration
	 *
	 * @return node of the function declaration if this scope corresponds to
	 *         function declaration
	 */
	public ASTNode getFunctionDeclarationLocation() {
		Scope current = this;
		while (current != null) {
			if (current.getRootNode().getNodeType() == ASTNode.FUNCTION_DECLARATION_STATEMENT) {
				org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((FunctionDeclarationStatement) current.getRootNode()).getDeclaration();
				return fd.getMethodName();
			}
			else if (current.getRootNode().getNodeType() == ASTNode.FUNCTION_DECLARATION) {
				org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((org.eclipse.wst.jsdt.core.dom.FunctionDeclaration) current.getRootNode());
				return fd.getMethodName();
			}
			else if (current.getRootNode().getNodeType() == ASTNode.FUNCTION_EXPRESSION) {
				org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((FunctionExpression) current.getRootNode()).getMethod();
				return fd.getMethodName();
			}
			current = current.parent;
		}
		return null;
	}

	/**
	 * Returns AST node of the class declaration if this scope corresponds to
	 * class declaration
	 *
	 * @return node of the class declaration if this scope corresponds to
	 *         class declaration
	 */
	public ASTNode getClassDeclarationLocation() {
		Scope current = this;
		while (current != null) {
			switch (current.getRootNode().getNodeType()) {
				case ASTNode.TYPE_DECLARATION_STATEMENT : {
					TypeDeclaration td = (TypeDeclaration) ((TypeDeclarationStatement) current.getRootNode()).getDeclaration();
					return td.getName();
				}
				case ASTNode.TYPE_DECLARATION_EXPRESSION : {
					TypeDeclaration td = (TypeDeclaration) ((TypeDeclarationExpression) current.getRootNode()).getDeclaration();
					return td.getName();
				}
			}
			current = current.parent;
		}
		return null;
	}

	/**
	 * Returns string key that identifies scope in the AST. It's used by
	 * Symbols and bindings to generate their own keys.
	 *
	 * @return string key that identifies scope in the AST
	 */
	public String getKey() {
		Stack<String> scopes = new Stack<>();
		Scope current = this;
		while (current != null) {
			switch (current.getRootNode().getNodeType()) {
				case ASTNode.TYPE_DECLARATION_STATEMENT : {
					TypeDeclaration td = (TypeDeclaration) ((TypeDeclarationStatement) current.getRootNode()).getDeclaration();
					scopes.push(td.getName().getIdentifier());
				}
					break;
				case ASTNode.TYPE_DECLARATION_EXPRESSION : {
					TypeDeclaration td = (TypeDeclaration) ((TypeDeclarationExpression) current.getRootNode()).getDeclaration();
					scopes.push(td.getName().getIdentifier());
				}
					break;
				case ASTNode.FUNCTION_DECLARATION_STATEMENT : {
					org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((FunctionDeclarationStatement) current.getRootNode()).getDeclaration();
					SimpleName fname = (SimpleName) fd.getMethodName();
					if (fname != null) {
						scopes.push(fname.getIdentifier());
					} else {
						scopes.push("@"); //$NON-NLS-1$
					}			
				}
					break;
				case ASTNode.FUNCTION_DECLARATION : {
					org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((org.eclipse.wst.jsdt.core.dom.FunctionDeclaration) current.getRootNode());
					SimpleName fname = (SimpleName) fd.getMethodName();
					if (fname != null) {
						scopes.push(fname.getIdentifier());
					} else {
						scopes.push("@"); //$NON-NLS-1$
					}
				}
					break;
				case ASTNode.FUNCTION_EXPRESSION : {
					org.eclipse.wst.jsdt.core.dom.FunctionDeclaration fd = ((FunctionExpression) current.getRootNode()).getMethod();
					SimpleName fname = (SimpleName) fd.getMethodName();
					if (fname != null) {
						scopes.push(fname.getIdentifier());
					} else {
						scopes.push("@"); //$NON-NLS-1$
					}
				}
					break;
				case ASTNode.BLOCK : {
					if (current.getRootNode().getParent() != null && current.getRootNode().getParent().getNodeType() == ASTNode.FUNCTION_DECLARATION) {
						scopes.push("*"); //$NON-NLS-1$
					}
				}
					break;
			}
			current = current.parent;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("/"); //$NON-NLS-1$
		while (!scopes.isEmpty()) {
			sb.append(scopes.pop());
			sb.append("/"); //$NON-NLS-1$
		}

		return sb.toString();
	}

	/**
	 * Register new reference in scope, increments counter and return
	 * available reference number. It's used to generate sequential numeric
	 * id's for declarations/references contained in the scope which are used
	 * in turn to create symbol's keys.
	 *
	 * @return declaration/reference id to use
	 */
	public int registerReference() {
		return referenceCount++;
	}
}
