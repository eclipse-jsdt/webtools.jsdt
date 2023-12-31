/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 * Type declaration AST node type. A type declaration
 * is the union of a class declaration and an interface declaration.
 * For JLS2:
 * <pre>
 * TypeDeclaration:
 * 		ClassDeclaration
 * 		InterfaceDeclaration
 * ClassDeclaration:
 *      [ jsdoc ] { Modifier } <b>class</b> Identifier
 *			[ <b>extends</b> Type]
 *			[ <b>implements</b> Type { <b>,</b> Type } ]
 *			<b>{</b> { ClassBodyDeclaration | <b>;</b> } <b>}</b>
 * InterfaceDeclaration:
 *      [ jsdoc ] { Modifier } <b>interface</b> Identifier
 *			[ <b>extends</b> Type { <b>,</b> Type } ]
 * 			<b>{</b> { InterfaceBodyDeclaration | <b>;</b> } <b>}</b>
 * </pre>
 * For JLS3, type parameters and reified modifiers
 * (and annotations) were added, and the superclass type name and superinterface
 * types names are generalized to type so that parameterized types can be
 * referenced:
 * <pre>
 * TypeDeclaration:
 * 		ClassDeclaration
 * 		InterfaceDeclaration
 * ClassDeclaration:
 *      [ jsdoc ] { ExtendedModifier } <b>class</b> Identifier
 *			[ <b>&lt;</b> TypeParameter { <b>,</b> TypeParameter } <b>&gt;</b> ]
 *			[ <b>extends</b> Type ]
 *			[ <b>implements</b> Type { <b>,</b> Type } ]
 *			<b>{</b> { ClassBodyDeclaration | <b>;</b> } <b>}</b>
 * InterfaceDeclaration:
 *      [ jsdoc ] { ExtendedModifier } <b>interface</b> Identifier
 *			[ <b>&lt;</b> TypeParameter { <b>,</b> TypeParameter } <b>&gt;</b> ]
 *			[ <b>extends</b> Type { <b>,</b> Type } ]
 * 			<b>{</b> { InterfaceBodyDeclaration | <b>;</b> } <b>}</b>
 * </pre>
 * <p>
 * When a jsdoc comment is present, the source
 * range begins with the first character of the "/**" comment delimiter.
 * When there is no jsdoc comment, the source range begins with the first
 * character of the first modifier or annotation (if any), or the
 * first character of the "class" or "interface" keyword (if no
 * modifiers or annotations). The source range extends through the last character of the "}"
 * token following the body declarations.
 * </p>
  * 
 * <p><b>Note: This Class only applies to ECMAScript 4 which is not yet supported</b></p>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class TypeDeclaration extends AbstractTypeDeclaration {

	/**
	 * The "javadoc" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor JAVADOC_PROPERTY =
		internalJavadocPropertyFactory(TypeDeclaration.class);

	/**
	 * The "modifiers" structural property of this node type (JLS2 API only).
	 *  
	 */
	public static final SimplePropertyDescriptor MODIFIERS_PROPERTY =
		internalModifiersPropertyFactory(TypeDeclaration.class);

	/**
	 * The "modifiers" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildListPropertyDescriptor MODIFIERS2_PROPERTY =
		internalModifiers2PropertyFactory(TypeDeclaration.class);

	/**
	 * The "name" structural property of this node type.
	 *  
	 */
	public static final ChildPropertyDescriptor NAME_PROPERTY =
		internalNamePropertyFactory(TypeDeclaration.class);


	/**
	 * The "superclass" structural property of this node type (JLS2 API only).
	 * @deprecated use {@link #SUPERCLASS_EXPRESSION_PROPERTY} 
	 */
	public static final ChildPropertyDescriptor SUPERCLASS_PROPERTY =
		new ChildPropertyDescriptor(TypeDeclaration.class, "superclass", Name.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	
	/**
	 * The "superclass expression" structural property of this node type
	 *  
	 */
	public static final ChildPropertyDescriptor SUPERCLASS_EXPRESSION_PROPERTY =
		new ChildPropertyDescriptor(TypeDeclaration.class, "superclassExpression", Expression.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$
	

	/**
	 * The "superclassType" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildPropertyDescriptor SUPERCLASS_TYPE_PROPERTY =
		new ChildPropertyDescriptor(TypeDeclaration.class, "superclassType", Type.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "bodyDeclarations" structural property of this node type (added in JLS3 API).
	 *  
	 */
	public static final ChildListPropertyDescriptor BODY_DECLARATIONS_PROPERTY =
		internalBodyDeclarationPropertyFactory(TypeDeclaration.class);

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;


	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList(8);
		createPropertyList(TypeDeclaration.class, propertyList);
		addProperty(JAVADOC_PROPERTY, propertyList);
		addProperty(MODIFIERS_PROPERTY, propertyList);
		addProperty(NAME_PROPERTY, propertyList);
		addProperty(SUPERCLASS_PROPERTY, propertyList);
		addProperty(BODY_DECLARATIONS_PROPERTY, propertyList);
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
	 * The optional superclass expression; <code>null</code> if none.
	 * Defaults to none
	 */
	private Expression superclassExpression = null;

	/**
	 * The optional superclass type; <code>null</code> if none.
	 * Defaults to none. Note that this field is not used for
	 * interface declarations. Null in JLS2. Added in JLS3.
	 *  
	 */
	private Type optionalSuperclassType = null;

	/**
	 * Creates a new AST node for a type declaration owned by the given
	 * AST. By default, the type declaration is for a class of an
	 * unspecified, but legal, name; no modifiers; no javadoc;
	 * no type parameters; no superclass or superinterfaces; and an empty list
	 * of body declarations.
	 * <p>
	 * N.B. This constructor is package-private; all subclasses must be
	 * declared in the same package; clients are unable to declare
	 * additional subclasses.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 */
	TypeDeclaration(AST ast) {
		super(ast);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 *  
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
				internalSetModifiers(value);
				return 0;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetIntProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		// allow default implementation to flag the error
		return super.internalGetSetBooleanProperty(property, get, value);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == JAVADOC_PROPERTY) {
			if (get) {
				return getJavadoc();
			} else {
				setJavadoc((JSdoc) child);
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
		if (property == SUPERCLASS_PROPERTY) {
			if (get) {
				return getSuperclass();
			} else {
				setSuperclass((Name) child);
				return null;
			}
		}
		if (property == SUPERCLASS_EXPRESSION_PROPERTY) {
			if (get) {
				return getSuperclassExpression();
			} else {
				setSuperclassExpression((Expression) child);
				return null;
			}
		}
		if (property == SUPERCLASS_TYPE_PROPERTY) {
			if (get) {
				return getSuperclassType();
			} else {
				setSuperclassType((Type) child);
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
		if (property == BODY_DECLARATIONS_PROPERTY) {
			return bodyDeclarations();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final ChildPropertyDescriptor internalJavadocProperty() {
		return JAVADOC_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final ChildListPropertyDescriptor internalModifiers2Property() {
		return MODIFIERS2_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on BodyDeclaration.
	 */
	final SimplePropertyDescriptor internalModifiersProperty() {
		return MODIFIERS_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on AbstractTypeDeclaration.
	 */
	final ChildPropertyDescriptor internalNameProperty() {
		return NAME_PROPERTY;
	}

	/* (omit javadoc for this method)
	 * Method declared on AbstractTypeDeclaration.
	 */
	final ChildListPropertyDescriptor internalBodyDeclarationsProperty() {
		return BODY_DECLARATIONS_PROPERTY;
	}


	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return TYPE_DECLARATION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		TypeDeclaration result = new TypeDeclaration(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setJavadoc(
			(JSdoc) ASTNode.copySubtree(target, getJavadoc()));
		result.setSuperclassExpression(
					(Expression) ASTNode.copySubtree(target, getSuperclassExpression()));
		if (this.ast.apiLevel == AST.JLS2_INTERNAL) {
			result.internalSetModifiers(getModifiers());
		}
		result.setName((SimpleName) getName().clone(target));
		result.bodyDeclarations().addAll(
			ASTNode.copySubtrees(target, bodyDeclarations()));
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
			acceptChild(visitor, getJavadoc());
			acceptChildren(visitor, this.modifiers);
			acceptChild(visitor, getName());
			acceptChild(visitor, getSuperclassExpression());
			acceptChildren(visitor, this.bodyDeclarations);
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns the name of the superclass declared in this type
	 * declaration, or <code>null</code> if there is none (JLS2 API only).
	 * <p>
	 * Note that this child is not relevant for interface
	 * declarations (although it does still figure in subtree
	 * equality comparisons).
	 * </p>
	 *
	 * @return the superclass name node, or <code>null</code> if
	 *    there is none
	 */
	public Name getSuperclass() {
		return internalGetSuperclass();
	}

	/**
	 * Internal synonym for deprecated method. Used to avoid
	 * deprecation warnings.
	 *  
	 */
	/*package*/ final Name internalGetSuperclass() {
		return (Name) this.superclassExpression;
	}

	/**
	* Returns the superclass declared in this type
	* declaration, or <code>null</code> if there is none (added in JLS3 API).
	* <p>
	* Note that this child is not relevant for interface
	* declarations (although it does still figure in subtree
	* equality comparisons).
	* </p>
	*
	* @return the superclass type node, or <code>null</code> if
	*    there is none
	*  
	*/
	public Type getSuperclassType() {
		return this.optionalSuperclassType;
	}

	/**
	 * Sets or clears the name of the superclass declared in this type
	 * declaration (JLS2 API only).
	 * <p>
	 * Note that this child is not relevant for interface
	 * declarations (although it does still figure in subtree
	 * equality comparisons).
	 * </p>
	 *
	 * @param superclassName the superclass name node, or <code>null</code> if
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setSuperclass(Name superclassName) {
		internalSetSuperclass(superclassName);
	}

	/**
	* Returns the superclass expression declared in this type
	* declaration, or <code>null</code> if there is none
	*
	* @return the superclass type node, or <code>null</code> if
	*    there is none
	*  
	*/
	public Expression getSuperclassExpression() {
		return superclassExpression;
	}

	/**
	 * Sets or clears the expression of the superclass declared in this type
	 * declaration
	 *
	 * @param superclassName the superclass name node, or <code>null</code> if
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 */
	public void setSuperclassExpression(Expression superclassExpression) {
		ASTNode oldChild = this.superclassExpression;
		preReplaceChild(oldChild, superclassExpression, SUPERCLASS_EXPRESSION_PROPERTY);
		this.superclassExpression = superclassExpression;
		postReplaceChild(oldChild, superclassExpression, SUPERCLASS_EXPRESSION_PROPERTY);
	}

	/**
	 * Internal synonym for deprecated method. Used to avoid
	 * deprecation warnings.
	 *  
	 */
	/*package*/ final void internalSetSuperclass(Name superclassName) {
		ASTNode oldChild = this.superclassExpression;
		preReplaceChild(oldChild, superclassName, SUPERCLASS_PROPERTY);
		this.superclassExpression = superclassName;
		postReplaceChild(oldChild, superclassName, SUPERCLASS_PROPERTY);
	}

	/**
	 * Sets or clears the superclass declared in this type
	 * declaration (added in JLS3 API).
	 * <p>
	 * Note that this child is not relevant for interface declarations
	 * (although it does still figure in subtree equality comparisons).
	 * </p>
	 *
	 * @param superclassType the superclass type node, or <code>null</code> if
	 *    there is none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @exception UnsupportedOperationException if this operation is used in
	 * a JLS2 AST
	 *  
	 */
	public void setSuperclassType(Type superclassType) {
	    unsupportedIn2();
		ASTNode oldChild = this.optionalSuperclassType;
		preReplaceChild(oldChild, superclassType, SUPERCLASS_TYPE_PROPERTY);
		this.optionalSuperclassType = superclassType;
		postReplaceChild(oldChild, superclassType, SUPERCLASS_TYPE_PROPERTY);
 	}

	/**
	 * Returns the ordered list of field declarations of this type
	 * declaration. For a class declaration, these are the
	 * field declarations; for an interface declaration, these are
	 * the constant declarations.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-fields filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 *
	 * @return the (possibly empty) list of field declarations
	 */
	public FieldDeclaration[] getFields() {
		List bd = bodyDeclarations();
		int fieldCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof FieldDeclaration) {
				fieldCount++;
			}
		}
		FieldDeclaration[] fields = new FieldDeclaration[fieldCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof FieldDeclaration) {
				fields[next++] = (FieldDeclaration) decl;
			}
		}
		return fields;
	}

	/**
	 * Returns the ordered list of method declarations of this type
	 * declaration.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-methods filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 *
	 * @return the (possibly empty) list of method (and constructor)
	 *    declarations
	 */
	public FunctionDeclaration[] getMethods() {
		List bd = bodyDeclarations();
		int methodCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof FunctionDeclaration) {
				methodCount++;
			}
		}
		FunctionDeclaration[] methods = new FunctionDeclaration[methodCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof FunctionDeclaration) {
				methods[next++] = (FunctionDeclaration) decl;
			}
		}
		return methods;
	}

	/**
	 * Returns the ordered list of member type declarations of this type
	 * declaration.
	 * <p>
	 * This convenience method returns this node's body declarations
	 * with non-types filtered out. Unlike <code>bodyDeclarations</code>,
	 * this method does not return a live result.
	 * </p>
	 *
	 * @return the (possibly empty) list of member type declarations
	 */
	public TypeDeclaration[] getTypes() {
		List bd = bodyDeclarations();
		int typeCount = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			if (it.next() instanceof TypeDeclaration) {
				typeCount++;
			}
		}
		TypeDeclaration[] memberTypes = new TypeDeclaration[typeCount];
		int next = 0;
		for (Iterator it = bd.listIterator(); it.hasNext(); ) {
			Object decl = it.next();
			if (decl instanceof TypeDeclaration) {
				memberTypes[next++] = (TypeDeclaration) decl;
			}
		}
		return memberTypes;
	}

	/* (omit javadoc for this method)
	 * Method declared on AsbtractTypeDeclaration.
	 */
	ITypeBinding internalResolveBinding() {
		return this.ast.getBindingResolver().resolveType(this);
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return super.memSize() + 6 * 4;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return memSize()
			+ (this.optionalDocComment == null ? 0 : getJavadoc().treeSize())
			+ (this.modifiers == null ? 0 : this.modifiers.listSize())
			+ (this.typeName == null ? 0 : getName().treeSize())
			+ (this.superclassExpression == null ? 0 : getSuperclassExpression().treeSize())
			+ (this.optionalSuperclassType == null ? 0 : getSuperclassType().treeSize())
			+ this.bodyDeclarations.listSize();
	}
}

