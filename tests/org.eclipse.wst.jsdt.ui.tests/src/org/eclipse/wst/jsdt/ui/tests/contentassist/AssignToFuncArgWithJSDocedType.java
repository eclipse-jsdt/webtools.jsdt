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
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class AssignToFuncArgWithJSDocedType {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName_OtherFile_BeforeOpen() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_1.js", 0, 8, expectedProposals);
	}

	@Ignore @Test
	public void test_AssignToFuncArgWithDocedType_InsideFunc0_CompleteOnArgumentField_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "arg0 : {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 6, 3, expectedProposals);
	}

	@Ignore @Test
	public void test_AssignToFuncArgWithDocedType_InsideFunc0_CompleteOnArgumentName_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "foo : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 7, 6, expectedProposals);
	}

	@Ignore @Test
	public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_0.js", 10, 8, expectedProposals);
	}

	@Ignore @Test
	public void test_AssignToFuncArgWithJSDocedType_OutsideFunc_ConpleteOnFuncName_AfterOpen() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"assignToFuncArgWithJSDocedType_0(String arg0) - Global"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "AssignToFuncArgWithJSDocedType_1.js", 0, 8, expectedProposals);
	}
}