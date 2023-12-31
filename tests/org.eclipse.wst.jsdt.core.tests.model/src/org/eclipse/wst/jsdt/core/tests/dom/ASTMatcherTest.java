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

package org.eclipse.wst.jsdt.core.tests.dom;


import static org.junit.Assert.*;


import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTMatcher;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayCreation;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BlockComment;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.ContinueStatement;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.EmptyStatement;
import org.eclipse.wst.jsdt.core.dom.EnhancedForStatement;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FieldDeclaration;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.FunctionRef;
import org.eclipse.wst.jsdt.core.dom.FunctionRefParameter;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.LineComment;
import org.eclipse.wst.jsdt.core.dom.MemberRef;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.PrimitiveType;
import org.eclipse.wst.jsdt.core.dom.QualifiedName;
import org.eclipse.wst.jsdt.core.dom.QualifiedType;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SimpleType;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.Statement;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.SuperConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.SuperFieldAccess;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TagElement;
import org.eclipse.wst.jsdt.core.dom.TextElement;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.ThrowStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.Type;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.TypeLiteral;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for <code>ASTMatcher</code> and <code>ASTNode.subtreeMatch</code>.
 */
public class ASTMatcherTest { 

	
	AST ast;
	SimpleName N1;
	SimpleName N2;
	SimpleName N3;
	SimpleName N4;
	Expression E1;
	Expression E2;
	Type T1;
	String T1S;
	Type T2;
	String T2S;
	String PT1S;
	Statement S1;
	Statement S2;
	Block B1;
	SingleVariableDeclaration V1;
	SingleVariableDeclaration V2;
	VariableDeclarationFragment W1;
	VariableDeclarationFragment W2;
	FieldDeclaration FD1;
	FieldDeclaration FD2;
	PackageDeclaration PD1;
	ImportDeclaration ID1;
	ImportDeclaration ID2;
	TypeDeclaration TD1;
	TypeDeclaration TD2;
	AnonymousClassDeclaration ACD1;
	JSdoc JD1;
	JSdoc JD2;
	String JD2S;
	String TP1S;
	String TP2S;
	TagElement TAG1;
	TagElement TAG2;
	TextElement TEXT1;
	MemberRef MBREF1;
	FunctionRef MTHREF1;
	FunctionRefParameter MPARM1;
	LineComment LC1;
	BlockComment BC1;
	Modifier MOD1;
	Modifier MOD2;
	
	final StringBuffer b = new StringBuffer();
	
	int API_LEVEL;

	public ASTMatcherTest() {
		this.API_LEVEL = AST.JLS3;
	}
		
	/**
	 * @deprecated (not really - just suppressing the warnings
	 * that come from testing Javadoc.getComment())
	 *
	 */
	@Before
	public void setUp() throws Exception {
		
		ast = AST.newAST(this.API_LEVEL);
		N1 = ast.newSimpleName("N"); //$NON-NLS-1$
		N2 = ast.newSimpleName("M"); //$NON-NLS-1$
		N3 = ast.newSimpleName("O"); //$NON-NLS-1$
		N4 = ast.newSimpleName("P"); //$NON-NLS-1$
		E1 = ast.newSimpleName("X"); //$NON-NLS-1$
		E2 = ast.newSimpleName("Y"); //$NON-NLS-1$
		T1 = ast.newSimpleType(ast.newSimpleName("Z")); //$NON-NLS-1$
		T1S = "(tS(nSZZnS)tS)"; //$NON-NLS-1$
		T2 = ast.newSimpleType(ast.newSimpleName("Y")); //$NON-NLS-1$
		T2S = "(tS(nSYYnS)tS)"; //$NON-NLS-1$
		S1 = ast.newContinueStatement();
		S2 = ast.newBreakStatement();
		B1 = ast.newBlock();
		V1 = ast.newSingleVariableDeclaration();
		V1.setType(ast.newPrimitiveType(PrimitiveType.INT));
		V1.setName(ast.newSimpleName("a")); //$NON-NLS-1$
		V2 = ast.newSingleVariableDeclaration();
		V2.setType(ast.newPrimitiveType(PrimitiveType.BYTE));
		V2.setName(ast.newSimpleName("b")); //$NON-NLS-1$
		W1 = ast.newVariableDeclarationFragment();
		W1.setName(ast.newSimpleName("a")); //$NON-NLS-1$
		W2 = ast.newVariableDeclarationFragment();
		W2.setName(ast.newSimpleName("b")); //$NON-NLS-1$
		{
			VariableDeclarationFragment temp = ast.newVariableDeclarationFragment();
			temp.setName(ast.newSimpleName("f")); //$NON-NLS-1$
			FD1 = ast.newFieldDeclaration(temp);
			FD1.setType(ast.newPrimitiveType(PrimitiveType.INT));
		}
		{
			VariableDeclarationFragment temp = ast.newVariableDeclarationFragment();
			temp.setName(ast.newSimpleName("g")); //$NON-NLS-1$
			FD2 = ast.newFieldDeclaration(temp);
			FD2.setType(ast.newPrimitiveType(PrimitiveType.CHAR));
		}
		PD1 = ast.newPackageDeclaration();
		PD1.setName(ast.newSimpleName("p")); //$NON-NLS-1$
		ID1 = ast.newImportDeclaration();
		ID1.setName(ast.newSimpleName("i")); //$NON-NLS-1$
		ID2 = ast.newImportDeclaration();
		ID2.setName(ast.newSimpleName("j")); //$NON-NLS-1$
		TD1 = ast.newTypeDeclaration();
		TD1.setName(ast.newSimpleName("c")); //$NON-NLS-1$
		TD2 = ast.newTypeDeclaration();
		TD2.setName(ast.newSimpleName("d")); //$NON-NLS-1$
		
		ACD1 = ast.newAnonymousClassDeclaration();
		
		JD1 = ast.newJSdoc();
		JD2 = ast.newJSdoc();
		if (ast.apiLevel() == AST.JLS2) {
			JD1.setComment("/**X*/"); //$NON-NLS-1$
			JD2.setComment("/**Y*/"); //$NON-NLS-1$
		}

		BC1 = ast.newBlockComment();
		
		TAG1 = ast.newTagElement();
		TAG1.setTagName("@foo"); //$NON-NLS-1$

		TAG2 = ast.newTagElement();
		TAG2.setTagName("@bar"); //$NON-NLS-1$

		TEXT1 = ast.newTextElement();
		TEXT1.setText("foo"); //$NON-NLS-1$

		MBREF1 = ast.newMemberRef();
		MBREF1.setName(ast.newSimpleName("p")); //$NON-NLS-1$

		MTHREF1 = ast.newFunctionRef();
		MTHREF1.setName(ast.newSimpleName("p")); //$NON-NLS-1$

		MPARM1 = ast.newFunctionRefParameter();
		MPARM1.setType(ast.newPrimitiveType(PrimitiveType.CHAR));

		if (ast.apiLevel() >= AST.JLS3) {
			PT1S = "[(tM[(tS[(nSZZnS)]tS)]tM)]"; //$NON-NLS-1$

			TP1S = "[(tTP[(nSxxnS)]tTP)]"; //$NON-NLS-1$

			TP2S = "[(tTP[(nSyynS)]tTP)]"; //$NON-NLS-1$
			LC1 = ast.newLineComment();

			
			MOD1 = ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD);
			MOD2 = ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD);
			
		}

	}
	
	@After
	public void tearDown() throws Exception {
		ast = null;
	}
	
	/**
	 * An ASTMatcher that simply records the arguments it is passed,
	 * immediately returns a pre-ordained answer, and counts how many
	 * times it is called.
	 */
	class TestMatcher extends ASTMatcher {

		public Object receiverNode;
		public Object otherNode;
		public boolean result;
		public boolean superMatch;
		public boolean superMatchResult;
		public int matchCalls = 0;

		TestMatcher() {
			this(false);
		}

		TestMatcher(boolean visitDocTags) {
			super(visitDocTags);
		}

		boolean standardBody(ASTNode receiver, Object other, boolean matchResult) {
			matchCalls++;
			this.receiverNode = receiver;
			this.otherNode = other;
			this.superMatchResult = matchResult;
			if (superMatch) {
				return this.superMatchResult;
			}
			return this.result;
		}

		public boolean match(AnonymousClassDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ArrayAccess node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ArrayCreation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ArrayInitializer node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ArrayType node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(Assignment node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(Block node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(BlockComment node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(BooleanLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(BreakStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(CatchClause node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(CharacterLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ClassInstanceCreation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(JavaScriptUnit node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ConditionalExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ConstructorInvocation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ContinueStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(DoStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(EmptyStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(EnhancedForStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ExpressionStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FieldAccess node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FieldDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ForStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(IfStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ImportDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(InfixExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(Initializer node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(JSdoc node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(LabeledStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(LineComment node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(MemberRef node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FunctionDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FunctionInvocation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FunctionRef node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(FunctionRefParameter node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(Modifier node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(NullLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(NumberLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(PackageDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ParenthesizedExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(PostfixExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(PrefixExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(PrimitiveType node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(QualifiedName node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(QualifiedType node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ReturnStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SimpleName node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SimpleType node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SingleVariableDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(StringLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SuperConstructorInvocation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SuperFieldAccess node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SuperMethodInvocation node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SwitchCase node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(SwitchStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}

		public boolean match(TagElement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(TextElement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ThisExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(ThrowStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(TryStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(TypeDeclaration node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(TypeDeclarationStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(TypeLiteral node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(VariableDeclarationExpression node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(VariableDeclarationFragment node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(VariableDeclarationStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
		public boolean match(WhileStatement node, Object other) {
			return standardBody(node, other, superMatch ? super.match(node, other) : false);
		}
	}
	
	/**
	 * AST node visitor that counts the nodes visited.
	 */
	static class NodeCounter extends ASTVisitor {
		public NodeCounter(boolean visitDocTags) {
			super(visitDocTags);
		}
		
		public int count = 0;

		/* (no javadoc for this method)
		 * Method declared on ASTVisitor.
		 */
		public void preVisit(ASTNode node) {
			count++;
		}

	}
	
	/**
	 * Returns the number of AST nodes in the given subtree.
	 * 
	 * @param node the root of the subtree
	 * @param visitDocTags true if doc tags should be visited
	 * @return the number of nodes (always positive)
	 */
	static int nodeCount(ASTNode node, boolean visitDocTags) {
		NodeCounter c = new NodeCounter(visitDocTags);
		node.accept(c);
		return c.count;
	}		
	
	/**
	 * Checks that the ASTNode.subtreeMatch mechanism is working
	 * for a node of a given type. 
	 */
	void basicMatch(ASTNode node) {
		TestMatcher[] m = {
			new TestMatcher(),
			new TestMatcher(true),
			new TestMatcher(false)};
		for (int i = 0; i < m.length; i++) {
			// check that matcher was called with right arguments
			// and that matches succeed
			TestMatcher m1 = m[i];
			Object o1 = new Object();
			m1.result = true;
			boolean result = node.subtreeMatch(m1, o1);
			assertTrue(m1.matchCalls == 1);
			assertTrue(m1.receiverNode == node);
			assertTrue(m1.otherNode == o1);
			assertTrue(result == true);
		}
		
		m = new TestMatcher[] {
							new TestMatcher(),
							new TestMatcher(true),
							new TestMatcher(false)};
		for (int i = 0; i < m.length; i++) {
			// check that matcher was called with right arguments
			// and that non-matches fail
			TestMatcher m1 = m[i];
			Object o1 = new Object();
			m1.result = false;
			boolean result = node.subtreeMatch(m1, o1);
			assertTrue(m1.matchCalls == 1);
			assertTrue(m1.receiverNode == node);
			assertTrue(m1.otherNode == o1);
			assertTrue(result == false);
		}
		
		// check that ASTMatcher() default implementations delegate
		{
			int count = nodeCount(node, false); // ignore doc tags
			TestMatcher m1 = new TestMatcher();
			m1.superMatch = true;
			boolean result = node.subtreeMatch(m1, node);
			assertTrue(m1.matchCalls == count);
			assertTrue(result == true);
		}
		
		// check that ASTMatcher(false) default implementations delegate
		{
			int count = nodeCount(node, false); // ignore doc tags
			TestMatcher m1 = new TestMatcher(false);
			m1.superMatch = true;
			boolean result = node.subtreeMatch(m1, node);
			assertTrue(m1.matchCalls == count);
			assertTrue(result == true);
		}
		
		// check that ASTMatcher(true) default implementations delegate
		{
			int count = nodeCount(node, true); // include doc tags
			TestMatcher m1 = new TestMatcher(true);
			m1.superMatch = true;
			boolean result = node.subtreeMatch(m1, node);
			assertTrue(m1.matchCalls == count);
			assertTrue(result == true);
		}
		
	}

	// NAMES
	@Test
	public void testSimpleName() {
		Name x1 = ast.newName(new String[]{"Z"}); //$NON-NLS-1$
		basicMatch(x1);
	}

	@Test
	public void testQualifiedName() {
		Name x1 = ast.newName(new String[]{"X", "Y"}); //$NON-NLS-1$ //$NON-NLS-2$
		basicMatch(x1);
	}

	
	// TYPES
	@Test
	public void testPrimitiveType() {
		Type x1 = ast.newPrimitiveType(PrimitiveType.CHAR);
		basicMatch(x1);
	}

	@Test
	public void testSimpleType() {
		Type x1 = ast.newSimpleType(N1);
		basicMatch(x1);
	}

	@Test
	public void testArrayType() {
		Type x0 = ast.newPrimitiveType(PrimitiveType.CHAR);
		Type x1 = ast.newArrayType(x0);
		basicMatch(x1);
	}

	/** @deprecated using deprecated code */
	@Test
	public void testQualifiedType() {
		if (ast.apiLevel() == AST.JLS2) {
			return;
		}
		Type x1 = ast.newQualifiedType(T1, N1);
		basicMatch(x1);
	}

	// EXPRESSIONS and STATEMENTS

	@Test
	public void testAnonymousClassDeclaration() {
		AnonymousClassDeclaration x1 = ast.newAnonymousClassDeclaration();
		x1.bodyDeclarations().add(FD1);
		x1.bodyDeclarations().add(FD2);
		basicMatch(x1);
	}
	@Test
	public void testArrayAccess() {
		ArrayAccess x1 = ast.newArrayAccess();
		x1.setArray(E1);
		x1.setIndex(E2);
		basicMatch(x1);
	}
	@Test
	public void testArrayCreation() {
		ArrayCreation x1 = ast.newArrayCreation();
		x1.setType(ast.newArrayType(T1));
		x1.dimensions().add(E1);
		x1.dimensions().add(E2);
		x1.setInitializer(ast.newArrayInitializer());
		basicMatch(x1);
	}
	@Test
	public void testArrayInitializer() {
		ArrayInitializer x1 = ast.newArrayInitializer();
		x1.expressions().add(E1);
		x1.expressions().add(E2);
		basicMatch(x1);
	}
	@Test
	public void testAssignment() {
		Assignment x1 = ast.newAssignment();
		x1.setLeftHandSide(E1);
		x1.setRightHandSide(E2);
		basicMatch(x1);
	}
	@Test
	public void testBlock() {
		Block x1 = ast.newBlock();
		x1.statements().add(S1);
		x1.statements().add(S2);
		basicMatch(x1);
	}
	
	@Test
	public void testBlockComment() {
		BlockComment x1 = ast.newBlockComment();
		basicMatch(x1);
	}

	@Test
	public void testBooleanLiteral() {
		BooleanLiteral x1 = ast.newBooleanLiteral(true);
		basicMatch(x1);
	}
	@Test
	public void testBreakStatement() {
		BreakStatement x1 = ast.newBreakStatement();
		x1.setLabel(N1);
		basicMatch(x1);
	}
	@Test
	public void testCatchClause() {
		CatchClause x1 = ast.newCatchClause();
		x1.setException(V1);
		x1.setBody(B1);
		basicMatch(x1);
	}
	@Test
	public void testCharacterLiteral() {
		CharacterLiteral x1 = ast.newCharacterLiteral();
		x1.setCharValue('q');
		basicMatch(x1);
	}
	/** @deprecated using deprecated code */
	@Test
	public void testClassInstanceCreation() {
		ClassInstanceCreation x1 = ast.newClassInstanceCreation();
		x1.setExpression(E1);
		if (ast.apiLevel() == AST.JLS2) {
			x1.setName(N1);
		} else {
			x1.setType(T1);
		}
		x1.setAnonymousClassDeclaration(ACD1);
		basicMatch(x1);
	}
	@Test
	public void testConditionalExpression() {
		ConditionalExpression x1 = ast.newConditionalExpression();
		x1.setExpression(E1);
		x1.setThenExpression(E2);
		x1.setElseExpression(N1);
		basicMatch(x1);
	}
	@Test
	public void testConstructorInvocation() {
		ConstructorInvocation x1 = ast.newConstructorInvocation();
		
		x1.arguments().add(E1);
		x1.arguments().add(E2);
		basicMatch(x1);
	}
	@Test
	public void testContinueStatement() {
		ContinueStatement x1 = ast.newContinueStatement();
		x1.setLabel(N1);
		basicMatch(x1);
	}
	@Test
	public void testDoStatement() {
		DoStatement x1 = ast.newDoStatement();
		x1.setExpression(E1);
		x1.setBody(S1);
		basicMatch(x1);
	}
	@Test
	public void testEmptyStatement() {
		EmptyStatement x1 = ast.newEmptyStatement();
		basicMatch(x1);
	}
	/** @deprecated using deprecated code */
	@Test
	public void testEnhancedForStatement() {
		if (ast.apiLevel() == AST.JLS2) {
			return;
		}
		EnhancedForStatement x1 = ast.newEnhancedForStatement();
		x1.setParameter(V1);
		x1.setExpression(E1);
		x1.setBody(S1);
		basicMatch(x1);
	}
	@Test
	public void testExpressionStatement() {
		ExpressionStatement x1 = ast.newExpressionStatement(E1);
		basicMatch(x1);
	}
	@Test
	public void testFieldAccess() {
		FieldAccess x1 = ast.newFieldAccess();
		x1.setExpression(E1);
		x1.setName(N1);
		basicMatch(x1);
	}
	@Test
	public void testFieldDeclaration() {
		FieldDeclaration x1 = ast.newFieldDeclaration(W1);
		x1.setJavadoc(JD1);
		if (ast.apiLevel() >= AST.JLS3) {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.setType(T1);
		x1.fragments().add(W2);
		basicMatch(x1);
	}
	@Test
	public void testForStatement() {
		ForStatement x1 = ast.newForStatement();
		x1.initializers().add(E1);
		x1.initializers().add(E2);
		x1.setExpression(N1);
		x1.updaters().add(N2);
		x1.updaters().add(N3);
		x1.setBody(S1);
		basicMatch(x1);
	}
	@Test
	public void testIfStatement() {
		IfStatement x1 = ast.newIfStatement();
		x1.setExpression(E1);
		x1.setThenStatement(S1);
		x1.setElseStatement(S2);
		basicMatch(x1);
	}
	@Test
	public void testImportDeclaration() {
		ImportDeclaration x1 = ast.newImportDeclaration();
		x1.setName(N1);
		basicMatch(x1);
	}
	@Test
	public void testInfixExpression() {
		InfixExpression x1 = ast.newInfixExpression();
		x1.setOperator(InfixExpression.Operator.PLUS);
		x1.setLeftOperand(E1);
		x1.setRightOperand(E2);
		x1.extendedOperands().add(N1);
		x1.extendedOperands().add(N2);
		basicMatch(x1);
	}
	@Test
	public void testInitializer() {
		Initializer x1 = ast.newInitializer();
		x1.setJavadoc(JD1);
		if (ast.apiLevel() >= AST.JLS3) {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.setBody(B1);
		basicMatch(x1);
	}
	/**
	 * @deprecated (not really - just suppressing the warnings
	 * that come from testing Javadoc.getComment())
	 *
	 */
	@Test
	public void testJavadoc() {
		JSdoc x1 = ast.newJSdoc();
		if (ast.apiLevel() == AST.JLS2) {
			x1.setComment("/**?*/"); //$NON-NLS-1$
		}
		x1.tags().add(TAG1);
		x1.tags().add(TAG2);
		basicMatch(x1);
	}

	@Test
	public void testLabeledStatement() {
		LabeledStatement x1 = ast.newLabeledStatement();
		x1.setLabel(N1);
		x1.setBody(S1);
		basicMatch(x1);
	}

	@Test
	public void testLineComment() {
		LineComment x1 = ast.newLineComment();
		basicMatch(x1);
	}

	@Test
	public void testMemberRef() {
		MemberRef x1 = ast.newMemberRef();
		x1.setQualifier(N1);
		x1.setName(N2);
		basicMatch(x1);
	}

	/** @deprecated using deprecated code */
	@Test
	public void testMethodDeclaration() {
		FunctionDeclaration x1 = ast.newFunctionDeclaration();
		x1.setJavadoc(JD1);
		if (ast.apiLevel() == AST.JLS2) {
			x1.setReturnType(T1);
		} else {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
			x1.setReturnType2(T1);
		}
		x1.setName(N1);
		x1.parameters().add(V1);
		x1.parameters().add(V2);
		x1.thrownExceptions().add(N2);
		x1.thrownExceptions().add(N3);
		x1.setBody(B1);
		basicMatch(x1);
	}
	@Test
	public void testMethodInvocation() {
		FunctionInvocation x1 = ast.newFunctionInvocation();
		x1.setExpression(N1);
		
		x1.setName(N2);
		x1.arguments().add(E1);
		x1.arguments().add(E2);
		basicMatch(x1);
	}
	
	@Test
	public void testMethodRef() {
		FunctionRef x1 = ast.newFunctionRef();
		basicMatch(x1);
		x1.setQualifier(N1);
		x1.setName(N2);
		x1.parameters().add(MPARM1);
	}
	@Test
	public void testMethodRefParameter() {
		FunctionRefParameter x1 = ast.newFunctionRefParameter();
		x1.setType(T1);
		x1.setName(N1);
		basicMatch(x1);
	}
	
	@Test
	public void testNullLiteral() {
		NullLiteral x1 = ast.newNullLiteral();
		basicMatch(x1);
	}
	@Test
	public void testNumberLiteral() {
		NumberLiteral x1 = ast.newNumberLiteral("1.0"); //$NON-NLS-1$
		basicMatch(x1);
	}
	@Test
	public void testParenthesizedExpression() {
		ParenthesizedExpression x1 = ast.newParenthesizedExpression();
		basicMatch(x1);
	}
	@Test
	public void testPostfixExpression() {
		PostfixExpression x1 = ast.newPostfixExpression();
		x1.setOperand(E1);
		x1.setOperator(PostfixExpression.Operator.INCREMENT);
		basicMatch(x1);
	}
	@Test
	public void testPrefixExpression() {
		PrefixExpression x1 = ast.newPrefixExpression();
		x1.setOperand(E1);
		x1.setOperator(PrefixExpression.Operator.INCREMENT);
		basicMatch(x1);
	}
	@Test
	public void testReturnStatement() {
		ReturnStatement x1 = ast.newReturnStatement();
		x1.setExpression(E1);
		basicMatch(x1);
	}
	@Test
	public void testSingleVariableDeclaration() {
		SingleVariableDeclaration x1 = ast.newSingleVariableDeclaration();
		if (ast.apiLevel() >= AST.JLS3) {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.setType(T1);
		x1.setName(N1);
		x1.setInitializer(E1);
		basicMatch(x1);
	}
	@Test
	public void testStringLiteral() {
		StringLiteral x1 = ast.newStringLiteral();
		x1.setLiteralValue("H"); //$NON-NLS-1$
		basicMatch(x1);
	}
	@Test
	public void testSuperConstructorInvocation() {
		SuperConstructorInvocation x1 = ast.newSuperConstructorInvocation();
		x1.setExpression(N1);

		x1.arguments().add(E1);
		x1.arguments().add(E2);
		basicMatch(x1);
	}
	@Test
	public void testSuperFieldAccess() {
		SuperFieldAccess x1 = ast.newSuperFieldAccess();
		x1.setQualifier(N1);
		x1.setName(N2);
		basicMatch(x1);
	}
	@Test
	public void testSuperMethodInvocation() {
		SuperMethodInvocation x1 = ast.newSuperMethodInvocation();
		x1.setQualifier(N1);

		x1.setName(N2);
		x1.arguments().add(E1);
		x1.arguments().add(E2);
		basicMatch(x1);
	}
	@Test
	public void testSwitchCase() {
		SwitchCase x1 = ast.newSwitchCase();
		x1.setExpression(E1);
		basicMatch(x1);
	}
	@Test
	public void testSwitchStatement() {
		SwitchStatement x1 = ast.newSwitchStatement();
		x1.setExpression(E1);
		x1.statements().add(S1);
		x1.statements().add(S2);
		basicMatch(x1);
	}
	
	@Test
	public void testTagElement() {
		TagElement x1 = ast.newTagElement();
		x1.setTagName("@foo"); //$NON-NLS-1$
		x1.fragments().add(TAG1);
		x1.fragments().add(TEXT1);
		x1.fragments().add(N1);
		x1.fragments().add(MTHREF1);
		basicMatch(x1);
	}
	@Test
	public void testTextElement() {
		TextElement x1 = ast.newTextElement();
		x1.setText("foo"); //$NON-NLS-1$
		basicMatch(x1);
	}

	@Test
	public void testThisExpression() {
		ThisExpression x1 = ast.newThisExpression();
		x1.setQualifier(N1);
		basicMatch(x1);
	}
	@Test
	public void testThrowStatement() {
		ThrowStatement x1 = ast.newThrowStatement();
		x1.setExpression(E1);
		basicMatch(x1);
	}
	@Test
	public void testTryStatement() {
		TryStatement x1 = ast.newTryStatement();
		x1.setBody(B1);
		CatchClause c1 = ast.newCatchClause();
		c1.setException(V1);
		c1.setBody(ast.newBlock());
		x1.catchClauses().add(c1);
		CatchClause c2 = ast.newCatchClause();
		c2.setException(V2);
		c2.setBody(ast.newBlock());
		x1.catchClauses().add(c2);
		x1.setFinally(ast.newBlock());
		basicMatch(x1);
	}
	/** @deprecated using deprecated code */
	@Test
	public void testTypeDeclaration() {
		TypeDeclaration x1 = ast.newTypeDeclaration();
		x1.setJavadoc(JD1);
		x1.setName(N1);
		if (ast.apiLevel() == AST.JLS2) {
			x1.setSuperclass(N2);
		} else {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.bodyDeclarations().add(FD1);
		x1.bodyDeclarations().add(FD2);
		basicMatch(x1);
	}
	@Test
	public void testTypeDeclarationStatement() {
		TypeDeclarationStatement x1 = ast.newTypeDeclarationStatement(TD1);
		basicMatch(x1);
	}
	@Test
	public void testTypeLiteral() {
		TypeLiteral x1 = ast.newTypeLiteral();
		x1.setType(T1);
		basicMatch(x1);
	}
	@Test
	public void testVariableDeclarationFragment() {
		VariableDeclarationFragment x1 = ast.newVariableDeclarationFragment();
		x1.setName(N1);
		x1.setInitializer(E1);
		basicMatch(x1);
	}
	@Test
	public void testVariableDeclarationExpression() {
		VariableDeclarationExpression x1 = ast.newVariableDeclarationExpression(W1);
		if (ast.apiLevel() >= AST.JLS3) {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.setType(T1);
		x1.fragments().add(W2);
		basicMatch(x1);
	}
	@Test
	public void testVariableDeclarationStatement() {
		VariableDeclarationStatement x1 = ast.newVariableDeclarationStatement(W1);
		if (ast.apiLevel() >= AST.JLS3) {
			x1.modifiers().add(MOD1);
			x1.modifiers().add(MOD2);
		}
		x1.setType(T1);
		x1.fragments().add(W2);
		basicMatch(x1);
	}
	@Test
	public void testWhileStatement() {
		WhileStatement x1 = ast.newWhileStatement();
		x1.setExpression(E1);
		x1.setBody(S1);
		basicMatch(x1);
	}
	

}
