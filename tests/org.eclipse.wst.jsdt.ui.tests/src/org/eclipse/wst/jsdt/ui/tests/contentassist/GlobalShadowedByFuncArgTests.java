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
public class GlobalShadowedByFuncArgTests {
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
	public void testGlobalShadowedByFuncArgTests_OtherFile_BeforeOpen() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"globalShadowedByFuncArg - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalShadowedByFuncArg_1.js", 0, 9,
				expectedProposals, false, true);
	}

	@Test
	public void testGlobalShadowedByFuncArgTests_InsideFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"globalShadowedByFuncArg"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalShadowedByFuncArg_0.js", 4, 10,
				expectedProposals, false, true);
	}

	@Test
	public void testGlobalShadowedByFuncArgTests_OutsideFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"globalShadowedByFuncArg - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalShadowedByFuncArg_0.js", 7, 9,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testGlobalShadowedByFuncArgTests_OtherFile_AfterOpen() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"globalShadowedByFuncArg : Number - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalShadowedByFuncArg_1.js", 0, 9,
				expectedProposals, false, true);
	}

}