/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Main test suite entry point
 * @since 1.0
 */
public class RhinoDebugTestSuite extends TestSuite {

	/**
	 * @return the new test suite
	 */
	public static Test suite() {
		return new RhinoDebugTestSuite();
	}

	/**
	 * Constructor
	 */
	public RhinoDebugTestSuite() {
		addTestSuite(TransportTest.class);
		addTestSuite(DebugSessionTest.class);
		addTestSuite(RhinoDebuggerTest.class);
		addTestSuite(RequestBadCommandTest.class);
		addTestSuite(BreakpointRequestTests.class);
		addTestSuite(ThreadRequestTests.class);
		addTestSuite(RequestContinueTest.class);
		addTestSuite(RequestDisposeTest.class);
		addTestSuite(FrameRequestTests.class);
		addTestSuite(RequestLookupTest.class);
		addTestSuite(ScriptRequestTests.class);
		addTestSuite(RequestSuspendTest.class);
		addTestSuite(RequestVersionTest.class);
	}
}
