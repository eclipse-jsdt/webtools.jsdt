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
public class CamelCasingTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@Ignore @BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		editFile_TestConstructorCamelCase_0();
		editFile_test13_0();
		editFile_test11_0();
		editFile_test11_1();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testCamelCasing_thisFile_AfterEdit_Expression1_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_thisFile_AfterEdit_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_thisFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression1_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { { "iGotMessage(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OhisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun", "mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotSentMessage(param1) - iGotSentMessage" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OhisFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_Thisfile_AfterEdit_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 15, 3, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global",
				"globalEditedString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 17, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedString : String - Global",
				"globalEditedNumber : Number - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 6, 2, expectedProposals);
	}

	/**
	 * file -> TestConstructorCamelCase_0.js
	 * iGotMessage -> iSentMessage
	 *
	 * @throws Exception
	 */
	private static void editFile_TestConstructorCamelCase_0() throws Exception {
		fTestProjectSetup.editFile("TestConstructorCamelCase_0.js", 0, 4, 11, "iGotSentMessage");
		fTestProjectSetup.editFile("TestConstructorCamelCase_0.js", 0, 31, 11, "iGotSentMessage");
	}

	private static void editFile_test11_0() throws Exception {
		fTestProjectSetup.editFile("test11_0.js", 1, 4, 12, "globalEditedNumber");
		fTestProjectSetup.editFile("test11_0.js", 7, 0, 12, "globalEditedString");
		fTestProjectSetup.editFile("test11_0.js", 11, 6, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 15, 1, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 17, 1, 1, "E");
	}

	private static void editFile_test11_1() throws Exception {
		fTestProjectSetup.editFile("test11_1.js", 3, 6, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 5, 1, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 7, 1, 1, "E");
	}

	private static void editFile_test13_0() throws Exception {
		fTestProjectSetup.editFile("test13_0.js", 7, 11, 6, "edited");
		fTestProjectSetup.editFile("test13_0.js", 0, 9, 9, "editedFunc");
	}
}