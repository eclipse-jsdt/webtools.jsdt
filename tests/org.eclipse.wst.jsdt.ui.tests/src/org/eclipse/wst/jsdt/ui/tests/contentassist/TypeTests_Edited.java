/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
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
public class TypeTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		/* file -> TestJSClasses_0.js
		 * IntelQuadCore -> IntelCentrino
		 * IntelDualCore -> IntelXeonProc
		 * Installed -> Developed */
		fTestProjectSetup.editFile("TestJSClasses_0.js", 15, 19, 8, "Centrino");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 9, 19, 8, "XeonProc");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 12, 19, 8, "XeonProc");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 7, 4, 9, "Developed");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 22, 0, 9, "Developed");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 27, 1, 1, "X");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 31, 1, 2, "XP");
		fTestProjectSetup.editFile("TestJSClasses_1.js", 0, 1, 1, "X");
		fTestProjectSetup.editFile("TestJSClasses_0.js", 35, 0, 3, "Dev");
		fTestProjectSetup.editFile("TestJSClasses_1.js", 4, 1, 2, "XP");
		fTestProjectSetup.editFile("TestJSClasses_1.js", 8, 0, 3, "Dev");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelXeonProc1",
				"Computer.proc.IntelXeonProc2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 27, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.CeleronXSeries", "Developed.CorelXSoftware" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 29, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_ThisFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelXeonProc1",
				"Computer.proc.IntelXeonProc2" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 31, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_ThisFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer : {} - Global", "Computer.proc.IntelXeonProc1",
				"Computer.proc.IntelXeonProc2", "Computer.proc.CeleronXSeries" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 33, 4, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_ThisFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "Developed.CorelXSoftware", "Developed : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_0.js", 35, 3, expectedProposals);
	}

	@Ignore @Test
	public void testCameCasing_OtherFile_AfterEdit_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelXeonProc1 - Computer.proc",
				"Computer.proc.IntelXeonProc2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 0, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.CeleronXSeries - Computer.proc",
				"Developed.CorelXSoftware - Developed" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testCamelCasing_OtherFile_AfterEdit_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer.proc.IntelXeonProc1 - Computer.proc",
				"Computer.proc.IntelXeonProc2 - Computer.proc" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 4, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_AfterEdit_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "Computer : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 6, 4, expectedProposals);
	}

	@Ignore @Test
	public void testClassProperties_OtherFile_AfterEdit_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "Developed : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestJSClasses_1.js", 8, 3, expectedProposals);
	}
}