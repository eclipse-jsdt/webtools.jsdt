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

import java.util.List;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.ForOfStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;
import org.eclipse.wst.jsdt.core.dom.WithStatement;

/**
 * Declarations scanner. Scans given scope and adds all found symbol
 * declarations to it.
 *
 * Note. It scans only declarations. Reference scan is done in
 * <code>SymbolCollector</code> which creates scopes and invokes this scanner.
 *
 * @since 2.0
 *
 */
public class ScopeDeclarationScanner {

	/**
	 * Scans given scope and adds all found symbol declarations to it.
	 *
	 * @param scope
	 */
	public static void process(Scope scope) {
		new Visitor().process(scope);
	}

	/**
	 * Scope tree traversal that walks given scope and adds all found symbol
	 * declarations to it.
	 *
	 */
	private static class Visitor {

		private static final String ARGUMENTS = "arguments"; //$NON-NLS-1$

		private Scope rootScope;

		/**
		 * Scans given scope and adds all found symbol declarations to it.
		 *
		 * @param scope
		 */
		void process(Scope scope) {
			this.rootScope = scope;
			scanRoot(scope.getRootNode());
		}

		/**
		 * Scan scope root AST Node. The scopes are created for Classes,
		 * Functions and JavaScriptUnits
		 *
		 * @param n
		 *            node to scan
		 */
		private void scanRoot(ASTNode n) {
			switch (n.getNodeType()) {
				case ASTNode.FUNCTION_EXPRESSION : {
					FunctionDeclaration fd = ((FunctionExpression) n).getMethod();
					SimpleName fNameNode = ((SimpleName) fd.getMethodName());
					if (fNameNode != null) {
						declareFunction(fNameNode);
					}
					declareExpressions(rootScope, fd.parameters());
				}
					break;
				case ASTNode.FUNCTION_DECLARATION_STATEMENT : {
					FunctionDeclaration fd = ((FunctionDeclarationStatement) n).getDeclaration();
					declareExpressions(rootScope, fd.parameters());
				}
					break;
				case ASTNode.FUNCTION_DECLARATION : {
					FunctionDeclaration fd = ((FunctionDeclaration) n);
					SimpleName fNameNode = ((SimpleName) fd.getMethodName());
					if (fNameNode != null) {
						declareFunction(fNameNode);
					}
					declareExpressions(rootScope, fd.parameters());
				}
					break;
				case ASTNode.TYPE_DECLARATION_EXPRESSION : {
					AbstractTypeDeclaration cd = ((TypeDeclarationExpression) n).getDeclaration();
					if (cd != null) {
						declareClass(cd.getName());
					}
				}
					break;
				case ASTNode.TYPE_DECLARATION :
					break;
				default :
					scanDeclarations(n);
					break;
			}

		}

		/**
		 * Scan declarations in the given node
		 *
		 * @param n
		 *            node to scan
		 */
		private void scanDeclarations(ASTNode n) {
			if (n == null) {
				return;
			}
			switch (n.getNodeType()) {
				case ASTNode.VARIABLE_DECLARATION_STATEMENT : {
					VariableDeclarationStatement vd = ((VariableDeclarationStatement) n);
					switch (vd.getKind()) {
						case VAR :
							Scope s = rootScope.getHoistScope();
							for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) vd.fragments()) {
								declareExpression(s, f);
							}
							break;
						case LET :
						case CONST :
							if (!isAtCurrentLexicalScope(n)) {
								return;
							}
							for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) vd.fragments()) {
								declareExpression(rootScope, f);
							}
							break;
					}
				}
					return;
				case ASTNode.VARIABLE_DECLARATION_EXPRESSION : {
					VariableDeclarationExpression vd = ((VariableDeclarationExpression) n);
					switch (vd.getKind()) {
						case VAR :
							Scope s = rootScope.getHoistScope();
							for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) vd.fragments()) {
								declareExpression(s, f);
							}
							break;
						case LET :
						case CONST :
							if (!isAtCurrentLexicalScope(n)) {
								return;
							}
							for (VariableDeclarationFragment f : (List<VariableDeclarationFragment>) vd.fragments()) {
								declareExpression(rootScope, f);
							}
							break;
					}
				}
					return;
				case ASTNode.FUNCTION_EXPRESSION :
					// Intentional
					return;
				case ASTNode.FUNCTION_DECLARATION_STATEMENT : {
						FunctionDeclaration fd = ((FunctionDeclarationStatement) n).getDeclaration();
						if (fd != null) {
							declareFunction(((SimpleName) fd.getMethodName()));
						}
					}
					return;
				case ASTNode.FUNCTION_DECLARATION : {
						declareFunction(((SimpleName) ((FunctionDeclaration) n).getMethodName()));
					}
					break;
				case ASTNode.TYPE_DECLARATION_EXPRESSION :
					return;
				case ASTNode.TYPE_DECLARATION_STATEMENT : {
					TypeDeclaration td = (TypeDeclaration) ((TypeDeclarationStatement) n).getDeclaration();
					if (td != null) {
						declareClass(td.getName());
					}
				}
					return;
				case ASTNode.CATCH_CLAUSE : {
					CatchClause cc = ((CatchClause) n);
					if (isAtCurrentLexicalScope(n)) {
						declareExpression(rootScope, cc.getException());
					}
					scanDeclarations(cc.getBody());
				}
					return;
			}
			switch (n.getNodeType()) {
				case ASTNode.FOR_STATEMENT : {
					ForStatement fs = ((ForStatement) n);
					scanDeclarations(fs.initializers());
					scanDeclarations(fs.getExpression());
					scanDeclarations(fs.updaters());
					scanDeclarations(fs.getBody());
				}
					break;
				case ASTNode.FOR_OF_STATEMENT : {
					ForOfStatement fs = ((ForOfStatement) n);
					scanDeclarations(fs.getIterationVariable());
					scanDeclarations(fs.getCollection());
					scanDeclarations(fs.getBody());
				}
					break;
				case ASTNode.DO_STATEMENT : {
					DoStatement fs = ((DoStatement) n);
					scanDeclarations(fs.getBody());
					scanDeclarations(fs.getExpression());
				}
					break;
				case ASTNode.WHILE_STATEMENT : {
					WhileStatement fs = ((WhileStatement) n);
					scanDeclarations(fs.getExpression());
					scanDeclarations(fs.getBody());
				}
					break;
				case ASTNode.WITH_STATEMENT : {
					WithStatement fs = ((WithStatement) n);
					scanDeclarations(fs.getExpression());
					scanDeclarations(fs.getBody());
				}
					break;
				case ASTNode.IF_STATEMENT : {
					IfStatement fs = ((IfStatement) n);
					scanDeclarations(fs.getExpression());
					scanDeclarations(fs.getThenStatement());
					scanDeclarations(fs.getElseStatement());
				}
					break;
				case ASTNode.LABELED_STATEMENT : {
					LabeledStatement fs = ((LabeledStatement) n);
					scanDeclarations(fs.getBody());
				}
					break;
				case ASTNode.TRY_STATEMENT : {
					TryStatement ts = ((TryStatement) n);
					scanDeclarations(ts.getBody());
					scanDeclarations(ts.catchClauses());
					scanDeclarations(ts.getFinally());
				}
					break;
				case ASTNode.SWITCH_STATEMENT : {
					SwitchStatement ss = ((SwitchStatement) n);
					scanDeclarations(ss.getExpression());
					scanDeclarations(ss.statements());
				}
					break;
				case ASTNode.SWITCH_CASE : {
					SwitchCase ss = ((SwitchCase) n);
					scanDeclarations(ss.getExpression());
				}
					break;
				case ASTNode.BLOCK : {
					Block ss = ((Block) n);
					scanDeclarations(ss.statements());
				}
					break;
				case ASTNode.JAVASCRIPT_UNIT : {
					JavaScriptUnit ss = ((JavaScriptUnit) n);
					scanDeclarations(ss.statements());
				}
			}
		}

		/**
		 * Scan Declarations in the list of nodes
		 *
		 * @param nodes
		 */
		private void scanDeclarations(List<ASTNode> nodes) {
			if (nodes != null) {
				for (ASTNode node : nodes) {
					scanDeclarations(node);
				}
			}
		}

		/**
		 * Declare list of given expressions if possible
		 *
		 * @param scope
		 *            scope to place declaration to
		 * @param lhs
		 *            expression
		 */
		private void declareExpressions(Scope scope, List<ASTNode> lhs) {
			if (lhs != null) {
				for (ASTNode p : lhs) {
					declareExpression(scope, p);
				}
			}
		}

		/**
		 * Declare given expressions if possible
		 *
		 * @param scope
		 *            scope to place declaration to
		 * @param lhs
		 *            expression
		 */
		private void declareExpression(Scope scope, ASTNode lhs) {
			if (lhs != null) {
				switch (lhs.getNodeType()) {
					case ASTNode.SIMPLE_NAME :
						declareVariable(scope, (SimpleName) lhs);
						break;
					case ASTNode.SINGLE_VARIABLE_DECLARATION :
						declareVariable(scope, ((SingleVariableDeclaration) lhs).getName());
						break;
					case ASTNode.VARIABLE_DECLARATION_FRAGMENT :
						declareVariable(scope, ((VariableDeclarationFragment) lhs).getName());
						break;
					case ASTNode.OBJECT_LITERAL :
						break;
				}
			}
		}

		/**
		 * Check if given node is at current lexical scope
		 *
		 * @param n
		 *            node to check
		 * @return
		 */
		private boolean isAtCurrentLexicalScope(ASTNode n) {
			ASTNode parent = n.getParent();
			ASTNode grandparent = NodeUtil.grandParent(n);

			if (grandparent != null && (grandparent.getNodeType() == ASTNode.SWITCH_CASE)) {
				ASTNode switchNode = grandparent.getParent();
				return rootScope.getRootNode() == switchNode;
			}

			if (parent == rootScope.getRootNode() || parent.getNodeType() == ASTNode.JAVASCRIPT_UNIT || (grandparent != null && grandparent.getNodeType() == ASTNode.CATCH_CLAUSE && (parent.getParent() != null ? parent.getParent() : null) == rootScope.getRootNode())) {
				return true;
			}

			while (parent.getNodeType() == ASTNode.LABELED_STATEMENT) {
				if (parent.getParent() == rootScope.getRootNode()) {
					return true;
				}
				parent = parent.getParent();
			}
			return false;
		}

		/**
		 * Declare variable at given scope
		 *
		 * @param s
		 *            scope
		 * @param n
		 *            variable name node
		 */
		private void declareVariable(Scope s, SimpleName n) {
			if  (n != null) {
				String name = n.getIdentifier();
				if (checkRedeclaration(s, n, name)) {
					s.declareVariable(name, n, org.eclipse.wst.jsdt.internal.core.dom.binding.VariableDeclaration.VariableKind.VAR);
				}
			}
		}


		/**
		 * Declare function at given scope
		 *
		 * @param s
		 *            scope
		 * @param n
		 *            function name node
		 */
		private void declareFunction(Scope s, SimpleName n) {
			if (n != null) {
				String name = n.getIdentifier();
				if (checkRedeclaration(s, n, name)) {
					s.declareFunction(name, n);
				}
			}
		}

		/**
		 * Declare function at default scope
		 *
		 * @param n
		 *            function name node
		 */
		private void declareFunction(SimpleName n) {
			declareFunction(rootScope, n);
		}

		/**
		 * Declare class at given scope
		 *
		 * @param s
		 *            scope
		 * @param n
		 *            class name node
		 */
		private void declareClass(Scope s, SimpleName n) {
			if (n != null) {
				String name = n.getIdentifier();
				if (checkRedeclaration(s, n, name)) {
					s.declareClass(name, n);
				}
			}
		}

		/**
		 * Declare class at default scope
		 *
		 * @param n
		 *            function name node
		 */
		private void declareClass(SimpleName n) {
			declareClass(rootScope, n);
		}

		private boolean checkRedeclaration(Scope s, SimpleName n, String name) {
			if (s.isDeclared(name, false) || (s.isLocal() && ARGUMENTS.equals(name))) {
				// TODO Should we handle it somehow?
				return false;
			}
			return true;
		}

	}

}
