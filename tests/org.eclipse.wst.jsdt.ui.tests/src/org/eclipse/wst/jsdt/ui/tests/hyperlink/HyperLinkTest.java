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
package org.eclipse.wst.jsdt.ui.tests.hyperlink;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


@SuppressWarnings("nls")
public class HyperLinkTest {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@Ignore @Test
	public void testHyperlink_0() throws Exception {
		ArrayList<String> expectedFiles = new ArrayList<String>();
		expectedFiles.add("HyperLink_0.js");
		HyperLinkTestUtilities.checkHyperlink(fTestProjectSetup, 1, 15, "HyperLink_1.js", expectedFiles);
	}
}