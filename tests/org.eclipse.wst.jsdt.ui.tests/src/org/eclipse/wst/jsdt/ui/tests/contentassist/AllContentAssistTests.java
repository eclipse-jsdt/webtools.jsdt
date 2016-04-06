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

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Test suite containing all JSDT content assist tests.
 * </p>
 */
public class AllContentAssistTests extends TestSuite {
	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "All Content Assist Tests";

	/**
	 * <p>
	 * Default constructor
	 * <p>
	 * <p>
	 * Use {@link #class}
	 * </p>
	 *
	 * @see #class
	 */
	public AllContentAssistTests() {
		this(TEST_NAME);
	}

	/**
	 * <p>
	 * Constructor that takes a test name.
	 * </p>
	 * <p>
	 * Use {@link #class}
	 * </p>
	 *
	 * @param name
	 *            The name this test run should have.
	 *
	 * @see #class
	 */
	public AllContentAssistTests(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite all = new TestSuite(TEST_NAME);

		all.addTest(new JUnit4TestAdapter(GlobalFunctionTests.class));
		all.addTest(new JUnit4TestAdapter(GlobalVariableTests.class));
		all.addTest(new JUnit4TestAdapter(ConstructorTests.class));
		all.addTest(new JUnit4TestAdapter(InnerFunctionTests.class));
		all.addTest(new JUnit4TestAdapter(DoublyNestedFunctionTests.class));
		all.addTest(new JUnit4TestAdapter(CamelCasingTests.class));
		all.addTest(new JUnit4TestAdapter(OtherContentAssistTests.class));
		all.addTest(new JUnit4TestAdapter(TypeTests.class));
		all.addTest(new JUnit4TestAdapter(DuplicatesTests.class));
		all.addTest(new JUnit4TestAdapter(StaticTests.class));
		all.addTest(new JUnit4TestAdapter(GlobalObjectLiteralsTests.class));
		all.addTest(new JUnit4TestAdapter(ClosureTests.class));
		all.addTest(new JUnit4TestAdapter(LocalVarDefinedInFunctionInObjectLiteralTests.class));
		all.addTest(new JUnit4TestAdapter(ProposalInfoTest.class));
		all.addTest(new JUnit4TestAdapter(BrowserLibraryTests.class));
		all.addTest(new JUnit4TestAdapter(NestedVarsTests.class));
		all.addTest(new JUnit4TestAdapter(GlobalVariable_LocalDeclaration_DefinedInOneFile_Tests.class));
		all.addTest(new JUnit4TestAdapter(GlobalVariable_LocalDeclaration_DefinedInMultiFiles_Tests.class));
		all.addTest(new JUnit4TestAdapter(GlobalVariable_AssignmentOnly_DefinedInOneFile_Tests.class));
		all.addTest(new JUnit4TestAdapter(FunctionPrototypeTests.class));
		all.addTest(new JUnit4TestAdapter(FunctionParamsTest.class));
		all.addTest(new JUnit4TestAdapter(FuncArgsWithFullyQualifedTypeNamesTests.class));
		all.addTest(new JUnit4TestAdapter(AssignToFuncArgWithJSDocedType.class));
		all.addTest(new JUnit4TestAdapter(OrderOfRecomendationsTests.class));
		all.addTest(new JUnit4TestAdapter(AlreadyDefinedFunctionAssingedToFieldTests.class));
		all.addTest(new JUnit4TestAdapter(GlobalsFieldAssignmentsTests.class));
		all.addTest(new JUnit4TestAdapter(GlobalShadowedByFuncArgTests.class));
		all.addTest(new JUnit4TestAdapter(AddToNavigatorTests.class));
		all.addTest(new JUnit4TestAdapter(Dom5LibraryTests.class));
		all.addTest(new JUnit4TestAdapter(NestedWithinParenthesesTests.class));
		all.addTest(new JUnit4TestAdapter(ArrayStylePropertyAccessTests.class));
		all.addTest(new JUnit4TestAdapter(ObjectDeclaraionTest.class));

		// tests that do editing to the files
		all.addTest(new JUnit4TestAdapter(GlobalFunctionTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(GlobalVariableTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(ConstructorTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(InnerFunctionTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(DoublyNestedFunctionTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(CamelCasingTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(TypeTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(StaticTests_Edited.class));
		all.addTest(new JUnit4TestAdapter(ProposalInfoTest_Edited.class));

		//delete the project after running all JSDT content assist tests
		return all;
	}
}