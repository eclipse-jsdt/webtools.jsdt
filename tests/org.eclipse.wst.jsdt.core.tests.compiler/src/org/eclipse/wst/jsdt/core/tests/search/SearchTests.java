/*******************************************************************************
 * Copyright (c) 2011,2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test suite for all JSDT Search Tests.<p>
 */
public class SearchTests extends TestSuite {
	/**
	 * <p>Default constructor</p>
	 */
	public SearchTests() {
		this("JavaScript Search Tests"); //$NON-NLS-1$
	}

	/**
	 * <p>Constructor with specified test name.</p>
	 *
	 * @param testName of this test suite
	 */
	public SearchTests(String testName) {
		super(testName);
	}
	
	public static Test suite() {
		TestSuite all = new TestSuite("JavaScript Search Tests"); //$NON-NLS-1$
		
		all.addTestSuite(TestMethodPattern.class);
		all.addTest(new JUnit4TestAdapter(TestConstructorSearch.class));
		all.addTest(new JUnit4TestAdapter(TestTypeDeclarationPattern.class));
		all.addTest(new JUnit4TestAdapter(TestMethodSearch.class));
		all.addTest(new JUnit4TestAdapter(TestFunctionSearch.class));
		all.addTest(new JUnit4TestAdapter(TestFieldSearch.class));
		all.addTest(new JUnit4TestAdapter(TestVarSearch.class));
		all.addTest(new JUnit4TestAdapter(TestGetAllSubtypeNames.class));
		return all;
	}
}
