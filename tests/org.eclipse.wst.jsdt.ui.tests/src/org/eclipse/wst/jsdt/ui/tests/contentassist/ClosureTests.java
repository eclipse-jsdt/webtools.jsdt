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
public class ClosureTests {
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
	public void testClosures_OtherFile_BeforeOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_OtherFile_BeforeOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 2, 3, expectedProposals);
	}

	@Ignore @Test
	public void _testClosures_OtherFile_BeforeOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 4, 9, expectedProposals);
	}

	@Ignore @Test
	public void _testClosures_OtherFile_BeforeOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 6, 10, expectedProposals);
	}

	@Test
	public void testClosures_SameFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"closure - Global",
			"closure2 - Global"
		} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 12, 0, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_SameFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 14, 3, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_SameFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 16, 9, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_SameFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_0.js", 18, 10, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_OtherFile_AfterOpen_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 0, 0, expectedProposals);
	}

	@Ignore @Test
	public void testClosures_OtherFile_AfterOpen_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] { { "closure : {} - Global", "closure2 : {} - Global" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 2, 3, expectedProposals);
	}

	@Ignore @Test
	public void _testClosures_OtherFile_AfterOpen_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "nifty : Number - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 4, 9, expectedProposals);
	}

	@Ignore @Test
	public void _testClosures_OtherFile_AfterOpen_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "burg : String - {}" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestGlobalVarsDefinedInClosure_1.js", 6, 10, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Var() throws Exception {
		String[][] expectedProposals = new String[][] { { "NaN : Number - Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 20, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Assign() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 22, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Var_function() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 24, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_InsideOtherClosure() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_0.js", 26, 20, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Var_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "NaN : Number - Number" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 0, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Assign_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "charAt(Number position) : String - String" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 2, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	@Ignore @Test
	public void testClosures_ReturnValue_Var_function_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 4, 18, expectedProposals);
	}

	//WI97137 - JSDT: Closure functions need to pass back their return value
	public void testClosures_ReturnValue_InsideOtherClosure_OtherFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "call(Object thisObject, Object args) : Object - Function" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosureReturnValue_1.js", 6, 20, expectedProposals);
	}

	// WI97001 - tests for WI97000 - we need to be able to determine the types of parameters passed into a closure function
	@Ignore @Test
	public void testClosure_ArgumentTypeAddedTo_1() throws Exception {
		String[][] expectedProposals = new String[][] { { "TestClosure_ArgumentTypeAddedTo_1 : Number - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosure_ArgumentTypeAddedTo_1.js", 3, 7, expectedProposals);
	}

	// WI97001 - tests for WI97000 - we need to be able to determine the types of parameters passed into a closure function
	@Ignore @Test
	public void testClosure_ArgumentTypeAddedTo_2() throws Exception {
		String[][] expectedProposals = new String[][] { { "TestClosure_ArgumentTypeAddedTo_2 : Number - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestClosure_ArgumentTypeAddedTo_2.js", 3, 7, expectedProposals);
	}
}