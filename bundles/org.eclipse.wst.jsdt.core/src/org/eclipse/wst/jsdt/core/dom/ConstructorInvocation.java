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

package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Alternate constructor invocation statement AST node type.
 * For JLS2:
 * <pre>
 * ConstructorInvocation:
 *		<b>this</b> <b>(</b> [ Expression { <b>,</b> Expression } ] <b>)</b> <b>;</b>
 * </pre>
 * For JLS3, type arguments are added:
 * <pre>
 * ConstructorInvocation:
 *      [ <b>&lt;</b> Type { <b>,</b> Type } <b>&gt;</b> ]
 *		      <b>this</b> <b>(</b> [ Expression { <b>,</b> Expression } ] <b>)</b> <b>;</b>
 * </pre>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class ConstructorInvocation extends Statement {

	/**
	 * The "typeArguments" structural property of this node type (added in JLS3 API).
	 */
	public static final ChildListPropertyDescriptor TYPE_ARGUMENTS_PROPERTY =
		new ChildListPropertyDescriptor(ConstructorInvocation.class, "typeArguments", Type.class, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "arguments" structural property of this node type.
	 */
	public static final ChildListPropertyDescriptor ARGUMENTS_PROPERTY =
		new ChildListPropertyDescriptor(ConstructorInvocation.class, "arguments", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS_2_0;

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS_3_0;

	static {
		List properyList = new ArrayList(2);
		createPropertyList(ConstructorInvocation.class, properyList);
		addProperty(ARGUMENTS_PROPERTY, properyList);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList(properyList);

		properyList = new ArrayList(3);
		createPropertyList(ConstructorInvocation.class, properyList);
		addProperty(TYPE_ARGUMENTS_PROPERTY, properyList);
		addProperty(ARGUMENTS_PROPERTY, properyList);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList(properyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel == AST.JLS2_INTERNAL) {
			return PROPERTY_DESCRIPTORS_2_0;
		} else {
			return PROPERTY_DESCRIPTORS_3_0;
		}
	}

	/**
	 * The type arguments (element type: <code>Type</code>).
	 * Null in JLS2. Added in JLS3; defaults to an empty list
	 * (see constructor).
	 */
	private ASTNode.NodeList typeArguments = null;

	/**
	 * The list of argument expressions (element type:
	 * <code>Expression</code>). Defaults to an empty list.
	 */
	private ASTNode.NodeList arguments =
		new ASTNode.NodeList(ARGUMENTS_PROPERTY);

	/**
	 * Creates a new AST node for an alternate constructor invocation statement
	 * owned by the given AST. By default, an empty list of arguments.
	 *
	 * @param ast the AST that is to own this node
	 */
	ConstructorInvocation(AST ast) {
		super(ast);
		if (ast.apiLevel >= AST.JLS3) {
			this.typeArguments = new ASTNode.NodeList(TYPE_ARGUMENTS_PROPERTY);
		}
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == ARGUMENTS_PROPERTY) {
			return arguments();
		}
		if (property == TYPE_ARGUMENTS_PROPERTY) {
			return typeArguments();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return CONSTRUCTOR_INVOCATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		ConstructorInvocation result = new ConstructorInvocation(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.copyLeadingComment(this);
		if (this.ast.apiLevel >= AST.JLS3) {
			result.typeArguments().addAll(ASTNode.copySubtrees(target, typeArguments()));
		}
		result.arguments().addAll(ASTNode.copySubtrees(target, arguments()));
		return result;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			if (this.ast.apiLevel >= AST.JLS3) {
				acceptChildren(visitor, this.typeArguments);
			}
			acceptChildren(visitor, this.arguments);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the live ordered list of type arguments of this constructor
	 * invocation (added in JLS3 API).
	 *
	 * @return the live list of type arguments
	 *    (element type: <code>Type</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 */
	public List typeArguments() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.typeArguments == null) {
			unsupportedIn2();
		}
		return this.typeArguments;
	}

	/**
	 * Returns the live ordered list of argument expressions in this alternate
	 * constructor invocation statement.
	 *
	 * @return the live list of argument expressions
	 *    (element type: <code>Expression</code>)
	 */
	public List arguments() {
		return this.arguments;
	}

	/**
	 * Resolves and returns the binding for the constructor invoked by this
	 * expression.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 *
	 * @return the constructor binding, or <code>null</code> if the binding
	 *    cannot be resolved
	 */
	public IFunctionBinding resolveConstructorBinding() {
		return this.ast.getBindingResolver().resolveConstructor(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		// treat Code as free
		return BASE_NODE_SIZE + 2 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.typeArguments == null ? 0 : this.typeArguments.listSize())
			+ (this.arguments == null ? 0 : this.arguments.listSize());
	}
}

