/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.rewrite.describing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.FieldDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.ListRewrite;
import org.eclipse.wst.jsdt.core.dom.rewrite.TargetSourceRangeComputer;
import org.eclipse.wst.jsdt.core.dom.rewrite.TargetSourceRangeComputer.SourceRange;

public class ASTRewritingInsertBoundTest extends ASTRewritingTest {

	private static final Class THIS= ASTRewritingInsertBoundTest.class;
	
	public ASTRewritingInsertBoundTest(String name) {
		super(name);
	}
	
	public static Test allTests() {
		return new Suite(THIS);
	}
	
	public static Test setUpTest(Test someTest) {
		TestSuite suite= new Suite("one test");
		suite.addTest(someTest);
		return suite;
	}
	
	public static Test suite() {
		return allTests();
	}
	
	/** @deprecated using deprecated code */
	private FunctionDeclaration newMethodDeclaration(AST ast, String name) {
		FunctionDeclaration decl= ast.newFunctionDeclaration();
		decl.setName(ast.newSimpleName(name));
		decl.setBody(null);
//		decl.setReturnType(ast.newPrimitiveType(PrimitiveType.VOID));
		return decl;
	}
	
	private FieldDeclaration newFieldDeclaration(AST ast, String name) {
		VariableDeclarationFragment frag= ast.newVariableDeclarationFragment();
		frag.setName(ast.newSimpleName(name));
		FieldDeclaration decl= ast.newFieldDeclaration(frag);
//		decl.setType(ast.newPrimitiveType(PrimitiveType.INT));
		return decl;
	}	
	
	
	/** @deprecated using deprecated code */
	public void testInsert1() throws Exception {
		// insert first and last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n}\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	/** @deprecated using deprecated code */
	public void testInsert3() throws Exception {
		// insert 2 x before 
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		
		ASTNode middleDecl= (ASTNode) decls.get(1);
		ASTNode lastDecl= (ASTNode) decls.get(2);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, middleDecl, null);
		listRewrite.insertBefore(decl2, lastDecl, null);
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	
	public void testInsert2() throws Exception {
		// insert 2 x first and 2 x last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		FunctionDeclaration decl3= newMethodDeclaration(ast, "new3");
		FunctionDeclaration decl4= newMethodDeclaration(ast, "new4");
				
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertAfter(decl2, decl1, null);
		listRewrite.insertLast(decl3, null);
		listRewrite.insertAfter(decl4, decl3, null);
		
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n}\n");
		buf.append("\n");
		buf.append("    function new2() {\n}\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("    function new3() {\n    }\n");
		buf.append("\n");
		buf.append("    function new4() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}	

	public void testInsert1Before() throws Exception {
		// insert first and last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
		
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("//c4\n");
		assertEqualString(preview, buf.toString());

	}
	
	public void testInsert2Before() throws Exception {
		// insert 2x first and 2 x last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");
		FieldDeclaration decl3= newFieldDeclaration(ast, "new3");
		FieldDeclaration decl4= newFieldDeclaration(ast, "new4");
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertAfter(decl2, decl1, null);
		listRewrite.insertLast(decl3, null);
		listRewrite.insertAfter(decl4, decl3, null);
		
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("    var new3;\n");
		buf.append("\n");
		buf.append("    var new4;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}	
	
	public void testInsert3Before() throws Exception {
		// insert 2 x after 
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");

		ASTNode firstDecl= (ASTNode) decls.get(0);
		ASTNode middleDecl= (ASTNode) decls.get(1);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertAfter(decl1, firstDecl, null);
		listRewrite.insertAfter(decl2, middleDecl, null);
				
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("    var new2;\n");	
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemove1() throws Exception {
		// remove first and last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(2), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemove2() throws Exception {
		// remove second
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(1), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemove3() throws Exception {
		// remove 2nd and 3rd
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemove4() throws Exception {
		// remove all
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	
	public void testRemoveInsert1() throws Exception {
		// remove first add before first, remove last add after last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");

		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert2() throws Exception {
		// remove first add 2x first, remove last add 2x  last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		FunctionDeclaration decl3= newMethodDeclaration(ast, "new3");
		FunctionDeclaration decl4= newMethodDeclaration(ast, "new4");
		
		ASTNode firstDecl= (ASTNode) decls.get(0);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, firstDecl, null);
		listRewrite.insertAfter(decl2, firstDecl, null);
		listRewrite.insertLast(decl3, null);
		listRewrite.insertAfter(decl4, decl3, null);
		
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function new3() {\n    }\n");
		buf.append("\n");
		buf.append("    function new4() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert3() throws Exception {
		// remove middle, add before, add after
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(1), null);
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		
		ASTNode middleDecl= (ASTNode) decls.get(1);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, middleDecl, null);
		listRewrite.insertAfter(decl2, middleDecl, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	
	public void testRemoveInsert1Before() throws Exception {
		// remove first add before first, remove last add after last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		buf.append("}\n");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");
				
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
		
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert2Before() throws Exception {
		// remove first add 2x first, remove last add 2x  last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		buf.append("}\n");		
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");
		FieldDeclaration decl3= newFieldDeclaration(ast, "new3");
		FieldDeclaration decl4= newFieldDeclaration(ast, "new4");

		ASTNode firstDecl= (ASTNode) decls.get(0);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, firstDecl, null);
		listRewrite.insertAfter(decl2, firstDecl, null);
		listRewrite.insertLast(decl3, null);
		listRewrite.insertAfter(decl4, decl3, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var new3;\n");
		buf.append("\n");
		buf.append("    var new4;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert3Before() throws Exception {
		// remove middle, add before, add after
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");

		ASTNode middleDecl= (ASTNode) decls.get(1);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.remove(middleDecl, null);
		
		listRewrite.insertBefore(decl1, middleDecl, null);
		listRewrite.insertAfter(decl2, middleDecl, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert4() throws Exception {
		// remove first and add after first, remove last and add before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");

		ASTNode firstDecl= (ASTNode) decls.get(0);
		ASTNode lastDecl= (ASTNode) decls.get(2);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		
		listRewrite.remove(firstDecl, null);
		listRewrite.remove(lastDecl, null);
		
		listRewrite.insertAfter(decl1, firstDecl, null);
		listRewrite.insertBefore(decl2, lastDecl, null);	   
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}

	public void testRemoveInsert4Before() throws Exception {
		// remove first and add after first, remove last and add before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");

		ASTNode firstDecl= (ASTNode) decls.get(0);
		ASTNode lastDecl= (ASTNode) decls.get(2);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.remove(firstDecl, null);
		listRewrite.remove(lastDecl, null);
		
		listRewrite.insertAfter(decl1, firstDecl, null);
		listRewrite.insertBefore(decl2, lastDecl, null);	   
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    int new1;\n");
		buf.append("\n");
		buf.append("    public int x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    int new2;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}	
	
	public void testRemoveInsert5() throws Exception {
		// remove first and add after and before first, remove last and add after and before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		FunctionDeclaration decl3= newMethodDeclaration(ast, "new3");
		FunctionDeclaration decl4= newMethodDeclaration(ast, "new4");
		
		ASTNode firstDecl= (ASTNode) decls.get(0);
		ASTNode lastDecl= (ASTNode) decls.get(2);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		
		rewrite.remove(firstDecl, null);
		rewrite.remove(lastDecl, null);
		
		listRewrite.insertBefore(decl1, firstDecl, null);
		listRewrite.insertAfter(decl2, firstDecl, null);
		listRewrite.insertBefore(decl3, lastDecl, null);
		listRewrite.insertAfter(decl4, lastDecl, null);
		
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function new3() {\n    }\n");
		buf.append("\n");
		buf.append("    function new4() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		buf.append("}\n");	
		
		assertEqualString(preview, buf.toString());

	}

	public void testRemoveInsert5Before() throws Exception {
		// remove first and add after first, remove last and add before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		buf.append("}\n");	
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");
		FieldDeclaration decl3= newFieldDeclaration(ast, "new3");
		FieldDeclaration decl4= newFieldDeclaration(ast, "new4");

		ASTNode firstDecl= (ASTNode) decls.get(0);
		ASTNode lastDecl= (ASTNode) decls.get(2);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		
		rewrite.remove(firstDecl, null);
		rewrite.remove(lastDecl, null);
		
		listRewrite.insertBefore(decl1, firstDecl, null);
		listRewrite.insertAfter(decl2, firstDecl, null);
		listRewrite.insertBefore(decl3, lastDecl, null);
		listRewrite.insertAfter(decl4, lastDecl, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var new3;\n");
		buf.append("\n");
		buf.append("    var new4;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}	
	

	public void testRemoveInsert6() throws Exception {
		// remove all, add before first and after last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert6Before() throws Exception {
		// remove all, add before first and after last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    public int x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    public int x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    public int x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");

		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(decl1, null);
		listRewrite.insertLast(decl2, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    int new1;\n");
		buf.append("\n");
		buf.append("    int new2;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	

	public void testRemoveInsert7() throws Exception {
		// remove all, add after first and before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function foo1();\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    function foo2();\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    function foo3();\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FunctionDeclaration decl1= newMethodDeclaration(ast, "new1");
		FunctionDeclaration decl2= newMethodDeclaration(ast, "new2");

		ASTNode middleDecl= (ASTNode) decls.get(1);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, middleDecl, null);
		listRewrite.insertAfter(decl2, middleDecl, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    function new1() {\n    }\n");
		buf.append("\n");
		buf.append("    function new2() {\n    }\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	
	public void testRemoveInsert7Before() throws Exception {
		// remove all, add after first and before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("//c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("//c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("//c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		AST ast= astRoot.getAST();
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
		List decls= astRoot.statements();

		rewrite.remove((ASTNode) decls.get(0), null);
		rewrite.remove((ASTNode) decls.get(1), null);
		rewrite.remove((ASTNode) decls.get(2), null);
		
		FieldDeclaration decl1= newFieldDeclaration(ast, "new1");
		FieldDeclaration decl2= newFieldDeclaration(ast, "new2");

		ASTNode middleDecl= (ASTNode) decls.get(1);
		
		ListRewrite listRewrite= rewrite.getListRewrite(astRoot, JavaScriptUnit.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(decl1, middleDecl, null);
		listRewrite.insertAfter(decl2, middleDecl, null);
			
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("//c1\n");
		buf.append("\n");
		buf.append("    var new1;\n");
		buf.append("\n");
		buf.append("    var new2;\n");
		buf.append("\n");
		buf.append("//c4\n");
		
		assertEqualString(preview, buf.toString());

	}
	

	public void testTargetSourceRangeComputer () throws Exception {
		// remove all, add after first and before last
		
		IPackageFragment pack1= this.sourceFolder.createPackageFragment("test1", false, null);
		StringBuffer buf= new StringBuffer();
		buf.append("    //c1\n");
		buf.append("\n");
		buf.append("    var x1;\n");
		buf.append("\n");
		buf.append("    //c2\n");
		buf.append("\n");
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("    //c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("    //c4\n");
		IJavaScriptUnit cu= pack1.createCompilationUnit("C.js", buf.toString(), false, null);
	
		JavaScriptUnit astRoot= createAST(cu);
		
		ASTRewrite rewrite= ASTRewrite.create(astRoot.getAST());
		
//		TypeDeclaration type= findTypeDeclaration(astRoot, "C");
//		FieldDeclaration[] decls= type.getFields();
		List decls = astRoot.statements();
		final Map extendedRanges= new HashMap();
	
		VariableDeclarationStatement f1=(VariableDeclarationStatement)decls.get(0);
//		FieldDeclaration f1= decls[0];
		int off1= buf.indexOf("//c1");
		int end1= f1.getStartPosition() + f1.getLength();
		extendedRanges.put(f1, new SourceRange(off1, end1 - off1));
			
		rewrite.setTargetSourceRangeComputer(new TargetSourceRangeComputer() {
			public SourceRange computeSourceRange(ASTNode node) {
				SourceRange range= (SourceRange) extendedRanges.get(node);
				if (range != null)
					return range;
				return super.computeSourceRange(node);
			}
		});

		rewrite.remove(f1, null);
					
		String preview= evaluateRewrite(cu, rewrite);
		
		buf= new StringBuffer();
		buf.append("    var x2;\n");
		buf.append("\n");
		buf.append("    //c3\n");
		buf.append("\n");
		buf.append("    var x3;\n");
		buf.append("\n");
		buf.append("    //c4\n");
		assertEqualString(preview, buf.toString());

	}
	
}



