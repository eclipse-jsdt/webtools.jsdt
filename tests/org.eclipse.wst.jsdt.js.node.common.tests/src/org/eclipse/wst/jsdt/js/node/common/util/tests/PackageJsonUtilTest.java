/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.common.util.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.wst.jsdt.js.node.common.json.objects.PackageJson;
import org.eclipse.wst.jsdt.js.node.common.tests.utils.ResourceUtils;
import org.eclipse.wst.jsdt.js.node.common.util.PackageJsonUtil;
import org.junit.Test;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class PackageJsonUtilTest {
	//Resources
	public static final String RESOURCE_PACKAGE_JSON = "resources/package.json";

	//TEST VARIABLES
	public static final String NAME_ATTR = "name";
	public static final String EMAIL_ATTR = "email";
	public static final String FIRST_CONTRIBUTOR_NAME = "John Smith";
	public static final String FIRST_CONTRIBUTOR_EMAIL = "jsmith@gmail.com";
	public static final String SECOND_CONTRIBUTOR_NAME = "Chris Jones";
	public static final String SECOND_CONTRIBUTOR_EMAIL = "cjones@gmail.com";
	public static final String NAME_VALUE = "test name";
	public static final String AUTHOR_VALUE = "test author";
	public static final String DESCRIPTION_VALUE = "test description";
	public static final String LICENSE_VALUE = "test license";

	@Test
	public void testReadPackageJsonFromFileString() {
		try {
			File resourcePath = ResourceUtils.getResource(RESOURCE_PACKAGE_JSON);
			PackageJson packageJson = PackageJsonUtil.readPackageJsonFromFile(resourcePath.getAbsolutePath());

			//Asserts
			assertTrue("Package.json name attribute is wrong.", packageJson.getName().equals(NAME_VALUE));
			assertTrue("Package.json author attribute is wrong.", packageJson.getAuthor().equals(AUTHOR_VALUE));
			assertTrue("Package.json first contributor name is wrong.", packageJson.getContributors().get(0).get(NAME_ATTR).equals(FIRST_CONTRIBUTOR_NAME));
			assertTrue("Package.json first contributor email is wrong.", packageJson.getContributors().get(0).get(EMAIL_ATTR).equals(FIRST_CONTRIBUTOR_EMAIL));
			assertTrue("Package.json second contributor name is wrong.", packageJson.getContributors().get(1).get(NAME_ATTR).equals(SECOND_CONTRIBUTOR_NAME));
			assertTrue("Package.json second contributor email is wrong.", packageJson.getContributors().get(1).get(EMAIL_ATTR).equals(SECOND_CONTRIBUTOR_EMAIL));
			assertTrue("Package.json description attribute is wrong.", packageJson.getDescription().equals(DESCRIPTION_VALUE));
			assertTrue("Package.json license attribute is wrong.", packageJson.getLicense().equals(LICENSE_VALUE));
			assertTrue("Package.json private attribute is wrong.", packageJson.getIsPrivate() == true);
		} catch (IOException e) {
			fail("Could not read file.");
		}
	}
}
