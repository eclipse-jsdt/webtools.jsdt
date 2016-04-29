/*******************************************************************************
 * Copyright (c) 2011, 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("nls")
public class ObjectDeclaraionTest {
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
	public void testObjectFields_ExpressionStarted_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"field1",
			"field2",
			"field3",
			"funcField1(param1)",
			"funcField2(param2)"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestObjectLiteralDeclaration_0.js", 14, 5, expectedProposals, false, false);
	}

	@Test
	public void testVarNestedInObjectLiteralField_ExpressionStarted_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"objNestedVar"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestObjectLiteralDeclaration_0.js", 9, 25, expectedProposals, false, false);
	}
}