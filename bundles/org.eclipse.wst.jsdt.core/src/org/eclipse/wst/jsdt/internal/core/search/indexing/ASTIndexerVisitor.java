/*******************************************************************************
* Copyright (c) 2016, 2017 Red Hat, Inc.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* 	Contributors:
* 		 Red Hat Inc. - initial API and implementation and/or initial documentation
*******************************************************************************/

package org.eclipse.wst.jsdt.internal.core.search.indexing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.internal.core.util.Util;

/**
 * Traverses an AST and adds JavaScript element references to the
 * index through {@link SourceIndexer}. Supports indexing: <p>
 *
 * - Classes references and declarations (ES6 only) <br>
 * - Method/Function references and declarations <br>
 * - Field/Variable references and declarations <p>
 *
 * Note that in general, no distinction is made between functions and methods,
 * and between fields and variables.
 *
 * <p>
 * The indexer tries to distinguish between property references and 
 * declarations. However, since it is possible to add properties to objects
 * in JavaScript at will, only the first reference to a field is labeled
 * a declaration.
 */
public class ASTIndexerVisitor extends ASTVisitor {

	private SourceIndexer indexer;

	/** Holds the names of elements higher in the AST */
	private Stack<char[]> contextNames;

	/** Tracks the expected depth of the contextNames stack, to know when popping is appropriate */
	private int stackDepth;

	/** Tracks field names encountered already, since fields can be declared anywhere */
	private Set<String> fieldNames;

	public ASTIndexerVisitor(SourceIndexer indexer) {
		super(false);
		this.indexer = indexer;
	}

	@Override
	public boolean visit(JavaScriptUnit node) {
		contextNames = new Stack<>();
		stackDepth = 0;
		fieldNames = new HashSet<>();
		return true;
	}

	@Override
	public void endVisit(JavaScriptUnit node) {
		contextNames = null;
		fieldNames = null;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		stackDepth++;
		SimpleName name = node.getName();
		if (name == null) {
			return true;
		}
		char[] fieldName = name.getIdentifier().toCharArray();
		char[] fieldSuperName = contextNames.isEmpty() ? null : contextNames.peek();
		if (fieldSuperName != null) {
			indexer.addFieldDeclaration(null, fieldName, fieldSuperName, 0, false);
		} else {
			indexer.addFieldDeclaration(null, fieldName, fieldSuperName, 0, true);
		}
		contextNames.push(fieldName);
		if (node.getInitializer() != null) {
			node.getInitializer().accept(this);
		}
		return false;
	}

	@Override
	public void endVisit(VariableDeclarationFragment node) {
		stackDepth--;
		if (stackDepth < contextNames.size()){
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(ObjectLiteralField node) {
		stackDepth++;
		char[] fieldName, superName;
		Expression name = node.getFieldName();
		if (name == null) {
			return true;
		}
		switch (name.getNodeType()) {
			case ASTNode.SIMPLE_NAME :
				fieldName = ((SimpleName) name).getIdentifier().toCharArray();
				break;
			case ASTNode.STRING_LITERAL :
				fieldName = ((StringLiteral) name).getEscapedValue().toCharArray();
				break;
			default:
				return true;
		}
		superName = contextNames.isEmpty() ? new char[0] : contextNames.peek();
		ASTNode initializer = node.getInitializer();
		if (initializer.getNodeType() != ASTNode.FUNCTION_EXPRESSION) {
			indexer.addFieldDeclaration(null, fieldName, superName, 0, false);
		}
		contextNames.push(fieldName);
		return true;
	}

	@Override
	public void endVisit(ObjectLiteralField node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(FunctionDeclaration node) {
		stackDepth++;
		Expression name = node.getMethodName();
		char[] methodName;
		char[] superName;
		boolean isFunction = false; // We don't care about distinguishing functions from methods
		if (name != null) {
			// name is not null when the function is declared as "function foo() {...}"
			methodName = ((SimpleName) name).getIdentifier().toCharArray();
			superName = contextNames.isEmpty() ? null : contextNames.peek();
		} else {
			// name is null in e.g. "var x = function() {}"
			if (contextNames.isEmpty()) {
				return true; // Anonymous function
			}
			methodName = contextNames.pop();
			superName = contextNames.isEmpty() ? null : contextNames.peek();
			contextNames.push(methodName);
		}

		// node.parameters will always return a list of SingleVariableDeclaration
		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> parameters = node.parameters();
		char[][] parameterNames = extractParameters(parameters);
		if (node.isConstructor()) {
			indexer.addConstructorDeclaration(superName, null, parameterNames, 0);
		} else {
			indexer.addMethodDeclaration(methodName, null, parameterNames,
						null, superName, isFunction, 0);
		}
		contextNames.push(methodName);
		if (node.getBody() != null) {
			node.getBody().accept(this);
		}
		return false;
	}

	@Override
	public void endVisit(FunctionDeclaration node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(FunctionInvocation node) {
		String name;
		if (node.getName() != null) {
			name = node.getName().getIdentifier();
		} else if (node.getExpression() != null && node.getExpression().getNodeType() == ASTNode.FIELD_ACCESS) {
			name = ((FieldAccess) node.getExpression()).getName().getIdentifier();
		} else {
			// Can't index an anonymous function.
			return true;
		}
		char[] methodName = name.toCharArray();
		indexer.addMethodReference(methodName);
		return true;
	}

	@Override
	public boolean visit(FieldAccess node) {
		char[] fieldName = node.getName().getIdentifier().toCharArray();
		char[] superName = new char[0];

		Expression expression = node.getExpression();
		switch (expression.getNodeType()) {
			case ASTNode.THIS_EXPRESSION :
				superName = contextNames.isEmpty() ? null : contextNames.peek();
				break;
			case ASTNode.SIMPLE_NAME :
				superName = ((SimpleName) expression).getIdentifier().toCharArray();
				break;
			case ASTNode.FIELD_ACCESS :
				superName = ((FieldAccess) expression).getName().getIdentifier().toCharArray();
				break;
		}

		// FieldAccess can be a reference or a declaration in JS
		if (node.getParent().getNodeType() != ASTNode.FUNCTION_INVOCATION) {
			// Function Invocation is handled in the appropriate method
			if (node.getParent().getNodeType() == ASTNode.ASSIGNMENT
						&& ((Assignment) node.getParent()).getRightHandSide() == node) {
				indexer.addFieldReference(fieldName);
			}
			if (isFieldDeclaration(superName, fieldName)) {
				indexer.addFieldDeclaration(null, fieldName, superName, 0, false);
			} else {
				indexer.addFieldReference(fieldName);
			}
		}
		return true;
	}

	@Override
	public boolean visit(Assignment node) {
		stackDepth++;
		ASTNode lhs = node.getLeftHandSide();
		char[] elementName = null;
		switch (lhs.getNodeType()) {
			case ASTNode.SIMPLE_NAME :
				elementName = ((SimpleName) lhs).getIdentifier().toCharArray();
				break;
			case ASTNode.FIELD_ACCESS :
				elementName = ((FieldAccess) lhs).getName().getIdentifier().toCharArray();
				break;
		}
		lhs.accept(this);
		if (elementName != null) {
			contextNames.push(elementName);
		}
		node.getRightHandSide().accept(this);
		return false;
	}

	@Override
	public void endVisit(Assignment node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(SimpleName node) {
		// This does over-report index entries, but is the only way to
		// catch all names.
		char[] name = node.getIdentifier().toCharArray();
		indexer.addNameReference(name);
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		stackDepth++;
		SimpleName name = node.getName();
		if (name == null) {
			return true;
		}
		char[] simpleTypeName = name.getIdentifier().toCharArray();

		Name superName = null;
		try {
			superName = node.getSuperclass();
		} catch (ClassCastException e) {
			// To allow remainder of file to be indexed --
			// Occurs with e.g. `class A extends 0 {}` and is an
			// ASTParser bug (due to Esprima tolerant parsing)
			Util.verbose("ClassCastException while trying to get superType --- "  //$NON-NLS-1$
				+ e.getMessage() + "\n\t in file : " + indexer.document.getPath()); //$NON-NLS-1$
		}

		char[] superTypeName = null;
		if (superName != null) {
			superTypeName = superName.getFullyQualifiedName().toCharArray();
		}
		indexer.addTypeDeclaration(0, null, simpleTypeName, superTypeName);
		contextNames.push(simpleTypeName);
		return true;
	}

	@Override
	public void endVisit(TypeDeclaration node) {
		stackDepth--;
		if (stackDepth < contextNames.size()) {
			contextNames.pop();
		}
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		Expression name = node.getMember();
		if (name == null || (name.getNodeType() != ASTNode.SIMPLE_NAME)) {
			return true;
		}
		char[] className = ((SimpleName) name).getIdentifier().toCharArray();
		indexer.addTypeReference(className);
		indexer.addConstructorReference(className, 0);
		return true;
	}

	/**
	 * Transforms a List<SingleVariableDeclaration> into a char[][] of the
	 * parameter names.
	 *
	 * @param parameters
	 * @return
	 */
	private char[][] extractParameters(List<SingleVariableDeclaration> parameters) {
		char[][] parameterNames = new char[parameters.size()][];
		SimpleName parameterName;
		for (int i = 0; i < parameters.size(); i++) {
			parameterName = parameters.get(i).getName();
			if (parameterName == null) {
				continue;
			}
			parameterNames[i] = parameterName.getIdentifier().toCharArray();
		}

		return parameterNames;
	}

	/**
	 * Checks whether the current names have been used to declare a field
	 * earlier in the visiting of this AST. Since object properties can be
	 * defined anywhere in JavaScript, this is necessary to properly find
	 * field declarations versus field references.
	 * <p>
	 * Parameters are intended to represent objectName.propName
	 *
	 * @param sName
	 * @param propName
	 * @return true if this is the first time objectName.propName has been
	 *         encountered, false otherwise.
	 */
	private boolean isFieldDeclaration(char[] superName, char[] fieldName) {
		char[] fullNameArray;
		String fullName;

		if (superName == null) {
			superName = new char[0];
		}

		fullNameArray = new char[fieldName.length + superName.length + 1];
		System.arraycopy(superName, 0, fullNameArray, 0, superName.length);
		fullNameArray[superName.length] = '.';
		System.arraycopy(fieldName, 0, fullNameArray, superName.length+1,
					fieldName.length);
		fullName = new String(fullNameArray);
		if (fieldNames.contains(fullName)) {
			return false;
		} else {
			fieldNames.add(fullName);
			return true;
		}
	}
}
