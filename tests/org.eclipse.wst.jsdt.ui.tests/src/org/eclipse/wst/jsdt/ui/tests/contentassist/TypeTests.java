/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class TypeTests {
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
	public void testCamelCase_OtherFile_BeforeOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 0, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.CeleronXSeries - Computer.proc",
				"Installed.CorelXSoftware - Installed" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 4, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_BeforeOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 6, 4, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_BeforeOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "Installed : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 8, 3, expectedProposals);
	}

	@Ignore @Test
	public void testTypes_OtherFile_BeforeOpen_ExpressionStarted_NegativeTest() throws Exception {
		String[][] proposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc", "Computer.proc.IntelQuadCore - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 10, 10, proposals, true, false);
	}

	@Ignore @Test
	public void testCamelCase_ThisFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1",
				"Computer.proc.IntelDualCore2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 27, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.CeleronXSeries", "Installed.CorelXSoftware" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 29, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1",
				"Computer.proc.IntelDualCore2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 31, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_ThisFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer : {} - Global", "Computer.proc.IntelDualCore1",
				"Computer.proc.IntelDualCore2", "Computer.proc.CeleronXSeries" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 33, 4, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_ThisFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "Installed.CorelXSoftware", "Installed : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 35, 3, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 0, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.CeleronXSeries - Computer.proc",
				"Installed.CorelXSoftware - Installed" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCase_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 4, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_AfterOpen_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 6, 4, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_AfterOpen_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "Installed : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 8, 3, expectedProposals);
	}

	@Ignore @Test
	public void testTypes_OtherFile_AfterOpen_ExpressionStarted_NegativeTest() throws Exception {
		String[][] proposals = new String[][] { { "Computer.proc.IntelDualCore1 - Computer.proc",
				"Computer.proc.IntelDualCore2 - Computer.proc", "Computer.proc.IntelQuadCore - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 10, 10, proposals, true, false);
	}
}