/*******************************************************************************
 * Copyright (c) 2013, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class ArrayStylePropertyAccessTests {
	private static TestProjectSetup fTestProjectSetup;

	@BeforeClass
	public static void setup() throws Exception {
		fTestProjectSetup = new TestProjectSetup("ContentAssist", "root", false);
		fTestProjectSetup.setUp();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		fTestProjectSetup.tearDown();
	}

	@Ignore @Test
	public void testPropertyAccessUsingArrayStyle_377241() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][] {{
			"id : Number - bar"
		}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "test377241.js", 5, 10, expectedProposals);
	}

	@Ignore @Test
	public void testPropertyAccessUsingArrayStyle_377241_01() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][] {{
			"id : Number - bar"
		}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_01.js", 5, 10, expectedProposals);
	}

	@Ignore @Test
	public void testPropertyAccessUsingArrayStyle_377241_02() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][] {{
			"id : Number - bar"
		}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_02.js", 5, 10, expectedProposals);
	}

	@Ignore @Test
	public void testPropertyAccessUsingArrayStyle_377241_03() throws Exception {
		// When an object property is referenced by array-style, test if
		// ContentAssist lists are properly show.
		String[][] expectedProposals = new String[][] {{
			"id : Number - {}",
			"data : String - {}"
		}};
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup,"test377241_03.js", 2, 10, expectedProposals);
	}
}