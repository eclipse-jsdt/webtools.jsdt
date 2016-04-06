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
public class DuplicatesTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void testForDuplicates_Expression1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 17, 6);
	}

	@Ignore @Test
	public void testForDuplicates_Expression2() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 19, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression3() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_0.js", 21, 8);
	}

	@Ignore @Test
	public void testForDuplicates_Expression4() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_1.js", 2, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression5() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestConstructorCamelCase_1.js", 4, 8);
	}

	@Ignore @Test
	public void testForDuplicates_Expression6() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 2, 6);
	}

	@Ignore @Test
	public void testForDuplicates_Expression7() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 6, 10);
	}

	@Ignore @Test
	public void testForDuplicates_Expression8() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_1.js", 10, 5);
	}

	@Ignore @Test
	public void testForDuplicates_Expression9() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 19, 6);
	}

	@Ignore @Test
	public void testForDuplicates_Expression10() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 23, 9);
	}

	@Ignore @Test
	public void testForDuplicates_Expression11() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test2_0.js", 29, 5);
	}

	@Ignore @Test
	public void testForDuplicates_Expression12() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test6.js", 0, 8);
	}

	@Ignore @Test
	public void testForDuplicates_Expression13() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test7.js", 5, 8);
	}

	@Ignore @Test
	public void testForDuplicates_Expression14() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test9_1.js", 0, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression15() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test9_0.js", 7, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression16() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_1.js", 0, 6);
	}

	@Ignore @Test
	public void testForDuplicates_Expression17() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "ThisReferenceInMemberAndStaticFunctions_0.js", 9, 6);
	}

	@Ignore @Test
	public void testForDuplicates_InsideDoubleNestedFunctionDeclaration_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 4, 3);
	}

	@Ignore @Test
	public void testForDuplicates_InsideDoubleNestedFunctionDeclaration_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 5, 3);
	}

	@Ignore @Test
	public void testForDuplicates_InsideFunctionDeclaration_0() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 1, 2);
	}

	@Ignore @Test
	public void testForDuplicates_InsideFunctionDeclaration_1() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_0.js", 2, 2);
	}

	@Test
	public void testForDuplicates_Expression20() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test13_1.js", 4, 1);
	}

	@Ignore @Test // TODO: Halts tests?
	public void testForDuplicates_Expression21() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_0.js", 51, 1);
	}

	@Ignore @Test
	public void testForDuplicates_Expression22() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_0.js", 59, 4);
	}

	@Ignore @Test
	public void testForDuplicates_Expression23() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_1.js", 2, 3);
	}

	@Ignore @Test
	public void testForDuplicates_Expression24() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test0_1.js", 4, 5);
	}

	@Ignore @Test
	public void testForDuplicates_Expression26() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_0.js", 10, 1);
	}

	@Ignore @Test
	public void testForDuplicates_Expression27() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "TestNamedFunctionsAssignedToVariables_1.js", 2, 1);
	}

	@Ignore @Test
	public void testForDuplicates_Expression30() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test11_1.js", 3, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression31() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test11_0.js", 11, 7);
	}

	@Ignore @Test
	public void testForDuplicates_Expression39() throws Exception {
		ContentAssistTestUtilities.verifyNoDuplicates(fTestProjectSetup, "test10_1.js", 1, 4);
	}
}