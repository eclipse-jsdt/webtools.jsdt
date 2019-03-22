/*******************************************************************************
 * Copyright (c) 2011, 2016 IBM Corporation and others.
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
public class BrowserLibraryTests {
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
	public void testDocumentDotG() throws Exception {
		String[][] expectedProposals = new String[][] { { "getElementById(String elementId) : Element - Document",
				"getElementsByName(String elementName) : NodeList - HTMLDocument",
				"getElementsByTagName(String tagname) : NodeList - Document",
				"getElementsByTagNameNS(String namespaceURI, String localName) : NodeList - Document" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 0, 10, expectedProposals);
	}

	@Ignore @Test
	public void testAlert() throws Exception {
		String[][] expectedProposals = new String[][] { { "alert(String message) - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 2, 2, expectedProposals);
	}

	@Ignore @Test
	public void testDocument() throws Exception {
		String[][] expectedProposals = new String[][] { { "document : HTMLDocument - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 4, 3, expectedProposals);
	}

	@Ignore @Test
	public void testNavigator() throws Exception {
		String[][] expectedProposals = new String[][] { { "navigator : Navigator - Window" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 8, 3, expectedProposals);
	}

	@Ignore @Test
	public void testNavigatorDotA() throws Exception {
		String[][] expectedProposals = new String[][] { { "appName : String - Navigator",
				"appVersion : String - Navigator", "availHeight : Number - Navigator",
				"availWidth : Number - Navigator" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 6, 11, expectedProposals);
	}

	@Ignore @Test
	public void testNavigatorDotC_AfterJSDoc_InsideFunction() throws Exception {
	}

	@Ignore @Test
	public void testFindNewObjectOnNavigator_SameFile() throws Exception {
		String[][] expectedProposals = new String[][] { { "colorDepth : Number - Navigator",
				"constructor : Function - Object", "contacts : Contacts - Navigator", "cookieEnabled : Boolean - Navigator" } };
		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_0.js", 27, 11, expectedProposals);
	}

	//	public void testFindNewObjectOnNavigator_OtherFile() throws Exception {
	//		String[][] expectedProposals = new String[][] { { "colorDepth : Number - Navigator",
	//				"constructor : Function - Object", "contacts - Navigator", "cookieEnabled : Boolean - Navigator" } };
	//		ContentAssistTestUtilities.runProposalTest(fTestProjectSetup, "TestBrowserLibrary_1.js", 0, 11, expectedProposals);
	//	}
}