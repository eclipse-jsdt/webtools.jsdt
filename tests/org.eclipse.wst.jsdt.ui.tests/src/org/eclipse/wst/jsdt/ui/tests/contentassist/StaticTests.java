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
public class StaticTests {
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

//	public void testCamelCase_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 0, 11, expectedProposals);
//	}

	@Ignore @Test
	public void testStatic_CamelCase_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 2, 3, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_CamelCase_OtherFile_BeforeOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 4, 15, expectedProposals);
	}

//	public void testStatic_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getClientPort() - Server",
//				"getClientIP() : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 13, expectedProposals);
//	}

	@Ignore @Test
	public void testStatic_OtherFile_BeforeOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "serverIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 8, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_NegativeTest_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 13, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testStatic_NegativeTest_OtherFile_BeforeOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "getClientIP() - Global",
				"getClientPort() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 8, expectedProposals, true, false);
	}

//	public void testCamelCase_ThisFile_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 28, 11, expectedProposals);
//	}

	@Ignore @Test
	public void testStatic_CamelCase_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 30, 3, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_CamelCase_ThisFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 32, 15, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_ThisFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "port - Server",
				"prototype - Server", "getClientIP() : String - Server", "getClientPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 34, 12, expectedProposals);
	}

//	public void testStatic_ThisFile_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "serverIP : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 36, 7, expectedProposals);
//	}

//	public void testCamelCase_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
//		String[][] expectedProposals = new String[][] { { "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 0, 11, expectedProposals);
//	}

	@Ignore @Test
	public void testStatic_CamelCase_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "yahooDotCom : Server - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 2, 3, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_CamelCase_OtherFile_AfterOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 4, 15, expectedProposals);
	}

	@Ignore @Test
	public void testStatic_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "port - Server",
				"prototype - Server", "getClientIP() : String - Server", "getClientPort() - Server" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals);
	}

//	public void testStatic_OtherFile_AfterOpen_ExpressionStarted_5() throws Exception {
//		String[][] expectedProposals = new String[][] { { "serverIP : String - Server", "getServerIP() : String - Server" } };
//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals);
//	}

	@Ignore @Test
	public void testStatic_NegativeTest_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 6, 12, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testStatic_NegativeTest_OtherFile_AfterOpen_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "clientIP : String - Server", "getClientIP() - Global",
				"getClientPort() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 8, 7, expectedProposals, true, false);
	}
}