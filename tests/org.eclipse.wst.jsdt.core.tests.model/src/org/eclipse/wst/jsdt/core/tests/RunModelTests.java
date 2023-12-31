/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.jsdt.core.tests.model.JSDTModelTests;

/**
 * Runs all Java model tests.
 */
public class RunModelTests extends TestCase {
public RunModelTests(String name) {
	super(name);
}
public static Test suite() {
	TestSuite suite = new TestSuite(RunModelTests.class.getName());
	suite.addTest(JSDTModelTests.suite());
	return suite;
}
}

