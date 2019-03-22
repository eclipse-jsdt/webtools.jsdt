/*******************************************************************************
 * Copyright (c) 2012, 2016 IBM Corporation and others.
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
public class FuncArgsWithFullyQualifedTypeNamesTests {
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
	public void testFindFuncWithFullyQualifiedArgTypeNames_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_1.js", 0, 5,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFuncWithFullyQualifiedArgTypeNames_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_0.js", 12, 5,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testFindFuncWithFullyQualifiedArgTypeNames_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
				"funcArgsWithFullyQualifedTypeNames(fully.Qualified blarg) - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFuncArgsWithFullyQualifedTypeNames_1.js", 0, 5,
				expectedProposals, false, true);
	}
}