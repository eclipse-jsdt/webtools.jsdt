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
 * Variable declaration fragment AST node type, used in field declarations,
 * local variable declarations, and <code>ForStatement</code> initializers.
 * It contrast to <code>SingleVariableDeclaration</code>, fragments are
 * missing the modifiers and the type; these are located in the fragment's
 * parent node.
 *
 * <pre>
 * VariableDeclarationFragment:
 *    Identifier { <b>[</b><b>]</b> } [ <b>=</b> Expression ]
 * </pre>
 *
  * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class VariableDeclarationFragment extends VariableDeclaration {

	/**
	 * The "name" structural property of this node type.
	 * @deprecated use #PATTERN_PROPERTY 
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY =
		new ChildPropertyDescriptor(VariableDeclarationFragment.class, "name",SimpleName.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$
	
	
	/**
	 * The "name" structural property of this node type.
	 * 
	 *  @since 2.0
	 */
	public static final ChildPropertyDescriptor  PATTERN_PROPERTY = 
				new ChildPropertyDescriptor(VariableDeclarationFragment.class, "pattern", Name.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$


	/**
	 * The "extraDimensions" structural property of this node type.
	 *  
	 */
	public static final SimplePropertyDescriptor EXTRA_DIMENSIONS_PROPERTY =
		new SimplePropertyDescriptor(VariableDeclarationFragment.class, "extraDimensions", int.class, MANDATORY); //$NON-NLS-1$

	/**
	 * The "initializer" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor INITIALIZER_PROPERTY =
		new ChildPropertyDescriptor(VariableDeclarationFragment.class, "initializer", Expression.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List propertyList = new ArrayList(4);
		createPropertyList(VariableDeclarationFragment.class, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		addProperty(PATTERN_PROPERTY, propertyList);
		addProperty(EXTRA_DIMENSIONS_PROPERTY, propertyList);
		addProperty(INITIALIZER_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
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
		return PROPERTY_DESCRIPTORS;
	}

	/**
	 * The variable name; lazily initialized; defaults to an unspecified,
	 * legal JavaScript identifier.
	 */
	private SimpleName variableName = null;
	
	/**
	 * The variable pattern name lazily initialized; defaults to a unspecified,
	 * legal JavaScript identifier.
	 */
	private Name pattern = null;

	/**
	 * The number of extra array dimensions that this variable has;
	 * defaults to 0.
	 */
	private int extraArrayDimensions = 0;

	/**
	 * The initializer expression, or <code>null</code> if none;
	 * defaults to none.
	 */
	private Expression optionalInitializer = null;

	/**
	 * Creates a new AST node for a variable declaration fragment owned by the
	 * given AST. By default, the variable declaration has: an unspecified
	 * (but legal) variable name, no initializer, and no extra array dimensions.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 */
	VariableDeclarationFragment(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 *  
	 */
	final SimplePropertyDescriptor internalExtraDimensionsProperty() {
		return EXTRA_DIMENSIONS_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 *  
	 */
	final ChildPropertyDescriptor internalInitializerProperty() {
		return INITIALIZER_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 *  
	 */
	final ChildPropertyDescriptor internalNameProperty() {
		return NAME_PROPERTY;
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
	final int internalGetSetIntProperty(SimplePropertyDescriptor property, boolean get, int value) {
		if (property == EXTRA_DIMENSIONS_PROPERTY) {
			if (get) {
				return getExtraDimensions();
			} else {
				setExtraDimensions(value);
				return 0;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetIntProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == NAME_PROPERTY) {
			if (get) {
				return getName();
			} else {
				setName((SimpleName) child);
				return null;
			}
		}
		if (property == INITIALIZER_PROPERTY) {
			if (get) {
				return getInitializer();
			} else {
				setInitializer((Expression) child);
				return null;
			}
		}
		if(property == PATTERN_PROPERTY ){
			if(get){
				return this.getPattern();
			}else{
				setPattern((Name)child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return VARIABLE_DECLARATION_FRAGMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		VariableDeclarationFragment result = new VariableDeclarationFragment(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setPattern((Name) getPattern().clone(target));
		result.setExtraDimensions(getExtraDimensions());
		result.setInitializer(
			(Expression) ASTNode.copySubtree(target, getInitializer()));
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
			acceptChild(visitor, getPattern());
			acceptChild(visitor, getInitializer());
		}
		visitor.endVisit(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 */
	public SimpleName getName() {
		if(this.getPattern().getNodeType() != SIMPLE_NAME){
			return null;
		}
		return (SimpleName) this.getPattern();
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 */
	public void setName(SimpleName variableName) {
		setPattern(variableName);
	}

	/**
	 * Returns the number of extra array dimensions this variable has over
	 * and above the type specified in the enclosing declaration.
	 * <p>
	 * For example, in the AST for <code>int[] i, j[], k[][]</code> the
	 * variable declaration fragments for the variables <code>i</code>,
	 * <code>j</code>, and <code>k</code>, have 0, 1, and 2 extra array
	 * dimensions, respectively.
	 * </p>
	 *
	 * @return the number of extra array dimensions this variable has over
	 *         and above the type specified in the enclosing declaration
	 *  
	 */
	public int getExtraDimensions() {
		return this.extraArrayDimensions;
	}

	/**
	 * Sets the number of extra array dimensions this variable has over
	 * and above the type specified in the enclosing declaration.
	 * <p>
	 * For example, in the AST for <code>int[] i, j[], k[][]</code> the
	 * variable declaration fragments for the variables <code>i</code>,
	 * <code>j</code>, and <code>k</code>, have 0, 1, and 2 extra array
	 * dimensions, respectively.
	 * </p>
	 *
	 * @param dimensions the given dimensions
	 *  
	 */
	public void setExtraDimensions(int dimensions) {
		if (dimensions < 0) {
			throw new IllegalArgumentException();
		}
		preValueChange(EXTRA_DIMENSIONS_PROPERTY);
		this.extraArrayDimensions = dimensions;
		postValueChange(EXTRA_DIMENSIONS_PROPERTY);
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 */
	public Expression getInitializer() {
		return this.optionalInitializer;
	}

	/* (omit javadoc for this method)
	 * Method declared on VariableDeclaration.
	 */
	public void setInitializer(Expression initializer) {
		ASTNode oldChild = this.optionalInitializer;
		preReplaceChild(oldChild, initializer, INITIALIZER_PROPERTY);
		this.optionalInitializer = initializer;
		postReplaceChild(oldChild, initializer, INITIALIZER_PROPERTY);
	}

	/**
	 * Returns the variable pattern for this node.
	 * @return
	 */
	public Name getPattern() {
		if (this.pattern == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.pattern == null) {
					preLazyInit();
					this.pattern = new SimpleName(this.ast);
					postLazyInit(this.pattern, PATTERN_PROPERTY);
				}
			}
		}
		return pattern;
	}
	/**
	 * Sets the pattern of the variable declared in this variable declaration
	 * to the given pattern.
	 *
	 * @param pattern the new pattern
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setPattern(Name pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.pattern;
		preReplaceChild(oldChild, pattern, PATTERN_PROPERTY);
		this.pattern = pattern;
		postReplaceChild(oldChild, pattern, PATTERN_PROPERTY);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		// treat Operator as free
		return BASE_NODE_SIZE + 3 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.variableName == null ? 0 : getName().treeSize())
			+ (this.optionalInitializer == null ? 0 : getInitializer().treeSize());
	}
}
