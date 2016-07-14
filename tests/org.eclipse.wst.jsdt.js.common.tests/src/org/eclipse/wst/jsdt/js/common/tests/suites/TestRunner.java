/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.common.tests.suites;

import org.eclipse.wst.jsdt.js.common.tests.BuildSystemVisitorTest;
import org.eclipse.wst.jsdt.js.common.tests.NpmScriptsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@RunWith(Suite.class)
@SuiteClasses({ BuildSystemVisitorTest.class, NpmScriptsTest.class })
public class TestRunner {
	public static final String PLUGIN_ID = "org.eclipse.wst.jsdt.js.common.tests";
}