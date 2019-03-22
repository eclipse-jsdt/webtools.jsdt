/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
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
public class InnerFunctionTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@Ignore @BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		fTestProjectSetup.editFile("TestInnerFunctions_0.js", 2, 22, 0, "Edit");
		fTestProjectSetup.editFile("TestInnerFunctions_0.js", 6, 17, 0, "Edit");
		fTestProjectSetup.editFile("TestInnerFunctions_0.js", 10, 13, 0, "Edit");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 0, 0, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions_OtherFile_ExpresionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_ExpressionStarted() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 2, 5);
	}

	@Ignore @Test
	public void testFindDuplicateInnerFunctions_OtherFile_CamelCase() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestInnerFunctions_1.js", 4, 2);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit2 : Function - Global", "funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 27, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 29, 1, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_InsideInnerFunction_CamelCase() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTen(paramEleven, paramTwelve) - Global",
				"funcTenInnerEdit1 : Function", "funcTenInnerEdit2 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String", "funcTenInnerEdit1(param1)",
				"funcTenInnerEdit2(param1, param2) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 28, 2, expectedProposals);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_EmptyLine_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 32, 0, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_ExpressionStarted_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 33, 5, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions_SameFile_OutsideInnerFunction_CamelCase_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "funcTenInnerEdit : Function - Global",
				"funcTenInnerEdit1 : Function - Global",
				"funcTenInnerEdit(newParam111, newParam222) : String - Global", "funcTenInnerEdit1(param1) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestInnerFunctions_0.js", 34, 2, expectedProposals, true, false);
	}
}