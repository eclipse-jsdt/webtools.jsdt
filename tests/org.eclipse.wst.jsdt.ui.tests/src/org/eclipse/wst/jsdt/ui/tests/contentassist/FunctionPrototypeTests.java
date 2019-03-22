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
public class FunctionPrototypeTests {
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
	public void testPrototypeFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - FunctionPrototype0", "getServerIP() - FunctionPrototype0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_0.js", 25, 19, expectedProposals);
	}

	@Ignore @Test
	public void testSimpleFunction_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype - FunctionPrototype0" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_0.js", 27, 10, expectedProposals);
	}

	@Ignore @Test
	public void testPrototypeFunction_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"prototype - Function",
			"getServerIP() - FunctionPrototype0"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_1.js", 0, 19, expectedProposals);
	}

	@Ignore @Test
	public void testSimpleFunction_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "prototype" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestFunctionPrototype_1.js", 2, 10, expectedProposals);
	}

	@Ignore @Test
	public void testNotGlobal_ThisFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP()", "getClientIP" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_0.js", 37, 0, expectedProposals, true, false);
	}

	@Ignore @Test
	public void testNotGlobal_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "getServerIP()", "getClientIP" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "StaticTests_1.js", 7, 0, expectedProposals, true, false);
	}
}