/*******************************************************************************
 * Copyright (c) 2009, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc. - refactoring
 *******************************************************************************/

package org.eclipse.wst.jsdt.ui.tests;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.ui.tests.contentassist.AllContentAssistTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AllContentAssistTests.class
})
public class JSDTUITests extends TestSuite {

	/**
	 * @param name
	 */
	public JSDTUITests(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	static {
		JavaScriptCore.getPlugin().getPluginPreferences().setValue("semanticValidation", true);
	}

	public static Test suite() {
		ArrayList standardTests = new ArrayList();

		// $JUnit-BEGIN$
		standardTests.add(EditorTests.class);
		// $JUnit-END$

		TestSuite all = new TestSuite("JSDT UI Tests");
		for(Iterator iter = standardTests.iterator(); iter.hasNext();) {
			Class test = (Class) iter.next();
			all.addTestSuite(test);
		}

		all.addTest(AllContentAssistTests.suite());
//		all.addTest(FormattingTests.suite());
//		all.addTest(new JUnit4TestAdapter(HyperLinkTest.class));
//		all.addTest(DocumentationTest.suite());
//		all.addTestSuite(NewJavaProjectPreferencePageTest.class);

		return all;
	}
}