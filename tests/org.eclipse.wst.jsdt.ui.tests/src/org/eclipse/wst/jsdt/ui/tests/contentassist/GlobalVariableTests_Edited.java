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
public class GlobalVariableTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@Ignore @BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		editFile_test11_0__0();
		editFile_test11_0__1();
		editFile_test11_1();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	/**
	 * file -> test11_0.js
	 * globalNum -> globalEditedNumber
	 * globalString -> globalEditedString
	 *
	 * @throws Exception
	 */
	public static void editFile_test11_0__0() throws Exception {
		fTestProjectSetup.editFile("test11_0.js", 1, 4, 12, "globalEditedNumber");
		fTestProjectSetup.editFile("test11_0.js", 7, 0, 12, "globalEditedString");
	}

	@Ignore @Test
	public void testFindFieldSuggestions_ThisFile_AfterEdit_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalEditedString : String - Global", "globalVar - Global", "globalEditedNumber : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 10, 0, expectedProposals);
	}

	@Ignore @Test
	public void testFindFieldSuggestions_ThisFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
				"globalEditedString : String - Global", "globalEditedNumber : Number - Global",
				"globalVarObject : {} - Global", "globalVarString : String - Global", "globalVar - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 9, 1, expectedProposals);
	}

	/**
	 * file -> test11_0.js
	 * globalV -> globalE
	 *
	 * @throws Excpeiton
	 */
	public static void editFile_test11_0__1() throws Exception {
		fTestProjectSetup.editFile("test11_0.js", 11, 6, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 15, 1, 1, "E");
		fTestProjectSetup.editFile("test11_0.js", 17, 1, 1, "E");
	}

	@Ignore @Test
	public void testFindFieldSuggestions_ThisFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global",
				"globalEditedString : String - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_0.js", 11, 7, expectedProposals);
	}

//	public void testFindFieldSuggestions_OtherFile_AfterEdit_Expression_NotStarted() throws Exception {
//		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
//				"globalEditedString : String - Global", "globalEditedNumber : Number - Global",
//				"globalVarObject : {} - Global", "globalVarString : String - Global", "globalVar - Global" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 0, 0, expectedProposals);
//	}
//
//	public void testFindFieldSuggestions_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "globalNum : Number - Global",
//				"globalEditedString : String - Global", "globalVar - Global", "globalEditedNumber : Number - Global",
//				"globalVarObject : {} - Global", "globalVarString : String - Global" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 1, 1, expectedProposals);
//	}

	/**
	 * file -> test11_1.js
	 * globalV -> globalE
	 *
	 * @throws Exception
	 */
	public static void editFile_test11_1() throws Exception {
		fTestProjectSetup.editFile("test11_1.js", 3, 6, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 5, 1, 1, "E");
		fTestProjectSetup.editFile("test11_1.js", 7, 1, 1, "E");
	}

//	public void testFindFieldSuggestions_OtherFile_AfterEdit_ExpressionStarted_2() throws Exception {
//		String[][] expectedProposals = new String[][] { { "globalEditedNumber : Number - Global",
//				"globalEditedString : String - Global" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test11_1.js", 3, 7, expectedProposals);
//	}
}