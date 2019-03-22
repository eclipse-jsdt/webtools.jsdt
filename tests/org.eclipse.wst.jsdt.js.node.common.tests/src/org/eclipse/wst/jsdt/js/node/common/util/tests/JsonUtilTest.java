/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
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
import org.eclipse.wst.jsdt.js.node.common.util.JsonUtil;
import org.junit.Test;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class JsonUtilTest {
	//Resources
	public static final String RESOURCE_PACKAGE_JSON = "resources/package.json";
	public static final String RESOURCE_PACKAGE_JSON_TEST = "/package.json_test";

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

	public static final String NAME_EXPECTED = "\"name\": \"" + NAME_VALUE + "\"";
	public static final String AUTHOR_EXPECTED = "\"author\": \"" + AUTHOR_VALUE + "\"";
	public static final String FIRST_NAME_CONTRIBUTOR_EXPECTED = "\"name\": \"" + FIRST_CONTRIBUTOR_NAME + "\"";
	public static final String FIRST_EMAIL_CONTRIBUTOR_EXPECTED = "\"email\": \"" + FIRST_CONTRIBUTOR_EMAIL + "\"";
	public static final String SECOND_NAME_CONTRIBUTOR_EXPECTED = "\"name\": \"" + SECOND_CONTRIBUTOR_NAME + "\"";
	public static final String SECOND_EMAIL_CONTRIBUTOR_EXPECTED = "\"email\": \"" + SECOND_CONTRIBUTOR_EMAIL + "\"";
	public static final String DESCRIPTION_EXPECTED = "\"description\": \"" + DESCRIPTION_VALUE + "\"";
	public static final String LICENSE_EXPECTED = "\"license\": \"" + LICENSE_VALUE + "\"";
	public static final String PRIVATE_EXPECTED = "\"private\": true";

	@Test
	public void testReadJsonFromFileStringClassOfT() {
		try {
			File resourcePath = ResourceUtils.getResource(RESOURCE_PACKAGE_JSON);
			PackageJson packageJson = JsonUtil.readJsonFromFile(resourcePath.getAbsolutePath(), PackageJson.class);

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

	@Test
	public void testWriteJsonToFile() {
		File file = null;
		try {
			File resourcePath = ResourceUtils.getResource(RESOURCE_PACKAGE_JSON);
			file = new File(resourcePath.getParent() + RESOURCE_PACKAGE_JSON_TEST);
			PackageJson packageJson = JsonUtil.readJsonFromFile(resourcePath.getAbsolutePath(), PackageJson.class);

			//writeJsonToFile(File fileDestination, Object jsonJavaObject)
			JsonUtil.writeJsonToFile(file, packageJson);

			//Assert file exists
			assertTrue(RESOURCE_PACKAGE_JSON_TEST + "does not exists.", file.exists());

			//Delete file
			file.delete();

			//Assert file does not exists
			assertTrue(RESOURCE_PACKAGE_JSON_TEST + "exists.", !file.exists());

			//writeJsonToFile(String fileDestination, Object jsonJavaObject)
			JsonUtil.writeJsonToFile(file.getAbsolutePath(), packageJson);

			//Assert file exists
			assertTrue(RESOURCE_PACKAGE_JSON_TEST + "does not exists.", file.exists());

			//Delete file
			file.delete();

			//Assert file does not exists
			assertTrue(RESOURCE_PACKAGE_JSON_TEST + "exists.", !file.exists());
		} catch (IOException e) {
			fail("Could not read file.");
		}
	}

	@Test
	public void testConvertJavaObjectToJson() {
		try {
			File resourcePath = ResourceUtils.getResource(RESOURCE_PACKAGE_JSON);
			PackageJson packageJson = JsonUtil.readJsonFromFile(resourcePath.getAbsolutePath(), PackageJson.class);

			//Convert PackageJson object to string
			String json = JsonUtil.convertJavaObjectToJson(packageJson);

			//Asserts
			assertTrue("Package.json name attribute is wrong.", json.contains(NAME_EXPECTED));
			assertTrue("Package.json author attribute is wrong.", json.contains(AUTHOR_EXPECTED));
			assertTrue("Package.json first contributor name is wrong.", json.contains(FIRST_NAME_CONTRIBUTOR_EXPECTED));
			assertTrue("Package.json first contributor email is wrong.", json.contains(FIRST_EMAIL_CONTRIBUTOR_EXPECTED));
			assertTrue("Package.json second contributor name is wrong.", json.contains(SECOND_NAME_CONTRIBUTOR_EXPECTED));
			assertTrue("Package.json second contributor email is wrong.", json.contains(SECOND_EMAIL_CONTRIBUTOR_EXPECTED));
			assertTrue("Package.json description attribute is wrong.", json.contains(DESCRIPTION_EXPECTED));
			assertTrue("Package.json license attribute is wrong.", json.contains(LICENSE_EXPECTED));
			assertTrue("Package.json private attribute is wrong.", json.contains(PRIVATE_EXPECTED));
		} catch (IOException e) {
			fail("Could not read file.");
		}
	}

	@Test
	public void testConvertJsonToJavaObject() {
		try {
			File resourcePath = ResourceUtils.getResource(RESOURCE_PACKAGE_JSON);
			PackageJson packageJson = JsonUtil.readJsonFromFile(resourcePath.getAbsolutePath(), PackageJson.class);

			//Convert PackageJson object to string
			String json = JsonUtil.convertJavaObjectToJson(packageJson);

			//Convert Json to PackageJson object
			packageJson = JsonUtil.convertJsonToJavaObject(json, PackageJson.class);

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
