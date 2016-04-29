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
public class DoublyNestedFunctionTests {
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
	public void testFindInnerFunctions2_OtherFile_BeforeOpen_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "outerFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void test_FindInnerFunctions2_OtherFile_BeforeOpen_NegativeTest_ExpressionStarted_1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "innerFunc()", "insideInnerFunc()",
				"localInnerFunc : Function", "localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 4, 1, unexpectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions2_ThisFile_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global",
			"innerFunc()"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 3, 0, expectedProposals);
	}

	@Test
	public void test_InnerFunctions2_ThisFile_NegativeTest_Expression1() throws Exception {
		String[][] unexpectedProposals = new String[][] { {
			"insideInnerFunc()",
			"localInnerFunc(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 2, 0, unexpectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions2_ThisFile_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global",
			"innerFunc()",
			"insideInnerFunc()"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 7, 0, expectedProposals);
	}

	@Test
	public void test_FindInnerFunctions2_ThisFile_NegativeTest_Expression2() throws Exception {
		String[][] unexpectedProposals = new String[][] { {
			"localInnerFunc(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 5, 0, unexpectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions2_ThisFile_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global",
			"innerFunc()",
			"insideInnerFunc()",
			"localInnerFunc(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 12, 0, expectedProposals);
	}

	@Test
	public void testFindInnerFunctions2_ThisFile_Expression4() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global",
			"innerFunc()",
			"insideInnerFunc()"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 15, 0, expectedProposals);
	}

	@Test
	public void test_FindInnerFunctions2_ThisFile_NegativeTest_Expression4() throws Exception {
		String[][] unexpectedProposals = new String[][] { {
			"localInnerFunc(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 15, 0, unexpectedProposals, true, false);
	}

	@Test
	public void testFindInnerFunctions2_ThisFile_Expression5() throws Exception {
		String[][] expectedProposals = new String[][] { { "outerFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 21, 0, expectedProposals);
	}

	@Test
	public void test_FindInnerFunctions2_ThisFile_NegativeTest_Expression5() throws Exception {
		String[][] unexpectedProposals = new String[][] { {
			"innerFunc()",
			"insideInnerFunc()",
			"localInnerFunc(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 21, 0, unexpectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions2_OtherFile_AfterOpen_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void test_FindInnerFunctions2_OtherFile_NegativeTest_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "innerFunc()", "insideInnerFunc()",
				"localInnerFunc : Function", "localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 4, 1, unexpectedProposals, true, false);
	}
}