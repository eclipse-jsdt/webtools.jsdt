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
public class GlobalVariable_LocalDeclaration_DefinedInMultiFiles_Tests {
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
	public void testFindGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles1 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles2 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles3 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles4 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles5 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles6 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles7 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles8 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles9 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles10 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles11 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles12 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 0, 35,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 2, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 4, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 6, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_4 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_5 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 8, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_4() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles4_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 10,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 12,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles6_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 14,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles7_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 16,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles8_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 18,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 20,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles10_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 22,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles11_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 24,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles12_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 26,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_BeforeOpen_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 28,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindGlobalVariables_DefinedInMultiFiles_DefineFile0_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles1 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles2 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles3 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles4 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles5 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles6 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles7 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles8 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles9 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles10 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles11 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles12 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 90,
				35, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 92,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 94,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 96,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_4 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_5 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 98,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_4() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles4_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 100,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 102,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles6_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 104,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles7_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 106,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles8_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 108,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 110,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles10_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 112,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles11_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 114,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles12_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 116,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile0_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_0.js", 118,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindGlobalVariables_DefinedInMultiFiles_DefineFile1_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles1 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles2 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles3 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles4 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles5 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles6 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles7 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles8 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles9 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles10 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles11 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles12 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 140,
				35, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 142,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 144,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 146,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_4 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_5 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 148,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_4() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles4_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 150,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 152,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles6_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 154,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles7_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 156,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles8_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 158,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 160,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles10_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 162,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles11_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 164,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles12_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 166,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_DefineFile1_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_1.js", 168,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles1 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles2 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles3 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles4 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles5 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles6 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles7 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles8 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles9 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles10 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles11 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles12 : {} - Global",
				"global_LocalDeclaration_DeffinedInMultiFiles13 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 0, 35,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles0_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles0_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 2, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_1() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles1_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles1_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 4, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_2() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles2_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles2_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 6, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_3() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles3_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_4 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles3_5 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 8, 46,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_4() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles4_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles4_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 10,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_5() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"global_LocalDeclaration_DeffinedInMultiFiles5_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles5_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 12,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_6() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles6_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles6_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 14,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_7() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles7_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles7_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 16,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles8_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles8_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 18,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles9_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles9_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 20,
				46, expectedProposals, false, true);
	}

	@Ignore @Test
	public void _testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles10_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles10_2 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 22,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles11_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles11_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 24,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles12_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles12_3 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 26,
				47, expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFieldsOnGlobalVariables_DefinedInMultiFiles_NoDefinesFile_AfterOpen_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String",
				"global_LocalDeclaration_DeffinedInMultiFiles13_0 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_1 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_2 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_3 : Number - {}",
				"global_LocalDeclaration_DeffinedInMultiFiles13_4 : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_LocalDeclaration_DeffinedInMultiFiles_2.js", 28,
				47, expectedProposals, false, true);
	}

}