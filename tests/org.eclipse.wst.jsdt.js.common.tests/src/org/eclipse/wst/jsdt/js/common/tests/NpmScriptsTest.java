/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
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
		File npm = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/package.json"); //$NON-NLS-1$

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNpmProject"); //$NON-NLS-1$
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription("Testing common js functionality"); //$NON-NLS-1$
		description.setNatureIds(new String[] { JavaScriptCore.NATURE_ID });
		project.create(description, null);
		project.open(null);

		packageJsonFile = project.getFile("package.json"); //$NON-NLS-1$
		packageJsonFile.create(new FileInputStream(npm.getAbsolutePath()), true, null);
	}

	@AfterClass
	public static void teardown() throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestNpmProject"); //$NON-NLS-1$
		project.delete(true, true, null);
	}

	@Test
	public void TestNpmScriptContentProvider() {
		try {
			PackageJson packageJson = NpmUtil.parsePackageJsonFile(packageJsonFile);

			assertTrue("Not all npm scripts found", packageJson.getScripts().size() == 3); //$NON-NLS-1$

			boolean foundTests = false;
			boolean foundDoSomething = false;
			boolean foundFoo = false;

			for (String scriptName : packageJson.getScripts().keySet()) {

				if ("tests".equals(scriptName)) { //$NON-NLS-1$
					foundTests = true;
				} else if ("doSomething".equals(scriptName)) { //$NON-NLS-1$
					foundDoSomething = true;
				} else if ("foo".equals(scriptName)) { //$NON-NLS-1$
					foundFoo = true;
				}
			}

			assertTrue("Couldn't find script 'tests'", foundTests); //$NON-NLS-1$
			assertTrue("Couldn't find script 'doSomething'", foundDoSomething); //$NON-NLS-1$
			assertTrue("Couldn't find script 'foo'", foundFoo); //$NON-NLS-1$
		} catch (CoreException e) {
			fail("CoreException occured while reading file"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			fail("UnsupportedEncodingException occured while reading file"); //$NON-NLS-1$
		}

	}
}
