/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.preferences;

import junit.framework.TestCase;

public class NewJavaProjectPreferencePageTest extends TestCase {

	public void testEmptyExclusionPatternList() {
		assertTrue(NewJavaProjectPreferencePage.verifyExclusionPatternList(""));
	}

	public void testExclusionPatternListTwoEmptyItems() {
		assertFalse(NewJavaProjectPreferencePage.verifyExclusionPatternList(","));
	}

	public void testExclusionPatternListOneLastEmptyItem() {
		assertFalse(NewJavaProjectPreferencePage.verifyExclusionPatternList("script1,script2,"));
	}

	public void testExclusionPatternListOneMiddleEmptyItem() {
		assertFalse(NewJavaProjectPreferencePage.verifyExclusionPatternList("script1,,script2"));
	}

	public void testExclusionPatternListAbsolutePathItem() {
		assertFalse(NewJavaProjectPreferencePage.verifyExclusionPatternList("/script1/script2,script2"));
	}

	public void testExclusionPatternListDuplicatePathItem() {
		assertFalse(NewJavaProjectPreferencePage.verifyExclusionPatternList("script1,script1"));
	}

	public void testExclusionPatternListWithWildcards() {
		assertTrue(NewJavaProjectPreferencePage.verifyExclusionPatternList("**/script2/*"));
		assertTrue(NewJavaProjectPreferencePage.verifyExclusionPatternList("**/*"));
		assertTrue(NewJavaProjectPreferencePage.verifyExclusionPatternList("**/**"));
		NewJavaProjectPreferencePage.verifyExclusionPatternList("**/*.min.js");
		assertTrue(NewJavaProjectPreferencePage.verifyExclusionPatternList("**/*.min.js,**/script2/*,script"));
	}
}
