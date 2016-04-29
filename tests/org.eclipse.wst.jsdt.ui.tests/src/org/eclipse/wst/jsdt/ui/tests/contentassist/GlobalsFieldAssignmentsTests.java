/*******************************************************************************
 * Copyright (c) 2012, 2016 IBM Corporation and others.
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
public class GlobalsFieldAssignmentsTests {
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

	@Test
	public void testFindGlobalsDefinedFromFieldAssignments_0_1() throws Exception {
		String[][] expectedProposals =
				new String[][] { {
						"global_FieldAssignments0 - Global",
						"global_FieldAssignments2 - Global",
						"global_FieldAssignments3 - Global",
						"global_FieldAssignments5 - Global",
						"global_FieldAssignments6 - Global",
						"global_FieldAssignments7 - Global",
						"global_FieldAssignments8 - Global"
				} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_1.js", 0, 18, expectedProposals,
				false, true);
	}

	@Test
	public void testFindGlobalsDefinedFromFieldAssignments_0_2() throws Exception {
		String[][] expectedProposals =
				new String[][] { {
						"global_FieldAssignments0 - Global",
						"global_FieldAssignments2 - Global",
						"global_FieldAssignments3 - Global",
						"global_FieldAssignments5 - Global",
						"global_FieldAssignments6 - Global",
						"global_FieldAssignments7 - Global",
						"global_FieldAssignments8 - Global"
				} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 26, 18, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments0_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 28, 25, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_3() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments2_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 34, 25, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_4() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments2_0_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 36, 52, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_5() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments3_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 38, 25, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_8() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments5_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 46, 25, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_9() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments5_0_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 48, 52, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_10() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments6_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 50, 25, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_11() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments7_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 54, 25, expectedProposals,
				false, true);
	}

	@Ignore @Test
	public void testFindFieldsAssignedToGlobals_12() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments7_0_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 56, 52, expectedProposals,
				false, true);
	}

	@Test
	public void testFindFieldsAssignedToGlobals_13() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments8_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 58, 25, expectedProposals,
				false, true);
	}

	@Ignore @Test
	public void testFindFieldsAssignedToGlobals_14() throws Exception {
		String[][] expectedProposals = new String[][] { { "global_FieldAssignments8_0_0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_0.js", 60, 52, expectedProposals,
				false, true);
	}

	@Test
	public void testFindGlobalsDefinedFromFieldAssignments_0() throws Exception {
		String[][] expectedProposals =
				new String[][] { {
						"global_FieldAssignments0 - Global",
						"global_FieldAssignments2 - Global",
						"global_FieldAssignments3 - Global",
						"global_FieldAssignments5 - Global",
						"global_FieldAssignments6 - Global",
						"global_FieldAssignments7 - Global",
						"global_FieldAssignments8 - Global"
				} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobals_FieldAssignments_1.js", 0, 18, expectedProposals,
				false, true);
	}
}