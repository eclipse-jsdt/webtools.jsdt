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
import java.net.URISyntaxException;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.js.common.build.system.ITask;
import org.eclipse.wst.jsdt.js.common.build.system.util.ASTUtil;
import org.eclipse.wst.jsdt.js.common.tests.suites.TestRunner;
import org.eclipse.wst.jsdt.js.common.tests.util.ResourceUtil;
import org.eclipse.wst.jsdt.js.grunt.util.GruntVisitor;
import org.eclipse.wst.jsdt.js.gulp.util.GulpVisitor;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BuildSystemVisitorTest {
	private static IFile gruntfile;
	private static IFile gulpfile;
	
	@BeforeClass
	public static void init() throws CoreException, IOException, URISyntaxException {
		File grunt = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/gruntfile.js");
		File gulp = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/gulpfile.js");
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestJsProject");
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription("Testing common js functionality");
		description.setNatureIds(new String[] { JavaScriptCore.NATURE_ID });
		project.create(description, null);
		project.open(null);
		
		gruntfile = project.getFile("gruntfile.js");
		gruntfile.create(new FileInputStream(grunt.getAbsolutePath()), true, null);
		
		gulpfile = project.getFile("gulpfile.js");
		gulpfile.create(new FileInputStream(gulp.getAbsolutePath()), true, null);
	}

	@Test
	public void GruntTasksTest() {
		try {
			JavaScriptUnit unit = ASTUtil.getJavaScriptUnit(gruntfile);
			GruntVisitor visitor = new GruntVisitor(gruntfile);
			unit.accept(visitor);
			Set<ITask> tasks = visitor.getTasks();
			assertTrue("Task list is empty", !tasks.isEmpty());
			// There are 8 tasks defined in gruntfile.js
			assertTrue("Not all grunt tasks were found", tasks.size() == 8);
		} catch (CoreException e) {
			fail("Exception occured while reading file");
		}
	}
	
	@Test
	public void GulpTasksTest() {
		try {
			JavaScriptUnit unit = ASTUtil.getJavaScriptUnit(gulpfile);
			GulpVisitor visitor = new GulpVisitor(gruntfile);
			unit.accept(visitor);
			Set<ITask> tasks = visitor.getTasks();
			assertTrue("Task list is empty", !tasks.isEmpty());
			// There are 6 tasks defined in gulpfile.js
			assertTrue("Not all grunt tasks were found", tasks.size() == 6);
		} catch (CoreException e) {
			fail("Exception occured while reading file");
		}
	}
	
}
