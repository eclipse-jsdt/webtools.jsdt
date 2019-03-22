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
public class GlobalFunctionTests {
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
	public void testFindFunctions_OtherFile_BeforeOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(Number paramOne, String paramTwo) - Global",
			"funcSix(paramOne, String paramTwo) - Global",
			"funcSeven(String paramOne, paramTwo) - Global",
			"funcEight(paramOne) - Global",
			"funcNine(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 0, 1, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(Number paramOne, String paramTwo) - Global",
			"funcSix(paramOne, String paramTwo) - Global",
			"funcSeven(String paramOne, paramTwo) - Global",
			"funcEight(paramOne) - Global",
			"funcNine(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 2, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTwo() - Global",
			"funcThree(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 4, 5, expectedProposals);
	}

	@Test
	public void testFindFunctions_ThisFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 54, 0, expectedProposals);
	}

	@Test
	public void testFindFunctions_ThisFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(paramOne, paramTwo) - Global",
			"funcSix(paramOne, paramTwo) - Global",
			"funcSeven(paramOne, paramTwo) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 57, 1, expectedProposals);
	}

	@Test
	public void testFindFunctions_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(paramOne, paramTwo) - Global",
			"funcSix(paramOne, paramTwo) - Global",
			"funcSeven(paramOne, paramTwo) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 59, 4, expectedProposals);
	}

	@Test
	public void testFindFunctions_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTwo() - Global",
			"funcThree(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_0.js", 61, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_AftereOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(Number paramOne, String paramTwo) - Global",
			"funcSix(paramOne, String paramTwo) - Global",
			"funcSeven(String paramOne, paramTwo) - Global",
			"funcEight(paramOne) - Global",
			"funcNine(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 0, 1, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcOne() - Global",
			"funcTwo() - Global",
			"funcThree(paramOne) - Global",
			"funcFour(paramOne, paramTwo) - Global",
			"funcFive(Number paramOne, String paramTwo) - Global",
			"funcSix(paramOne, String paramTwo) - Global",
			"funcSeven(String paramOne, paramTwo) - Global",
			"funcEight(paramOne) - Global",
			"funcNine(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 2, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTwo() - Global",
			"funcThree(paramOne) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test0_1.js", 4, 5, expectedProposals);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_BeforeOpen_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_BeforeOpen_ExpressionStarted1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_BeforeOpen_ExpressionNotStarted_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_BeforeOpen_ExpressionStarted1_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals, true, false);
	}

	@Test // This one fails?
	public void testNamedFunctionsAssignedToVariables_ThisFile_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 8, 0,
				expectedProposals);
	}

	@Test
	public void testNamedFunctionsAssignedToVariables_ThisFile_ExpressionStarted1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1,
				expectedProposals);
	}

	@Test
	public void testNamedFunctionsAssignedToVariables_ThisFile_ExpressionNotStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 8, 0,
				expectedProposals, true, false);
	}

	@Test
	public void testNamedFunctionsAssignedToVariables_ThisFile_ExpressionStarted1_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_AftereOpen_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterOpen_ExpressionStarted1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1(param2) - Global",
			"foo2(param3, param4) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterOpen_ExpressionNotStarted_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 0, 0,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testNamedFunctionsAssignedToVariables_OtherFile_AfterOpen_ExpressionStarted1_NegativeTest()
			throws Exception {
		String[][] expectedProposals = new String[][] { {
			"foo1Ignored",
			"foo2Ignored"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1,
				expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindFunctions_OtherFile_FromJAR_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"t(args) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test14_0.js", 0, 1, expectedProposals);
	}

}