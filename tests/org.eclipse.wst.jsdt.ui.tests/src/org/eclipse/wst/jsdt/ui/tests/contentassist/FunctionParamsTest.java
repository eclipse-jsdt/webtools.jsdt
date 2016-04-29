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
public class FunctionParamsTest {
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
	public void testFindParamInFunctionAsLocal() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_0.js", 2, 0, expectedProposals);
	}

	@Test
	public void testDoNotFindParamOutsideFunctionAsGlobalSameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_0.js", 5, 28, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testDoNotFindParamOutsideFunctionAsGlobalOtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "testfunctionParam_Fun1_param1" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionParams_1.js", 0, 28, expectedProposals, true, false);
	}
}