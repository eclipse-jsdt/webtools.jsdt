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
import java.util.Iterator;
import java.util.List;

/**
 * Local variable declaration statement AST node type.
 * <p>
 * This kind of node collects several variable declaration fragments
 * (<code>VariableDeclarationFragment</code>) into a statement
 * (<code>Statement</code>), all sharing the same modifiers and base type.
 * </p>
 * For JLS2:
 * <pre>
 * VariableDeclarationStatement:
 *    { Modifier } Type VariableDeclarationFragment
 *        { <b>,</b> VariableDeclarationFragment } <b>;</b>
 * </pre>
 * For JLS3, the modifier flags were replaced by
 * a list of modifier nodes (intermixed with annotations):
 * <pre>
 * VariableDeclarationStatement:
 *    { ExtendedModifier } Type VariableDeclarationFragment
 *        { <b>,</b> VariableDeclarationFragment } <b>;</b>
 * </pre>
 * <p>
 * Note: This type of node is a convenience of sorts.
 * An equivalent way to represent the same statement is to use
 * a <code>VariableDeclarationExpression</code>
 * wrapped in an <code>ExpressionStatement</code>.
 * </p>
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class VariableDeclarationStatement extends Statement {
	
	/**
	 * The "modifiers" structural property of this node type (JLS2 API only).
	 *  
	 */
	public static final SimplePropertyDescriptor MODIFIERS_PROPERTY =
		new SimplePropertyDescriptor(VariableDeclarationStatement.class, "modifiers", int.class, MANDATORY); //$NON-NLS-1$

	public static final ChildPropertyDescriptor JAVADOC_PROPERTY =
		new ChildPropertyDescriptor(VariableDeclarationStatement.class, "javadoc", JSdoc.class, OPTIONAL, NO_CYCLE_RISK);  //$NON-NLS-1$

	/**
	 * The "modifiers" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildListPropertyDescriptor MODIFIERS2_PROPERTY =
		new ChildListPropertyDescriptor(VariableDeclarationStatement.class, "modifiers", IExtendedModifier.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "type" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor TYPE_PROPERTY =
		new ChildPropertyDescriptor(VariableDeclarationStatement.class, "type", Type.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "fragments" structural property of this node type).
	 *  
	 */
	public static final ChildListPropertyDescriptor FRAGMENTS_PROPERTY =
		new ChildListPropertyDescriptor(VariableDeclarationStatement.class, "fragments", VariableDeclarationFragment.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * the kind structured property as introduced in Ecmascript 2015 
	 */
	public static final SimplePropertyDescriptor KIND_PROPERTY = 
				new SimplePropertyDescriptor(VariableDeclarationStatement.class, "kind", VariableKind.class, MANDATORY); //$NON-NLS-1$

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
		List propertyList = new ArrayList(5);
		createPropertyList(VariableDeclarationStatement.class, propertyList);
		addProperty(MODIFIERS_PROPERTY, propertyList);
		addProperty(TYPE_PROPERTY, propertyList);
		addProperty(FRAGMENTS_PROPERTY, propertyList);
		addProperty(JAVADOC_PROPERTY, propertyList);
		addProperty(KIND_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS_2_0 = reapPropertyList(propertyList);

		propertyList = new ArrayList(5);
		createPropertyList(VariableDeclarationStatement.class, propertyList);
		addProperty(MODIFIERS2_PROPERTY, propertyList);
		addProperty(TYPE_PROPERTY, propertyList);
		addProperty(FRAGMENTS_PROPERTY, propertyList);
		addProperty(KIND_PROPERTY, propertyList);
		addProperty(JAVADOC_PROPERTY, propertyList);
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
	 * The extended modifiers (element type: <code>IExtendedModifier</code>).
	 * Null in JLS2. Added in JLS3; defaults to an empty list
	 * (see constructor).
	 *  
	 */
	private ASTNode.NodeList modifiers = null;

	/**
	 * The modifier flagss; bit-wise or of Modifier flags.
	 * Defaults to none. Not used in JLS3.
	 */
	private int modifierFlags = Modifier.NONE;
	
	private VariableKind kind = VariableKind.VAR;


	JSdoc optionalDocComment = null;

	/**
	 * The base type; lazily initialized; defaults to an unspecified,
	 * legal type.
	 */
	private Type baseType = null;

	/**
	 * The list of variable variable declaration fragments (element type:
	 * <code VariableDeclarationFragment</code>).  Defaults to an empty list.
	 */
	private ASTNode.NodeList variableDeclarationFragments =
		new ASTNode.NodeList(FRAGMENTS_PROPERTY);

	/**
	 * Creates a new unparented local variable declaration statement node owned
	 * by the given AST.  By default, the variable declaration has: no modifiers,
	 * an unspecified (but legal) type, and an empty list of variable
	 * declaration fragments (which is syntactically illegal).
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 */
	VariableDeclarationStatement(AST ast) {
		super(ast);
		if (ast.apiLevel >= AST.JLS3) {
			this.modifiers = new ASTNode.NodeList(MODIFIERS2_PROPERTY);
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
	final int internalGetSetIntProperty(SimplePropertyDescriptor property, boolean get, int value) {
		if (property == MODIFIERS_PROPERTY) {
			if (get) {
				return getModifiers();
			} else {
				setModifiers(value);
				return 0;
			}
		}

		// allow default implementation to flag the error
		return super.internalGetSetIntProperty(property, get, value);
	}
/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalGetSetObjectProperty(org.eclipse.wst.jsdt.core.dom.SimplePropertyDescriptor, boolean, java.lang.Object)
 */
Object internalGetSetObjectProperty(SimplePropertyDescriptor property, boolean get, Object value) {
	if (property == KIND_PROPERTY) {
		if (get) {
			return getKind();
		} else {
			setKind((VariableKind) value);
			return null;
		}
	}
	return super.internalGetSetObjectProperty(property, get, value);
}
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == TYPE_PROPERTY) {
			if (get) {
				return getType();
			} else {
				setType((Type) child);
				return null;
			}
		}
		if (property == JAVADOC_PROPERTY) {
			if (get) {
				return getJavadoc();
			} else {
				setJavadoc((JSdoc) child);
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
		if (property == MODIFIERS2_PROPERTY) {
			return modifiers();
		}
		if (property == FRAGMENTS_PROPERTY) {
			return fragments();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return VARIABLE_DECLARATION_STATEMENT;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		VariableDeclarationStatement result =
			new VariableDeclarationStatement(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.copyLeadingComment(this);
		if (this.ast.apiLevel == AST.JLS2_INTERNAL) {
			result.setModifiers(getModifiers());
		}
		if (this.ast.apiLevel >= AST.JLS3) {
			result.modifiers().addAll(ASTNode.copySubtrees(target, modifiers()));
		}
		result.setType((Type) getType().clone(target));
		result.setJavadoc(
				(JSdoc) ASTNode.copySubtree(target, getJavadoc()));
		result.setKind(getKind());
		result.fragments().addAll(
			ASTNode.copySubtrees(target, fragments()));
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
			if (this.ast.apiLevel >= AST.JLS3) {
				acceptChildren(visitor, this.modifiers);
			}
			acceptChild(visitor, getType());
			acceptChild(visitor, getJavadoc());
			acceptChildren(visitor, this.variableDeclarationFragments);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the live ordered list of modifiers and annotations
	 * of this declaration (added in JLS3 API).
	 * <p>
	 * Note that the final modifier is the only meaningful modifier for local
	 * variable declarations.
	 * </p>
	 *
	 * @return the live list of modifiers and annotations
	 *    (element type: <code>IExtendedModifier</code>)
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 *  
	 */
	public List modifiers() {
		// more efficient than just calling unsupportedIn2() to check
		if (this.modifiers == null) {
			unsupportedIn2();
		}
		return this.modifiers;
	}

	/**
	 * Returns the modifiers explicitly specified on this declaration.
	 * <p>
	 * In the JLS3 API, this method is a convenience method that
	 * computes these flags from <code>modifiers()</code>.
	 * </p>
	 *
	 * @return the bit-wise or of <code>Modifier</code> constants
	 * @see Modifier
	 */
	public int getModifiers() {
		// more efficient than checking getAST().API_LEVEL
		if (this.modifiers == null) {
			// JLS2 behavior - bona fide property
			return this.modifierFlags;
		} else {
			// JLS3 behavior - convenience method
			// performance could be improved by caching computed flags
			// but this would require tracking changes to this.modifiers
			int computedModifierFlags = Modifier.NONE;
			for (Iterator it = modifiers().iterator(); it.hasNext(); ) {
				Object x = it.next();
				if (x instanceof Modifier) {
					computedModifierFlags |= ((Modifier) x).getKeyword().toFlagValue();
				}
			}
			return computedModifierFlags;
		}
	}

	/**
	 * Sets the modifiers explicitly specified on this declaration (JLS2 API only).
	 * <p>
	 * Note that the final modifier is the only meaningful modifier for local
	 * variable declarations.
	 * </p>
	 *
	 * @param modifiers the given modifiers (bit-wise or of <code>Modifier</code> constants)
	 * @exception UnsupportedOperationException if this operation is used in
	 * an AST later than JLS2
	 * @see Modifier
	 * @deprecated In the JLS3 API, this method is replaced by
	 * {@link  #modifiers()} which contains a list of a <code>Modifier</code> nodes.
	 */
	public void setModifiers(int modifiers) {
		internalSetModifiers(modifiers);
	}

	/**
	 * Internal synonym for deprecated method. Used to avoid
	 * deprecation warnings.
	 *  
	 */
	/*package*/ final void internalSetModifiers(int pmodifiers) {
	    supportedOnlyIn2();
		preValueChange(MODIFIERS_PROPERTY);
		this.modifierFlags = pmodifiers;
		postValueChange(MODIFIERS_PROPERTY);
	}
	
	public VariableKind getKind() {
		return kind;
	}

	public void setKind(VariableKind kind) {
		preValueChange(KIND_PROPERTY);
		this.kind = kind;
		postValueChange(KIND_PROPERTY);
	}

	/**
	 * Returns the base type declared in this variable declaration statement.
	 * <p>
	 * N.B. The individual child variable declaration fragments may specify
	 * additional array dimensions. So the type of the variable are not
	 * necessarily exactly this type.
	 * </p>
	 *
	 * @return the base type
	 */
	public Type getType() {
		if (this.baseType == null) {
			// lazy init must be thread-safe for readers
			synchronized (this) {
				if (this.baseType == null) {
					preLazyInit();
					this.baseType = this.ast.newInferredType(null);
					postLazyInit(this.baseType, TYPE_PROPERTY);
				}
			}
		}
		return this.baseType;
	}

	/**
	 * Sets the base type declared in this variable declaration statement to
	 * the given type.
	 *
	 * @param type the new base type
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setType(Type type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.baseType;
		preReplaceChild(oldChild, type, TYPE_PROPERTY);
		this.baseType = type;
		postReplaceChild(oldChild, type, TYPE_PROPERTY);
	}

	/**
	 * Returns the live list of variable declaration fragments in this statement.
	 * Adding and removing nodes from this list affects this node dynamically.
	 * All nodes in this list must be <code>VariableDeclarationFragment</code>s;
	 * attempts to add any other type of node will trigger an
	 * exception.
	 *
	 * @return the live list of variable declaration fragments in this
	 *    statement (element type: <code>VariableDeclarationFragment</code>)
	 */
	public List fragments() {
		return this.variableDeclarationFragments;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 5 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
			memSize()
			+ (this.modifiers == null ? 0 : this.modifiers.listSize())
			+ (this.baseType == null ? 0 : getType().treeSize())
			+ this.variableDeclarationFragments.listSize();
	}

	public IVariableBinding resolveBinding() {
		return this.ast.getBindingResolver().resolveVariable(this);
	}

	/**
	 * Returns the doc comment node.
	 *
	 * @return the doc comment node, or <code>null</code> if none
	 */
	public JSdoc getJavadoc() {
		return this.optionalDocComment;
	}

	/**
	 * Sets or clears the doc comment node.
	 *
	 * @param docComment the doc comment node, or <code>null</code> if none
	 * @exception IllegalArgumentException if the doc comment string is invalid
	 */
	public void setJavadoc(JSdoc docComment) {
		ChildPropertyDescriptor p = internalJavadocProperty();
		ASTNode oldChild = this.optionalDocComment;
		preReplaceChild(oldChild, docComment, p);
		this.optionalDocComment = docComment;
		postReplaceChild(oldChild, docComment, p);
	}
	final ChildPropertyDescriptor internalJavadocProperty() {
		return JAVADOC_PROPERTY;
	}


}

