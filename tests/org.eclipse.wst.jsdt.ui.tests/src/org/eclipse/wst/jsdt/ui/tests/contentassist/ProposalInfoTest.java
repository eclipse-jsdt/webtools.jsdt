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
public class ProposalInfoTest {
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
	public void testProposalInfo_OtherFile_BeforeOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 0, 0, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void testProposalInfo_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global",
				"HelloAmerica(State, City)  - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 2, 3, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void _testProposalInfo_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 4, 3, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void testProposalInfo_ThisFile_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 0, 0, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void testProposalInfo_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 15, 3, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void _testProposalInfo_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 22, 3, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void testProposalInfo_OtherFile_AfterOpen_Expression_NotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "City" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 0, 0, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void testProposalInfo_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "HelloWorld() - Global", "HelloAmerica(State, City) - Global" } };
		String[][] expectedInfo = new String[][] { { "Hello World", "State" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 2, 3, expectedProposals, expectedInfo);
	}

	@Ignore @Test
	public void _testProposalInfo_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "nodeOne : String - Global" } };
		String[][] expectedInfo = new String[][] { { "nodeOne = test" } };
		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_1.js", 4, 3, expectedProposals, expectedInfo);
	}

//	public void testWI82131() throws Exception {
//		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
//		String[][] expectedInfo = new String[][] { { "hi" } };
//		ContentAssistTestUtilities.runProposalInfoTest(fTestProjectSetup, "TestProposalInfo_0.js", 29, 6, expectedProposals, expectedInfo);
//	}
}