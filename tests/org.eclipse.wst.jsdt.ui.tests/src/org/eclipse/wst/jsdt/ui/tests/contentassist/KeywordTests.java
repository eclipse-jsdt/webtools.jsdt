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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class KeywordTests {
	private static final Set<String> keywords = new HashSet<String>(27);
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();

		keywords.add("break");
		keywords.add("case");
		keywords.add("catch");
		keywords.add("class");
		keywords.add("const");
		keywords.add("continue");
		keywords.add("debugger");
		keywords.add("default");
		keywords.add("delete");
		keywords.add("do");
		keywords.add("else");
		keywords.add("elseif");
		keywords.add("export");
		keywords.add("extends");
		keywords.add("finally");
		keywords.add("for");
		keywords.add("function");
		keywords.add("if");
		keywords.add("import");
		keywords.add("in");
		keywords.add("instanceof");
		keywords.add("let");
		keywords.add("new");
		keywords.add("return");
		keywords.add("super");
		keywords.add("static");
		keywords.add("switch");
		keywords.add("this");
		keywords.add("throw");
		keywords.add("try");
		keywords.add("typeof");
		keywords.add("var");
		keywords.add("void");
		keywords.add("while");
		keywords.add("with");
		keywords.add("yield");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testSingleLetterKeywordCompletionLowercase() throws Exception {
		for (int i = 0; i < 26; i++) {
			char ch = (char) ('a' + i);
			String[][] expectedKeywords =  { (String[]) keywords.stream().filter(k -> k.charAt(0) == ch).collect(Collectors.toList()).toArray() };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestKeywordsSingleLetterLowerCase.js", i, 1, expectedKeywords);
		}

	}

	@Ignore @Test
	public void testSingleLetterKeywordCompletionUppercase() throws Exception {
		for (int i = 0; i < 26; i++) {
			char ch = (char) ('a' + i);
			String[][] expectedKeywords =  { (String[]) keywords.stream().filter(k -> k.charAt(0) == ch).collect(Collectors.toList()).toArray() };
			ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestKeywordsSingleLetterUpperCase.js", i, 1, expectedKeywords);
		}

	}

}
