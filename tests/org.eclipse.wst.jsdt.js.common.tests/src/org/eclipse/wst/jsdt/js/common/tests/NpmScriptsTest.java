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
package org.eclipse.wst.jsdt.js.common.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.js.common.tests.suites.TestRunner;
import org.eclipse.wst.jsdt.js.common.tests.util.ResourceUtil;
import org.eclipse.wst.jsdt.js.npm.PackageJson;
import org.eclipse.wst.jsdt.js.npm.util.NpmUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Shane Bryzak
 *
 */
public class NpmScriptsTest {

	private static IFile packageJsonFile;

	@BeforeClass
	public static void init() throws CoreException, IOException, URISyntaxException {
		File npm = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/package.json");

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNpmProject");
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription("Testing common js functionality");
		description.setNatureIds(new String[] { JavaScriptCore.NATURE_ID });
		project.create(description, null);
		project.open(null);

		packageJsonFile = project.getFile("package.json");
		packageJsonFile.create(new FileInputStream(npm.getAbsolutePath()), true, null);
	}

	@AfterClass
	public static void teardown() throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNpmProject");
		project.delete(true, true, null);
	}

	@Test
	public void TestNpmScriptContentProvider() {
		try {
			PackageJson packageJson = NpmUtil.parsePackageJsonFile(packageJsonFile);

			assertTrue("Not all npm scripts found", packageJson.getScripts().size() == 3);

			boolean foundTests = false;
			boolean foundDoSomething = false;
			boolean foundFoo = false;

			for (String scriptName : packageJson.getScripts().keySet()) {

				if ("tests".equals(scriptName)) {
					foundTests = true;
				} else if ("doSomething".equals(scriptName)) {
					foundDoSomething = true;
				} else if ("foo".equals(scriptName)) {
					foundFoo = true;
				}
			}

			assertTrue("Couldn't find script 'tests'", foundTests);
			assertTrue("Couldn't find script 'doSomething'", foundDoSomething);
			assertTrue("Couldn't find script 'foo'", foundFoo);
		} catch (CoreException e) {
			fail("CoreException occured while reading file");
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException occured while reading file");
		}

	}
}
