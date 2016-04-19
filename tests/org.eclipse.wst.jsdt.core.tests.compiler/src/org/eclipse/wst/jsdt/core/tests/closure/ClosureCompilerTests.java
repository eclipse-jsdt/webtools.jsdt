/*******************************************************************************
 * Copyright (c) 2015-2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.closure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayName;
import org.eclipse.wst.jsdt.core.dom.ArrowFunctionExpression;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Assignment.Operator;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ContinueStatement;
import org.eclipse.wst.jsdt.core.dom.DebuggerStatement;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.EmptyStatement;
import org.eclipse.wst.jsdt.core.dom.ExportDeclaration;
import org.eclipse.wst.jsdt.core.dom.Expression;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.ForInStatement;
import org.eclipse.wst.jsdt.core.dom.ForOfStatement;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionExpression;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.ListExpression;
import org.eclipse.wst.jsdt.core.dom.ModuleSpecifier;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteral;
import org.eclipse.wst.jsdt.core.dom.ObjectLiteralField;
import org.eclipse.wst.jsdt.core.dom.ObjectName;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.RegularExpressionLiteral;
import org.eclipse.wst.jsdt.core.dom.RestElementName;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.SpreadElement;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TemplateElement;
import org.eclipse.wst.jsdt.core.dom.TemplateLiteral;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.ThrowStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.VariableKind;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;
import org.eclipse.wst.jsdt.core.dom.WithStatement;
import org.eclipse.wst.jsdt.core.dom.YieldExpression;
import org.eclipse.wst.jsdt.internal.compiler.closure.ClosureCompiler;
import org.junit.Ignore;
import org.junit.Test;

import com.google.javascript.jscomp.parsing.Config.LanguageMode;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.SimpleSourceFile;

@SuppressWarnings("nls")
public class ClosureCompilerTests {

	@Test
	public void createParserInstance(){
		ClosureCompiler parser = ClosureCompiler.newInstance();
		assertNotNull(parser);
	}

	@Test
	public void parseMultipleCalls(){
		JavaScriptUnit unit = parse("var a = 1;");
		assertNotNull(unit);
		unit = parse("var a = true;");
		assertNotNull(unit);
		unit = parse("var a = \'atest\';");
		assertNotNull(unit);
		unit = parse("var a = null;");
		assertNotNull(unit);
	}

	@Test
	public void checkRange(){
		JavaScriptUnit unit = parse("var a = 1;");
		assertNotNull(unit);
		assertEquals(0,unit.getStartPosition());
		assertEquals(10, unit.getLength());
	}



	@Test
	public void testVariableDeclaration_VAR(){
		JavaScriptUnit unit = parse("var a,b;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT){
				VariableDeclarationStatement  vd = (VariableDeclarationStatement) astNode;
				assertEquals(VariableKind.VAR, vd.getKind());
				assertEquals(2, vd.fragments().size());
				return;
			}
		}
		fail();
	}

	@Test
	public void testVariableDeclaration_LET(){
		JavaScriptUnit unit = parse("let a,b;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT){
				VariableDeclarationStatement  vd = (VariableDeclarationStatement) astNode;
				assertEquals(VariableKind.LET, vd.getKind());
				assertEquals(2, vd.fragments().size());
				return;
			}
		}
		fail();
	}

	@Test
	public void testSimpleAssignment(){
		JavaScriptUnit unit = parse("a += 123;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				Assignment assignment = (Assignment) ((ExpressionStatement)astNode).getExpression();
				assertSame(Operator.PLUS_ASSIGN, assignment.getOperator());
				return;
			}
		}
		fail();
	}

	@Test
	public void testThisExpression(){
		JavaScriptUnit unit = parse("this;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				assertTrue(((ExpressionStatement)astNode).getExpression() instanceof ThisExpression );
				return;
			}
		}
		fail();
	}

	@Test
	public void testArrayExpression(){
		JavaScriptUnit unit = parse("[1,2,3];");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				assertTrue(((ExpressionStatement)astNode).getExpression() instanceof ArrayInitializer );
				return;
			}
		}
		fail();
	}

	@Test
	public void testObjectExpression(){
		JavaScriptUnit unit = parse("o={ test:9 };");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				Expression c1 = ((ExpressionStatement)astNode).getExpression();
				assertTrue(c1 instanceof Assignment);
				Assignment a = (Assignment)c1;
				assertSame(a.getRightHandSide().getNodeType(),ASTNode.OBJECT_LITERAL);
				ObjectLiteral ol = (ObjectLiteral) a.getRightHandSide();
				assertNotNull(ol.fields().get(0));
				ObjectLiteralField lf = (ObjectLiteralField) ol.fields().get(0);
				assertEquals(ObjectLiteralField.FieldKind.INIT, lf.getKind());
				assertTrue(lf.getInitializer().getNodeType() == ASTNode.NUMBER_LITERAL);
				return;
			}
		}
		fail();
	}

	@Test
	public void testPosxFixExpression(){
		JavaScriptUnit unit = parse("123++;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				PostfixExpression pf = (PostfixExpression) ((ExpressionStatement)astNode).getExpression();
				assertSame(PostfixExpression.Operator.INCREMENT, pf.getOperator());
				return;
			}
		}
		fail();
	}

	@Test
	public void testPreFixExpression(){
		JavaScriptUnit unit = parse("delete a;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				PrefixExpression pf = (PrefixExpression) ((ExpressionStatement)astNode).getExpression();
				assertSame(PrefixExpression.Operator.DELETE, pf.getOperator());
				SimpleName operand = (SimpleName) pf.getOperand();
				assertEquals("a", operand.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testInfixExpression_0(){
		JavaScriptUnit unit = parse("d+7;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				InfixExpression pf = (InfixExpression) ((ExpressionStatement)astNode).getExpression();
				assertSame(InfixExpression.Operator.PLUS, pf.getOperator());
				SimpleName operand = (SimpleName) pf.getLeftOperand();
				assertEquals("d", operand.getIdentifier());
				return;
			}
		}
		fail();
	}
	@Test
	public void testInfixExpression_1(){
		JavaScriptUnit unit = parse("d && a;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				InfixExpression pf = (InfixExpression) ((ExpressionStatement)astNode).getExpression();
				assertSame(InfixExpression.Operator.CONDITIONAL_AND, pf.getOperator());
				SimpleName operand = (SimpleName) pf.getLeftOperand();
				assertEquals("d", operand.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testArrayAccess(){
		JavaScriptUnit unit = parse("a[b];");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				assertTrue(((ExpressionStatement)astNode).getExpression() instanceof ArrayAccess );
				return;
			}
		}
		fail();
	}

	@Test
	public void testFieldAccess(){
		JavaScriptUnit unit = parse("a.b;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				assertTrue(((ExpressionStatement)astNode).getExpression() instanceof FieldAccess );
				return;
			}
		}
		fail();
	}

	@Test
	public void testConditionalExpression(){
		JavaScriptUnit unit = parse("a==1?b:c;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof ConditionalExpression );
				ConditionalExpression conditional = (ConditionalExpression)anode.getExpression();
				assertTrue( conditional.getExpression() instanceof InfixExpression );
				return;
			}
		}
		fail();
	}

	@Test
	public void testCallExpression(){
		JavaScriptUnit unit = parse("f();");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof FunctionInvocation );
				return;
			}
		}
		fail();
	}

	@Test
	public void testListExpression(){
		JavaScriptUnit unit = parse("a,b.y,a[3];");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof ListExpression );
				return;
			}
		}
		fail();
	}

	@Test
	public void testClassInstanceCreation(){
		JavaScriptUnit unit = parse("new ab(c,d);");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof ClassInstanceCreation);
				return;
			}
		}
		fail();
	}

	@Test
	public void testFunctionExpression_withIdentifierPattern(){
		JavaScriptUnit unit = parse("f = function(a){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof Assignment);
				Assignment assignment = (Assignment) anode.getExpression();
				assertTrue( assignment.getRightHandSide() instanceof FunctionExpression);
				FunctionExpression fe = (FunctionExpression) assignment.getRightHandSide();
				assertFalse(fe.getMethod().parameters().isEmpty());
				return;
			}
		}
		fail();
	}

	@Test
	public void testGeneratorFunctionExpression(){
		JavaScriptUnit unit = parse("f = function* (a){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof Assignment);
				Assignment assignment = (Assignment) anode.getExpression();
				assertTrue( assignment.getRightHandSide() instanceof FunctionExpression);
				FunctionExpression fe = (FunctionExpression) assignment.getRightHandSide();
				assertTrue(fe.getMethod().isGenerator());
				assertFalse(fe.getMethod().parameters().isEmpty());
				return;
			}
		}
		fail();
	}

	@Test
	public void testFunctionExpression_withObjectPatternParameter(){
		JavaScriptUnit unit = parse("f= function({foo, bar: baz}){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof Assignment);
				Assignment assignment = (Assignment) anode.getExpression();
				assertTrue( assignment.getRightHandSide() instanceof FunctionExpression);
				FunctionExpression fe = (FunctionExpression) assignment.getRightHandSide();
				assertTrue(fe.getMethod().parameters().get(0) instanceof SingleVariableDeclaration);
				SingleVariableDeclaration svd = (SingleVariableDeclaration)fe.getMethod().parameters().get(0);
				ObjectName on = (ObjectName)svd.getPattern();
				assertEquals(2, on.objectProperties().size());
				ObjectLiteralField olf = (ObjectLiteralField) on.objectProperties().get(0);
				assertEquals("foo", ((SimpleName)olf.getFieldName()).getIdentifier());
				return;
			}
		}
		fail();
	}


	@Test
	public void testArrowFunctionExpression(){
		JavaScriptUnit unit = parse("foo = (bar) => 5+1;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement anode = (ExpressionStatement)astNode;
				assertTrue(anode.getExpression() instanceof Assignment);
				Assignment assignment = (Assignment) anode.getExpression();
				assertTrue( assignment.getRightHandSide() instanceof ArrowFunctionExpression );
				return;
			}
		}
		fail();
	}

	@Test
	public void testBlockStatement(){
		JavaScriptUnit unit = parse("{a=2;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.BLOCK){
				Block anode = (Block)astNode;
				assertFalse(anode.statements().isEmpty());
				assertTrue(anode.statements().get(0) instanceof ExpressionStatement);
				ExpressionStatement anotherNode = (ExpressionStatement)anode.statements().get(0);
				assertTrue(anotherNode.getExpression() instanceof Assignment);
				Assignment assignment = (Assignment) anotherNode.getExpression();
				assertTrue(assignment.getRightHandSide() instanceof NumberLiteral);
				assertTrue(assignment.getLeftHandSide()instanceof SimpleName);
				return;
			}
		}
		fail();
	}

	@Test
	public void testYieldExpression(){
		JavaScriptUnit unit = parse("function* foo(){yield index++;}");
		assertNotNull(unit);
		FunctionDeclaration func = (FunctionDeclaration) unit.statements().get(0);
		ExpressionStatement stmt = (ExpressionStatement) func.getBody().statements().get(0);
		assertEquals(ASTNode.YIELD_EXPRESSION,stmt.getExpression().getNodeType());
		YieldExpression yieldE = (YieldExpression) stmt.getExpression();
		assertFalse(yieldE.getDelegate());
		assertNotNull(yieldE.getArgument());
	}
	
	@Test
	public void testFunctionCallTolerantParsing(){
		JavaScriptUnit unit = parse("f(a function(){} c);");
		assertNotNull(unit);
	}
	
	@Test
	public void testInvalidForIn(){
		JavaScriptUnit unit = parse("for (var i, i2 in {});");
		assertNotNull(unit);
	}
	
	@Test
	public void testComplexRestInArrowFailure(){
		JavaScriptUnit unit = parse("(a,...[a]) => 0;");
		assertNotNull(unit);
	}

	@Test
	public void testEmptyStatement(){
		JavaScriptUnit unit = parse("{;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.BLOCK){
				Block anode = (Block)astNode;
				assertFalse(anode.statements().isEmpty());
				assertTrue(anode.statements().get(0) instanceof EmptyStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testDebuggerStatement(){
		JavaScriptUnit unit = parse("{debugger;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.BLOCK){
				Block anode = (Block)astNode;
				assertFalse(anode.statements().isEmpty());
				assertTrue(anode.statements().get(0) instanceof DebuggerStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testWithStatement(){
		JavaScriptUnit unit = parse("with(console){log(\"test\");};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.WITH_STATEMENT){
				WithStatement anode = (WithStatement)astNode;
				assertTrue(anode.getExpression() instanceof SimpleName );
				assertTrue(anode.getBody() instanceof Block );
				return;
			}
		}
		fail();
	}

	@Test
	public void testReturnStatement(){
		JavaScriptUnit unit = parse("f= function(){return 0;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement stmt = (ExpressionStatement)astNode;
				Assignment asgn = (Assignment) stmt.getExpression();
				FunctionExpression anode = (FunctionExpression)asgn.getRightHandSide();
				assertFalse(anode.getMethod().getBody().statements().isEmpty() );
				assertTrue(anode.getMethod().getBody().statements().get(0) instanceof ReturnStatement );
				ReturnStatement rs = (ReturnStatement) anode.getMethod().getBody().statements().get(0);
				assertTrue(rs.getExpression() instanceof NumberLiteral);
				return;
			}
		}
		fail();
	}

	@Test
	public void testLabeledStatement(){
		JavaScriptUnit unit = parse("abc: a=7+1;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.LABELED_STATEMENT){
				LabeledStatement ls = (LabeledStatement)astNode;
				assertEquals("abc", ls.getLabel().getIdentifier());
				assertTrue(ls.getBody() instanceof ExpressionStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testBreakStatement(){
		JavaScriptUnit unit = parse("while(e){break;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.WHILE_STATEMENT){
				WhileStatement ws = (WhileStatement)astNode;
				assertTrue(ws.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName)ws.getExpression();
				assertEquals("e", sn.getIdentifier());
				assertTrue(ws.getBody() instanceof Block);
				Block b = (Block)ws.getBody();
				assertTrue(b.statements().get(0) instanceof BreakStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testContinueStatement(){
		JavaScriptUnit unit = parse("while(e){continue;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.WHILE_STATEMENT){
				WhileStatement ws = (WhileStatement)astNode;
				assertTrue(ws.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName)ws.getExpression();
				assertEquals("e", sn.getIdentifier());
				assertTrue(ws.getBody() instanceof Block);
				Block b = (Block)ws.getBody();
				assertTrue(b.statements().get(0) instanceof ContinueStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testIfStatement(){
		JavaScriptUnit unit = parse("if(a){ab;}else ac;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.IF_STATEMENT){
				IfStatement is = (IfStatement)astNode;
				assertTrue(is.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName) is.getExpression();
				assertEquals("a", sn.getIdentifier());
				assertTrue(is.getThenStatement() instanceof Block);
				assertTrue(is.getElseStatement() instanceof ExpressionStatement );
				return;
			}
		}
		fail();
	}

	@Test
	public void testSwitchStatement(){
		JavaScriptUnit unit = parse("switch(a) {};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.SWITCH_STATEMENT){
				SwitchStatement ss = (SwitchStatement)astNode;
				assertTrue(ss.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName)ss.getExpression();
				assertEquals("a", sn.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testSwitchCaseStatement(){
		JavaScriptUnit unit = parse("switch(a) {case a:b;};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.SWITCH_STATEMENT){
				SwitchStatement ss = (SwitchStatement)astNode;
				assertFalse(ss.statements().isEmpty());
				assertTrue(ss.statements().get(0) instanceof SwitchCase);
				SwitchCase sc = (SwitchCase)ss.statements().get(0);
				assertTrue(sc.getExpression() instanceof SimpleName);
				assertTrue(ss.statements().get(1) instanceof ExpressionStatement);
				return;
			}
		}
		fail();
	}

	@Test
	public void testNestedSwitchCaseStatement(){
		JavaScriptUnit unit = parse("switch(a) {case a: switch(b){case c: d;};};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.SWITCH_STATEMENT){
				SwitchStatement ss = (SwitchStatement)astNode;
				assertFalse(ss.statements().isEmpty());
				assertTrue(ss.statements().get(0) instanceof SwitchCase);
				assertEquals(SwitchStatement.class, ss.statements().get(1).getClass());
				return;
			}
		}
		fail();
	}

	@Test
	public void testThrowStatement(){
		JavaScriptUnit unit = parse("throw d;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.THROW_STATEMENT){
				ThrowStatement ts = (ThrowStatement) astNode;
				assertTrue(ts.getExpression() instanceof SimpleName );
				SimpleName sn = (SimpleName)ts.getExpression();
				assertEquals("d", sn.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testTryCatchFinallyStatement(){
		JavaScriptUnit unit = parse("try{}catch(e){}finally{}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.TRY_STATEMENT){
				TryStatement ts = (TryStatement) astNode;
				assertEquals(1,ts.catchClauses().size());
				CatchClause cc = (CatchClause) ts.catchClauses().get(0);
				assertEquals("e",cc.getException().getName().getIdentifier());
				assertNotNull(ts.getFinally());
				return;
			}
		}
		fail();
	}

	@Test
	public void testTryCatchObjectPattern(){
		JavaScriptUnit unit = parse("try{}catch({a,b}){}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.TRY_STATEMENT){
				TryStatement ts = (TryStatement) astNode;
				assertEquals(1,ts.catchClauses().size());
				CatchClause cc = (CatchClause) ts.catchClauses().get(0);
				assertNotNull(cc.getException().getPattern());
				return;
			}
		}
		fail();
	}

	@Test
	public void testWhileStatement(){
		JavaScriptUnit unit = parse("while(e){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.WHILE_STATEMENT){
				WhileStatement ws = (WhileStatement)astNode;
				assertTrue(ws.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName)ws.getExpression();
				assertEquals("e", sn.getIdentifier());
				assertTrue(ws.getBody() instanceof Block);
				return;
			}
		}
		fail();
	}

	@Test
	public void testDoWhileStatement(){
		JavaScriptUnit unit = parse("do{}while(e);");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.DO_STATEMENT){
				DoStatement ws = (DoStatement)astNode;
				assertTrue(ws.getExpression() instanceof SimpleName);
				SimpleName sn = (SimpleName)ws.getExpression();
				assertEquals("e", sn.getIdentifier());
				assertTrue(ws.getBody() instanceof Block);
				return;
			}
		}
		fail();
	}

	@Test
	public void testForStatement(){
		JavaScriptUnit unit = parse("for(let i=0;i<10;i++){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FOR_STATEMENT){
				ForStatement fs = (ForStatement)astNode;
				assertTrue(fs.getBody() instanceof Block);
				assertEquals(1, fs.initializers().size());
				assertEquals(1, fs.updaters().size());
				assertTrue(fs.initializers().get(0) instanceof VariableDeclarationExpression);
				VariableDeclarationExpression vde = (VariableDeclarationExpression) fs.initializers().get(0);
				assertEquals(VariableKind.LET, vde.getKind());
				assertNotNull(fs.getExpression());
				return;
			}
		}
		fail();
	}

	@Test
	public void testForInStatement(){
		JavaScriptUnit unit = parse("for(let i in a){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FOR_IN_STATEMENT){
				ForInStatement fs = (ForInStatement)astNode;
				assertTrue(fs.getBody() instanceof Block);
				assertTrue(fs.getIterationVariable() instanceof VariableDeclarationExpression );
				VariableDeclarationExpression vde = (VariableDeclarationExpression) fs.getIterationVariable();
				assertEquals(VariableKind.LET, vde.getKind());
				assertTrue(fs.getCollection()instanceof SimpleName);
				SimpleName sn = (SimpleName)fs.getCollection();
				assertEquals("a", sn.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testForInStatement_1(){
		JavaScriptUnit unit = parse("for(i in a){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FOR_IN_STATEMENT){
				ForInStatement fs = (ForInStatement)astNode;
				assertTrue(fs.getIterationVariable() instanceof Expression );
				return;
			}
		}
		fail();
	}

	@Test
	public void testForOfStatement(){
		JavaScriptUnit unit = parse("for(let i of a){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FOR_OF_STATEMENT){
				ForOfStatement fs = (ForOfStatement)astNode;
				assertTrue(fs.getBody() instanceof Block);
				assertTrue(fs.getIterationVariable() instanceof VariableDeclarationExpression );
				VariableDeclarationExpression vde = (VariableDeclarationExpression) fs.getIterationVariable();
				assertEquals(VariableKind.LET, vde.getKind());
				assertTrue(fs.getCollection()instanceof SimpleName);
				SimpleName sn = (SimpleName)fs.getCollection();
				assertEquals("a", sn.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testAnonymousFunction(){
		JavaScriptUnit unit = parse("f(function(){});");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement)astNode;
				assertTrue(es.getExpression() instanceof FunctionInvocation);
				FunctionInvocation fi = (FunctionInvocation) es.getExpression();
				assertTrue (fi.arguments().get(0) instanceof FunctionExpression);
				FunctionExpression func = (FunctionExpression)fi.arguments().get(0);
				assertNull(func.getMethod().getName());
				return;
			}
		}
		fail();
	}

	@Test
	public void testFunctionExpression_andParameter(){
		JavaScriptUnit unit = parse("(function(y){});");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement)astNode;
				assertTrue(es.getExpression() instanceof ParenthesizedExpression);
				ParenthesizedExpression pe = (ParenthesizedExpression) es.getExpression();
				FunctionExpression func = (FunctionExpression)pe.getExpression();
				assertFalse(func.getMethod().parameters().isEmpty());
				assertEquals(1, func.getMethod().parameters().size());

				return;
			}
		}
		fail();
	}

	@Test
	public void testRegularExpression(){
		JavaScriptUnit unit = parse("/.{0}/;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement)astNode;
				assertTrue(es.getExpression() instanceof RegularExpressionLiteral);
				RegularExpressionLiteral rel = (RegularExpressionLiteral)es.getExpression();
				assertEquals("/.{0}/", rel.getRegularExpression());
				return;
			}
		}
		fail();
	}
	
	@Test
	public void testRegularExpression_2(){
		JavaScriptUnit unit = parse("/(^\\/)|(^#)|(^[\\d-\\u]*:)/;");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		assertFalse( (unit.getFlags() & ASTNode.MALFORMED) != 0);
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement)astNode;
				assertTrue(es.getExpression() instanceof RegularExpressionLiteral);
				RegularExpressionLiteral rel = (RegularExpressionLiteral)es.getExpression();
				assertEquals("/(^\\/)|(^#)|(^[\\d-\\u]*:)/", rel.getRegularExpression());
				return;
			}
		}
		fail();
	}

	@Test
	public void testRegularExpressionWithES6flags(){

		JavaScriptUnit unit = parse("/\\u{00000001d306}/u");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement) astNode;
				assertEquals(ASTNode.REGULAR_EXPRESSION_LITERAL, es.getExpression().getNodeType());
				return;
			}
		}
		fail();
	}

	@Test
	public void testClassDeclaration(){
		JavaScriptUnit unit = parse("class MyClass extends YourClass{}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT){
				TypeDeclarationStatement tds = (TypeDeclarationStatement) astNode;
				TypeDeclaration td = (TypeDeclaration) tds.getDeclaration();
				assertEquals("MyClass",td.getName().getIdentifier());
				assertNotNull(td.getSuperclass());
				assertEquals("YourClass", td.getSuperclass().getFullyQualifiedName());
				assertTrue(td.bodyDeclarations().isEmpty());
				return;
			}
		}
		fail();
	}

	@Test
	public void testMethodDefiniton(){
		JavaScriptUnit unit = parse("class MyClass extends YourClass{" +
																"constructor(){};" +
																"get test(){};" +
																"static aStaticMethod(){" +
																"var a=1;};}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT){
				TypeDeclarationStatement td = (TypeDeclarationStatement) astNode;
				final TypeDeclaration declaration = (TypeDeclaration) td.getDeclaration();
				assertEquals("MyClass",declaration.getName().getIdentifier());
				assertNotNull(declaration.getSuperclass());
				assertEquals("YourClass", declaration.getSuperclass().getFullyQualifiedName());
				assertFalse(declaration.bodyDeclarations().isEmpty());
				assertEquals(3,declaration.bodyDeclarations().size());
				return;
			}
		}
		fail();
	}

	@Test
	public void testSuper(){
		JavaScriptUnit unit = parse("class MyClass extends YourClass{" +
																"constructor(){super();};" +
																"}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.TYPE_DECLARATION_STATEMENT){
				TypeDeclarationStatement tds = (TypeDeclarationStatement) astNode;
				TypeDeclaration td = (TypeDeclaration) tds.getDeclaration();
				assertEquals("MyClass",td.getName().getIdentifier());
				assertNotNull(td.getSuperclass());
				assertEquals("YourClass", td.getSuperclass().getFullyQualifiedName());
				assertFalse(td.bodyDeclarations().isEmpty());
				for(int i = 0; i<td.bodyDeclarations().size(); i++){
					FunctionDeclaration fd = (FunctionDeclaration) td.bodyDeclarations().get(i);
					if(fd.isConstructor()){
						for(int j = 0; j < fd.getBody().statements().size(); j++){
							ASTNode n  = (ASTNode) fd.getBody().statements().get(i);
							if(n.getNodeType() == ASTNode.EXPRESSION_STATEMENT ){
								ExpressionStatement estmt = (ExpressionStatement)n;
								if(estmt.getExpression().getNodeType() == ASTNode.FUNCTION_INVOCATION){
									return;
								}
							}
						}
					}
				}
			}
		}
		fail();
	}

	@Test
	public void testRestElement(){
		JavaScriptUnit unit = parse("function foo (bar, ...rest) {}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FUNCTION_DECLARATION){
				FunctionDeclaration fd = (FunctionDeclaration) astNode;
				assertFalse(fd.parameters().isEmpty());
				assertEquals(2, fd.parameters().size());
				SingleVariableDeclaration vd = (SingleVariableDeclaration) fd.parameters().get(1);
				assertTrue(vd.getPattern() instanceof RestElementName);
				RestElementName ren = (RestElementName)vd.getPattern();
				assertEquals("rest", ((SimpleName)ren.getArgument()).getIdentifier());
				assertEquals("...rest", ren.toString());
				return;
			}
		}
		fail();
	}

	@Test
	public void testArrayPattern(){
		JavaScriptUnit unit = parse("f = function([foo, bar]){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement)astNode;
				Assignment assign =  (Assignment) es.getExpression();
				FunctionExpression fe = (FunctionExpression) assign.getRightHandSide();
				assertFalse(fe.getMethod().parameters().isEmpty());
				assertTrue(fe.getMethod().parameters().get(0) instanceof SingleVariableDeclaration);
				SingleVariableDeclaration sd = (SingleVariableDeclaration)fe.getMethod().parameters().get(0);
				assertTrue(sd.getPattern().getNodeType() == ASTNode.ARRAY_NAME);
				ArrayName arrayName = (ArrayName) sd.getPattern();
				assertEquals(2, arrayName.elements().size());
				assertEquals("foo", ((SimpleName)arrayName.elements().get(0)).getIdentifier());
				assertEquals("bar",((SimpleName)arrayName.elements().get(1)).getIdentifier());
				assertEquals("[foo,bar]",arrayName.toString());
				return;
			}
		}
		fail();
	}

	@Test
	public void testObjectPattern(){
		JavaScriptUnit unit = parse("function f({ age, name }){};");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.FUNCTION_DECLARATION){
				FunctionDeclaration fd = (FunctionDeclaration) astNode;
				assertEquals(1, fd.parameters().size());
				SingleVariableDeclaration svd = (SingleVariableDeclaration) fd.parameters().get(0);
				ObjectName on = (ObjectName) svd.getPattern();
				assertEquals(2, on.objectProperties().size());
				ObjectLiteralField field = (ObjectLiteralField) on.objectProperties().get(0);
				assertEquals("age", ((SimpleName)field.getFieldName()).getIdentifier());
				field =  (ObjectLiteralField) on.objectProperties().get(1);
				assertEquals("name", ((SimpleName)field.getFieldName()).getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testTemplateLiteral(){
		JavaScriptUnit unit = parse("`this blog lives ${cheer} at ${host}`");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement) astNode;
				assertEquals(ASTNode.TEMPLATE_LITERAL, es.getExpression().getNodeType());
				TemplateLiteral tl = (TemplateLiteral)es.getExpression();
				assertEquals(3, tl.elements().size());
				assertEquals(2, tl.expressions().size());
				assertNull(tl.getTag());
				TemplateElement el = (TemplateElement)tl.elements().get(0);
				assertEquals("this blog lives ", el.getRawValue());
				assertFalse(el.isTail());
				el = (TemplateElement)tl.elements().get(2);
				assertTrue(el.isTail());
				SimpleName name = (SimpleName)tl.expressions().get(0);
				assertEquals("cheer", name.getIdentifier());
				return;
			}
		}
		fail();
	}

	@Test
	public void testTagTemplateLiteral(){
		JavaScriptUnit unit = parse("aTag`this blog lives ${cheer} at ${host}`");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement es = (ExpressionStatement) astNode;
				assertEquals(ASTNode.TEMPLATE_LITERAL, es.getExpression().getNodeType());
				TemplateLiteral tl = (TemplateLiteral)es.getExpression();
				assertEquals(3, tl.elements().size());
				assertEquals(2, tl.expressions().size());
				assertNotNull(tl.getTag());
				SimpleName tag = (SimpleName)tl.getTag();
				assertEquals("aTag", tag.getIdentifier());
				TemplateElement el = (TemplateElement)tl.elements().get(0);
				assertEquals("this blog lives ", el.getRawValue());
				assertFalse(el.isTail());
				el = (TemplateElement)tl.elements().get(2);
				assertTrue(el.isTail());
				SimpleName name = (SimpleName)tl.expressions().get(0);
				assertEquals("cheer", name.getIdentifier());
				assertEquals("aTag`this blog lives ${cheer} at ${host}`", tl.toString());
				return;

			}
		}
		fail();
	}

	@Test
	public void testSpreadElement(){
		JavaScriptUnit unit = parse("[...document.querySelectorAll('div')]");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		ExpressionStatement es = (ExpressionStatement)statements.get(0);
		assertEquals(ASTNode.ARRAY_INITIALIZER, es.getExpression().getNodeType());
		ArrayInitializer ari = (ArrayInitializer) es.getExpression();
		assertEquals(1, ari.expressions().size());
		assertTrue( ari.expressions().get(0) instanceof SpreadElement);
		SpreadElement sel = (SpreadElement) ari.expressions().get(0);
		assertNotNull(sel.getArgument());
		assertEquals(ASTNode.FUNCTION_INVOCATION, sel.getArgument().getNodeType());
	}


	@Test
	public void testFunctionDeclaration(){
		JavaScriptUnit unit = parse("function fName(){return;}");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		FunctionDeclaration fd = (FunctionDeclaration) statements.get(0);
		assertNotNull(fd.getMethodName());
		assertEquals(fd.getName(), fd.getMethodName());
		assertEquals("fName", ((SimpleName)fd.getMethodName()).getIdentifier());
	}

	@Test
	public void testAbortingError(){
		JavaScriptUnit unit = parse("if(true){a =;};");
		assertNotNull(unit);
		assertEquals(1, unit.getMessages().length);
	}

	@Test
	@Ignore
	public void testAbortingError_2(){
		JavaScriptUnit unit = parse("}");
		assertNotNull(unit);
		assertEquals(1, unit.getMessages().length);
	}

	@Test
	public void testTolerantError(){
		JavaScriptUnit unit = parse(" function(){ ");
		assertNotNull(unit);
		assertEquals(2, unit.getMessages().length);
	}

	@Test
	public void testObjectDeclare(){
		JavaScriptUnit unit = parse("var app = {\n"
									+"initialize: function() {\n"
									+"this.bindEvents();\n}};");
		assertFalse(unit.statements().isEmpty());
		VariableDeclarationStatement vd = (VariableDeclarationStatement)unit.statements().get(0);
		assertFalse(vd.fragments().isEmpty());
		VariableDeclarationFragment fragment = (VariableDeclarationFragment) vd.fragments().get(0);
		assertEquals(ObjectLiteral.class, fragment.getInitializer().getClass());
		ObjectLiteral objectLiteral = (ObjectLiteral) fragment.getInitializer();
		assertFalse(objectLiteral.fields().isEmpty());
		ObjectLiteralField field = (ObjectLiteralField) objectLiteral.fields().get(0);
		assertEquals("initialize",((SimpleName)field.getFieldName()).getIdentifier());

	}

	@Test
	public void testImport_1(){
		JavaScriptUnit unit = parse("import {foo} from 'lib';");
		assertFalse(unit.imports().isEmpty());
		ImportDeclaration importDeclaration = (ImportDeclaration) unit.imports().get(0);
		assertEquals("lib", importDeclaration.getSource().getLiteralValue());
		assertFalse(importDeclaration.specifiers().isEmpty());
		ModuleSpecifier specifier = (ModuleSpecifier) importDeclaration.specifiers().get(0);
		assertEquals("foo", specifier.getDiscoverableName().getIdentifier());
		assertNull(specifier.getLocal());
		assertFalse(specifier.isDefault());
		assertFalse(specifier.isNamespace());
	}

	@Test
	public void testImport_2(){
		JavaScriptUnit unit = parse("import {foo as baz} from 'lib';");
		assertFalse(unit.imports().isEmpty());
		ImportDeclaration importDeclaration = (ImportDeclaration) unit.imports().get(0);
		assertEquals("lib", importDeclaration.getSource().getLiteralValue());
		assertFalse(importDeclaration.specifiers().isEmpty());
		ModuleSpecifier specifier = (ModuleSpecifier) importDeclaration.specifiers().get(0);
		assertEquals("baz", specifier.getLocal().getIdentifier());
		assertEquals("foo", specifier.getDiscoverableName().getIdentifier());
		assertFalse(specifier.isDefault());
		assertFalse(specifier.isNamespace());
	}

	@Test
	public void testImport_3(){
		JavaScriptUnit unit = parse("import foo from 'lib';");
		assertFalse(unit.imports().isEmpty());
		ImportDeclaration importDeclaration = (ImportDeclaration) unit.imports().get(0);
		assertEquals("lib", importDeclaration.getSource().getLiteralValue());
		assertFalse(importDeclaration.specifiers().isEmpty());
		ModuleSpecifier specifier = (ModuleSpecifier) importDeclaration.specifiers().get(0);
		assertEquals("foo", specifier.getLocal().getIdentifier());
		assertTrue(specifier.isDefault());
		assertFalse(specifier.isNamespace());
	}

	@Test
	public void testImport_4(){
		JavaScriptUnit unit = parse("import * as foo from 'lib';");
		assertFalse(unit.imports().isEmpty());
		ImportDeclaration importDeclaration = (ImportDeclaration) unit.imports().get(0);
		assertEquals("lib", importDeclaration.getSource().getLiteralValue());
		assertFalse(importDeclaration.specifiers().isEmpty());
		ModuleSpecifier specifier = (ModuleSpecifier) importDeclaration.specifiers().get(0);
		assertEquals("foo", specifier.getLocal().getIdentifier());
		assertFalse(specifier.isDefault());
		assertTrue(specifier.isNamespace());
	}

	@Test
	public void testExport_1(){
		JavaScriptUnit unit = parse("export {foo, bar};");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertEquals(2, export.specifiers().size());
		ModuleSpecifier specifier = (ModuleSpecifier) export.specifiers().get(0);
		assertEquals("foo",specifier.getLocal().getIdentifier());
	}

	@Test
	public void testExport_2(){
		JavaScriptUnit unit = parse("export {foo} from 'mod';");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertEquals("mod",export.getSource().getLiteralValue());
		assertEquals(1, export.specifiers().size());
		ModuleSpecifier specifier = (ModuleSpecifier) export.specifiers().get(0);
		assertEquals("foo",specifier.getLocal().getIdentifier());
	}

	@Test
	public void testExport_3(){
		JavaScriptUnit unit = parse("export var foo = 1;");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertNotNull(export.getDeclaration());
	}

	@Test
	public void testExport_4(){
		JavaScriptUnit unit = parse("export default function () {}");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertNotNull(export.getDeclaration());
		assertTrue(export.isDefault());
	}

	@Test
	public void testExport_5(){
		JavaScriptUnit unit = parse("export * from 'mod';");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertTrue(export.isAll());
	}

	@Test
	public void testExport_6(){
		JavaScriptUnit unit = parse("export function* agen(){}");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertNotNull(export.getDeclaration());
		assertEquals(ASTNode.FUNCTION_DECLARATION, export.getDeclaration().getNodeType());
		FunctionDeclaration func = (FunctionDeclaration)export.getDeclaration();
		assertEquals("agen", func.getMethodName().toString());
		assertTrue(func.isGenerator());
	}
	
	@Test
	public void testExport_7(){
		JavaScriptUnit unit = parse("export default identifier");
		assertFalse(unit.exports().isEmpty());
		ExportDeclaration export = (ExportDeclaration) unit.exports().get(0);
		assertNotNull(export.getDeclaration());
		assertTrue(export.isDefault());
	}
	
	@Test
	public void testComments(){
		JavaScriptUnit unit = parse("/**\n"
					+ "*Life, Universe, and Everything\n"
					+ "*/\n"
					+ "var answer = 6 * 7;\n"
					+ "//asdfasf\n"
					+ "var f =function(){\n"
					+ "//asdfasdfff\n"
					+"tata}");
		List commentList = unit.getCommentList();
		assertNotNull(commentList);
		assertEquals(3,commentList.size());
	}
	
	@Test
	public void testCommentAttachment(){
		JavaScriptUnit unit = parse( "/** JavaDoc Comment*/\n" +
									 "function foo(i) {}");
		List<?> commentList = unit.getCommentList();
		assertNotNull(commentList);
		assertEquals(1,commentList.size());		
		FunctionDeclaration fd = (FunctionDeclaration) unit.statements().get(0);
		assertNotNull("JSDoc is NOT attached to function ", fd.getJavadoc());
	}
	
	@Test
	public void testCommentAttachment_2(){
			JavaScriptUnit unit = parse( "/** JavaDoc Comment*/\n" +
										 "var i;");
			List commentList = unit.getCommentList();
			assertNotNull(commentList);
			assertEquals(1,commentList.size());		
			VariableDeclarationStatement vd = (VariableDeclarationStatement) unit.statements().get(0);
			assertNotNull("JSDoc is NOT attached to variable", vd.getJavadoc());
		}
	

	@Test
	public void testClassExpression(){
		JavaScriptUnit unit = parse("(class A extends 0{})");
		assertFalse(unit.statements().isEmpty());
		assertEquals(ASTNode.EXPRESSION_STATEMENT, unit.statements().get(0).getNodeType());
		ExpressionStatement es= (ExpressionStatement)unit.statements().get(0);
		ParenthesizedExpression pe = (ParenthesizedExpression) es.getExpression();
		TypeDeclarationExpression dex = (TypeDeclarationExpression) pe.getExpression();
		TypeDeclaration td = (TypeDeclaration)dex.getDeclaration();
		assertEquals("A", td.getName().getIdentifier());
	}

	@Test
	public void tesArrayPatternAssignment(){
		JavaScriptUnit unit = parse("[...a[0]] = 0;");
		assertFalse(unit.statements().isEmpty());
		ExpressionStatement expressionStatement = (ExpressionStatement)unit.statements().get(0);
		assertEquals(ASTNode.ASSIGNMENT, expressionStatement.getExpression().getNodeType());
		Assignment assignment = (Assignment)expressionStatement.getExpression();
		assertEquals(ASTNode.ARRAY_NAME, assignment.getLeftHandSide().getNodeType());
		ArrayName arrayName = (ArrayName)assignment.getLeftHandSide();
		assertFalse(arrayName.elements().isEmpty());
		assertEquals(ASTNode.ARRAY_ACCESS, ((ASTNode)arrayName.elements().get(0)).getNodeType());
		ArrayAccess access = (ArrayAccess)arrayName.elements().get(0);
		assertEquals("a", ((SimpleName)access.getArray()).getIdentifier());
		assertEquals("0", ((NumberLiteral)access.getIndex()).getToken());

	}
	@Test
	public void tesArrayPatternAssignment_2(){
		JavaScriptUnit unit = parse("[a,b=0,[c,...a[0]]={}]=0;");
		assertFalse(unit.statements().isEmpty());
		ExpressionStatement expressionStatement = (ExpressionStatement)unit.statements().get(0);
		assertEquals(ASTNode.ASSIGNMENT, expressionStatement.getExpression().getNodeType());
		Assignment assignment = (Assignment)expressionStatement.getExpression();
		assertEquals(ASTNode.ARRAY_NAME, assignment.getLeftHandSide().getNodeType());
		ArrayName arrayName = (ArrayName)assignment.getLeftHandSide();
		assertEquals(3,arrayName.elements().size());
	}

	@Test
	public void tesArrayPattern(){
		JavaScriptUnit unit = parse("for([a,b[a],{c,d=e,[f]:[g,h().a,(0).k,...i[0]]}] in 0);");
		assertFalse(unit.statements().isEmpty());
		ForInStatement forin = (ForInStatement) unit.statements().get(0);
		assertEquals(ASTNode.ARRAY_NAME, forin.getIterationVariable().getNodeType());
		ArrayName name  = (ArrayName)forin.getIterationVariable();
		assertEquals(3, name.elements().size());
	}

	@Test
	public void testFunctionDeclarationStatement(){
		JavaScriptUnit unit = parse("if (foo) function a(){} else{}");
		assertFalse(unit.statements().isEmpty());
		assertEquals(ASTNode.IF_STATEMENT, unit.statements().get(0).getNodeType());
		IfStatement ifstmt = (IfStatement) unit.statements().get(0);
		assertEquals(ASTNode.FUNCTION_DECLARATION_STATEMENT, ifstmt.getThenStatement().getNodeType() );
	}

	@Test
	public void testFunctionDeclarationStatement_2(){
		JavaScriptUnit unit = parse("while (foo)function a(){}");
		assertFalse(unit.statements().isEmpty());
		assertEquals(ASTNode.WHILE_STATEMENT, unit.statements().get(0).getNodeType());
		WhileStatement wstmt = (WhileStatement) unit.statements().get(0);
		assertEquals(ASTNode.FUNCTION_DECLARATION_STATEMENT, wstmt.getBody().getNodeType() );
	}

	@Test
	public void testVariableDeclarationAsStatement(){
		JavaScriptUnit unit = parse("if (foo) var bar = 0; else{}");
		assertFalse(unit.statements().isEmpty());
		assertEquals(ASTNode.IF_STATEMENT, unit.statements().get(0).getNodeType());
		IfStatement ifstmt = (IfStatement) unit.statements().get(0);
		assertEquals(ASTNode.VARIABLE_DECLARATION_STATEMENT, ifstmt.getThenStatement().getNodeType());
	}
	
	@Test
	public void testObjectLiteralWithFunctionDeclarationField(){
		JavaScriptUnit unit = parse("({ myFunction(){} });");
		assertNotNull(unit);
		List<ASTNode> statements = unit.statements();
		for (ASTNode astNode : statements) {
			if(astNode.getNodeType() == ASTNode.EXPRESSION_STATEMENT){
				ExpressionStatement expression = (ExpressionStatement) astNode;
				assertEquals(ASTNode.PARENTHESIZED_EXPRESSION, expression.getExpression().getNodeType());
				ParenthesizedExpression parans = (ParenthesizedExpression) expression.getExpression();
				assertEquals(ASTNode.OBJECT_LITERAL, parans.getExpression().getNodeType());
				ObjectLiteral obj = (ObjectLiteral) parans.getExpression();
				assertEquals(1, obj.fields().size());
				ObjectLiteralField field = (ObjectLiteralField) obj.fields().get(0);
				assertEquals(ASTNode.FUNCTION_EXPRESSION, field.getInitializer().getNodeType());
				return;
			}
		}
		fail();
	}
	
	@Test
	public void testInvalidObjectLiteral(){
		JavaScriptUnit unit = parse("({get[a,b]:0})");
		assertNotNull(unit);
	}
	
	@Test
	public void testChainFunctionCall(){
		JavaScriptUnit unit = parse("browser \n"+
									".init({browserName: 'chrome'}) \n" +
									".get('http://angularjs.org/') \n" +
									"// element method chaining \n" +
									".elementById('the-basics') \n" +
									".click()    // The 'click' call returns nothing, \n"+
									".click()    // So we can call it many times without loosing the element scope \n"+
									".click()    // ... \n"+
									".text()     // The element scope is preserved and this 'text' call works. \n"+
									".should.become('The Basics'); \n");
		assertNotNull(unit);
		ExpressionStatement expression = (ExpressionStatement) unit.statements().get(0);
		assertSame(ASTNode.FUNCTION_INVOCATION, expression.getExpression().getNodeType());
		FunctionInvocation fi = (FunctionInvocation)expression.getExpression();
		assertNotNull(fi.getExpression());
		assertSame(ASTNode.FIELD_ACCESS, fi.getExpression().getNodeType());
		FieldAccess fa = (FieldAccess)fi.getExpression();
		assertNotNull(fa.getName());
		assertEquals("become",fa.getName().getIdentifier());
	}
	
	@Test
	public void testParserRunner(){
		class TestErrorReporter implements com.google.javascript.rhino.ErrorReporter {

			private void report(String message, String name, int line, int column) {
				System.out.println(message + " at " + name + " line: " + line + ":" + column);  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			}

			/* (non-Javadoc)
			 * @see com.google.javascript.rhino.ErrorReporter#warning(java.lang.String, java.lang.String, int, int)
			 */
			@Override
			public void warning(String message, String sourceName, int line, int lineOffset) {
				this.report("WARNING: " + message, sourceName, line , lineOffset);
			}

			/* (non-Javadoc)
			 * @see com.google.javascript.rhino.ErrorReporter#error(java.lang.String, java.lang.String, int, int)
			 */
			@Override
			public void error(String message, String sourceName, int line, int lineOffset) {
				this.report("ERROR: " + message, sourceName, line , lineOffset);
				
			}
		}
		
		InputStream in = this.getClass().getResourceAsStream("es5.js");
		String content = readFile(in);
		com.google.javascript.jscomp.parsing.Config config = ParserRunner.createConfig(true, LanguageMode.ECMASCRIPT6,null);
		SimpleSourceFile sf = new SimpleSourceFile("es2015-script.js", false );
		ParserRunner.parse(sf,content,config, new TestErrorReporter() );
	}
	
	@Test
	public void testTestBuilderJS(){
		JavaScriptUnit unit = loadParseJs("TestBuilder.js");
		assertNotNull(unit);
	}

	// #### Everything.js tests.

	@Test
	public void testEverythingJS_es5(){
		loadParseJs("es5.js");
	}

	@Test
	@Ignore
	public void testEverythingJS_es2015_script(){
		loadParseJs("es2015-script.js");
	}

	@Test
	@Ignore
	public void testEverythingJS_es2015_module(){
		loadParseJs("es2015-module.js");
	}

	private JavaScriptUnit parse(String content){
		return ClosureCompiler.newInstance().toggleComments(true).setSource(content).parse();
	}

	private JavaScriptUnit loadParseJs(String file){
		InputStream in = this.getClass().getResourceAsStream(file);
		JavaScriptUnit unit = ClosureCompiler.newInstance().toggleComments(true).setSource(readFile(in)).parse();
		assertNotNull(unit);
		assertFalse(unit.statements().isEmpty());
		return unit;
	}

	private String readFile(InputStream input){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(input,Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
