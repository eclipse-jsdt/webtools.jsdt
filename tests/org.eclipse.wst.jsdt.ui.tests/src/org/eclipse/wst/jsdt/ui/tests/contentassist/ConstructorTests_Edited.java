/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
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
public class ConstructorTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@Ignore @BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		editFile_test2_0();
		editFile_test4();
		editFile_test9_0();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_JustNew() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesomeness(param1, param2) - Awesomeness",
				"bar.ClassOne(a, b) - bar.ClassOne", "bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 17, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesomeness(param1, param2) - Awesomeness" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 19, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 21, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 23, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 25, 10, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 27, 13, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 29, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_AfterEdit_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_0.js", 31, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_VarDeclaration_AfterEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClassEdit1(a) - MyClassEdit1",
				"MyClassEdit2() - MyClassEdit2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test5.js", 7, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFileAndOtherFile_NestedVarDeclaration_AfterEdit_ExpressionStarted_0()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClass7(a) - MyClass7", "MyClassEdit1(a) - MyClassEdit1",
				"MyClassEdit2() - MyClassEdit2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test7.js", 10, 11, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_ThisFile_ArrayReferenceDeclaration_AfterEdit_ExpressionStarted_0()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "testEdit.Foo(x, y, z) - testEdit.Foo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test9_0.js", 7, 7, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Awesomeness(param1, param2) - Awesomeness" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 0, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 2, 6, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 4, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 6, 10, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 8, 13, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 10, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "bar.ClassOne(a, b) - bar.ClassOne",
				"bar.ClassTwo(c, d, e) - bar.ClassTwo",
				"bar.foo.ClassThree(param1, param2, param3, param4) - bar.foo.ClassThree" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test2_1.js", 12, 9, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "MyClassEdit1(a) - MyClassEdit1",
				"MyClassEdit2() - MyClassEdit2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test6.js", 0, 8, expectedProposals);
	}

	@Ignore @Test
	public void testFindConstructors_OtherFile_AfterEdit_ArrayReferenceDeclaration_ExpressionStarted_0()
			throws Exception {
		String[][] expectedProposals = new String[][] { { "testEdit.Foo(x, y, z) - testEdit.Foo" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test9_1.js", 0, 7, expectedProposals);
	}

	/**
	 * file -> test2_0.js
	 * Awesome -> Awesomeness
	 * Class1 -> ClassOne
	 * Class2 -> ClassTwo
	 * Class3 -> ClassThree
	 *
	 * @throws Exception
	 */
	private static void editFile_test2_0() throws Exception {
		fTestProjectSetup.editFile("test2_0.js", 0, 9, 7, "Awesomeness");
		fTestProjectSetup.editFile("test2_0.js", 10, 4, 6, "ClassOne");
		fTestProjectSetup.editFile("test2_0.js", 11, 4, 6, "ClassOne");
		fTestProjectSetup.editFile("test2_0.js", 12, 4, 6, "ClassTwo");
		fTestProjectSetup.editFile("test2_0.js", 13, 4, 6, "ClassTwo");
		fTestProjectSetup.editFile("test2_0.js", 14, 8, 6, "ClassThree");
		fTestProjectSetup.editFile("test2_0.js", 15, 8, 6, "ClassThree");
	}

	/**
	 * file -> test5.js
	 * MyClass1 -> MyClassEdit1
	 * MyClass2 -> MyClassEdit2
	 *
	 * @throws Exception
	 */
	private static void editFile_test4() throws Exception {
		fTestProjectSetup.editFile("test5.js", 0, 9, 8, "MyClassEdit1");
		fTestProjectSetup.editFile("test5.js", 4, 4, 8, "MyClassEdit2");
		fTestProjectSetup.editFile("test5.js", 5, 0, 8, "MyClassEdit2");
	}

	/**
	 * file -> test9_0.js
	 * test -> testEdit
	 *
	 * @throws Exception
	 */
	private static void editFile_test9_0() throws Exception {
		fTestProjectSetup.editFile("test9_0.js", 0, 4, 4, "testEdit");
		fTestProjectSetup.editFile("test9_0.js", 1, 0, 4, "testEdit");
	}
}