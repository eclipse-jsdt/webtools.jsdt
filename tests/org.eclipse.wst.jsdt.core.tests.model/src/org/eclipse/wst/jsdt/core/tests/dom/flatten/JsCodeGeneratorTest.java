/*******************************************************************************
 * Copyright (c) 2016 Eugene Melekhov and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Eugene Melekhov - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.dom.flatten;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.flatten.TrivialJsCodeGenerator;
import org.eclipse.wst.jsdt.internal.compiler.closure.ClosureCompiler;
import org.junit.Test;

@SuppressWarnings("nls")
public class JsCodeGeneratorTest {

	// ----------------------- Literals -----------------------------------
	@Test
	public void testLiteralStringExpression1() {
		test("\"\"");
	}

	@Test
	public void testLiteralStringExpression2() {
		test("\"\"", "('')");
	}

	@Test
	public void testLiteralStringExpression3() {
		test("\"a\"", "('a')");
	}

	@Test
	public void testLiteralStringExpression4() {
		test("\"\\\"\"");
	}

	@Test
	public void testLiteralStringExpression5() {
		test("\"a\"", "('a')");
	}

	@Test
	public void testLiteralStringExpression6() {
		test("\"'\"", "('\\'')");
	}

	@Test
	public void testLiteralStringExpression7() {
		test("\"a\"");
	}

	@Test
	public void testLiteralStringExpression8() {
		test("'\"'");
	}

	@Test
	public void testLiteralStringExpression9() {
		test("\"\\0\\b\\n\\r\\t\\v\\f\\\\\\\"'\\u2028\\u2029日本\"");
	}

	@Test
	public void testLiteralBooleanExpression() {
		test("true");
		test("false");
	}

	@Test
	public void testLiteralNullExpression() {
		test("null");
		test("null", "nul\\u006c");
	}

	@Test
	public void testLiteralRegExpExpression() {
		test("/a/");
		test("/a/i");
		test("/a/ig");
		test("/a\\s/ig");
		test("/a\\r/ig");
		test("/a\\r/ instanceof 3");
		test("/a\\r/g instanceof 3");
	}

	// ------------------------ Expression ---------------------------------
	@Test
	public void testExpressionStatement1() {
		test("a");
		test("~{a:3}");
		test("a:~{a:3}");
		test("~function(){}");
		test("~function(){}()");
		test("function name(){}");
	}

	@Test
	public void testExpressionStatement2() {
		test("({a:3}+1)");
		test("({a:3})");
		test("do({a:3});while(1)");
	}

	// --------------------------- Block --------------------------------------
	@Test
	public void testBlockStatement() {
		test("{}");
		test("{{}}");
		test("{a:{}}");
		test("{a;b}", "{a\nb\n}");
	}

	@Test
	public void testSequence() {
		test("a,b,c,d");
	}

	@Test
	public void testSpreadElement() {
		test("[...a]");
		test("[...a,...b]");
		test("[...a,b,...c]");
		test("[...a=b]");
		test("[...(a,b)]");
		test("f(...a)");
	}

	@Test
	public void testVariableDeclarationStatement1() {
		test("var a=0");
	}

	@Test
	public void testVariableDeclarationStatement2() {
		test("var a=0,b=0");
	}

	@Test
	public void testVariableDeclarationStatement3() {
		test("var a=(0,0)");
	}

	@Test
	public void testVariableDeclarationStatement4() {
		test("var a=(0,0,0)");
		test("var a");
		test("var a,b");
		test("var a=\"\"in{}");
	}

	// ------------------------ Equality ---------------------------------
	@Test
	public void testEquality1() {
		test("a==b");
		test("a!=b");
		test("a==b");
		test("a!=b");
		test("a==b==c");
	}

	@Test
	public void testEquality2() {
		test("a==(b==c)");
	}

	// ------------------------- Logical --------------------------------

	@Test
	public void testLogicalAnd() {
		test("a&&b");
	}

	@Test
	public void testLogicalOr() {
		test("a||b");
	}

	// ------------------------ Bitwise ---------------------------------
	@Test
	public void testBitwiseOr() {
		test("a|b");
	}

	@Test
	public void testBitwiseAnd() {
		test("a&b");
	}

	@Test
	public void testBitwiseXor1() {
		test("a^b");
	}

	@Test
	public void testBitwiseXor2() {
		test("a^b&b");
	}

	@Test
	public void testBitwiseXor3() {
		test("(a^b)&b");
	}

	// ------------------------ Additive ---------------------------------
	@Test
	public void testAdditive1() {
		test("a+b");
		test("a-b");
		test("a+b<<b");
	}

	@Test
	public void testAdditive2() {
		test("a+(b+b)");
	}

	@Test
	public void testAdditive3() {
		test("a+(b<<b)");
	}

	@Test
	public void testAdditive4() {
		test("(a<<b)+(c>>d)");
	}

	@Test
	public void testAdditive5() {
		test("a*b+c/d", "(a*b)+(c/d)");
	}

	@Test
	public void testAdditive6() {
		test("(a+b)*(c-d)");
	}

	// ------------------------ Multiplicative ---------------------------------
	@Test
	public void testMultiplicative1() {
		test("a*b");
		test("a/b");
		test("a%b");
		test("a%b%c");
		test("a+b%c");
		test("!a*b");
	}

	@Test
	public void testMultiplicative2() {
		test("a%(b%c)");
	}

	@Test
	public void testMultiplicative3() {
		test("(a+b)%c");
	}

	@Test
	public void testMultiplicative4() {
		test("a*(b+c)");
	}

	@Test
	public void testPostfix() {
		test("a++");
		test("a--");
	}

	@Test
	public void testPrefix() {
		test("+a");
		test("-a");
		test("!a");
		test("~a");
		test("typeof a");
		test("void a");
		test("delete a");
		test("++a");
		test("--a");
		test("+ ++a");
		test("- --a");
		test("a+ +a");
		test("a-a");
		test("typeof-a");
		test("!!a");
		test("!!(a+a)");
	}

	@Test
	public void testPrimary() {
		test("0");
		test("1");
		test("2");
		test("(\"a\")", "('a')");
		test("(\"'\")", "('\\'')");
		test(";\"a\"");
		test(";'\"'");
		test("/a/");
		test("/a/i");
		test("/a/ig");
		test("/a\\s/ig");
		test("/a\\r/ig");
		test("/a\\r/ instanceof 3");
		test("/a\\r/g instanceof 3");
		test("3/ /a/g");
		test("true");
		test("false");
		test("null");
		test("null", "nul\\u006c");
		test("(function(){})");
	}

	@Test
	public void testRelational() {
		test("a<b");
		test("a<=b");
		test("a>b");
		test("a>=b");
		test("a instanceof b");
		test("a in b");
		test("a==b<b");
		test("(a==b)<b");
		test("for((b in b);;);");
		test("for((b in b);b in b;b in b);");
		test("for(var a=(b in b);b in b;b in b);");
		test("for(var a=(b in b),c=(b in b);b in b;b in b);");
		test("for(b in c in d);");
	}

	@Test
	public void testShift() {
		test("a<<b");
		test("a>>b");
		test("a>>>b");
		test("a<<b<<c");
		test("a<<(b<<c)");
		test("a<<b<c");
		test("a<<b<c");
		test("a<<(b<c)");
	}

	// ----------------------- Assignment -------------------------------
	@Test
	public void testAssignment() {
		test("a=b");
	}

	@Test
	public void testCompoundAssignment1() {
		test("a+=b");
		test("a*=b");
		test("a%=b");
		test("a<<=b");
		test("a>>=b");
		test("a>>>=b");
	}

	@Test
	public void testCompoundAssignment2() {
		test("a/=b");
		test("a|=b");
		test("a^=b");
		test("a,b=c");
		test("a=b,c");
		test("a>>>=b");
		test("a,b^=c");
		test("a^=b,c");
		test("a.b=0");
		test("a[b]=0");
	}

	@Test
	public void testCompoundAssignment3() {
		test("a=(b,c)");
		test("a^=(b,c)");
	}

	@Test
	public void testCodeGenDirectives1() {
		test("\"use strict\"");
	}

	@Test
	public void testCodeGenDirectives2() {
		test("\"use strict\"", "\"use\\u0020strict\"");
	}

	// --------------- Array Tests --------------------
	@Test
	public void testArrayExpression1() {
		test("[]");
	}

	@Test
	public void testArrayExpression2() {
		test("[a]");
	}

	@Test
	public void testArrayExpression3() {
		test("[a]", "[a,]");
	}

	@Test
	public void testArrayExpression4() {
		test("[a,b,c]", "[a,b,c,]");
	}

	@Test
	public void testArrayExpression5() {
		test("[a]", "[a,,]");
	}

	@Test
	public void testArrayExpression6() {
		test("[a]", "[a,,,]");
	}

	@Test
	public void testArrayExpression7() {
		test("[[a]]");
	}

	@Test
	public void testArrayExpression8() {
		test("[(a,a)]");
	}

	@Test
	public void testArrayExpression9() {
		test("[]", "[,]");
	}

	@Test
	public void testArrayExpression10() {
		test("[,];", "[,,]");
	}

	@Test
	public void testArrayExpression11() {
		test("[,,]", "[,,,]");
	}

	@Test
	public void testArrayBinding1() {
		test("[]=0");
	}

	@Test
	public void testArrayBinding2() {
		test("[...a]=0");
	}

	@Test
	public void testArrayBinding3() {
		test("[a,...a]=0");
	}

	@Test
	public void testArrayBinding4() {
		test("[a,a=0,...a]=0");
	}

	@Test
	public void testArrayBinding5() {
		test("[,]=0;", "[,,]=0");
	}

	@Test
	public void testArrayBinding6() {
		test("[,...a]=0");
	}

	// --------------- BindingProperty Tests --------------------
	@Test
	public void testBindingPropertyIdentifier() {
		test("{a=0}=0", "({a=0}=0)");
	}

	@Test
	public void testBindingPropertyProperty() {
		test("({a : b}=0)");
	}

	@Test
	public void testBindingWithDefault1() {
		test("[a=0]=0");
	}

	@Test
	public void testBindingWithDefault2() {
		test("({a:b=0}=0)");
	}

	// --------------- Do while Tests --------------------
	@Test
	public void testDoWhileStatement1() {
		test("do;while(1)");
	}

	@Test
	public void testDoWhileStatement2() {
		test("do{}while(1)");
	}

	@Test
	public void testDoWhileStatement3() {
		test("do debugger;while(1)");
	}

	@Test
	public void testDoWhileStatement4() {
		test("do if(3){}while(1)");
	}

	@Test
	public void testDoWhileStatement5() {
		test("do 3;while(1)", "do(3);while(1)");
	}

	// --------------- While Tests --------------------
	@Test
	public void testWhileStatement1() {
		test("while(0);");
	}

	@Test
	public void testWhileStatement2() {
		test("while(0)while(0);");
	}

	// --------------- With Tests --------------------
	@Test
	public void testWithStatement1() {
		test("with(0);");
	}

	@Test
	public void testWithStatement2() {
		test("with(0)with(0);");
	}

	@Test
	public void testWithStatement3() {
		test("with(null);");
	}

	// --------------- "for" Tests --------------------
	@Test
	public void testForStatement1() {
		test("for(var i=(1 in[]);;);");
	}

	@Test
	public void testForStatement2() {
		test("for(var i=(1 in[]),b,c=(1 in[]);;);");
	}

	@Test
	public void testForStatement3() {
		test("for((1 in[]);;);");
	}

	@Test
	public void testForStatement4() {
		test("for(1*(1 in[]);;);");
	}

	@Test
	public void testForStatement5() {
		test("for(1*(1+1 in[]);;);");
	}

	@Test
	public void testForStatement6() {
		test("for(1*(1+1 in[]);;);");
	}

	@Test
	public void testForStatement7() {
		test("for(1*(1+(1 in[]));;);");
	}

	// --------------- "for in " Tests --------------------
	@Test
	public void testForInStatement1() {
		test("for(var a in 1);");
	}

	@Test
	public void testForInStatement2() {
		test("for((let)in 1);");
	}

	@Test
	public void testForInStatement3() {
		test("for(a in 1);");
	}

	// --------------- "for of " Tests --------------------
	@Test
	public void testForOfStatement1() {
		test("for(a of b);");
	}

	@Test
	public void testForOfStatement2() {
		test("for([a]of[b]);");
	}

	@Test
	public void testForOfStatement3() {
		test("for(let[a]of[b]);");
	}

	@Test
	public void testBreakStatement() {
		test("while(1)break", "while(1)break");
		test("while(1){break;break}", "while(1){break;break;}");
		test("a:while(1){break;break a}", "a:while(1){break;break a;}");
		test("switch(1){case 1:break}", "switch(1){case 1:break;}");
	}

	@Test
	public void testContinueStatement() {
		test("while(1)continue", "while(1)continue");
		test("while(1){continue;continue}", "while(1){continue;continue;}");
		test("a:while(1){continue;continue a}", "a:while(1){continue;continue a;}");
	}

	@Test
	public void testReturnStatement() {
		test("function a(){return}");
		test("function a(){return 0}");
		test("function a(){return function a(){return 0}}");
	}

	// --------------- "if" Tests --------------------
	@Test
	public void testIfStatement1() {
		test("if(a);");
	}

	@Test
	public void testIfStatement2() {
		test("if(a)b");
	}

	@Test
	public void testIfStatement3() {
		test("if(a)if(a)b");
	}

	@Test
	public void testIfStatement4() {
		test("if(a){}");
	}

	@Test
	public void testIfStatement5() {
		test("if(a);else;");
	}

	@Test
	public void testIfStatement6() {
		test("if(a);else{}");
	}

	@Test
	public void testIfStatement7() {
		test("if(a){}else{}");
	}

	@Test
	public void testIfStatement8() {
		test("if(a)if(a){}else{}else{}");
	}

	// --------------------------- Conditional Tests ----------------------
	@Test
	public void testConditional1() {
		test("a?b:c");
		test("a?b?c:d:e");
		test("a?b:c?d:e");
		test("a?b?c:d:e?f:g");
		test("a?b=c:d");
		test("a?b=c:d=e");
		test("a||b?c=d:e=f");
		test("a?b||c:d");
		test("a?b:c||d");
	}

	@Test
	public void testConditional2() {
		test("(a?b:c)?d:e");
	}

	@Test
	public void testConditional3() {
		test("(a,b)?(c,d):(e,f)");
	}

	@Test
	public void testConditional4() {
		test("(a=b)?c:d");
	}

	@Test
	public void testConditional5() {
		test("a||(b?c:d)");
	}

	// ------------------- switch ---------------------------------
	@Test
	public void testSwitchStatement() {
		test("switch(0){}");
		test("switch(0){default:}");
		test("switch(0){case 0:default:}");
		test("switch(0){case 0:a;default:c:b}");
	}

	// --------------- labeled statements Tests --------------------
	@Test
	public void testLabeledStatement1() {
		test("a:;");
	}

	@Test
	public void testLabeledStatement2() {
		test("a:b:;");
	}

	// --------------- function declaration Tests --------------------

	@Test
	public void testFunctionDeclaration1() {
		test("function f(){}");
	}

	@Test
	public void testFunctionDeclaration2() {
		test("function* f(){}");
	}

	@Test
	public void testFunctionDeclaration3() {
		test("function f(a){}");
	}

	@Test
	public void testFunctionDeclaration4() {
		test("function f(a,b){}");
	}

	@Test
	public void testFunctionDeclaration5() {
		test("function f(a,b,...rest){}");
	}

	// --------------- function expression Tests --------------------
	@Test
	public void testFunctionExpression1() {
		test("function (){}", "(function(){})");
	}

	@Test
	public void testFunctionExpression2() {
		test("function f(){}", "(function f(){})");
	}

	@Test
	public void testFunctionExpression3() {
		test("function* (){}", "(function*(){})");
	}

	@Test
	public void testFunctionExpression4() {
		test("function* f(){}", "(function*f(){})");
	}

	// --------------- switch statements Tests --------------------

	@Test
	public void testSwitchStatement1() {
		test("switch(0){}");
	}

	@Test
	public void testSwitchStatement2() {
		test("switch(0){default:}");
	}

	@Test
	public void testSwitchStatement3() {
		test("switch(0){case 0:default:}");
	}

	@Test
	public void testSwitchStatement4() {
		test("switch(0){case 0:a;default:c:b}");
	}

	// --------------- throw statements Tests --------------------

	@Test
	public void testThrowStatement1() {
		test("throw 0");
	}

	@Test
	public void testThrowStatement2() {
		test("throw(1<1)+1");
	}

	// --------------- try statements Tests --------------------

	@Test
	public void testTryStatement1() {
		test("try{}catch(a){}");
	}

	@Test
	public void testTryStatement2() {
		test("try{}catch(a){}finally{}");
	}

	@Test
	public void testTryStatement3() {
		test("try{}finally{}");
	}

	// --------------- Class declaration Tests --------------------
	@Test
	public void testClassDeclaration1() {
		test("class A{get[[]](){}[1.54321](){}}");
	}

	@Test
	public void testClassDeclaration2() {
		test("class A{}");
	}

	@Test
	public void testClassDeclaration3() {
		test("class A extends B{}");
	}

	// --------------- Class element Tests --------------------
	@Test
	public void testClassElement1() {
		test("(class{a(){}})");
	}

	@Test
	public void testClassElement2() {
		test("(class{*a(){}})");
	}

	@Test
	public void testClassElement3() {
		test("(class{static a(){}})");
	}

	@Test
	public void testClassElement4() {
		test("(class{static*a(){}})");
	}

	@Test
	public void testClassElement5() {
		test("(class{constructor(){}})");
	}


	// --------------- Object Expression Tests --------------------
	@Test
	public void testObjectExpression1() {
		test("{}", "({})");
	}

	@Test
	public void testObjectExpression2() {
		test("{a:1}", "({a:1,})");
	}

	@Test
	public void testObjectExpression3() {
		test("{}.a--", "({}.a--)");
	}

	@Test
	public void testObjectExpression4() {
		test("{1:1}", "({1.0:1})");
	}

	@Test
	public void testObjectExpression5() {
		test("{a:b}", "({a:b})");
	}

	@Test
	public void testObjectExpression6() {
		test("{get a(){;}}", "({get a(){;}})");
	}

	@Test
	public void testObjectExpression7() {
		test("{set a(param){;}}", "({set a(param){;}})");
	}

	@Test
	public void testObjectExpression8() {
		test("{get a(){;},set a(param){;},b:1}", "({get a(){;},set a(param){;},b:1})");
	}

	@Test
	public void testObjectExpression9() {
		test("{a:(a,b)}", "({a:(a,b)})");
	}

	@Test
	public void testObjectExpression10() {
		test("{a}", "({a})");
	}

	// --------------- Yield Expression Tests --------------------
	@Test
	public void testYieldExpression1() {
		test("function*f(){yield}");
	}

	@Test
	public void testYieldExpression2() {
		test("function*f(){yield a}");
	}

	@Test
	public void testYieldExpression3() {
		test("function*f(){yield 0}");
	}

	@Test
	public void testYieldExpression4() {
		test("function*f(){yield{}}");
	}

	@Test
	public void testYieldExpression5() {
		test("function*f(){yield a+b}");
	}

	@Test
	public void testYieldExpression6() {
		test("function*f(){yield a=b}");
	}

	@Test
	public void testYieldExpression7() {
		test("function*f(){yield(a,b)}");
	}

	@Test
	public void testYieldExpression8() {
		// esprima throws an exception here
		test("function*f(){f(yield,yield)}");
	}

	@Test
	public void testYieldExpression9() {
		test("function*f(){f(yield a,yield b)}");
	}

	@Test
	public void testYieldExpression10() {
		test("function*f(){yield yield yield}");
	}

	// --------------- Arrow Expression Tests --------------------
	@Test
	public void testArrowExpression1() {
		test("a=>a");
	}

	@Test
	public void testArrowExpression2() {
		test("()=>a");
	}

	@Test
	public void testArrowExpression3() {
		test("a=>a", "(a)=>a");
	}

	@Test
	public void testArrowExpression4() {
		test("(...a)=>a");
	}

	@Test
	public void testArrowExpression5() {
		test("(a,...b)=>a");
	}

	@Test
	public void testArrowExpression6() {
		test("(a=0)=>a");
	}

	@Test
	public void testArrowExpression7() {
		test("(a,b)=>a");
	}

	@Test
	public void testArrowExpression8() {
		test("({a})=>a");
	}

	@Test
	public void testArrowExpression9() {
		test("({a=0})=>a");
	}

	@Test
	public void testArrowExpression10() {
		test("([a])=>a");
	}

	@Test
	public void testArrowExpression11() {
		test("a=>({})");
	}

	@Test
	public void testArrowExpression12() {
		test("a=>{}");
	}

	@Test
	public void testArrowExpression13() {
		test("a=>{({})}");
	}

	@Test
	public void testArrowExpression14() {
		test("a=>{0;return}");
	}

	@Test
	public void testArrowExpression15() {
		test("()=>function(){}");
	}

	@Test
	public void testArrowExpression16() {
		test("()=>class{}");
	}

	@Test
	public void testArrowExpression17() {
		test("()=>(1,2)");
	}

	@Test
	public void testArrowExpression18() {
		test("(()=>0)()");
	}

	// --------------- Template Tests --------------------

	@Test
	public void testTemplateExpression1() {
		test("``");
		test("````");
		test("a``");
		test("a.b``");
		test("a[b]``");
		test("a()[b]``");
		test("a()``");
	}

	@Test
	public void testTemplateExpression2() {
		test("(a+b)``");
	}

	@Test
	public void testTemplateExpression3() {
		test("function(){}``", "(function(){})``");
	}

	@Test
	public void testTemplateExpression4() {
		test("class{}``", "(class{})``");
	}

	@Test
	public void testTemplateExpression5() {
		test("{}``", "({})``");
	}

	@Test
	public void testTemplateExpression6() {
		test("`a`");
	}

	@Test
	public void testTemplateExpression7() {
		test("a`a`");
	}

	@Test
	public void testTemplateExpression8() {
		test("`a${b}c`");
	}

	@Test
	public void testTemplateExpression9() {
		test("`${a}`");
	}

	@Test
	public void testTemplateExpression10() {
		test("`${a}${b}`");
	}

	@Test
	public void testTemplateExpression11() {
		test("` ${a} ${b} `");
	}

	@Test
	public void testTemplateExpression12() {
		test("` ${a} ${b} `", "` ${ a } ${ b } `");
	}

	@Test
	public void testTemplateExpression13() {
		test("`a\\${b}c`");
	}

	@Test
	public void testTemplateExpression14() {
		test("``.a");
	}

	@Test
	public void testTemplateExpression15() {
		test("``()");
	}

	@Test
	public void testTemplateExpression16() {
		test("new``");
	}

	@Test
	public void testTemplateExpression17() {
		test("new``", "new ``()");
	}

	@Test
	public void testTemplateExpression18() {
		test("new``(a)", "new ``(a)");
	}

	@Test
	public void testTemplateExpression19() {
		test("aTag`this blog lives ${cheer} at ${host}`");
	}

	// ------------------ Super --------------------------------
	@Test
	public void testSuper1() {
		test("class A extends B{constructor(){super()}}");
	}

	@Test
	public void testSuper2() {
		test("({m(){super.m()}})");
	}

	// ------------------ New --------------------------------
	@Test
	public void testNewCallMember() {
		test("new a");
		test("new a(a)");
		test("new a(a,b)");
		test("new this.a");
		test("a()");
		test("a(a)");
		test("a(a,b)");
		test("a.a");
		test("a[a]");
		test("new a", "new a()");
		test("new a(a)");
		test("(new a).a", "new a().a");
		test("new a(a).v");
		test("new(a(a).v)");
		test("(new a)()");
		test("(new new a(a).a.a).a", "(new (new a(a).a).a).a");
		test("new((new a)().a)", "new((new a)()).a");
		test("new a.a");
		test("new(a().a)");
		test("(new a``).a");
	}

	@Test
	public void testNewTargetExpression() {
		test("function f(){new.target}");
		test("function f(){new.target}", "function f() { new . target }");
	}

	// --------------------Import/Export -------------------
	@Test
	public void testExport() {
		test("export var a");
		test("export var a;0");
		test("export var a=0");
		test("export var a,b");
		test("export var a=0,b=0");
		test("export const a=0");
		test("export let a");
		test("export function f(){}");
		test("export function f(){}0");
		test("export class A{}");
		test("export class A{}0");
	}

	@Test
	public void testExportAllFrom() {
		test("export*from\"m\"");
		test("export*from\"m\";0");
	}

	@Test
	public void testExportDefault() {
		test("export default function(){}");
		test("export default function(){}0");
		test("export default 0");
		test("export default 0;0");
		test("export default function f(){}");
		test("export default function*f(){}");
		test("export default class A{}");
		test("export default(class{})");
		test("export default(function(){})");
		test("export default{}");
	}

	@Test
	public void testExportFrom() {
		test("export{}from\"m\"");
		test("export{}from\"m\";0");
		test("let a;export{a}from\"m\"");
		test("let a,b;export{a,b}from\"m\"");
		test("export{}");
		test("let a;export{a}");
		test("let a,b;export{a,b}");
	}

	@Test
	public void testExportSpecifier() {
		test("let a;export{a}");
		test("let a,b;export{a as b}");
		test("let a,b;export{a,b}");
		test("let a,b,c;export{a,b as c}");
		test("let a,b,c;export{a as b,c}");
		test("let a,b,c,d;export{a as b,c as d}");
	}

	@Test
	public void testImport1() {
		test("import\"m\"");
	}

	@Test
	public void testImport2() {
		test("import\"m\";0");
	}

	@Test
	public void testImport3() {
		test("import a from\"m\"");
	}

	@Test
	public void testImport4() {
		test("import{a}from\"m\"");
	}

	@Test
	public void testImport5() {
		test("import{a,b}from\"m\"");
	}

	@Test
	public void testImport6() {
		test("import a,{b}from\"m\"");
	}

	@Test
	public void testImport7() {
		test("import a,{b,c}from\"m\"");
	}

	@Test
	public void testImport8() {
		test("import a,{b,c}from\"m\";0");
	}

	@Test
	public void testImport9() {
		test("import\"m\"", "import {} from \"m\"");
	}

	@Test
	public void testImport10() {
		test("import a from\"m\"", "import a,{}from \"m\"");
	}

	@Test
	public void testImportNamespace1() {
		test("import*as a from\"m\"");
	}

	@Test
	public void testImportNamespace2() {
		test("import*as a from\"m\";0");
	}

	@Test
	public void testImportNamespace3() {
		test("import a,*as b from\"m\"");
	}

	@Test
	public void testImportSpecifier1() {
		test("import{a}from\"m\"");
	}

	@Test
	public void testImportSpecifier2() {
		test("import{a}from\"m\";0");
	}

	@Test
	public void testImportSpecifier3() {
		test("import{a as b}from\"m\"");
	}

	@Test
	public void testImportSpecifier4() {
		test("import{a,b}from\"m\"");
	}

	@Test
	public void testImportSpecifier5() {
		test("import{a,b as c}from\"m\"");
	}

	@Test
	public void testImportSpecifier6() {
		test("import{a as b,c}from\"m\"");
	}

	@Test
	public void testImportSpecifier7() {
		test("import{a as b,c as d}from\"m\"");
	}

	// --------------- Helper functions --------------------

	private void test(String source) {
		test(source, source);
	}

	public void test(String expected, String source) {
		String result = TrivialJsCodeGenerator.generate(parse(source));
		assertEquals(expected, result);

	}

	private JavaScriptUnit parse(String content) {
		return ClosureCompiler.newInstance().setSource(content).parse();
	}

	@SuppressWarnings("unused")
	private String readResource(String resource) throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream(resource);
				Scanner s = new Scanner(inputStream, "UTF-8")) {
			s.useDelimiter("\\A");
			return s.next();
		}
	}

}
