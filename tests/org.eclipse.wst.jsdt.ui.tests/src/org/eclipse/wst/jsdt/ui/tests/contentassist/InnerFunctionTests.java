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
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class InnerFunctionTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_BeforeOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner2 - Global",
			"funcTenInner2(param1, param2) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_BeforeOpen_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_BeforeOpen_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_BeforeOpen_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_BeforeOpen_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_BeforeOpen_ExpressionStarted() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_BeforeOpen_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}

	@Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_InsideFunctionCall_ExpressionStarted()
	throws Exception {
		String[][] expectedProposals = new String[][] { { "subtract(x, y)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 24, 3, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner(newParam111, newParam222)",
			"funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 26, 0, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner(newParam111, newParam222)",
			"funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner(newParam111, newParam222)",
			"funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner(newParam111, newParam222)",
			"funcTenInner1(param1)",
			"funcTenInner2(param1, param2) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner2(param1, param2) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner2(param1, param2) - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTen(paramEleven, paramTwelve) - Global",
			"funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTenInner(newParam111, newParam222) - Global",
			"funcTenInner1(param1) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTenInner(newParam111, newParam222) - Global",
			"funcTenInner1(param1) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"funcTenInner(newParam111, newParam222) - Global",
			"funcTenInner1(param1) - Global",
			"funcTenInner3(param1, param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals, true, false);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 37, 1);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1);
	}

	@Test
	public void testFindDuplicateInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_AfterOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_AfterOpen_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_AfterOpen_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInner2 : Function - Global", "funcTenInner2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_AfterOpen_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_AfterOpen_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInner : Function - Global",
				"funcTenInner1 : Function - Global", "funcTenInner(newParam111, newParam222) : String - Global",
				"funcTenInner1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 1);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_AfterOpen_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}
}