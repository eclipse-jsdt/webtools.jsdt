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
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class AlreadyDefinedFunctionAssingedToFieldTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void testAlreadyDefinedFunctionAssingedToField_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"func7",
			"func8",
			"func9",
			"func7(test)",
			"func8(blarg)",
			"func9(foo)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_2.js", 0, 40,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testAlreadyDefinedFunctionAssingedToField_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"func7",
			"func8",
			"func9",
			"func7(test)",
			"func8(blarg)",
			"func9(foo)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_1.js", 6, 40,
				expectedProposals, false, true);
	}

	@Ignore @Test
	public void testAlreadyDefinedFunctionAssingedToField_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"func7",
			"func8",
			"func9",
			"func7(test)",
			"func8(blarg)",
			"func9(foo)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestAlreadyDefinedFunctionAssingedToField_2.js", 0, 40,
				expectedProposals, false, true);
	}
}