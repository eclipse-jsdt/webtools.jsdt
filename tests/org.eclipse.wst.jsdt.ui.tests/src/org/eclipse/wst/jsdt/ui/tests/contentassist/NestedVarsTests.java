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
public class NestedVarsTests {
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
	public void testLocalVars_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"l1",
			"l2"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_0.js", 17, 1, expectedProposals, true, false);
	}

	@Test
	public void testLocalVars_SameFile2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"g1 - Global",
			"g2 - Global",
			"g3 - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_0.js", 18, 1, expectedProposals, false, false);
	}

	@Ignore @Test
	public void testLocalVars_OtherFile_NegativeTest() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"l1",
			"l2"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_1.js", 0, 1, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testLocalVars_OtherFile2() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"g1 : Number - Global",
			"g2 : Number - Global",
			"g3 : Number - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestNestedVars_1.js", 1, 1, expectedProposals, false, false);
	}
}