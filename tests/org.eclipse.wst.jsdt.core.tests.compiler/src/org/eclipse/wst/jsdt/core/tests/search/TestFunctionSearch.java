/*******************************************************************************
 * Copyright (c) 2012,2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;
import static org.junit.Assert.*;

import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.junit.Test;

public class TestFunctionSearch extends AbstractSearchTest {

	@Test
	public void testDeclarationSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js"},
			new String[] {
			"function " + getName() + "() {};\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	@Test
	public void testDeclarationSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName().toUpperCase(), 
			new String[] {"X.js"},
			new String[] {
			"var " + getName().toUpperCase() + " = function() {};\n" + 
			"function " + getName() + "XYZ() {};\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	@Test
	public void testDeclarationSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testdecl*", 
			new String[] {"X.js"},
			new String[] {
			"var " + getName() + " = function() {};\n" + 
			"function " + getName() + "XYZ() {};\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	@Test
	public void testReferencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			getName(), 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"function " + getName() + "() {};\n" +
			"function " + getName() + "XYZ() {};\n",
			getName() + "();\n" +
			getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_EXACT_MATCH);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	@Test
	public void testReferencesSearch02() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"testRef*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = function() {};\n" + 
			"function " + getName() + "XYZ() {};\n",
			getName() + "();\n" +
			getName() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 2, results.length);
	}
	
	@Test
	public void testReferencesSearch03() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
			"TESTREF*", 
			new String[] {"X.js", "Y.js"},
			new String[] {
			"var " + getName() + " = function() {};\n" + 
			"function " + getName().toUpperCase() + "XYZ() {};\n",
			getName() + "();\n" +
			getName().toUpperCase() + "XYZ();\n"
			}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.REFERENCES, SearchPattern.R_PATTERN_MATCH | SearchPattern.R_CASE_SENSITIVE);
		assertEquals("wrong number of files containing references found", 1, results.length);
	}
	
	@Test
	public void testOccurrencesSearch01() throws Exception {
		SearchMatch[] results = runSearchTest(getName(),
				"TESTOCC*", 
				new String[] {"X.js", "Y.js"},
				new String[] {
				"var " + getName() + " = function() {};\n" + 
				"function " + getName() + "ABC() {};\n" +
				"function " + getName().toUpperCase() + "XYZ() {};\n",
				getName() + "();\n" +
				getName().toUpperCase() + "XYZ();\n"
				}, 
			IJavaScriptSearchConstants.FUNCTION, IJavaScriptSearchConstants.ALL_OCCURRENCES, SearchPattern.R_PATTERN_MATCH);
		assertEquals("wrong number of files containing references found", 5, results.length);
	}
}
