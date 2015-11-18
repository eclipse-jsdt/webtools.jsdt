/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * AST Node for ES2015 Template literals
 * 
 * @author Gorkem Ercan
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *
 */
public class TemplateLiteral extends Expression {


	/**
	 * The "tag" structural property of this node type
	 */
	public static final ChildPropertyDescriptor TAG_PROPERTY =
				new ChildPropertyDescriptor(TemplateLiteral.class, "tag", Expression.class, OPTIONAL, NO_CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "elements" structural property of this node type
	 */	
	public static final ChildListPropertyDescriptor ELEMENTS_PROPERTY =
				new ChildListPropertyDescriptor(TemplateLiteral.class, "elements", TemplateElement.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "expressions" structural property of this node type
	 */
	public static final ChildListPropertyDescriptor EXPRESSIONS_PROPERTY =
				new ChildListPropertyDescriptor(TemplateLiteral.class, "parameters", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	
	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(4);
		createPropertyList(TemplateLiteral.class, propertyList);
		addProperty(TAG_PROPERTY, propertyList);
		addProperty(ELEMENTS_PROPERTY, propertyList);
		addProperty(EXPRESSIONS_PROPERTY, propertyList);
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
	public static List<StructuralPropertyDescriptor> propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	/**
	 * tag value for the template literal
	 */
	private Expression tag = null;
	
	private ASTNode.NodeList elements =
				new ASTNode.NodeList(ELEMENTS_PROPERTY);
	
	private ASTNode.NodeList expressions =
				new ASTNode.NodeList(EXPRESSIONS_PROPERTY);
	
	/**
	 * @param ast
	 */
	TemplateLiteral(AST ast) {
		super(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return TEMPLATE_LITERAL;
	}
	
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == TAG_PROPERTY) {
			if (get) {
				return getTag();
			} else {
				setTag((Expression)child);
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
		if (property == ELEMENTS_PROPERTY) {
			return elements();
		}
		if (property == EXPRESSIONS_PROPERTY) {
			return expressions();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}
	

	/**
	 * Returns the live ordered list of expressions for 
	 * this template literal
	 * 
	 * @return live list of expressions of type {@link Expression}
	 */
	public List expressions() {
		return this.expressions;
	}

	/**
	 * Returns the live ordered list of {@link TemplateElement}s 
	 * for this template literal 
	 * 
	 * @return live list of {@link TemplateElement}s
	 */
	public List elements() {
		return this.elements;
	}
	
	/**
	 * Returns the tag for the template Literal or null of the 
	 * literal is not tagged.
	 * @return tag or null
	 */
	public Expression getTag() {
		return tag;
	}
	/**
	 * Sets the given tag value as tad for this template literal
	 * 
	 * @param tag
	 */
	public void setTag(Expression tag) {
		if (tag == null) {
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.tag;
		preReplaceChild(oldChild,tag, TAG_PROPERTY);
		this.tag = tag;
		postReplaceChild(oldChild, tag, TAG_PROPERTY);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#subtreeMatch0(org.eclipse.wst.jsdt.core.dom.ASTMatcher, java.lang.Object)
	 */
	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		return matcher.match(this, other);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#clone0(org.eclipse.wst.jsdt.core.dom.AST)
	 */
	ASTNode clone0(AST target) {
		TemplateLiteral result = new TemplateLiteral(target);
		if(this.tag != null){
			result.setTag((Expression) getTag().clone(target));
		}
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.elements().addAll(ASTNode.copySubtrees(target, this.elements()));
		result.expressions().addAll(ASTNode.copySubtrees(target, this.expressions()));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren){
			acceptChild(visitor, getTag());
			acceptChildren(visitor, this.elements);
			acceptChildren(visitor, this.expressions);
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize() 
					+ this.elements.listSize()
					+ this.expressions.listSize();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NODE_SIZE + 3 * 4;
	}

}
