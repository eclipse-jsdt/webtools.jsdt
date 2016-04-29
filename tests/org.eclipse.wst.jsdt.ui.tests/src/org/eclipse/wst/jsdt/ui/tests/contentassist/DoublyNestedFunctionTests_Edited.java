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
public class DoublyNestedFunctionTests_Edited {
	private static TestProjectSetup fTestProjectSetup;

	@Ignore @BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
		editFile_test13_0();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	public static void editFile_test13_0() throws Exception {
		fTestProjectSetup.editFile("test13_0.js", 8, 11, 6, "edited");
		fTestProjectSetup.editFile("test13_0.js", 0, 9, 9, "editedFunc");
	}

	@Ignore @Test
	public void testFindInnerFunctions2_ThisFile_AfterEdit_Expression2() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global", "editedInnerFunc()", "innerFunc()" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 7, 0, expectedProposals);
	}

	@Ignore @Test
	public void testMustFail_InnerFunctions2_ThisFile_AfterEdit_Expression1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "editedInnerFunc()", "localInnerFunc",
				"localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_0.js", 2, 0, unexpectedProposals, true, false);
	}

	@Ignore @Test
	public void testFindInnerFunctions2_OtherFile_AfterEdit_ExpressionNotStarted() throws Exception {
		String[][] expectedProposals = new String[][] { { "editedFunc() - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testMustFail_FindInnerFunctions2_OtherFile_AfterEdit_ExpressionStarted_1() throws Exception {
		String[][] unexpectedProposals = new String[][] { { "innerFunc()", "editedInnerFunc()",
				"localInnerFunc : Function", "localInnerFunc(param1)" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test13_1.js", 4, 1, unexpectedProposals, true, false);
	}
}