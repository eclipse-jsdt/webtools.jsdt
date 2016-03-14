/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc. 

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

public class KeywordTests extends TestCase {
	private static final Set<String> keywords = new HashSet<String>(27);

	/**
	 * <p>
	 * This tests name
	 * </p>
	 */
	private static final String TEST_NAME = "Test Keywords JavaScript Content Assist";

	/**
	 * <p>
	 * Test project setup for this test.
	 * </p>
	 */
	private static TestProjectSetup fTestProjectSetup;

	/**
	 * <p>
	 * Default constructor
	 * <p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @see #suite()
	 */
	public KeywordTests() {
		super(TEST_NAME);
	}

	/**
	 * <p>
	 * Constructor that takes a test name.
	 * </p>
	 * <p>
	 * Use {@link #suite()}
	 * </p>
	 * 
	 * @param name
	 *            The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public KeywordTests(String name) {
		super(name);
	}

	/**
	 * <p>
	 * Use this method to add these tests to a larger test suite so set up and tear down can be
	 * performed
	 * </p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 *         with set up and tear down.
	 */

	public static Test suite() {
		TestSuite ts = new TestSuite(GlobalFunctionTests.class, TEST_NAME);

		fTestProjectSetup = new TestProjectSetup(ts, "ContentAssist", "root", false);

		keywords.add("break");      //$NON-NLS-1$
		keywords.add("case");       //$NON-NLS-1$
		keywords.add("catch");      //$NON-NLS-1$
		keywords.add("class");      //$NON-NLS-1$
		keywords.add("const");      //$NON-NLS-1$
		keywords.add("continue");   //$NON-NLS-1$
		keywords.add("debugger");   //$NON-NLS-1$
		keywords.add("default");    //$NON-NLS-1$
		keywords.add("delete");     //$NON-NLS-1$
		keywords.add("do");         //$NON-NLS-1$
		keywords.add("else");       //$NON-NLS-1$
		keywords.add("elseif");     //$NON-NLS-1$
		keywords.add("export");     //$NON-NLS-1$
		keywords.add("extends");    //$NON-NLS-1$
		keywords.add("finally");    //$NON-NLS-1$
		keywords.add("for");        //$NON-NLS-1$
		keywords.add("function");   //$NON-NLS-1$
		keywords.add("if");         //$NON-NLS-1$
		keywords.add("import");     //$NON-NLS-1$
		keywords.add("in");         //$NON-NLS-1$
		keywords.add("instanceof"); //$NON-NLS-1$
		keywords.add("let");        //$NON-NLS-1$
		keywords.add("new");        //$NON-NLS-1$
		keywords.add("return");     //$NON-NLS-1$
		keywords.add("super");      //$NON-NLS-1$
		keywords.add("static");     //$NON-NLS-1$
		keywords.add("switch");     //$NON-NLS-1$
		keywords.add("this");       //$NON-NLS-1$
		keywords.add("throw");      //$NON-NLS-1$
		keywords.add("try");        //$NON-NLS-1$
		keywords.add("typeof");     //$NON-NLS-1$
		keywords.add("var");        //$NON-NLS-1$
		keywords.add("void");       //$NON-NLS-1$
		keywords.add("while");      //$NON-NLS-1$
		keywords.add("with");       //$NON-NLS-1$
		keywords.add("yield");      //$NON-NLS-1$

		return fTestProjectSetup;
	}

	public void testSingleLetterKeywordCompletionLowercase() throws Exception {
		for (int i = 0; i < 26; i++) {
			char ch = (char) ('a' + i);
			String[][] expectedKeywords =  { (String[]) keywords.stream().filter(k -> k.charAt(0) == ch).collect(Collectors.toList()).toArray() };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestKeywordsSingleLetterLowerCase.js", i, 1, expectedKeywords);
		}

	}

	public void testSingleLetterKeywordCompletionUppercase() throws Exception {
		for (int i = 0; i < 26; i++) {
			char ch = (char) ('a' + i);
			String[][] expectedKeywords =  { (String[]) keywords.stream().filter(k -> k.charAt(0) == ch).collect(Collectors.toList()).toArray() };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestKeywordsSingleLetterUpperCase.js", i, 1, expectedKeywords);
		}

	}

}
