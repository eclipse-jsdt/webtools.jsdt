/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
public class NestedWithinParenthesesTests {
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
	public void testInsertToEditor_315660_1() throws Exception {
		String expectedResult =
				"var data1;\n" +
				"addEventListener(type, listener, useCapture)" ;
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_1.js", 1, 0, expectedResult);
	}

	@Ignore @Test
	public void testInsertToEditor_315660_2() throws Exception {
		String expectedResult =
				"var data1;\n" +
				"var data2;\n" +
				"(data1)";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_2.js", 2, 5, expectedResult);
	}

	@Ignore @Test
	public void testInsertToEditor_315660_3() throws Exception {
		String expectedResult =
				"var myData1;\n"+
				"var myData2;\n"+
				"(\n"+
				"	// myData\n"+
				"	(\n"+
				"        myData1	\n"+
				"	)\n"+
				");";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_3.js", 5, 12, expectedResult);
	}

	@Ignore @Test
	public void testInsertToEditor_315660_4() throws Exception {
		String expectedResult =
				"var data1;\n" +
				"var data2;\n" +
				"((10+data1));";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_4.js", 2, 7, expectedResult);
	}

	@Ignore @Test
	public void testInsertToEditor_315660_5() throws Exception {
		String expectedResult =
				"var point = new Object();\n"+
				"point.x = 2.3;\n"+
				"point.y = -1.2;\n"+
				"var square = {upperLeft: {x:point.x, y:point.y}, upperRight: {x:(parseFloat(s))} };";
		ContentAssistTestUtilities.runProposalandInertTest(fTestProjectSetup, "validateTest_315660_5.js", 3, 66, expectedResult);
	}

}
