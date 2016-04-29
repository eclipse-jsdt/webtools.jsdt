/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class ConstructorTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesome(param1, param2) - Awesome" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 0, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 2, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_Expression_2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar : {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 6, 10, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 8, 13, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 10, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 12, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_7_NegativeTest() throws Exception {
		String[][] proposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, proposals, true, false);
	}

	@Ignore @Test
	public void testDuplicateFindConstructors_OtherFile_BeforeOpen_ExpressionStarted_6() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 12, 8);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_JustNew() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesome(param1, param2) - Awesome",
				"bar.Class1(a, b) - bar.Class1", "bar.Class2(c, d, e) - bar.Class2",
				"bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 17, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesome(param1, param2) - Awesome" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 19, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 21, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 23, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 25, 10, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 27, 13, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 29, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 31, 8, expectedProposals);
	}

	@Ignore @Test
	public void testDuplicateFindConstructors_SameFile_ExpressionStarted_6() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 31, 8);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesome(param1, param2) - Awesome" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 0, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 2, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_Expression_2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar : {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 6, 10, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_Expression_3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b)", "bar.Class2(c, d, e)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 6, 10, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 8, 13, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 10, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.Class1(a, b) - bar.Class1",
				"bar.Class2(c, d, e) - bar.Class2", "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 12, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ExpressionStarted_7_NegativeTest() throws Exception {
		String[][] proposals = new String[][] { { "bar.foo.Class3(param1, param2, param3, param4) - bar.foo.Class3" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, proposals, true, false);
	}

	@Ignore @Test
	public void testDuplicateFindConstructors_OtherFile_AfterOpen_ExpressionStarted_6() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 12, 8);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass1(a) - MyClass1", "MyClass2() - MyClass2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test6.js", 0, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass1(a) - MyClass1", "MyClass2() - MyClass2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test5.js", 7, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass1(a) - MyClass1", "MyClass2() - MyClass2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test6.js", 0, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_NestedVarDeclaration_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass7(a) - MyClass7" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test7.js", 5, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFileAndOtherFile_NestedVarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass7(a) - MyClass7", "MyClass1(a) - MyClass1",
				"MyClass2() - MyClass2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test7.js", 10, 11, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ArrayReferenceDeclaration_ExpressionStarted_0()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "test.Foo(x, y, z) - test.Foo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test9_1.js", 0, 7, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ArrayReferenceDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "test.Foo(x, y, z) - test.Foo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test9_0.js", 7, 7, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterOpen_ArrayReferenceDeclaration_ExpressionStarted_0()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "test.Foo(x, y, z) - test.Foo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test9_1.js", 0, 7, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ThisReferenceInStaticFunction() throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func2(b) - ParentType0.func2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6,
				expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_BeforeOpen_ThisReferenceInMemberFunction_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func1(a)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ThisReferenceInStaticFunction() throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func2(b) - ParentType0.func2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_0.js", 9, 6,
				expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ThisReferenceInMemberFunctionShouldNotCreateAType_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func1(a)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_0.js", 9, 6,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindConstructors_OhterFile_AfterOpen_ThisReferenceInStaticFunction() throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func2(b) - ParentType0.func2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6,
				expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OhterFile_AfterOpen_ThisReferenceInMemberFunction_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "ParentType0.func1(a)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindDuplicateConstructors_OtherFile_BeforeOpen_AnonymousConstructorFunctionAssignedToSingleNameReference()
			throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 2, 7);
	}

	@Ignore @Test
	public void testFindDuplicateConstructors_SameFile_AnonymousConstructorFunctionAssignedToSingleNameReference()
			throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_0.js", 11, 7);
	}

	@Ignore @Test
	public void testFindDuplicateConstructors_OtherFile_AfterOpen_AnonymousConstructorFunctionAssignedToSingleNameReference()
			throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 2, 7);
	}
}