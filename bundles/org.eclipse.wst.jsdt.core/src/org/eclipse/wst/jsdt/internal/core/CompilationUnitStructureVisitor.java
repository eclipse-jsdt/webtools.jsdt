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

package org.eclipse.wst.jsdt.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableKind;
import org.eclipse.wst.jsdt.core.infer.IInferEngine;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;

/**
 * Visitor for computing the children of an IJavaScriptUnit.
 * 
 * @author Gorkem Ercan
 */
public class CompilationUnitStructureVisitor extends ASTVisitor{

	/**
	 * The handle to the compilation unit being parsed
	 */
	private final IJavaScriptElement unit;

	/**
	 * The info object for the compilation unit being parsed
	 */
	private final CompilationUnitElementInfo unitInfo;
	
	/**
	 * Stack of parent scope info objects. The info on the
	 * top of the stack is the parent of the next element found.
	 * For example, when we locate a method, the parent info object
	 * will be the type the method is contained in.
	 */
	private Stack<JavaElementInfo> infoStack;

	/**
	 * Stack of parent handles, corresponding to the info stack. We
	 * keep both, since info objects do not have back pointers to
	 * handles.
	 */
	private Stack<JavaElement> handleStack;
	
	/*
	 * Map from JavaElementInfo to of ArrayList of IJavaScriptElement representing the children
	 * of the given info.
	 */
	private HashMap<JavaElementInfo, ArrayList<IJavaScriptElement>> children;
	
	/**
	 * Hashtable of children elements of the compilation unit.
	 * Children are added to the table as they are found by
	 * the parser. Keys are handles, values are corresponding
	 * info objects.
	 */
	private final Map<JavaElement,Object> newElements;

	
	public CompilationUnitStructureVisitor (IJavaScriptElement unit, CompilationUnitElementInfo unitInfo, Map<JavaElement,Object> newElements){
		this.unit = unit;
		this.unitInfo = unitInfo;
		this.newElements =newElements;
	}
	
	public boolean visit(ForStatement node){
		return false;
	}
	
	public boolean visit(Assignment node){
		return false;
	}
	
	public boolean visit(ArrayInitializer node){
		return false;
	}
	
	public boolean visit(FunctionInvocation node ){
		JavaElement parentHandle= this.handleStack.peek();
		return parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT;
	}

	public boolean visit(JavaScriptUnit node){
		this.infoStack = new Stack<JavaElementInfo>();
		this.children = new HashMap<JavaElementInfo, ArrayList<IJavaScriptElement>>();
		this.handleStack= new Stack<JavaElement>();
		this.infoStack.push(this.unitInfo);
		this.handleStack.push((JavaElement) this.unit);
		return true;
	}
	
	public boolean visit(FunctionExpression node){
		node.getMethod().getBody().accept(this);
		return false;
	}
	
	public boolean visit(ObjectLiteral node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		SourceType handle = new SourceType(parentHandle, "", true);
		resolveDuplicates(handle);
		SourceTypeElementInfo info = 
					new SourceTypeElementInfo( parentHandle instanceof ClassFile , true);
		info.setHandle(handle);
		info.setSourceRangeStart(node.getStartPosition());
		info.setNameSourceStart(node.getStartPosition());
		info.setNameSourceEnd(node.getStartPosition() + node.getLength());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength());
//		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		info.setSuperclassName("".toCharArray());	
		
		addToChildren(parentInfo, handle);
		this.newElements.put(handle, info);
		this.infoStack.push(info);
		this.handleStack.push(handle);
		return true;
	}
	
	public void endVisit(ObjectLiteral node){
		SourceRefElementInfo info = (SourceRefElementInfo) this.infoStack.pop();
		setChildren(info);
		this.handleStack.pop();		
	}
	public boolean visit(ObjectLiteralField node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		String fieldName = JavaModelManager.getJavaModelManager().intern(new String(node.getFieldName().toString()));
		SourceField handle = new SourceField(parentHandle, fieldName);
		resolveDuplicates(handle);
		
		
		SourceFieldElementInfo info = new SourceFieldElementInfo();
		info.setNameSourceStart(node.getFieldName().getStartPosition());
		info.setNameSourceEnd(node.getFieldName().getStartPosition() + node.getFieldName().getLength());
		info.setSourceRangeStart(node.getStartPosition());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength());
		addToChildren(parentInfo, handle);
		this.newElements.put(handle, info);
		this.infoStack.push(info);
		this.handleStack.push(handle);
		if(node.getInitializer() != null ){
			node.getInitializer().accept(this);
		}
		return false;
	}
	
	public void endVisit(ObjectLiteralField node){
		SourceFieldElementInfo info = (SourceFieldElementInfo) this.infoStack.pop();
		setChildren(info);
		this.handleStack.pop();
	}
	
	
	public boolean visit(FunctionDeclaration node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		SourceMethod handle = null;
		
		final Expression methodName = node.getMethodName();
		char[] cs = methodName !=null ? node.getMethodName().toString().toCharArray(): CharOperation.concat(IInferEngine.ANONYMOUS_PREFIX, IInferEngine.ANONYMOUS_CLASS_ID);

		
		String selector = JavaModelManager.getJavaModelManager().intern(new String(cs));
		//TODO: Handle parameterSignatures
		String[] parameterTypeSigs = new String[0];
		handle = new SourceMethod(parentHandle, selector, parameterTypeSigs);
		resolveDuplicates(handle);
		
		SourceMethodElementInfo info;
		if (node.isConstructor())
			info = new SourceConstructorInfo();
		else
			info = new SourceMethodInfo();
		if(methodName != null ){
			info.setNameSourceStart(methodName.getStartPosition());
			info.setNameSourceEnd(methodName.getStartPosition() + methodName.getLength());
		}else{
			info.setNameSourceStart(node.getStartPosition());
			info.setNameSourceEnd(node.getStartPosition() + node.getLength());
		}
		info.setSourceRangeStart(node.getStartPosition());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength());
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		List<?> parameters = node.parameters();
		char[][] parameterNames = new char[parameters.size()][];
		for (int i = 0; i < parameters.size(); i++){
			SingleVariableDeclaration param = (SingleVariableDeclaration) parameters.get(i);
			parameterNames[i] = manager.intern(param.getPattern().toString().toCharArray());
		}
		info.setArgumentNames(parameterNames);
		
		List<?> modifiers = node.modifiers();
		int modifierFlag = ClassFileConstants.AccDefault;
		for (Iterator iterator = modifiers.iterator(); iterator.hasNext();) {
			Modifier modifier = (Modifier) iterator.next();
			if(modifier.isStatic()){
				modifierFlag |= ClassFileConstants.AccStatic;
			}
		}
		info.setFlags(modifierFlag);
		
					
		
//		char[] returnType = methodInfo.returnType == null ? null : manager.intern(methodInfo.returnType);
//		info.setReturnType(returnType);
		addToChildren(parentInfo, handle);
		this.newElements.put(handle, info);
		this.infoStack.push(info);
		this.handleStack.push(handle);
		return false;
	}
	
	public void endVisit(FunctionDeclaration node){
		SourceMethodElementInfo info = (SourceMethodElementInfo) this.infoStack.pop();
		setChildren(info);
		this.handleStack.pop();
	}
	
	public boolean visit(VariableDeclarationStatement node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		
//		Assert.isTrue( parentHandle.getElementType() == IJavaScriptElement.TYPE
//				|| parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT
//				|| parentHandle.getElementType() == IJavaScriptElement.CLASS_FILE
//				|| parentHandle.getElementType() == IJavaScriptElement.METHOD
//				) ;
		
		List<?> fragments = node.fragments();
		
		for (Iterator iterator = fragments.iterator(); iterator.hasNext();) {
			VariableDeclarationFragment  fragment = (VariableDeclarationFragment) iterator.next();
			fragment.accept(this);
		}
		return false;
		
	}
	
	public boolean visit(VariableDeclarationFragment node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		String fieldName = JavaModelManager.getJavaModelManager().intern(new String(node.getName().getIdentifier()));
		SourceField handle = new SourceField(parentHandle, fieldName);
		resolveDuplicates(handle);
		
		
		SourceFieldElementInfo info = new SourceFieldElementInfo();
		info.setNameSourceStart(node.getName().getStartPosition());
		info.setNameSourceEnd(node.getName().getStartPosition() + node.getName().getLength());
		info.setSourceRangeStart(node.getStartPosition());
		
		if(node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT &&
					node.getParent().getStructuralProperty(VariableDeclarationStatement.KIND_PROPERTY)
					== VariableKind.CONST){
			info.flags |= ClassFileConstants.AccStatic | ClassFileConstants.AccFinal;
		}
		
		if (node.getInitializer() != null) {
			int initializerType = node.getInitializer().getNodeType();
			String typeString = null;
			switch (initializerType) {
				case ASTNode.BOOLEAN_LITERAL :
					typeString = "boolean";
					break;
				case ASTNode.NUMBER_LITERAL :
					typeString = "Number";
					break;
				case ASTNode.STRING_LITERAL :
					typeString = "String";
					break;
				case ASTNode.OBJECT_LITERAL :
					typeString = "{}";
					break;
				default :
					break;
			}
			if (typeString != null) {
				char[] typeName = JavaModelManager.getJavaModelManager().intern(typeString.toCharArray());
				info.setTypeName(typeName);
			}
		}
		
		addToChildren(parentInfo, handle);
		this.newElements.put(handle, info);
		this.infoStack.push(info);
		this.handleStack.push(handle);
		if(node.getInitializer() != null ){
			node.getInitializer().accept(this);
		}
		return false;
	}
	
	public void endVisit(VariableDeclarationFragment node){
		SourceFieldElementInfo info = (SourceFieldElementInfo) this.infoStack.pop();
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength());
		setChildren(info);
		
		this.handleStack.pop();
	}
	
	public void endVisit(JavaScriptUnit node){
		// set import container children
//		if (this.importContainerInfo != null) {
//			setChildren(this.importContainerInfo);
//		}

		// set children
		setChildren(this.unitInfo);

		this.unitInfo.setSourceLength(node.getLength()+ 1);

		// determine if there were any parsing errors
		IProblem[] problems = node.getProblems();
		this.unitInfo.setIsStructureKnown(problems == null || problems.length == 0);
	}
	
	
	
	public boolean visit(TypeDeclaration node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		String nameString = node.getName().getIdentifier();
		SourceType handle = new SourceType(parentHandle, nameString, false);
		resolveDuplicates(handle);
		SourceTypeElementInfo info = 
					new SourceTypeElementInfo( parentHandle instanceof ClassFile , false);
		info.setHandle(handle);
		info.setSourceRangeStart(node.getStartPosition());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength());
		info.setNameSourceStart(node.getName().getStartPosition());
		info.setNameSourceEnd(node.getName().getStartPosition() + node.getName().getLength());
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		
		char[] superclass = null;
		if(node.getSuperclassExpression() != null ){
			superclass = node.getSuperclassExpression().toString().toCharArray();
		}
		info.setSuperclassName(superclass == null ? "".toCharArray() : manager.intern(superclass));
		addToChildren(parentInfo, handle);
		this.newElements.put(handle, info);
		this.infoStack.push(info);
		this.handleStack.push(handle);
		return true;
	}
	
	public void endVisit(TypeDeclaration node){
		SourceRefElementInfo info = (SourceRefElementInfo) this.infoStack.pop();
		setChildren(info);
		this.handleStack.pop();	
	}
	

	
	
	
	private void addToChildren(JavaElementInfo parentInfo, JavaElement handle) {
		ArrayList<IJavaScriptElement> childrenList =  this.children.get(parentInfo);
		if (childrenList == null)
			this.children.put(parentInfo, childrenList = new ArrayList<IJavaScriptElement>());
		childrenList.add(handle);
	}
	
	private void setChildren(JavaElementInfo info) {
		ArrayList<IJavaScriptElement> childrenList = this.children.get(info);
		if (childrenList != null) {
			int length = childrenList.size();
			IJavaScriptElement[] elements = new IJavaScriptElement[length];
			childrenList.toArray(elements);
			info.children = elements;
		}
	}
	
	/**
	 * Resolves duplicate handles by incrementing the occurrence count
	 * of the handle being created until there is no conflict.
	 */
	protected void resolveDuplicates(SourceRefElement handle) {
		while (this.newElements.containsKey(handle)) {
			handle.occurrenceCount++;
		}
	}
	
}
