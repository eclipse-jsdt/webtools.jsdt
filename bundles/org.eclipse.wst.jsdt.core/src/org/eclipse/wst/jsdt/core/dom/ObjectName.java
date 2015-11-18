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
 * Object pattern 
 * 
 * @author Gorkem Ercan
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 *
 */
public class ObjectName extends Name{

	
	/**
	 * The "objectProps" structural property of this node type.
	 */
	public static final ChildListPropertyDescriptor PROPERTIES_PROPERTY =
				new ChildListPropertyDescriptor(ObjectName.class, "objectProps", ObjectLiteralField.class, CYCLE_RISK); //$NON-NLS-1$
	
	
	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 *  
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;
	
	static {
		List<StructuralPropertyDescriptor> propertyList = new ArrayList(2);
		createPropertyList(ObjectName.class, propertyList);
		addProperty(PROPERTIES_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}
	
	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the AST.JLS* constants
	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 *  
	 */
	public static List<StructuralPropertyDescriptor> propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
	
	private ASTNode.NodeList objectProps = new ASTNode.NodeList(PROPERTIES_PROPERTY);
	
	
	ObjectName(AST ast) {
		super(ast);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#internalStructuralPropertiesForType(int)
	 */
	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}
	
	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == PROPERTIES_PROPERTY) {
			return objectProperties();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#getNodeType0()
	 */
	int getNodeType0() {
		return OBJECT_NAME;
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
		ObjectName result = new ObjectName(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.objectProperties().addAll(ASTNode.copySubtrees(target, objectProperties()));
		return result;
	}
	
	/**
	 * Returns the list of elements of this array pattern.
	 * 
	 * @return the live list of elements
	 * 
	 */
	public List objectProperties(){
		return this.objectProps;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#accept0(org.eclipse.wst.jsdt.core.dom.ASTVisitor)
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren){
			acceptChildren(visitor, objectProps);
		}
		visitor.endVisit(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#treeSize()
	 */
	int treeSize() {
		return memSize() 
			+ (objectProps == null? 0 : this.objectProps.listSize());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.ASTNode#memSize()
	 */
	int memSize() {
		return BASE_NAME_NODE_SIZE + 2*4;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.dom.Name#appendName(java.lang.StringBuffer)
	 */
	void appendName(StringBuffer buffer) {
		buffer.append(this.toString());
	}
}
