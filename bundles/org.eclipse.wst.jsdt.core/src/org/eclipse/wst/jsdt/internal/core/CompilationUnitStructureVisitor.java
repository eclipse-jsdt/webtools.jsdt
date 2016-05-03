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

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.Signature;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.DefaultASTVisitor;
import org.eclipse.wst.jsdt.core.dom.ExportDeclaration;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.ModuleSpecifier;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
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
public class CompilationUnitStructureVisitor extends DefaultASTVisitor{

	/**
	 * The handle to the compilation unit being parsed
	 */
	private final IJavaScriptElement unit;

	/**
	 * The info object for the compilation unit being parsed
	 */
	private final CompilationUnitElementInfo unitInfo;

	/**
	 * The import container info - null until created
	 */
	protected JavaElementInfo importContainerInfo = null;
	
 	/**
	 * The export container info - null until created
	 */
	protected JavaElementInfo exportContainerInfo = null;		
	
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
	
	protected boolean visitNode(ASTNode node) {
		return false;
	}
	
	public boolean visit(ExpressionStatement node){
		return true;
	}
	
	public boolean visit(Block node){
		return true;
	}
	
	public boolean visit(TypeDeclarationExpression node){
		return true;
	}
	
	public boolean visit(TypeDeclarationStatement node){
		return true;
	}
	
	public boolean visit(FieldAccess node){
		return true;
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
	
	public boolean visit(FunctionDeclarationStatement node){
		node.getDeclaration().accept(this);
		return false;
	}
	
	public boolean visit(ObjectLiteral node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		SourceType handle = new SourceType(parentHandle, "", true); //$NON-NLS-1$
		resolveDuplicates(handle);
		SourceTypeElementInfo info = 
					new SourceTypeElementInfo( parentHandle instanceof ClassFile , true);
		info.setHandle(handle);
		info.setSourceRangeStart(node.getStartPosition());
		info.setNameSourceStart(node.getStartPosition());
		info.setNameSourceEnd(node.getStartPosition() + node.getLength()-1);
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);
//		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		info.setSuperclassName("".toCharArray());	 //$NON-NLS-1$
		
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
		info.setNameSourceEnd(node.getFieldName().getStartPosition() + node.getFieldName().getLength()-1);
		info.setSourceRangeStart(node.getStartPosition());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);
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
			info.setNameSourceEnd(methodName.getStartPosition() + methodName.getLength() -1);
		}else{
			info.setNameSourceStart(node.getStartPosition());
			info.setNameSourceEnd(node.getStartPosition() + node.getLength()-1);
		}
		info.setSourceRangeStart(node.getStartPosition());
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);
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
		List<?> fragments = node.fragments();
		
		for (Iterator iterator = fragments.iterator(); iterator.hasNext();) {
			VariableDeclarationFragment  fragment = (VariableDeclarationFragment) iterator.next();
			fragment.accept(this);
		}
		return false;
	}
	
	
	private void visitImportModule(ImportDeclaration parent, ModuleSpecifier module){
		JavaElement parentHandle= this.handleStack.peek();
		if (!(parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT)) {
			Assert.isTrue(false); // Should not happen
		}

		IJavaScriptUnit parentCU= (IJavaScriptUnit)parentHandle;
		//create the import container and its info
		ImportContainer importContainer= (ImportContainer)parentCU.getImportContainer();
		if (this.importContainerInfo == null) {
			this.importContainerInfo = new JavaElementInfo();
			JavaElementInfo parentInfo = this.infoStack.peek();
			addToChildren(parentInfo, importContainer);
			this.newElements.put(importContainer, this.importContainerInfo);
		}

		final SimpleName localName = module.getLocal();
		StringBuilder nameString = new StringBuilder(parent.getSource().getLiteralValue());
		nameString.append(" as ");
		nameString.append(localName.getIdentifier());
		String elementName = JavaModelManager.getJavaModelManager().intern(nameString.toString());
		org.eclipse.wst.jsdt.internal.core.ImportDeclaration handle = 
					new org.eclipse.wst.jsdt.internal.core.ImportDeclaration(importContainer, elementName, parent.isOnDemand());
		resolveDuplicates(handle);

		ImportDeclarationElementInfo info = new ImportDeclarationElementInfo();
		info.setNameSourceStart(localName.getStartPosition());
		info.setNameSourceEnd(localName.getStartPosition() + localName.getLength() -1);
		info.setSourceRangeStart(parent.getStartPosition());
		info.setSourceRangeEnd(parent.getStartPosition() + parent.getLength()-1);

		addToChildren(this.importContainerInfo, handle);
		this.newElements.put(handle, info);
	}
	
	public boolean visit(ImportDeclaration node){
		List specifiers = node.specifiers();
		if(specifiers.isEmpty()){
			JavaElement parentHandle= this.handleStack.peek();
			if (!(parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT)) {
				Assert.isTrue(false); // Should not happen
			}

			IJavaScriptUnit parentCU= (IJavaScriptUnit)parentHandle;
			//create the import container and its info
			ImportContainer importContainer= (ImportContainer)parentCU.getImportContainer();
			if (this.importContainerInfo == null) {
				this.importContainerInfo = new JavaElementInfo();
				JavaElementInfo parentInfo = this.infoStack.peek();
				addToChildren(parentInfo, importContainer);
				this.newElements.put(importContainer, this.importContainerInfo);
			}

			String elementName = JavaModelManager.getJavaModelManager().intern(node.getSource().getLiteralValue() );
			org.eclipse.wst.jsdt.internal.core.ImportDeclaration handle = 
						new org.eclipse.wst.jsdt.internal.core.ImportDeclaration(importContainer, elementName, false);
			resolveDuplicates(handle);

			ImportDeclarationElementInfo info = new ImportDeclarationElementInfo();
			info.setNameSourceStart(node.getStartPosition());
			info.setNameSourceEnd(node.getStartPosition() + node.getLength()-1);
			info.setSourceRangeStart(node.getStartPosition());
			info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);

			addToChildren(this.importContainerInfo, handle);
			this.newElements.put(handle, info);

		}else{
			for (Iterator iterator = specifiers.iterator(); iterator.hasNext();) {
				ModuleSpecifier module = (ModuleSpecifier) iterator.next();
				visitImportModule(node, module);
			}
		}
		return false;
	}

	private void visitExportModule(ExportDeclaration parent, ModuleSpecifier module) {
		JavaElement parentHandle = this.handleStack.peek();
		if (!(parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT)) {
			Assert.isTrue(false); // Should not happen
		}

		IJavaScriptUnit parentCU = (IJavaScriptUnit) parentHandle;
		// create the export container and its info
		ExportContainer exportContainer = (ExportContainer) parentCU.getExportContainer();
		if (this.exportContainerInfo == null) {
			this.exportContainerInfo = new JavaElementInfo();
			JavaElementInfo parentInfo = this.infoStack.peek();
			addToChildren(parentInfo, exportContainer);
			this.newElements.put(exportContainer, this.exportContainerInfo);
		}

		final SimpleName localName = module.getLocal();
		StringBuilder nameString = parent.getSource() != null ? new StringBuilder(parent.getSource().getLiteralValue()) : new StringBuilder("export");
		nameString.append(" as ");
		nameString.append(localName.getIdentifier());
		String elementName = JavaModelManager.getJavaModelManager().intern(nameString.toString());
		org.eclipse.wst.jsdt.internal.core.ExportDeclaration handle = new org.eclipse.wst.jsdt.internal.core.ExportDeclaration(exportContainer, elementName);
		resolveDuplicates(handle);

		ExportDeclarationElementInfo info = new ExportDeclarationElementInfo();
		info.setNameSourceStart(localName.getStartPosition());
		info.setNameSourceEnd(localName.getStartPosition() + localName.getLength() - 1);
		info.setSourceRangeStart(parent.getStartPosition());
		info.setSourceRangeEnd(parent.getStartPosition() + parent.getLength() - 1);

		addToChildren(this.exportContainerInfo, handle);
		this.newElements.put(handle, info);
	}

	public boolean visit(ExportDeclaration node) {
		List specifiers = node.specifiers();
		if (specifiers.isEmpty()) {
			JavaElement parentHandle = this.handleStack.peek();
			if (!(parentHandle.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT)) {
				Assert.isTrue(false); // Should not happen
			}

			IJavaScriptUnit parentCU = (IJavaScriptUnit) parentHandle;
			// create the export container and its info
			ExportContainer exportContainer = (ExportContainer) parentCU.getExportContainer();
			if (this.exportContainerInfo == null) {
				this.exportContainerInfo = new JavaElementInfo();
				JavaElementInfo parentInfo = this.infoStack.peek();
				addToChildren(parentInfo, exportContainer);
				this.newElements.put(exportContainer, this.exportContainerInfo);
			}

			String elementName = node.getSource() != null ? JavaModelManager.getJavaModelManager().intern(node.getSource().getLiteralValue()) : "export";

			org.eclipse.wst.jsdt.internal.core.ExportDeclaration handle = new org.eclipse.wst.jsdt.internal.core.ExportDeclaration(exportContainer, elementName);
			resolveDuplicates(handle);

			ExportDeclarationElementInfo info = new ExportDeclarationElementInfo();
			info.setNameSourceStart(node.getStartPosition());
			info.setNameSourceEnd(node.getStartPosition() + node.getLength() - 1);
			info.setSourceRangeStart(node.getStartPosition());
			info.setSourceRangeEnd(node.getStartPosition() + node.getLength() - 1);

			addToChildren(this.exportContainerInfo, handle);
			this.newElements.put(handle, info);

		}
		else {
			for (Iterator iterator = specifiers.iterator(); iterator.hasNext();) {
				ModuleSpecifier module = (ModuleSpecifier) iterator.next();
				visitExportModule(node, module);
			}
		}
		return false;
	}
	
	public boolean visit(VariableDeclarationFragment node){
		JavaElementInfo parentInfo = this.infoStack.peek();
		JavaElement parentHandle= this.handleStack.peek();
		String fieldName = JavaModelManager.getJavaModelManager().intern(
					new String(node.getName().getIdentifier()));
		SourceField handle = new SourceField(parentHandle, fieldName);
		resolveDuplicates(handle);
		
		
		SourceFieldElementInfo info = new SourceFieldElementInfo();
		info.setNameSourceStart(node.getName().getStartPosition());
		info.setNameSourceEnd(node.getName().getStartPosition() + node.getName().getLength()-1);
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
					typeString = Signature.createTypeSignature("boolean", false);
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
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);
		setChildren(info);
		
		this.handleStack.pop();
	}
	
	public void endVisit(JavaScriptUnit node){
		// set import container children
		if (this.importContainerInfo != null) {
			setChildren(this.importContainerInfo);
		}

		// set export container children
		if (this.exportContainerInfo != null) {
			setChildren(this.exportContainerInfo);
		}		

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
		info.setSourceRangeEnd(node.getStartPosition() + node.getLength()-1);
		info.setNameSourceStart(node.getName().getStartPosition());
		info.setNameSourceEnd(node.getName().getStartPosition() + node.getName().getLength()-1);
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		
		char[] superclass = null;
		if(node.getSuperclassExpression() != null ){
			superclass = node.getSuperclassExpression().toString().toCharArray();
		}
		info.setSuperclassName(superclass == null ? "".toCharArray() : manager.intern(superclass)); //$NON-NLS-1$
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
