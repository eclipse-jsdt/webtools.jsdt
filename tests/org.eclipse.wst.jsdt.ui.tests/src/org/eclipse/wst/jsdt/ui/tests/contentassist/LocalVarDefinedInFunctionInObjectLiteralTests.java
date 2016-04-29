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
public class LocalVarDefinedInFunctionInObjectLiteralTests {
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
	public void testLocalVarDefinedInFuncationInOBjectLiteral_OtherFile_BeforeOpen_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal : Number - Global", "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_1.js", 0, 3,
				unexpectedProposals, true, false);
	}

	@Test
	public void testLocalVarDefinedInFuncationInOBjectLiteral_ThisFile_InFunction() throws Exception {
		String[][] expectedProposals = new String[][] { { "ninjaLocal" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_0.js", 3, 5,
				expectedProposals);
	}

	@Test
	public void testLocalVarDefinedInFuncationInOBjectLiteral_ThisFile_OutsideFunction_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal - Global", "ninjaLocal" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_0.js", 7, 3,
				unexpectedProposals, true, false);
	}

	@Ignore @Test
	public void testLocalVarDefinedInFuncationInOBjectLiteral_OtherFile_AfterOpen_NegativeTest() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "ninjaLocal : Number - Global", "ninjaLocal : Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestLocalVarDefinedInFunctionInObjectLiteral_1.js", 0, 3,
				unexpectedProposals, true, false);
	}
}