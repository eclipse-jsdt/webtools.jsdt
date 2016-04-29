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
@Ignore
public class CamelCasingTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_BeforeOpen_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_BeforeOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	@Ignore @Test
	public void testMustFail_OtherFile_Expression2_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"iGotMessage(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_BeforeOpen_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { { "mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_Expression3_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"iGotMessage(param1)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals, true,
				false);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterOpen_Expression1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam",
				"iGotMessage(param1) - iGotMessage"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 0, 6, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun",
				"mail.iGotSpam(a, b) - mail.iGotSpam"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterOpen_Expression3() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"mail.inbox.iGotStarredFun(param1) - mail.inbox.iGotStarredFun"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8, expectedProposals);
	}

	//	Camel case testing for global variables

	@Ignore @Test
	public void testGlobalVar_CamelCasing_OtherFile_BeforeOpen_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum : Number - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	@Ignore @Test
	public void testGlobalVar_CamelCasing_OtherFile_BeforeOpen_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum : Number - Global",
			"globalVar - Global",
			"globalVarObject : {} - Global",
			"globalVarString : String - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	@Test
	public void testGlobalVar_CamelCasing_Thisfile_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 15, 3, expectedProposals);
	}

	@Test
	public void testGlobalVar_CamelCasing_ThisFile_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum - Global",
			"globalVar - Global",
			"globalVarObject - Global",
			"globalVarString - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 17, 2, expectedProposals);
	}

	@Ignore @Test
	public void testGlobalVar_CamelCasing_OtherFile_AfterOpen_Expresssion1() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum : Number - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 5, 3, expectedProposals);
	}

	@Ignore @Test
	public void testGlobalVar_CamelCasing_OtherFile_AfterOpen_Expresssion2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"globalVarNum : Number - Global",
			"globalVar - Global",
			"globalVarObject : {} - Global",
			"globalVarString : String - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 7, 2, expectedProposals);
	}

	//	Camel Casing tests for doubly nested functions

	@Ignore @Test
	public void testDoublyNestedFunc_CamelCasing_OtherFile_BeforeOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testDoublyNestedFunc_CamelCasing_ThisFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testDoublyNestedFunc_CamelCasing_OtherFile_AfterOpen_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"outerFunc() - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 2, 2, expectedProposals);
	}
}