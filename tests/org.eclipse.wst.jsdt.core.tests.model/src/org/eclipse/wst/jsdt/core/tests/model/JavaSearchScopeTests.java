/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.util.HashMap;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.core.search.*;
import org.eclipse.wst.jsdt.core.tests.model.AbstractJavaSearchTests.JavaSearchResultCollector;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;

/**
 * Tests the Java search engine accross multiple projects.
 */
public class JavaSearchScopeTests extends ModifyingResourceTests implements IJavaScriptSearchConstants {
public JavaSearchScopeTests(String name) {
	super(name);
}
public static Test suite() {
	return buildModelTestSuite(JavaSearchScopeTests.class);
}
// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//	TESTS_NAMES = new String[] { "testMethodOccurences" };
//  TESTS_NUMBERS = new int[] { 101426 };
//	TESTS_RANGE = new int[] { 16, -1 };
}

protected void tearDown() throws Exception {
	// Cleanup caches
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	manager.containers = new HashMap(5);
	manager.variables = new HashMap(5);

	super.tearDown();
}
protected void assertScopeEquals(String expected, IJavaScriptSearchScope scope) {
	String actual = scope.toString();
	if (!expected.equals(actual)) {
		System.out.println(displayString(actual, 3) + ",");
	}
	assertEquals("Unexpected scope", expected, actual);
}
/*
 * Ensures that a Java search scope with SOURCES only is correct.
 */
public void testSources() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.SOURCES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java search scope with APPLICATION_LIBRARIES only is correct
 * (external jar case)
 */
public void testApplicationLibrairiesExternalJar() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.APPLICATION_LIBRARIES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	"+  getExternalJCLPath("").toOSString() +"\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java search scope with APPLICATION_LIBRARIES only is correct
 * (internal jar and class folder cases)
 */
public void testApplicationLibrairiesJarAndClassFolder() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"}, new String[] {"/P/internal.jar", "/P/classfolder"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.APPLICATION_LIBRARIES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P/internal.jar\n" + 
			"	/P/classfolder\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java search scope with APPLICATION_LIBRARIES only is correct
 * (classpath variable case)
 */
public void testApplicationLibrairiesClasspathVariable() throws CoreException {
	try {
		VariablesInitializer.setInitializer(new ClasspathInitializerTests.DefaultVariableInitializer(new String[] {"TEST_LIB", "/P/lib.jar"}));
		IJavaScriptProject project = createJavaProject("P", new String[] {}, new String[] {"TEST_LIB"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.APPLICATION_LIBRARIES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P/lib.jar\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
		VariablesInitializer.reset();
	}
}
/*
 * Ensures that a Java search scope with APPLICATION_LIBRARIES only is correct
 * (classpath container case)
 */
public void testApplicationLibrairiesJsGlobalScopeContainer() throws CoreException {
	try {
		ContainerInitializer.setInitializer(new ClasspathInitializerTests.DefaultContainerInitializer(new String[] {"P", "/P/lib.jar"}));
		IJavaScriptProject project = createJavaProject("P", new String[] {}, new String[] {"org.eclipse.wst.jsdt.core.tests.model.TEST_CONTAINER"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.APPLICATION_LIBRARIES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P/lib.jar\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java search scope with SYSTEM_LIBRARIES only is correct
 * (classpath container case)
 */
public void testSystemLibraries() throws CoreException {
	try {
		ClasspathInitializerTests.DefaultContainerInitializer intializer = new ClasspathInitializerTests.DefaultContainerInitializer(new String[] {"P", "/P/lib.jar"}) {
			protected DefaultContainer newContainer(char[][] libPaths) {
				return new DefaultContainer(libPaths) {
					public int getKind() {
						return IJsGlobalScopeContainer.K_SYSTEM;
					}
				};
			}
		};
		ContainerInitializer.setInitializer(intializer);
		IJavaScriptProject project = createJavaProject("P", new String[] {}, new String[] {"org.eclipse.wst.jsdt.core.tests.model.TEST_CONTAINER"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.SYSTEM_LIBRARIES);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P/lib.jar\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java search scope with SOURCES | REFERENCED_PROJECTS is correct
 * (direct reference case)
 */
public void testSourcesOrDirectReferencedProjects() throws CoreException {
	try {
		createJavaProject("P1");
		IJavaScriptProject project = createJavaProject("P2", new String[] {"src"}, new String[] {}, new String[] {"/P1"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.SOURCES | IJavaScriptSearchScope.REFERENCED_PROJECTS);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P1\n" + 
			"	/P2/src\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
/*
 * Ensures that a Java search scope with SOURCES | REFERENCED_PROJECTS is correct
 * (reference through a container case)
 */
public void testSourcesOrContainerReferencedProjects() throws CoreException {
	try {
		createJavaProject("P1");
		ContainerInitializer.setInitializer(new ClasspathInitializerTests.DefaultContainerInitializer(new String[] {"P2", "/P1"}));
		IJavaScriptProject project = createJavaProject("P2", new String[] {"src"}, new String[] {"org.eclipse.wst.jsdt.core.tests.model.TEST_CONTAINER"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project}, IJavaScriptSearchScope.SOURCES | IJavaScriptSearchScope.REFERENCED_PROJECTS);
		assertScopeEquals(
			"JavaSearchScope on [\n" + 
			"	/P1\n" + 
			"	/P2/src\n" + 
			"]",
			scope);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
/*
 * Ensures that a Java project is enclosed in a scope on the project (proj=src)
 * (resourcePath case)
 */
public void testScopeEncloses01() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P\"", scope.encloses("/P"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses02() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose P", scope.encloses(project));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj=src)
 * (resource path with traling slash case)
 */
public void testScopeEncloses03() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/\"", scope.encloses("/P/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses04() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragmentRoot root = project.getPackageFragmentRoot(project.getProject());
		assertTrue("scope on P should enclose root P", scope.encloses(root));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj=src)
 * (resource path case)
 */
public void testScopeEncloses05() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/x/y\"", scope.encloses("/P/x/y"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj=src)
 * (resource path with trailing slash case)
 */
public void testScopeEncloses06() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/x/y/\"", scope.encloses("/P/x/y/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses07() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P/x/y");
		assertTrue("scope on P should enclose package x.y", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a default package is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses08() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P");
		assertTrue("scope on P should enclose default package", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj=src)
 * (resource path case)
 */
public void testScopeEncloses09() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/x/y/A.java\"", scope.encloses("/P/x/y/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses10() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/x/y/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj=src)
 * (resource path case)
 */
public void testScopeEncloses11() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/A.java\"", scope.encloses("/P/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses12() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a source type is enclosed in a scope on the project (proj=src)
 * (Java element case)
 */
public void testScopeEncloses13() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P");
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/x/y/A.js");
		IType type = cu.getType("A");
		assertTrue("scope on P should enclose type A", scope.encloses(type));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src)
 * (resourcePath case)
 */
public void testScopeEncloses14() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"/P\"", scope.encloses("/P"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src)
 * (resourcePath case)
 */
public void testScopeEncloses14b() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"/\"", scope.encloses("/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src)
 * (resourcePath case)
 */
public void testScopeEncloses14c() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"\"", scope.encloses(""));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses15() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should enclose P", scope.encloses(project));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src)
 * (resource path case)
 */
public void testScopeEncloses16() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src\"", scope.encloses("/P/src"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src)
 * (resource path with traling slash case)
 */
public void testScopeEncloses17() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/\"", scope.encloses("/P/src/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses18() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragmentRoot root = project.getPackageFragmentRoot(project.getProject().getFolder("src"));
		assertTrue("scope on P should enclose root P/src", scope.encloses(root));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src)
 * (resource path case)
 */
public void testScopeEncloses19() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y\"", scope.encloses("/P/src/x/y"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src)
 * (resource path with trailing slash case)
 */
public void testScopeEncloses20() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y/\"", scope.encloses("/P/src/x/y/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses21() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P/src/x/y");
		assertTrue("scope on P should enclose package x.y", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a default package is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses22() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P/src");
		assertTrue("scope on P should enclose default package", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj != src)
 * (resource path case)
 */
public void testScopeEncloses23() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y/A.java\"", scope.encloses("/P/src/x/y/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses24() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/x/y/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj != src)
 * (resource path case)
 */
public void testScopeEncloses25() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/A.java\"", scope.encloses("/P/src/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses26() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a source type is enclosed in a scope on the project (proj != src)
 * (Java element case)
 */
public void testScopeEncloses27() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/x/y/A.js");
		IType type = cu.getType("A");
		assertTrue("scope on P should enclose type A", scope.encloses(type));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src/)
 * (resourcePath case)
 */
public void testScopeEncloses28() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"/P\"", scope.encloses("/P"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src/)
 * (resourcePath case)
 */
public void testScopeEncloses28b() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"/P\"", scope.encloses("/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src/)
 * (resourcePath case)
 */
public void testScopeEncloses28c() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should not enclose \"/P\"", scope.encloses(""));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a Java project is not enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses29() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertFalse("scope on P should enclose P", scope.encloses(project));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src/)
 * (resource path case)
 */
public void testScopeEncloses30() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src\"", scope.encloses("/P/src"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src/)
 * (resource path with traling slash case)
 */
public void testScopeEncloses31() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/\"", scope.encloses("/P/src/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a root is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses32() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragmentRoot root = project.getPackageFragmentRoot(project.getProject().getFolder("src"));
		assertTrue("scope on P should enclose root P/src", scope.encloses(root));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src/)
 * (resource path case)
 */
public void testScopeEncloses33() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y\"", scope.encloses("/P/src/x/y"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src/)
 * (resource path with trailing slash case)
 */
public void testScopeEncloses34() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y/\"", scope.encloses("/P/src/x/y/"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a package is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses35() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P/src/x/y");
		assertTrue("scope on P should enclose package x.y", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a default package is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses36() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IPackageFragment pkg = getPackage("/P/src");
		assertTrue("scope on P should enclose default package", scope.encloses(pkg));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj != src/)
 * (resource path case)
 */
public void testScopeEncloses37() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/x/y/A.java\"", scope.encloses("/P/src/x/y/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses38() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/x/y/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj != src/)
 * (resource path case)
 */
public void testScopeEncloses39() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		assertTrue("scope on P should enclose \"/P/src/A.java\"", scope.encloses("/P/src/A.js"));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a compilation unit in the default package is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses40() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/A.js");
		assertTrue("scope on P should enclose compilation unit A.js", scope.encloses(cu));
	} finally {
		deleteProject("P");
	}
}
/*
 * Ensures that a source type is enclosed in a scope on the project (proj != src/)
 * (Java element case)
 */
public void testScopeEncloses41() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P", new String[] {"src/"});
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		IJavaScriptUnit cu = getCompilationUnit("/P/src/x/y/A.js");
		IType type = cu.getType("A");
		assertTrue("scope on P should enclose type A", scope.encloses(type));
	} finally {
		deleteProject("P");
	}
}

/**
 * Bug 101022: [search] JUnit Test Runner on folder runs tests outside directory
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=101022"
 */
public void testBug101022() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P1", new String[] {"src", "test", "test2"});
		createFile(
			"/P1/src/Test.js",
			"public class Test {\n" +
			"	protected void foo() {}\n" +
			"}" 
		);
		createFile(
			"/P1/test/Test.js",
			"public class Test {\n" +
			"	protected void foo() {}\n" +
			"}" 
		);
		createFile(
			"/P1/test2/Test.js",
			"public class Test {\n" +
			"	protected void foo() {}\n" +
			"}" 
		);
		IPackageFragmentRoot root = project.getPackageFragmentRoot(getFolder("/P1/test"));
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {root});
		JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		resultCollector.showProject = true;
		search("foo", METHOD, DECLARATIONS, scope, resultCollector);
		assertSearchResults(
			"test/Test.java [in P1] void Test.foo() [foo]",
			resultCollector);
	}
	finally {
		deleteProject("P1");
	}
}

/**
 * Bug 101426: Search doesn't work with imported plugin
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=101426"
 */
public void testBug101426() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P1", new String[] {"src/", "test/", "test2/"}, new String[] {"JCL_LIB"});
		createFile(
			"/P1/src/Test.js",
			"public interface ITest {\n" +
			"}" 
		);
		createFile(
			"/P1/test/Test.js",
			"public class Test {\n" +
			"	ITest test;\n" +
			"}" 
		);
		createFile(
			"/P1/test2/Test.js",
			"public class Test2 {\n" +
			"	ITest test;\n" +
			"}" 
		);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {project});
		JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		resultCollector.showProject = true;
		search("ITest", TYPE, REFERENCES, scope, resultCollector);
		assertSearchResults(
			"test/Test.java [in P1] Test.test [ITest]\n" + 
			"test2/Test.java [in P1] Test2.test [ITest]",
			resultCollector);
	}
	finally {
		deleteProject("P1");
	}
}

/**
 * Bug 101777: [search] selecting class with a main type ignores the default package
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=101777"
 */
public void testBug101777() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P1");
		createFile(
			"/P1/Test.js",
			"public class Test {\n" +
			"	public static void main(String[] args) {}\n" +
			"}"
		);
		IPackageFragment[] fragments = project.getPackageFragments();
		IPackageFragment defaultFragment = null;
		for (int i = 0; i < fragments.length; i++) {
			IPackageFragment fragment = fragments[i];
			if (fragment.getElementName().length() == 0) {
				defaultFragment = fragment;
				break;
			}
		}
		assertNotNull("We should have a default fragment for project P1!", defaultFragment);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] {defaultFragment});
		JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		resultCollector.showProject = true;
		search("main(String[]) void", METHOD, DECLARATIONS, scope, resultCollector);
		assertSearchResults(
			"Test.java [in P1] void Test.main(String[]) [main]",
			resultCollector);
	}
	finally {
		deleteProject("P1");
	}
}

/**
 * Bug 119203: Search doesn't work with imported plugin
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=119203"
 * WARNING: Fix for this bug has been disabled due to bad regression
 * 
 * Bug 127048: [search] References to Java element 'CorrectionEngine' not found
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=127048"
 */
public void testBug119203() throws CoreException {
	try {
		IJavaScriptProject project = createJavaProject("P1", new String[] {"src"});
		createFile(
			"/P1/src/Test.js",
			"public class Test {\n" +
			"}" 
		);
		createFile(
			"/P1/src/X.js",
			"public class X {\n" +
			"	Test test;\n" +
			"}" 
		);
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaScriptElement[] { project });
		JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
		resultCollector.showProject = true;
		search("Test", TYPE, REFERENCES, scope, resultCollector);
		assertSearchResults(
			"src/X.java [in P1] X.test [Test]",
			resultCollector);
	}
	finally {
		deleteProject("P1");
	}
}
}
