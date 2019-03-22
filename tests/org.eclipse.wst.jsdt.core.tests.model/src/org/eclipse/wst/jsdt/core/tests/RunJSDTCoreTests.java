/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.tests.utils.SequenceReaderTests;

/**
 * Runs all JDT Core tests.
 */
public class RunJSDTCoreTests extends TestCase {
public RunJSDTCoreTests(String name) {
	super(name);
}
public static Test suite() {
	TestSuite suite = new TestSuite("JSDT 'Model' Tests"); //$NON-NLS-1$
	suite.addTest(RunDOMTests.suite());
	suite.addTest(RunFormatterTests.suite());
	suite.addTest(RunModelTests.suite());
	suite.addTestSuite(SequenceReaderTests.class);
	suite.addTest(new JUnit4TestAdapter(RunJSRuntimeTests.class));
	return suite;
}
}

