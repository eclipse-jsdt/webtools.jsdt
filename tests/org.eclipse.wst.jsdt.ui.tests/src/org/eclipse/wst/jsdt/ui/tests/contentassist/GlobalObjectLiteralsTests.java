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
public class GlobalObjectLiteralsTests {
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
	public void testFindGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindGlobalObjectLiteral_OtherFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1, expectedProposals);
	}

	@Ignore @Test
	public void testFindFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctionOnFieldOnGlobalObjectLiteral_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun() - {}", "crazy() - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12, expectedProposals);
	}

	@Test
	public void testFindGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 6, 0, expectedProposals);
	}

	@Test
	public void testFindGlobalObjectLiteral_SameFile_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 7, 1, expectedProposals);
	}

	@Test // new test
	public void testFindGlobalObjectLiteral_SameFile_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 7, 0,
					expectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse", "eclipse2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 4, expectedProposals);
	}

	@Test
	public void testFindFieldOnGlobalObjectLiteral_SameFile_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse", "eclipse2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 5, expectedProposals);
	}

	@Test
	public void testFindFunctionOnFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun()", "crazy()" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 12, 12, expectedProposals);
	}

	@Ignore @Test
	public void testFindGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindGlobalObjectLiteral_OtherFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "org : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1, expectedProposals);
	}

	@Ignore @Test
	public void testFindFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4, expectedProposals);
	}

	@Ignore @Test
	public void testFindFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "eclipse : {} - {}", "eclipse2 : {} - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5, expectedProposals);
	}

	@Ignore @Test
	public void testFindFunctionOnFieldOnGlobalObjectLiteral_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "fun() - {}", "crazy() - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12, expectedProposals);
	}

	@Ignore @Test
	public void testFindDuplicateGlobalObjectLiteral_OtherFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 2, 1);
	}

	@Ignore @Test
	public void testFindDuplicateFieldOnGlobalObjectLiteral_OtherFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 4);
	}

	@Ignore @Test
	public void testFindDuplicateFieldOnGlobalObjectLiteral_OtherFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 4, 5);
	}

	@Ignore @Test
	public void testFindDuplicateFunctionOnFieldOnGlobalObjectLiteral_OtherFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 6, 12);
	}

	@Test
	public void testFindDuplicateGlobalObjectLiteral_SameFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 8, 1);
	}

	@Test
	public void testFindDuplicateFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 4);
	}

	@Test
	public void testFindDuplicateFieldOnGlobalObjectLiteral_SameFile_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 10, 5);
	}

	@Test
	public void testFindDuplicateFunctionOnFieldOnGlobalObjectLiteral_SameFile_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 12, 12);
	}

//	// WI102885
//	public void testNoLeakedGlobal_SameFile() throws Exception {
//		String[][] expectedProposals = new String[][] { { "globalObjLitTestVar" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_0.js", 21, 19, expectedProposals, true, false);
//	}

	// WI102885
	@Ignore @Test
	public void testNoLeakedGlobal_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalObjLitTestVar" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalObjectLiterals_1.js", 8, 19, expectedProposals, true, false);
	}
}