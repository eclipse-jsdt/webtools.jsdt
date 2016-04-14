/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.dom.binding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.ForInStatement;
import org.eclipse.wst.jsdt.core.dom.ForOfStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;

/**
 * JS Unit "Indexer". Collects all Class/Function/Variable declarations and
 * references
 *
 * @since 2.0
 *
 */
public class SymbolCollector {

	Map<IDeclaration, List<IReference>> classReferenceMap = new LinkedHashMap<>();

	Map<IDeclaration, List<IReference>> functionReferenceMap = new LinkedHashMap<>();

	Map<IDeclaration, List<IReference>> variableReferenceMap = new LinkedHashMap<>();

	Map<String, List<IReference>> unresolvedReferences = new LinkedHashMap<>();

	JavaScriptUnit unit;

	private Stack<Scope> scopes = new Stack<>();

	/**
	 * Creates collector for given JavaScriptUnit
	 *
	 * @param unit
	 *            JavaScriptUnit to process
	 */
	public SymbolCollector(JavaScriptUnit unit) {
		this.unit = unit;
	}

	/**
	 * Collects all Class/Function/Variable declarations and references
	 */
	public void process() {
		unit.accept(new Visitor());
	}

	/**
	 * Returns map of found class declarations and references
	 *
	 * @return map of found class declarations and references
	 */
	public Map<IDeclaration, List<IReference>> getClassReferences() {
		return classReferenceMap;
	}

	/**
	 * Returns map of found function declarations and references
	 *
	 * @return map of found function declarations and references
	 */
	public Map<IDeclaration, List<IReference>> getFunctionReferences() {
		return functionReferenceMap;
	}

	/**
	 * Returns map of found variable declarations and references
	 *
	 * @return map of found variable declarations and references
	 */
	public Map<IDeclaration, List<IReference>> getVariableReferences() {
		return variableReferenceMap;
	}

	/**
	 * Returns map of found unresolved references
	 *
	 * @return map of found unresolved references
	 */
	public Map<String, List<IReference>> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	/**
	 * Create scope for given AST node and add it to the scope stack
	 *
	 * @param node
	 *            AST node to create scope for
	 * @return newly created scope
	 */
	private Scope createScope(ASTNode node) {
		Scope parent = scopes.empty() ? null : scopes.peek();
		Scope result = new Scope(parent, node);
		scopes.push(result);
		return result;
	}

	/**
	 * Remove scope from stack of scopes
	 */
	private void popScope() {
		scopes.pop();
	}

	/**
	 * Return current scope
	 *
	 * @return current scope
	 */
	private Scope getScope() {
		return scopes.empty() ? null : scopes.peek();
	}

	/**
	 * Process scope - find all declarations
	 *
	 * @param scope
	 *            scope to process
	 */
	private void processScope(Scope scope) {
		ScopeDeclarationScanner.process(scope);
	}

	/**
	 * Register reference
	 *
	 * @param node
	 *            AST node that represents reference
	 */
	private void addReference(SimpleName node) {
		IDeclaration v = getScope().getDeclaration(node.getIdentifier());
		if (v != null) {
			addReference(v, node, getScope());
		}
		else {
			addUnresolvedReference(node, getScope());
		}
	}

	/**
	 * Select proper map to add declaration to basing on the declaration kind
	 *
	 * @param v
	 *            declaration to add
	 * @return map to add declaration to
	 */
	private Map<IDeclaration, List<IReference>> selectMap(IDeclaration v) {
		switch (v.getKind()) {
			case CLASS :
				return classReferenceMap;
			case FUNCTION :
				return functionReferenceMap;
			case VARIABLE :
				return variableReferenceMap;
			default :
				return null;
		}
	}

	/**
	 * Add resolved reference
	 *
	 * @param v
	 *            declaration to which reference points
	 * @param node
	 *            AST node which presents reference symbol
	 * @param scope
	 *            scope in which reference is contained
	 */
	private void addReference(IDeclaration v, SimpleName node, Scope scope) {
		Map<IDeclaration, List<IReference>> map = selectMap(v);
		int index = scope.registerReference();
		List<IReference> referenceInfo = map.get(v);
		if (referenceInfo == null) {
			referenceInfo = new ArrayList<>();
			map.put(v, referenceInfo);
		}
		referenceInfo.add(new Reference(node, scope, index, v));
	}

	/**
	 * Add unresolved reference
	 *
	 * @param node
	 *            AST node which presents reference symbol
	 * @param scope
	 *            scope in which reference is contained
	 */
	private void addUnresolvedReference(SimpleName node, Scope scope) {
		int index = scope.registerReference();
		String name = node.getIdentifier();
		List<IReference> referenceInfo = unresolvedReferences.get(name);
		if (referenceInfo == null) {
			referenceInfo = new ArrayList<>();
			unresolvedReferences.put(name, referenceInfo);
		}
		referenceInfo.add(new Reference(node, scope, index, null));
	}

	/**
	 * Visitor that traverses AST, creates scopes, calls scope scanner detects
	 * and records references.
	 */
	private class Visitor extends ASTVisitor {
		@Override
		public boolean visit(JavaScriptUnit node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public boolean visit(FunctionDeclaration node) {
			// This is the case of the Class Method declaration
			processScope(createScope(node));
			internalVisit(node.getMethodName());
			internalVisit(node.parameters());
			node.getBody().accept(this);
			popScope();
			return false;
		}

		@Override
		public boolean visit(FunctionExpression node) {
			FunctionDeclaration fd = node.getMethod();
			processScope(createScope(node));
			internalVisit(fd.getMethodName());
			internalVisit(fd.parameters());
			fd.getBody().accept(this);
			popScope();
			return false;
		}

		@Override
		public boolean visit(FunctionDeclarationStatement node) {
			FunctionDeclaration fd = node.getDeclaration();
			internalVisit(fd.getMethodName());
			processScope(createScope(node));
			internalVisit(fd.parameters());
			internalVisit(fd.getBody());
			popScope();
			return false;
		}

		@Override
		public boolean visit(TypeDeclarationStatement node) {
			TypeDeclaration td = (TypeDeclaration) node.getDeclaration();
			processScope(createScope(node));
			internalVisit(td.getName());
			internalVisit(td.getSuperclassExpression());
			internalVisit(td.bodyDeclarations());
			popScope();
			return false;
		}

		@Override
		public boolean visit(TypeDeclarationExpression node) {
			TypeDeclaration td = (TypeDeclaration) node.getDeclaration();
			processScope(createScope(node));
			internalVisit(td.getName());
			internalVisit(td.getSuperclassExpression());
			internalVisit(td.bodyDeclarations());
			popScope();
			return false;
		}

		@Override
		public boolean visit(ForStatement node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public void endVisit(ForStatement node) {
			popScope();
		}

		@Override
		public boolean visit(ForInStatement node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public void endVisit(ForInStatement node) {
			popScope();
		}

		@Override
		public boolean visit(ForOfStatement node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public void endVisit(ForOfStatement node) {
			popScope();
		}

		@Override
		public boolean visit(SwitchStatement node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public void endVisit(SwitchStatement node) {
			popScope();
		}

		@Override
		public boolean visit(Block node) {
			processScope(createScope(node));
			return true;
		}

		@Override
		public void endVisit(Block node) {
			popScope();
		}

		@Override
		public boolean visit(ObjectLiteralField node) {
			// Process only initializer. Don't collect property names
			// FIXME getInitializer() always returns value. Is it correct? How
			// to test if it's real or not?
			internalVisit(node.getInitializer());
			return false;
		}

		@Override
		public boolean visit(FieldAccess node) {
			// In this simplified collector we process only the left hand
			// expresion.
			// Ignore field name since there is no type inference
			internalVisit(node.getExpression());
			return false;
		}

		@Override
		public boolean visit(SimpleName node) {
			addReference(node);
			return false;
		}

		protected void internalVisit(List<ASTNode> nodes) {
			if (nodes != null) {
				for (ASTNode node : nodes) {
					node.accept(this);
				}
			}
		}

		protected void internalVisit(ASTNode node) {
			if (node != null) {
				node.accept(this);
			}
		}

	}

}
