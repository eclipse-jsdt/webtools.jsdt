/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
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
import org.junit.AfterClass;
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
		File grunt = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/gruntfile.js"); //$NON-NLS-1$
		File gulp = ResourceUtil.getFileFromBundle(TestRunner.PLUGIN_ID, "resources/gulpfile.js"); //$NON-NLS-1$

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestJsProject"); //$NON-NLS-1$
		IProjectDescription description = ResourcesPlugin.getWorkspace()
				.newProjectDescription("Testing common js functionality"); //$NON-NLS-1$
		description.setNatureIds(new String[] { JavaScriptCore.NATURE_ID });
		project.create(description, null);
		project.open(null);

		gruntfile = project.getFile("gruntfile.js"); //$NON-NLS-1$
		gruntfile.create(new FileInputStream(grunt.getAbsolutePath()), true, null);

		gulpfile = project.getFile("gulpfile.js"); //$NON-NLS-1$
		gulpfile.create(new FileInputStream(gulp.getAbsolutePath()), true, null);
	}

	@AfterClass
	public static void teardown() throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("TestJsProject"); //$NON-NLS-1$
		project.delete(true, true, null);
	}

	@Test
	public void GruntTasksTest() {
		try {
			JavaScriptUnit unit = ASTUtil.getJavaScriptUnit(gruntfile);
			GruntVisitor visitor = new GruntVisitor(gruntfile);
			unit.accept(visitor);
			Set<ITask> tasks = visitor.getTasks();			
			assertTrue("Task list is empty", !tasks.isEmpty()); //$NON-NLS-1$
		
			boolean foundConcat = false;
			boolean foundUglify = false;
			boolean foundQunit = false;
			boolean foundJshint = false;
			boolean foundWatch = false;
			boolean foundTest = false;
			boolean foundDefault = false;
			boolean foundPublish = false;
			boolean foundFetchDependencies = false;
			boolean foundWatchCommon = false;
			boolean foundLog = false;
			boolean foundNoDescriptoionTask = false;



			for (ITask task : tasks) {
				String taskName = task.getName();
				if ("concat".equals(taskName)) { //$NON-NLS-1$
					foundConcat = true;
				} else if ("uglify".equals(taskName)) { //$NON-NLS-1$
					foundUglify = true;
				} else if ("qunit".equals(taskName)) { //$NON-NLS-1$
					foundQunit = true;
				} else if ("jshint".equals(taskName)) { //$NON-NLS-1$
					foundJshint = true;
				} else if ("watch".equals(taskName)) { //$NON-NLS-1$
					foundWatch = true;
				} else if ("test".equals(taskName)) { //$NON-NLS-1$
					foundTest = true;
				} else if ("default".equals(taskName)) { //$NON-NLS-1$
					foundDefault = true;
				} else if ("publish".equals(taskName)) { //$NON-NLS-1$
					foundPublish = true;
				} else if ("fetchDependencies".equals(taskName)) { //$NON-NLS-1$
					foundFetchDependencies = true;
				} else if ("watchCommon".equals(taskName)) { //$NON-NLS-1$
					foundWatchCommon = true;
				} else if ("log".equals(taskName)) { //$NON-NLS-1$
					foundLog = true;
				} else if ("noDescriptionTask".equals(taskName)) { //$NON-NLS-1$
					foundNoDescriptoionTask = true;
				}
			}
			
			assertTrue("Couldn't find task 'concat'", foundConcat); //$NON-NLS-1$
			assertTrue("Couldn't find task 'uglify'", foundUglify); //$NON-NLS-1$
			assertTrue("Couldn't find task 'qunit'", foundQunit); //$NON-NLS-1$
			assertTrue("Couldn't find task 'jshint'", foundJshint); //$NON-NLS-1$
			assertTrue("Couldn't find task 'watch'", foundWatch); //$NON-NLS-1$
			assertTrue("Couldn't find task 'test'", foundTest); //$NON-NLS-1$
			assertTrue("Couldn't find task 'default'", foundDefault); //$NON-NLS-1$
			assertTrue("Couldn't find task 'publish'", foundPublish); //$NON-NLS-1$
			assertTrue("Couldn't find task 'fetchDependencies'", foundFetchDependencies); //$NON-NLS-1$
			assertTrue("Couldn't find task 'watchCommon'", foundWatchCommon); //$NON-NLS-1$
			assertTrue("Couldn't find task 'log'", foundLog); //$NON-NLS-1$
			assertTrue("Couldn't find task 'noDescriptionTask'", foundNoDescriptoionTask); //$NON-NLS-1$
			
			// There are 12 tasks defined in gruntfile.js
			assertTrue("Not all grunt tasks were found", tasks.size() == 12); //$NON-NLS-1$
		} catch (CoreException e) {
			fail("Exception occured while reading file"); //$NON-NLS-1$
		}
	}

	@Test
	public void GulpTasksTest() {
		try {
			JavaScriptUnit unit = ASTUtil.getJavaScriptUnit(gulpfile);
			GulpVisitor visitor = new GulpVisitor(gruntfile);
			unit.accept(visitor);
			Set<ITask> tasks = visitor.getTasks();
			assertTrue("Task list is empty", !tasks.isEmpty()); //$NON-NLS-1$
			// There are 6 tasks defined in gulpfile.js
			assertTrue("Not all grunt tasks were found", tasks.size() == 6); //$NON-NLS-1$
		} catch (CoreException e) {
			fail("Exception occured while reading file"); //$NON-NLS-1$
		}
	}


}
