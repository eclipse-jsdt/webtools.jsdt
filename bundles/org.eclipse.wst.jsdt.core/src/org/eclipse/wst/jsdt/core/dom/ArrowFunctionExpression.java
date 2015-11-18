/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
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
 * Arrow expression AST Node type
 * 
 * <pre>
 * ArrowFunctionExpression:
 *  ( parameters ) => {body} | expression
 * </pre>
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 * 
 * @author Gorkem Ercan
 *
 */
public class ArrowFunctionExpression extends Expression {

	public static final ChildListPropertyDescriptor PARAMETERS_PROPERTY = 
			new ChildListPropertyDescriptor(ArrowFunctionExpression.class, "parameters", SingleVariableDeclaration.class, CYCLE_RISK); //$NON-NLS-1$

	public static final ChildPropertyDescriptor BODY_PROPERTY =
		new ChildPropertyDescriptor(ArrowFunctionExpression.class, "body", Block.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	public static final ChildPropertyDescriptor EXPRESSION_PROPERTY = 
				new ChildPropertyDescriptor(ArrowFunctionExpression.class, "expression", Expression.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	static{
		List<StructuralPropertyDescriptor> propertyList = new ArrayList<StructuralPropertyDescriptor>(4);
		createPropertyList(ArrowFunctionExpression.class, propertyList);
		addProperty(PARAMETERS_PROPERTY, propertyList);
		addProperty(BODY_PROPERTY, propertyList);
		addProperty(EXPRESSION_PROPERTY, propertyList);
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
	
	private ASTNode.NodeList parameters = new ASTNode.NodeList(PARAMETERS_PROPERTY);
	
	private Block body;
	
	private Expression expression;
	
	
	/**
	 * @param ast
	 */
	ArrowFunctionExpression(AST ast) {
		super(ast);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalGetChildListProperty(org.eclipse.wst.jsdt.core.dom.ChildListPropertyDescriptor)
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if(property == PARAMETERS_PROPERTY){
			return parameters();
		}
		return super.internalGetChildListProperty(property);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalGetSetChildProperty(org.eclipse.wst.jsdt.core.dom.ChildPropertyDescriptor, boolean, org.eclipse.wst.jsdt.core.dom.ASTNode)
	 */
	ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode value) {
		if(property==EXPRESSION_PROPERTY){
			if(get){
				return getExpression();
			}
			else{
				setExpression((Expression) value);
				return null;
			}
		}
		if(property == BODY_PROPERTY){
			if(get){
				return getBody();
			}
			else{
				setBody((Block) value);
				return null;
			}
		}
		return super.internalGetSetChildProperty(property, get, value);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return ARROW_FUNCTION_EXPRESSION;
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
		ArrowFunctionExpression result = new ArrowFunctionExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		if(expression != null ){
			result.setExpression((Expression) getExpression().clone(target));
		}
		if(body != null ){
			result.setBody((Block) ASTNode.copySubtree(target,getBody()));
		}
		result.parameters.addAll(ASTNode.copySubtrees(target,this.parameters()));
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren){
			if(expression != null ){
				acceptChild(visitor, getExpression());
			}
			if(body != null){
				acceptChild(visitor, getBody());
			}
			acceptChildren(visitor, parameters);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize()
					+ (this.expression == null ? 0 : getExpression().treeSize())
					+ (this.body == null ? 0 : getBody().treeSize())
					+ parameters.listSize();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		// both expression and body are never specified together
		return BASE_NODE_SIZE + 2 * 4;
	}

	public List parameters() {
		return parameters;
	}

	/**
	 * Returns block body or null
	 * @return
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * Set the body for this arrow expression 
	 * 
	 * @param body the argument block node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 */
	public void setBody(Block body) {
		if(body == null ){
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.body;
		preReplaceChild(oldChild, body, BODY_PROPERTY);
		this.body = body;
		postReplaceChild(oldChild, body, BODY_PROPERTY);
	}

	/**
	 * Returns expression or null
	 * @return
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Sets the expression of this arrow expression
	 * 
	 * @param expression the expression node
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * <li>a cycle in would be created</li>
	 * </ul>
	 * 
	 * @param expression
	 */
	public void setExpression(Expression expression) {
		if(expression == null){
			throw new IllegalArgumentException();
		}
		ASTNode oldChild = this.expression;
		preReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
		this.expression = expression;
		postReplaceChild(oldChild, expression, EXPRESSION_PROPERTY);
	}

}
