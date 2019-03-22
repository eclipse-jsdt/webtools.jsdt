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
 * Simple or qualified "super" method invocation expression AST node type.
 * For JLS2:
 * <pre>
 * SuperMethodInvocation:
 *     [ ClassName <b>.</b> ] <b>super</b> <b>.</b> Identifier
 *         <b>(</b> [ Expression { <b>,</b> Expression } ] <b>)</b>
 * </pre>
 * For JLS3, type arguments are added:
 * <pre>
 * SuperMethodInvocation:
 *     [ ClassName <b>.</b> ] <b>super</b> <b>.</b>
 *         [ <b>&lt;</b> Type { <b>,</b> Type } <b>&gt;</b> ]
 *         Identifier <b>(</b> [ Expression { <b>,</b> Expression } ] <b>)</b>
 * </pre>
 * 
 * <p><b>Note: This Class only applies to ECMAScript 4 which is not yet supported</b></p>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class SuperMethodInvocation extends Expression {

	/**
	 * The "qualifier" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor QUALIFIER_PROPERTY =
		new ChildPropertyDescriptor(SuperMethodInvocation.class, "qualifier", Name.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "typeArguments" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildListPropertyDescriptor TYPE_ARGUMENTS_PROPERTY =
		new ChildListPropertyDescriptor(SuperMethodInvocation.class, "typeArguments", Type.class, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "name" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY =
		new ChildPropertyDescriptor(SuperMethodInvocation.class, "name", SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "arguments" structural property of this node type.
	 *  
	 */
	public static final ChildListPropertyDescriptor ARGUMENTS_PROPERTY =
		new ChildListPropertyDescriptor(SuperMethodInvocation.class, "arguments", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List PROPERTY_DESCRIPTORS_2_0;

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List PROPERTY_DESCRIPTORS_3_0;

	static {
		List propertyList = new ArrayList(4);
		createPropertyList(SuperMethodInvocation.class, propertyList);
		addProperty(QUALIFIER_PROPERTY, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		addProperty(ARGUMENTS_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList(propertyList);

		propertyList = new ArrayList(5);
		createPropertyList(SuperMethodInvocation.class, propertyList);
		addProperty(QUALIFIER_PROPERTY, propertyList);
		addProperty(TYPE_ARGUMENTS_PROPERTY, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		addProperty(ARGUMENTS_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_3_0 = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 *  
	 */
	public static List propertyDescriptors(int apiLevel) {
		if (apiLevel == AST.JLS2_INTERNAL) {
			return PROPERTY_DESCRIPTORS_2_0;
		} else {
			return PROPERTY_DESCRIPTORS_3_0;
		}
	}

	/**
	 * The optional qualifier; <code>null</code> for none; defaults to none.
	 */
	private Name optionalQualifier = null;

	/**
	 * The type arguments (element type: <code>Type</code>).
	 * Null in JLS2. Added in JLS3; defaults to an empty list
	 * (see constructor).
	 *  
	 */
	private ASTNode.NodeList typeArguments = null;

	/**
	 * The method name; lazily initialized; defaults to a unspecified,
	 * legal JavaScript method name.
	 */
	private SimpleName methodName = null;

	/**
	 * The list of argument expressions (element type:
	 * <code>Expression</code>). Defaults to an empty list.
	 */
	private ASTNode.NodeList arguments =
		new ASTNode.NodeList(ARGUMENTS_PROPERTY);

	/**
	 * Creates a new AST node for a "super" method invocation expression owned
	 * by the given AST. By default, no qualifier, no type arguments,
	 * an unspecified, but legal, method name, and an empty list of arguments.
	 *
	 * @param ast the AST that is to own this node
	 */
	SuperMethodInvocation(AST ast) {
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
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == QUALIFIER_PROPERTY) {
			if (get) {
				return getQualifier();
			} else {
				setQualifier((Name) child);
				return null;
			}
		}
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((SimpleName) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
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
		return SUPER_METHOD_INVOCATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		SuperMethodInvocation result = new SuperMethodInvocation(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setName((SimpleName) getName().clone(target));
		result.setQualifier((Name) ASTNode.copySubtree(target, getQualifier()));
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
			// visit children in normal left to right reading order
			acceptChild(visitor, getQualifier());
			if (this.ast.apiLevel >= AST.JLS3) {
				acceptChildren(visitor, this.typeArguments);
			}
			acceptChild(visitor, getName());
			acceptChildren(visitor, this.arguments);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the qualifier of this "super" method invocation expression, or
	 * <code>null</code> if there is none.
	 *
	 * @return the qualifier name node, or <code>null</code> if there is none
	 */
	public Name getQualifier() {
		return this.optionalQualifier;
	}

	/**
	 * Sets or clears the qualifier of this "super" method invocation expression.
	 *
	 * @param name the qualifier name node, or <code>null</code> if
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setQualifier(Name name) {
		ASTNode oldChild = this.optionalQualifier;
		preReplaceChild(oldChild, name, QUALIFIER_PROPERTY);
		this.optionalQualifier = name;
		postReplaceChild(oldChild, name, QUALIFIER_PROPERTY);
	}


	/**
	 * Returns true if the resolved return type has been inferred from the assignment context (JLS3 15.12.2.8), false otherwise.
	 * <p>
	 * This information is available only when bindings are requested when the AST is being built
	 * </p>.
	 *
	 * @return true if the resolved return type has been inferred from the assignment context (JLS3 15.12.2.8), false otherwise
	 *  
	 */
	public boolean isResolvedTypeInferredFromExpectedType() {
		return this.ast.getBindingResolver().isResolvedTypeInferredFromExpectedType(this);
	}

	/**
	 * Returns the live ordered list of type arguments of this method
	 * invocation (added in JLS3 API).
	 *
	 * @return the live list of type arguments
	 *    (element type: <code>Type</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 *  
	 */
	public List typeArguments() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.typeArguments == null) {
			unsupportedIn2();
		}
		return this.typeArguments;
	}

	/**
	 * Returns the name of the method invoked in this expression.
	 *
	 * @return the method name node
	 */
	public SimpleName getName() {
		if (this.methodName == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.methodName == null) {
					preLazyInit();
					this.methodName = new SimpleName(this.ast);
					postLazyInit(this.methodName, NAME_PROPERTY);
				}
			}
		}
		return this.methodName;
	}

	/**
	 * Sets the name of the method invoked in this expression to the
	 * given name.
	 *
	 * @param name the new method name
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setName(SimpleName name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.methodName;
		preReplaceChild(oldChild, name, NAME_PROPERTY);
		this.methodName = name;
		postReplaceChild(oldChild, name, NAME_PROPERTY);
	}

	/**
	 * Returns the live ordered list of argument expressions in this
	 * "super" method invocation expression.
	 *
	 * @return the live list of argument expressions
	 *    (element type: <code>Expression</code>)
	 */
	public List arguments() {
		return this.arguments;
	}

	/**
	 * Resolves and returns the binding for the method invoked by this
	 * expression.
	 * <p>
	 * Note that bindings are generally unavailable unless requested when the
	 * AST is being built.
	 * </p>
	 *
	 * @return the method binding, or <code>null</code> if the binding cannot
	 * be resolved
	 *  
	 */
	public IFunctionBinding resolveMethodBinding() {
		return this.ast.getBindingResolver().resolveMethod(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		// treat Code as free
		return BASE_NODE_SIZE + 4 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.optionalQualifier == null ? 0 : getQualifier().treeSize())
			+ (this.typeArguments == null ? 0 : this.typeArguments.listSize())
			+ (this.methodName == null ? 0 : getName().treeSize())
			+ (this.arguments == null ? 0 : this.arguments.listSize());
	}
}

