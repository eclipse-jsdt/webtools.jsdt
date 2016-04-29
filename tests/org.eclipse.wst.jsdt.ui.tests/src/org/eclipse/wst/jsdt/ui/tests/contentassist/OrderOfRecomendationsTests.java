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
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("nls")
public class OrderOfRecomendationsTests {
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
	public void testOrderOfRecomendations_OtherFile_BeforeOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"fooFunction() : String - {}",
			"fooField : Number - {}",
			"orderofrecomendations.fooType - orderofrecomendations"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "OrderOfRecomendations_1.js", 0, 22,
				expectedProposals, false, true, true);
	}

	@Ignore @Test
	public void testOrderOfRecomendations_SameFile_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"fooFunction() : String - {}",
			"fooField : Number - {}",
			"orderofrecomendations.fooType"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "OrderOfRecomendations_0.js", 12, 22,
				expectedProposals, false, true, true);
	}

	@Ignore @Test
	public void testOrderOfRecomendations_OtherFile_AfterOpen_0() throws Exception {
		String[][] expectedProposals = new String[][] { {
			"fooFunction() : String - {}",
			"fooField : Number - {}",
			"orderofrecomendations.fooType - orderofrecomendations"} };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "OrderOfRecomendations_1.js", 0, 22,
				expectedProposals, false, true, true);
	}
}