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
public class GlobalVariable_LocalDeclaration_DefinedInOneFile_Tests {
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
	public void testFindGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile0 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile1 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile2 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile3 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile4 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile5 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile6 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile7 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile8 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile9 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile10 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile11 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile12 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 0, 4,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile0_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 2, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile1_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 4, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile2_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 6, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 8, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile4_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 10, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile5_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 12, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 14, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile7_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 16, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile8_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 18, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile9_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 20, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 22, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile11_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 24, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile12_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 26, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile13_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 28, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_WI94156_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 121, 40,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_WI94156_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 124, 17,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_WI94156_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 30, 17,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_BeforeOpen_WI94156_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 32, 17,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindGlobalVariables_DefinedInOneFile_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile0 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile1 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile2 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile3 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile4 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile5 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile6 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile7 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile8 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile9 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile10 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile11 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile12 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 90, 4,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile0_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 92, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile1_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 94, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile2_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 96, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 98, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile4_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 100, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile5_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 102, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 104, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile7_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 106, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile8_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 108, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile9_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 110, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 112, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile11_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 114, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile12_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 116, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_SameFile_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile13_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 118, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile0 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile1 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile2 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile3 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile4 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile5 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile6 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile7 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile8 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile9 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile10 : String - Global",
				"global_LocalDeclaration_DeffinedInOneFile11 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile12 : {} - Global",
				"global_LocalDeclaration_DeffinedInOneFile13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 0, 4,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile0_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 2, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile1_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 4, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile2_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 6, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile3_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 8, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_LocalDeclaration_DeffinedInOneFile4_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 10, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInOneFile5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile5_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 12, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 14, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile7_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 16, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile8_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 18, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile9_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 20, 43,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 22, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile11_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 24, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile12_0 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 26, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInOneFile13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInOneFile13_1 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 28, 44,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_WI94156_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 121, 40,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_WI94156_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_0.js", 124, 17,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_WI94156_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 30, 17,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInOneFile_OtherFile_AfterOpen_WI94156_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "getDay() : Number - Date"}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInOneFile_1.js", 32, 17,
				expectedProposals, false, true);
	}
}