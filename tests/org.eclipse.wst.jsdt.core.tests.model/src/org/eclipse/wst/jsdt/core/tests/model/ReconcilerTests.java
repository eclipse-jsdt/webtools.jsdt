/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;


import java.io.IOException;

import junit.framework.Test;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.wst.jsdt.core.IBuffer;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.core.compiler.CategorizedProblem;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.compiler.ReconcileContext;
import org.eclipse.wst.jsdt.core.compiler.ValidationParticipant;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.core.CompilationUnit;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IndexManager;

public class ReconcilerTests extends ModifyingResourceTests {
	
	protected IJavaScriptUnit workingCopy;
	protected ProblemRequestor problemRequestor;
	
	/* A problem requestor that auto-cancels on first problem */
	class CancelingProblemRequestor extends ProblemRequestor {
		IProgressMonitor progressMonitor = new IProgressMonitor() {
			boolean isCanceled = false;
			public void beginTask(String name, int totalWork) {}
			public void done() {}
			public void internalWorked(double work) {}
			public boolean isCanceled() {
				return this.isCanceled;
			}
			public void setCanceled(boolean value) {
				this.isCanceled = value;
			}
			public void setTaskName(String name) {}
			public void subTask(String name) {}
			public void worked(int work) {}
		};
	
		boolean isCanceling = false;
		public void acceptProblem(IProblem problem) {
			if (isCanceling) this.progressMonitor.setCanceled(true); // auto-cancel on first problem
			super.acceptProblem(problem);
		}		
	}
	
	class ReconcileParticipant extends ValidationParticipant {
		IJavaScriptElementDelta delta;
		org.eclipse.wst.jsdt.core.dom.JavaScriptUnit ast;
		ReconcileParticipant() {
			TestvalidationParticipant.PARTICIPANT = this;
		}
		public boolean isActive(IJavaScriptProject project) {
			return true;
		}
		public void reconcile(ReconcileContext context) {
			this.delta = context.getDelta();
			try {
				this.ast = context.getAST3();
			} catch (JavaScriptModelException e) {
				assertNull("Unexpected exception", e);
			}
		}
	}
/**
 */
public ReconcilerTests(String name) {
	super(name);
}
// Use this static initializer to specify subset for tests
// All specified tests which do not belong to the class are skipped...
static {
//	JavaModelManager.VERBOSE = true;
//	org.eclipse.jsdt.internal.core.search.BasicSearchEngine.VERBOSE = true;
//	TESTS_PREFIX = "testIgnoreIfBetterNonAccessibleRule";
//	TESTS_NAMES = new String[] { "testRawUsage" };
//	TESTS_NUMBERS = new int[] { 118823 };
//	TESTS_RANGE = new int[] { 16, -1 };
}
public static Test suite() {
	return buildModelTestSuite(ReconcilerTests.class);
}
protected void assertProblems(String message, String expected) {
	assertProblems(message, expected, this.problemRequestor);
}
// Expect no error as soon as indexing is finished
protected void assertNoProblem(char[] source, IJavaScriptUnit unit) throws InterruptedException, JavaScriptModelException {
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	if (this.problemRequestor.problemCount > 0) {
		// If errors then wait for indexes to finish
		while (indexManager.awaitingJobsCount() > 0) {
			Thread.sleep(100);
		}
		// Reconcile again to see if error goes away
		this.problemRequestor.initialize(source);
		unit.getBuffer().setContents(source); // need to set contents again to be sure that following reconcile will be really done
		unit.reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		if (this.problemRequestor.problemCount > 0) {
			assertEquals("Working copy should NOT have any problem!", "", this.problemRequestor.problems.toString());
		}
	}
}
protected void addClasspathEntries(IIncludePathEntry[] entries, boolean enableForbiddenReferences) throws JavaScriptModelException {
	IJavaScriptProject project = getJavaProject("Reconciler");
	IIncludePathEntry[] oldClasspath = project.getRawIncludepath();
	int oldLength = oldClasspath.length;
	int length = entries.length;
	IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldLength+length];
	System.arraycopy(oldClasspath, 0, newClasspath, 0, oldLength);
	System.arraycopy(entries, 0, newClasspath, oldLength, length);
	project.setRawIncludepath(newClasspath, null);
	
	if (enableForbiddenReferences) {
		project.setOption(JavaScriptCore.COMPILER_PB_FORBIDDEN_REFERENCE, JavaScriptCore.ERROR);
	}
}
protected void removeClasspathEntries(IIncludePathEntry[] entries) throws JavaScriptModelException {
	IJavaScriptProject project = getJavaProject("Reconciler");
	IIncludePathEntry[] oldClasspath = project.getRawIncludepath();
	int oldLength = oldClasspath.length;
	int length = entries.length;
	IIncludePathEntry[] newClasspath = new IIncludePathEntry[oldLength-length];
	System.arraycopy(oldClasspath, 0, newClasspath, 0, oldLength-length);
	project.setRawIncludepath(newClasspath, null);
}
/**
 * Setup for the next test.
 */
public void setUp() throws Exception {
	super.setUp();
	this.problemRequestor =  new ProblemRequestor();
	this.workingCopy = getCompilationUnit("Reconciler/src/p1/X.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
	this.problemRequestor.initialize(this.workingCopy.getSource().toCharArray());
	startDeltas();
}
public void setUpSuite() throws Exception {
	super.setUpSuite();

	// Create project with 1.4 compliance
	IJavaScriptProject project14 = createJavaProject("Reconciler", new String[] {"src"}, new String[] {"JCL_LIB"});
	createFolder("/Reconciler/src/p1");
	createFolder("/Reconciler/src/p2");
	createFile(
		"/Reconciler/src/p1/X.js", 
		"  function foo() {\n" +
		"  }\n" +
		""
	);
	project14.setOption(JavaScriptCore.COMPILER_SOURCE, JavaScriptCore.VERSION_1_4);
	project14.setOption(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, JavaScriptCore.IGNORE);
	project14.setOption(JavaScriptCore.COMPILER_PB_INVALID_JAVADOC, JavaScriptCore.WARNING);

	// Create project with 1.5 compliance
	IJavaScriptProject project15 = createJavaProject("Reconciler15", new String[] {"src"}, new String[] {"JCL15_LIB"}, "bin", "1.5");
//	addLibrary(
//		project15, 
//		"lib15.jar", 
//		"lib15src.zip", 
//		new String[] {
//			"java/util/List.js",
//			"package java.util;\n" +
//			"public class List<T> {\n" +
//			"}",
//			"java/util/Stack.js",
//			"package java.util;\n" +
//			"public class Stack<T> {\n" +
//			"}",
//			"java/util/Map.js",
//			"package java.util;\n" +
//			"public interface Map<K,V> {\n" +
//			"}",
//			"java/lang/annotation/Annotation.js",
//			"package java.lang.annotation;\n" +
//			"public interface Annotation {\n" +
//			"}",
//			"java/lang/Deprecated.js",
//			"package java.lang;\n" +
//			"public @interface Deprecated {\n" +
//			"}",
//			"java/lang/SuppressWarnings.js",
//			"package java.lang;\n" +
//			"public @interface SuppressWarnings {\n" +
//			"   String[] value();\n" +
//			"}"
//		}, 
//		JavaScriptCore.VERSION_1_5
//	);
//	project15.setOption(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, JavaScriptCore.IGNORE);
//	project15.setOption(JavaScriptCore.COMPILER_PB_RAW_TYPE_REFERENCE, JavaScriptCore.IGNORE);
}
private void setUp15WorkingCopy() throws JavaScriptModelException {
	setUp15WorkingCopy("Reconciler15/src/p1/X.js", new WorkingCopyOwner() {});
}
private void setUp15WorkingCopy(String path, WorkingCopyOwner owner) throws JavaScriptModelException {
	String contents = this.workingCopy.getSource();
	setUpWorkingCopy(path, contents, owner);
}
private void setUpWorkingCopy(String path, String contents) throws JavaScriptModelException {
	setUpWorkingCopy(path, contents, new WorkingCopyOwner() {});
}
private void setUpWorkingCopy(String path, String contents, WorkingCopyOwner owner) throws JavaScriptModelException {
	this.workingCopy.discardWorkingCopy();
	this.workingCopy = getCompilationUnit(path).getWorkingCopy(owner, null);
	setWorkingCopyContents(contents);
	this.workingCopy.makeConsistent(null);
}
void setWorkingCopyContents(String contents) throws JavaScriptModelException {
	this.workingCopy.getBuffer().setContents(contents);
	this.problemRequestor.initialize(contents.toCharArray());
}
/**
 * Cleanup after the previous test.
 */
public void tearDown() throws Exception {
	TestvalidationParticipant.PARTICIPANT = null;
	if (this.workingCopy != null) {
		this.workingCopy.discardWorkingCopy();
	}
	stopDeltas();
	super.tearDown();
}
public void tearDownSuite() throws Exception {
	deleteProject("Reconciler");
	deleteProject("Reconciler15");
	super.tearDownSuite();
}
/*
 * Ensures that no problem is created for a reference to a type that is included in a prereq project.
 */


public void test00() throws Exception {

}
 

public void test00a() throws JavaScriptModelException {
	setWorkingCopyContents(
		"function foo(){\n" +
		" alert();\n"+ 
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"----------\n"
	);
	}

public void testAccessRestriction() throws CoreException {
	try {
		createJavaProject("P1", new String[] {"src"}, new String[] {"JCL_LIB"}, null, null, new String[0], null, null, new boolean[0], new String[][] {{"**/X.js"}}, null, "1.4");
		createFile("/P1/src/X.js", "function foo() {}");
		
		createJavaProject("P2", new String[] {"src"}, new String[] {"JCL_LIB"}, new String[] {"/P1"});
		setUpWorkingCopy("/P2/src/Y.js", "foo();");
		assertProblems(
			"Unexpected problems", 
			"----------\n" + 
			"----------\n"
		);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
///*
// * Ensures that no problem is created for a reference to a binary type that is included in a prereq project.
// * (regression test for bug 82542 Internal error during AST creation)
// */
//public void testAccessRestriction2() throws CoreException, IOException {
//	try {
//		IJavaScriptProject project = createJavaProject("P1");
//		addLibrary(
//			project,
//			"lib.jar",
//			"libsrc.zip",
//			new String[] {
//				"p/X.js",
//				"package p;\n" +
//				"public class X {\n" +
//				"}",
//			},
//			new String[] {
//				"**/*"
//			},
//			null,
//			"1.4"
//		);
//		createJavaProject("P2", new String[] {"src"}, new String[] {"JCL_LIB"}, new String[] {"/P1"}, "bin");
//		setUpWorkingCopy("/P2/src/Y.js", "public class Y extends p.X {}");
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"----------\n"
//		);
//	} finally {
//		deleteProject("P1");
//		deleteProject("P2");
//	}
//}
///*
// * Ensures that no problem is created for a reference to a type that is included and not exported in a prereq project
// * but with combineAccessRestriction flag set to false.
// */
//public void testAccessRestriction3() throws CoreException {
//	try {
//		createJavaProject("P1");
//		createFolder("/P1/p");
//		createFile("/P1/p/X.js", "package p; public class X {}");
//		
//		createJavaProject("P2", new String[] {}, new String[] {}, null, null, new String[] {"/P1"}, null, null, new boolean[] {true}, "", null, null, null, "1.4");
//		
//		createJavaProject("P3", new String[] {"src"}, new String[] {"JCL_LIB"}, null, null, new String[] {"/P2"}, null, new String[][] {new String[] {"**/X"}}, false/*don't combine access restrictions*/, new boolean[] {true}, "bin", null, null, null, "1.4");
//		setUpWorkingCopy("/P3/src/Y.js", "public class Y extends p.X {}");
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"----------\n"
//		);
//	} finally {
//		deleteProjects(new String[] {"P1", "P2", "P3" });
//	}
//}
///*
// * Ensures that a problem is created for a reference to a type that is included and not exported in a prereq project
// * but with combineAccessRestriction flag set to true.
// */
//public void testAccessRestriction4() throws CoreException {
//	try {
//		createJavaProject("P1");
//		createFolder("/P1/p");
//		createFile("/P1/p/X.js", "package p; public class X {}");
//		
//		createJavaProject("P2", new String[] {}, new String[] {}, null, null, new String[] {"/P1"}, null, null, new boolean[] {true}, "", null, null, null, "1.4");
//		
//		createJavaProject("P3", new String[] {"src"}, new String[] {"JCL_LIB"}, null, null, new String[] {"/P2"}, null, new String[][] {new String[] {"**/X"}}, true/*combine access restrictions*/, new boolean[] {true}, "bin", null, null, null, "1.4");
//		setUpWorkingCopy("/P3/src/Y.js", "public class Y extends p.X {}");
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"1. ERROR in /P3/src/Y.js (at line 1)\n" + 
//			"	public class Y extends p.X {}\n" + 
//			"	                       ^^^\n" + 
//			"Access restriction: The type X is not accessible due to restriction on required project P1\n" + 
//			"----------\n"
//		);
//	} finally {
//		deleteProjects(new String[] {"P1", "P2", "P3" });
//	}
//}
///*
// * Ensures that a problem is created for a reference to a type that is no longer accessible in a prereq project.
// * (regression test for bug 91498 Reconcile still sees old access rules)
// */
//public void testAccessRestriction5() throws CoreException {
//	try {
//		createJavaProject("P1");
//		createFolder("/P1/p");
//		createFile("/P1/p/X.js", "package p; public class X {}");	
//		IJavaScriptProject p2 = createJavaProject("P2", new String[] {"src"}, new String[] {"JCL_LIB"}, "bin");
//		IIncludePathEntry[] classpath = p2.getRawClasspath();
//		int length = classpath.length;
//		System.arraycopy(classpath, 0, classpath = new IIncludePathEntry[length+1], 0, length);
//		classpath[length] = createSourceEntry("P2", "/P1", "+**/p/|-**/*");
//		p2.setRawClasspath(classpath, null);
//		setUpWorkingCopy("/P2/src/Y.js", "public class Y extends p.X {}");
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"----------\n"
//		);
//		
//		// remove accessible rule
//		System.arraycopy(classpath, 0, classpath = new IIncludePathEntry[length+1], 0, length);
//		classpath[length] = createSourceEntry("P2", "/P1", "-**/*");
//		p2.setRawClasspath(classpath, null);
//		this.problemRequestor.initialize(this.workingCopy.getSource().toCharArray());
//		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true/*force problem detection*/, null, null);
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"1. ERROR in /P2/src/Y.js (at line 1)\n" + 
//			"	public class Y extends p.X {}\n" + 
//			"	                       ^^^\n" + 
//			"Access restriction: The type X is not accessible due to restriction on required project P1\n" + 
//			"----------\n"
//		);
//
//	} finally {
//		deleteProjects(new String[] {"P1", "P2"});
//	}
//}
/**
 * Ensures that the reconciler handles duplicate members correctly.
 */
public void testAddDuplicateMember() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  function foo() {\n" +
		"  }\n" +
		"  function foo() {\n" +
		"  }\n" +
		" ");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"foo()#2[+]: {}"
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents, updating the structure of this reconciler's compilation
 * unit, and fires the Java element delta for the structural changes
 * of the addition of a field and a method.
 */
public void testAddFieldAndConstructor() throws JavaScriptModelException {
	setWorkingCopyContents(
			"  var i;\n" +
			"  function X(p) {\n" +
			"  }\n" +
			"  function foo() {\n" +
			"  }\n" +
			" ");
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertDeltas(
			"Unexpected delta", 
			"X(p0)[+]: {}\n"+
			"i[+]: {}"  
		);
}
///**
// * Ensures that the reconciler reconciles the new contents with the current
// * contents, updating the structure of this reconciler's compilation
// * unit, and fires the Java element delta for the structural changes
// * of the addition of a field and a constructor.
// */
//public void testAddImports() throws JavaScriptModelException {
//	setWorkingCopyContents(
//		"package p1;\n" +
//		"import p2.*;\n" +
//		"import java.lang.reflect.*;\n" +
//		"import java.util.Vector;\n" +
//		"public class X {\n" +
//		"  public void foo() {\n" +
//		"  }\n" +
//		"}");
//	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//	assertDeltas(
//		"Unexpected delta", 
//		"<import container>[*]: {CHILDREN | FINE GRAINED}\n" +
//		"	import java.lang.reflect.*[+]: {}\n" +
//		"	import java.util.Vector[+]: {}"
//	);
//}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents, updating the structure of this reconciler's compilation
 * unit, and fires the Java element delta for the structural changes
 * of the addition of a method.
 */
public void testAddMethod1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  function foo() {\n" +
		"  }\n" +
		"  function bar() {\n" +
		"  }\n" +
		" ");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"bar()[+]: {}"
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents,updating the structure of this reconciler's compilation
 * unit, and fires the Java element delta for the structural changes
 * of the addition of a portion of a new method.
 */
public void testAddPartialMethod1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  function some()\n" +
		"  function foo() {\n" +
		"  }\n" +
		" ");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"some()[+]: {}"
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents,updating the structure of this reconciler's compilation
 * unit, and fires the Java element delta for the structural changes
 * of the addition of a portion of a new method.  Ensures that when a
 * second part is added to the new method no structural changes are recognized.
 */
public void testAddPartialMethod1and2() throws JavaScriptModelException {
	// Add partial method before foo
	setWorkingCopyContents(
		"  function some()\n" +
		"  function foo() {\n" +
		"  }\n" +
		 "");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	
	// Add { on partial method
	clearDeltas();
	setWorkingCopyContents(
		"  function some() {\n" +
		"  function foo() {\n" +
		"  }\n" +
		"");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"[Working copy] X.js[*]: {CONTENT | FINE GRAINED}"
	);
}
/*
 * Ensures that the AST broadcasted during a reconcile operation is correct.
 * (case of a working copy being reconciled with changes, creating AST and no problem detection)
 */
public void testBroadcastAST1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"var i;\n" +
		"");
	this.workingCopy.reconcile(AST.JLS3, false/*don't force problem detection*/, null/*primary owner*/, null/*no progress*/);
	assertASTNodeEquals(
		"Unexpected ast", 
		"var i;\n" + 
		"",
		this.deltaListener.getCompilationUnitAST(this.workingCopy));
}
/*
 * Ensures that the AST broadcasted during a reconcile operation is correct.
 * (case of a working copy being reconciled with NO changes, creating AST and forcing problem detection)
 */
public void testBroadcastAST2() throws JavaScriptModelException {
	this.workingCopy.reconcile(AST.JLS3, true/*force problem detection*/, null/*primary owner*/, null/*no progress*/);
	assertASTNodeEquals(
		"Unexpected ast", 
		"function foo(){\n" + 
		"}\n" + 
		"",
		this.deltaListener.getCompilationUnitAST(this.workingCopy));
}
/*
 * Ensures that the AST broadcasted during a reconcile operation is correct.
 * (case of a working copy being reconciled with NO changes, creating AST and no problem detection)
 */
public void testBroadcastAST3() throws JavaScriptModelException {
	this.workingCopy.reconcile(AST.JLS3, false/*don't force problem detection*/, null/*primary owner*/, null/*no progress*/);
	assertASTNodeEquals(
		"Unexpected ast", 
		"function foo(){\n" + 
		"}" + 
		"\n",
		this.deltaListener.getCompilationUnitAST(this.workingCopy));
}
/*
 * Ensures that the AST broadcasted during a reconcile operation is correct.
 * (case of a working copy being reconciled twice in a batch operation)
 */
public void testBroadcastAST4() throws CoreException {
	JavaScriptCore.run(
		new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				ReconcilerTests.this.workingCopy.reconcile(AST.JLS3, true/*force problem detection*/, null/*primary owner*/, monitor);
				setWorkingCopyContents(
					"var i;");
				ReconcilerTests.this.workingCopy.reconcile(AST.JLS3, false/*don't force problem detection*/, null/*primary owner*/, monitor);				
			}
		},
		null/*no progress*/);
	assertASTNodeEquals(
		"Unexpected ast", 
		"var i;\n",
		this.deltaListener.getCompilationUnitAST(this.workingCopy));
}
/*
 * Ensures that reconciling a subclass doesn't close the buffer while resolving its superclass.
 * (regression test for bug 62854 refactoring does not trigger reconcile)
 */
public void testBufferOpenAfterReconcile() throws CoreException {
 	try {
		createFile(
			"/Reconciler/src/p1/Super.js",
			"package p1;\n" +
			"public class Super {\n" +
			"}"
		);
		setWorkingCopyContents(
			"  function foo() {\n" +
			"  }\n" +
			"");
		IBuffer buffer = this.workingCopy.getBuffer();
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true, null, null);
		assertTrue("Buffer should still be open", !buffer.isClosed());
	} finally {
		deleteFile("/Reconciler/src/p1/Super.js");
	}
}
/**
 * Ensure an OperationCanceledException is correcly thrown when progress monitor is canceled
 * @deprecated using deprecated code
 */
public void testCancel() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  function foo( s) {\n" +
		"  }\n" +
		""
	);
	this.workingCopy.makeConsistent(null);
	
	// count the number of time isCanceled() is called when converting this source unit
	CancelCounter counter = new CancelCounter();
	this.workingCopy.reconcile(AST.JLS2, true, null, counter);
	
	// throw an OperatonCanceledException at each point isCanceled() is called
	for (int i = 0; i < counter.count; i++) {
		boolean gotException = false;
		try {
			this.workingCopy.reconcile(AST.JLS2, true, null, new Canceler(i));
		} catch (OperationCanceledException e) {
			gotException = true;
		}
		assertTrue("Should get an OperationCanceledException (" + i + ")", gotException);
	}
	
	// last should not throw an OperationCanceledException
	this.workingCopy.reconcile(AST.JLS2, true, null, new Canceler(counter.count));
}
/**
 * Ensures that the delta is correct when adding a category
 */
public void testCategories1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat1\n" +
		"   */\n" +
		"  function foo() {\n" +
		"  }\n" +
		""
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"foo()[*]: {CATEGORIES}"
	);
}
/**
 * Ensures that the delta is correct when removing a category
 */
public void testCategories2() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat1\n" +
		"   */\n" +
		"  function foo() {\n" +
		"  }\n" +
		""
	);
	this.workingCopy.makeConsistent(null);
	
	setWorkingCopyContents(
		"  function foo() {\n" +
		"  }\n" +
		""
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	foo()[*]: {CATEGORIES}"
	);
}
/**
 * Ensures that the delta is correct when changing a category
 */
public void testCategories3() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat1\n" +
		"   */\n" +
		"  function foo() {\n" +
		"  }\n" +
		" "
	);
	this.workingCopy.makeConsistent(null);
	
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat2\n" +
		"   */\n" +
		"  function foo() {\n" +
		"  }\n" +
		""
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	foo()[*]: {CATEGORIES}"
	);
}
/*
 * Ensures that the delta is correct when adding a category to a second field
 * (regression test for bug 125675 @category not reflected in outliner in live fashion)
 */
public void testCategories4() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat1\n" +
		"   */\n" +
		"  int f1;\n" +
		"  int f2;\n" +
		""
	);
	this.workingCopy.makeConsistent(null);
	
	setWorkingCopyContents(
		"  /**\n" +
		"   * @category cat1\n" +
		"   */\n" +
		"  int f1;\n" +
		"  /**\n" +
		"   * @category cat2\n" +
		"   */\n" +
		"  int f2;\n" +
		""
	);	
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	f2[*]: {CATEGORIES}"
	);
}
///*
// * Ensures that changing and external jar and refreshing takes the change into account
// * (regression test for bug 134110 [regression] Does not pick-up interface changes from classes in the build path)
// */
//public void testChangeExternalJar() throws CoreException, IOException {
//	IJavaScriptProject project = getJavaProject("Reconciler");
//	String jarPath = getExternalPath() + "lib.jar";
//	try {
//		org.eclipse.jsdt.core.tests.util.Util.createJar(new String[] {
//			"p/Y.js",
//			"package p;\n" +
//			"public class Y {\n" +
//			"  function foo() {\n" +
//			"  }\n" +
//			"}"
//		}, jarPath, "1.4");
//		addLibraryEntry(project, jarPath, false);
//		
//		// force Y.class file to be cached during resolution
//		setWorkingCopyContents(
//			"package p1;\n" +
//			"public class X extends p.Y {\n" +
//			"  function bar() {\n" +
//			"    foo();\n" +
//			"  }\n" +
//			"}");
//		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//		
//		// change jar and refresh
//		org.eclipse.jsdt.core.tests.util.Util.createJar(new String[] {
//			"p/Y.js",
//			"package p;\n" +
//			"public class Y {\n" +
//			"  function foo(String s) {\n" +
//			"  }\n" +
//			"}"
//		}, jarPath, "1.4");
//		getJavaModel().refreshExternalArchives(null,null);
//		
//		setWorkingCopyContents(
//			"package p1;\n" +
//			"public class X extends p.Y {\n" +
//			"  function bar() {\n" +
//			"    foo(\"a\");\n" +
//			"  }\n" +
//			"}");
//		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//		assertProblems(
//			"Unexpected problems", 
//			"----------\n" + 
//			"----------\n"
//		);
//	} finally {
//		removeLibraryEntry(project, new Path(jarPath));
//		deleteFile(new File(jarPath));
//	}
//}
///**
// * Ensures that the reconciler reconciles the new contents with the current
// * contents,updating the structure of this reconciler's compilation
// * unit, and fires the Java element deltas for the structural changes
// * of a method's type parameter change.
// */
//public void testChangeMethodTypeParameters() throws JavaScriptModelException {
//	setUp15WorkingCopy();
//	clearDeltas();
//	setWorkingCopyContents(
//		"  public <T> void foo() {\n" +
//		"  }\n" +
//		"");
//	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//	assertDeltas(
//		"Unexpected delta", 
//		"X[*]: {CHILDREN | FINE GRAINED}\n" +
//		"	foo()[*]: {CONTENT}"
//	);
//}
///**
// * Ensures that the reconciler reconciles the new contents with the current
// * contents,updating the structure of this reconciler's compilation
// * unit, and fires the Java element deltas for the structural changes
// * of a type's type parameter change.
// */
//public void testChangeTypeTypeParameters() throws JavaScriptModelException {
//	setUp15WorkingCopy();
//	clearDeltas();
//	setWorkingCopyContents(
//		"package p1;\n" +
//		"import p2.*;\n" +
//		"public class X <T> {\n" +
//		"  function foo() {\n" +
//		"  }\n" +
//		"}");
//	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//	assertDeltas(
//		"Unexpected delta", 
//		"X[*]: {CONTENT}"
//	);
//}
///**
// * Ensures that the reconciler reconciles the new contents with the current
// * contents,updating the structure of this reconciler's compilation
// * unit, and fires the Java element deltas for the structural changes
// * of a method visibility change.
// */
//public void testChangeMethodVisibility() throws JavaScriptModelException {
//	setWorkingCopyContents(
//		"package p1;\n" +
//		"import p2.*;\n" +
//		"public class X {\n" +
//		"  private void foo() {\n" +
//		"  }\n" +
//		"}");
//	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//	assertDeltas(
//		"Unexpected delta", 
//		"X[*]: {CHILDREN | FINE GRAINED}\n" +
//		"	foo()[*]: {MODIFIERS CHANGED}"
//	);
//}
/**
 * Ensures that the correct delta is reported when closing the working copy and modifying its buffer.
 */
public void testCloseWorkingCopy() throws JavaScriptModelException {
	IBuffer buffer = this.workingCopy.getBuffer();
	this.workingCopy.close();
	buffer.setContents(
		"  function foo() {\n" +
		"  }\n" +
		"  function bar() {\n" +
		"  }\n" +
		"");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"bar()[+]: {}"
	);
}

///**
// * Ensures that a reference to a constant with type mismatch doesn't show an error.
// * (regression test for bug 17104 Compiler does not complain but "Quick Fix" ??? complains)
// */
//public void testConstantReference() throws CoreException {
//	try {
//		createFile(
//			"/Reconciler/src/p1/OS.js",
//			"package p1;\n" +
//			"public class OS {\n" +
//			"	public static final int CONST = 23 * 1024;\n" +
//			"}");
//		setWorkingCopyContents(
//			"package p1;\n" +
//			"public class X {\n" +
//			"	public short c;\n" +
//			"	public static void main(String[] arguments) {\n" +
//			"		short c = 1;\n" +
//			"		switch (c) {\n" +
//			"			case OS.CONST: return;\n" +
//			"		}\n" +
//			"	}\n" +
//			"}");
//		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
//		assertProblems(
//			"Unexpected problems",
//			"----------\n" + 
//			"----------\n"
//		);
//	} finally {
//		deleteFile("/Reconciler/src/p1/OS.js");
//	}
//}
/*
// * Ensures that the source type converter doesn't throw an OutOfMemoryError if converting a generic type with a primitive type array as argument
// * (regression test for bug 135296 opening a special java file results in an "out of memory" message)
// */
//public void testConvertPrimitiveTypeArrayTypeArgument() throws CoreException {
//	IJavaScriptUnit otherCopy = null;
//	try {
//		WorkingCopyOwner owner = new WorkingCopyOwner() {};
//		otherCopy = getWorkingCopy(
//			"Reconciler15/src/Y.js", 
//			"public class Y {\n" +
//			"  void foo(Z<int[]> z) {}\n" +
//			"}\n" +
//			"class Z<E> {\n" +
//			"}",
//			owner,
//			false/*don't compute problems*/);
//		setUp15WorkingCopy("/Reconciler15/src/X.js", owner);
//		setWorkingCopyContents(
//			"public class X {\n" +
//			"  void bar(Y y) {\n" +
//			"    y.foo(new Z<int[]>());\n" +
//			"  }\n" +
//			"}"
//		);
//		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
//		assertProblems(
//			"Unexpected problems",
//			"----------\n" + 
//			"----------\n"
//		);
//	} finally {
//		if (otherCopy != null)
//			otherCopy.discardWorkingCopy();
//	}
//}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents, updating the structure of this reconciler's compilation
 * unit, and fires the Java element deltas for the structural changes
 * of a method being deleted.
 */
public void testDeleteMethod1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"  ");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"foo()[-]: {}"
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents, updating the structure of this reconciler's compilation
 * unit, and fires the Java element deltas for the structural changes
 * of two methods being deleted.
 */
public void testDeleteTwoMethods() throws JavaScriptModelException {
	// create 2 methods
	setWorkingCopyContents(
		"  function foo() {\n" +
		"  }\n" +
		"  function bar() {\n" +
		"  }\n" +
		"");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	
	// delete the 2 methods
	clearDeltas();
	setWorkingCopyContents(
		"  ");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"bar()[-]: {}\n" +
		"foo()[-]: {}"
	);
}
/*
 * Ensures that excluded part of prereq project are not visible
 */
public void testExcludePartOfAnotherProject1() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P", "-**/internal/"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P");
		createFolder("/P/p/internal");
		createFile(
			"/P/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/X.java (at line 2)\n" + 
			"	public class X extends p.internal.Y {\n" + 
			"	                       ^^^^^^^^^^^^\n" + 
			"Access restriction: The type Y is not accessible due to restriction on required project P\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProject("P");
	}
}
/*
 * Ensures that packages that are not in excluded part of prereq project are visible
 */
public void testExcludePartOfAnotherProject2() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P", "-**/internal/"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P");
		createFolder("/P/p/api");
		createFile(
			"/P/p/api/Y.js",
			"package p.api;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.api.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProject("P");
	}
}
/*
 * Ensures that included part of prereq project are visible
 */
public void testIncludePartOfAnotherProject1() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P", "+**/api/"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P");
		createFolder("/P/p/api");
		createFile(
			"/P/p/api/Y.js",
			"package p.api;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.api.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProject("P");
	}
}
/*
 * Ensures that packages that are not in included part of prereq project are not visible
 */
public void testIncludePartOfAnotherProject2() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P", "+**/api/|-**"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P");
		createFolder("/P/p/internal");
		createFile(
			"/P/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/X.java (at line 2)\n" + 
			"	public class X extends p.internal.Y {\n" + 
			"	                       ^^^^^^^^^^^^\n" + 
			"Access restriction: The type Y is not accessible due to restriction on required project P\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProject("P");
	}
}
/**
 * Start with no imports, add an import, and then append to the import name.
 */
public void testGrowImports() throws JavaScriptModelException {
	// no imports
	setWorkingCopyContents(
		"package p1;\n" +
		"public class X {\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	
	// add an import
	clearDeltas();
	setWorkingCopyContents(
		"package p1;\n" +
		"import p\n" +
		"public class X {\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"<import container>[+]: {}"
	);
		
	// append to import name
	clearDeltas();
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2\n" +
		"public class X {\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"<import container>[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	import p[-]: {}\n" + 
		"	import p2[+]: {}"
	);
}
/*
 * Ensures that a type matching a ignore-if-better non-accessible rule is further found when accessible
 * on another classpath entry.
 * (regression test for bug 98127 Access restrictions started showing up after switching to bundle)
 */
public void testIgnoreIfBetterNonAccessibleRule1() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P1", "?**/internal/", "/P2", "+**/internal/Y"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P1");
		createFolder("/P1/p/internal");
		createFile(
			"/P1/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		createJavaProject("P2");
		createFolder("/P2/p/internal");
		createFile(
			"/P2/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/*
 * Ensures that a type matching a ignore-if-better non-accessible rule is further found when accessible
 * on another classpath entry.
 * (regression test for bug 98127 Access restrictions started showing up after switching to bundle)
 */
public void testIgnoreIfBetterNonAccessibleRule2() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P1", "?**/internal/", "/P2", "~**/internal/Y"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P1");
		createFolder("/P1/p/internal");
		createFile(
			"/P1/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		createJavaProject("P2");
		createFolder("/P2/p/internal");
		createFile(
			"/P2/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. WARNING in /Reconciler/src/p1/X.java (at line 2)\n" + 
			"	public class X extends p.internal.Y {\n" + 
			"	                       ^^^^^^^^^^^^\n" + 
			"Discouraged access: The type Y is not accessible due to restriction on required project P2\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/*
 * Ensures that a type matching a ignore-if-better non-accessible rule is further found non-accessible
 * on another classpath entry.
 * (regression test for bug 98127 Access restrictions started showing up after switching to bundle)
 */
public void testIgnoreIfBetterNonAccessibleRule3() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P1", "?**/internal/", "/P2", "-**/internal/Y"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P1");
		createFolder("/P1/p/internal");
		createFile(
			"/P1/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		createJavaProject("P2");
		createFolder("/P2/p/internal");
		createFile(
			"/P2/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/X.java (at line 2)\n" + 
			"	public class X extends p.internal.Y {\n" + 
			"	                       ^^^^^^^^^^^^\n" + 
			"Access restriction: The type Y is not accessible due to restriction on required project P1\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/*
 * Ensures that a type matching a ignore-if-better non-accessible rule is found non-accessible
 * if no other classpath entry matches it.
 * (regression test for bug 98127 Access restrictions started showing up after switching to bundle)
 */
public void testIgnoreIfBetterNonAccessibleRule4() throws CoreException {
	IIncludePathEntry[] newEntries = createClasspath("Reconciler", new String[] {"/P1", "?**/internal/"});
	try {
		addClasspathEntries(newEntries, true);
		createJavaProject("P1");
		createFolder("/P1/p/internal");
		createFile(
			"/P1/p/internal/Y.js",
			"package p.internal;\n" +
			"public class Y {\n" +
			"}"
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X extends p.internal.Y {\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/X.java (at line 2)\n" + 
			"	public class X extends p.internal.Y {\n" + 
			"	                       ^^^^^^^^^^^^\n" + 
			"Access restriction: The type Y is not accessible due to restriction on required project P1\n" + 
			"----------\n"
		);
	} finally {
		removeClasspathEntries(newEntries);
		deleteProjects(new String[] {"P1"});
	}
}
/**
 * Introduces a syntax error in the modifiers of a method.
 */
public void testMethodWithError01() throws CoreException {
	// Introduce syntax error
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public.void foo() {\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta after syntax error", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" +
		"	foo()[*]: {MODIFIERS CHANGED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 4)\n" + 
		"	public.void foo() {\n" + 
		"	      ^\n" + 
		"Syntax error on token \".\", delete this token\n" + 
		"----------\n"
	);

	// Fix the syntax error
	clearDeltas();
	String contents =
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"  }\n" +
		"}";
	setWorkingCopyContents(contents);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta after fixing syntax error", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" +
		"	foo()[*]: {MODIFIERS CHANGED}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. WARNING in /Reconciler/src/p1/X.java (at line 2)\n" + 
		"	import p2.*;\n" + 
		"	       ^^\n" + 
		"The import p2 is never used\n" + 
		"----------\n"
	);
}
/**
 * Test reconcile force flag
 */
public void testMethodWithError02() throws CoreException {
	String contents =
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public.void foo() {\n" +
		"  }\n" +
		"}";		
	setWorkingCopyContents(contents);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);

	// use force flag to refresh problems			
	this.problemRequestor.initialize(contents.toCharArray());
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true, null, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 4)\n" + 
		"	public.void foo() {\n" + 
		"	      ^\n" + 
		"Syntax error on token \".\", delete this token\n" + 
		"----------\n"
	);
}

/**
 * Test reconcile force flag off
 */
public void testMethodWithError03() throws CoreException {
	String contents =
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public.void foo() {\n" +
		"  }\n" +
		"}";
	setWorkingCopyContents(contents);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);

	// reconcile with force flag turned off
	this.problemRequestor.initialize(contents.toCharArray());
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		""
	);
}
/**
 * Test reconcile force flag + cancel
 */
public void testMethodWithError04() throws CoreException {

	CancelingProblemRequestor myPbRequestor = new CancelingProblemRequestor();
	
	this.workingCopy.discardWorkingCopy();
	IJavaScriptUnit x = getCompilationUnit("Reconciler", "src", "p1", "X.js");
	this.problemRequestor = myPbRequestor;
	this.workingCopy = x.getWorkingCopy(new WorkingCopyOwner() {}, null);

	String contents =
		"package p1;\n" +
		"public class X {\n" +
		"	Zork f;	\n"+
		"	void foo(Zork z){\n"+
		"	}\n"+
		"}	\n";
	setWorkingCopyContents(contents);

	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);

	// use force flag to refresh problems			
	myPbRequestor.isCanceling = true;
	myPbRequestor.initialize(contents.toCharArray());
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true, null, myPbRequestor.progressMonitor);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 3)\n" + 
		"	Zork f;	\n" + 
		"	^^^^\n" + 
		"Zork cannot be resolved to a type\n" + 
		"----------\n"
	);
}

/**
 * Test reconcile force flag off
 */
public void testMethodWithError05() throws CoreException {
	try {
		createFolder("/Reconciler/src/tests");
		String contents =
			"package tests;	\n"+
			"abstract class AbstractSearchableSource extends AbstractSource implements SearchableSource {	\n"+
			"	abstract int indexOfImpl(long value);	\n"+
			"	public final int indexOf(long value) {	\n"+
			"		return indexOfImpl(value);	\n"+
			"	}	\n"+
			"}	\n";
		createFile(
			"/Reconciler/src/tests/AbstractSearchableSource.js", 
			contents);
	
		createFile(
			"/Reconciler/src/tests/Source.js", 
			"package tests;	\n"+
			"interface Source {	\n"+
			"	long getValue(int index);	\n"+
			"	int size();	\n"+
			"}	\n");
	
		createFile(
			"/Reconciler/src/tests/AbstractSource.js", 
			"package tests;	\n"+
			"abstract class AbstractSource implements Source {	\n"+
			"	AbstractSource() {	\n"+
			"	}	\n"+
			"	void invalidate() {	\n"+
			"	}	\n"+
			"	abstract long getValueImpl(int index);	\n"+
			"	abstract int sizeImpl();	\n"+
			"	public final long getValue(int index) {	\n"+
			"		return 0;	\n"+
			"	}	\n"+
			"	public final int size() {	\n"+
			"		return 0;	\n"+
			"	}	\n"+
			"}	\n");
	
		createFile(
			"/Reconciler/src/tests/SearchableSource.js", 
			"package tests;	\n"+
			"interface SearchableSource extends Source {	\n"+
			"	int indexOf(long value);	\n"+
			"}	\n");
	
		IJavaScriptUnit compilationUnit = getCompilationUnit("Reconciler", "src", "tests", "AbstractSearchableSource.js");
		ProblemRequestor pbReq =  new ProblemRequestor();
		IJavaScriptUnit wc = compilationUnit.getWorkingCopy(new WorkingCopyOwner() {}, null);
		pbReq.initialize(contents.toCharArray());
		startDeltas();
		wc.reconcile(IJavaScriptUnit.NO_AST, true, null, null);
		String actual = pbReq.problems.toString();
		String expected = 
			"----------\n" + 
			"----------\n";
		if (!expected.equals(actual)){
		 	System.out.println(Util.displayString(actual, 2));
		}
		assertEquals(
			"unexpected errors",
			expected,
			actual);
	} finally {
		deleteFile("/Reconciler/src/tests/AbstractSearchableSource.js");
		deleteFile("/Reconciler/src/tests/SearchableSource.js");
		deleteFile("/Reconciler/src/tests/Source.js");
		deleteFile("/Reconciler/src/tests/AbstractSource.js");
		deleteFolder("/Reconciler/src/tests");
	}
}
/*
 * Test that the creation of a working copy detects errors
 * (regression test for bug 33757 Problem not detected when opening a working copy)
 */
public void testMethodWithError06() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	try {
		String contents =
			"package p1;\n" +
			"public class Y {\n" +
			"  public.void foo() {\n" +
			"  }\n" +
			"}";
		createFile(
			"/Reconciler/src/p1/Y.js", 
			contents
		);
		this.problemRequestor =  new ProblemRequestor();
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy = getCompilationUnit("Reconciler/src/p1/Y.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/Y.java (at line 3)\n" + 
			"	public.void foo() {\n" + 
			"	      ^\n" + 
			"Syntax error on token \".\", delete this token\n" + 
			"----------\n"
		);
	} finally {
		deleteFile("/Reconciler/src/p1/Y.js");
	}
}
/*
 * Test that the opening of a working copy detects errors
 * (regression test for bug 33757 Problem not detected when opening a working copy)
 */
public void testMethodWithError07() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	try {
		String contents =
			"package p1;\n" +
			"public class Y {\n" +
			"  public.void foo() {\n" +
			"  }\n" +
			"}";
		createFile(
			"/Reconciler/src/p1/Y.js", 
			contents
		);
		this.problemRequestor =  new ProblemRequestor();
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy = getCompilationUnit("Reconciler/src/p1/Y.js").getWorkingCopy(new WorkingCopyOwner() {}, null);

		// Close working copy
		JavaModelManager.getJavaModelManager().removeInfoAndChildren((CompilationUnit)workingCopy); // use a back door as working copies cannot be closed
		
		// Reopen should detect syntax error
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy.open(null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler/src/p1/Y.java (at line 3)\n" + 
			"	public.void foo() {\n" + 
			"	      ^\n" + 
			"Syntax error on token \".\", delete this token\n" + 
			"----------\n"
		);
	} finally {
		deleteFile("/Reconciler/src/p1/Y.js");
	}
}
/*
 * Test that the units with similar names aren't presenting each other errors
 * (regression test for bug 39475)
 */
public void testMethodWithError08() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	try {
		createFile(
			"/Reconciler/src/p1/X01.js", 
			"package p1;\n" +
			"public abstract class X01 {\n" +
			"	public abstract void bar();	\n"+
			"  public abstract void foo(Zork z); \n"+
			"}"
		);
		String contents = 
			"package p2;\n" +
			"public class X01 extends p1.X01 {\n" +
			"	public void bar(){}	\n"+
			"}";
		createFile(
			"/Reconciler/src/p2/X01.js", 
			contents
		);
		this.problemRequestor =  new ProblemRequestor();
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy = getCompilationUnit("Reconciler/src/p2/X01.js").getWorkingCopy(new WorkingCopyOwner() {}, null);

		// Close working copy
		JavaModelManager.getJavaModelManager().removeInfoAndChildren((CompilationUnit)workingCopy); // use a back door as working copies cannot be closed
		
		// Reopen should detect syntax error
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy.open(null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n" // shouldn't report problem against p2.X01
		);
	} finally {
		deleteFile("/Reconciler/src/p1/X01.js");
		deleteFile("/Reconciler/src/p2/X01.js");
	}
}
/*
 * Scenario of reconciling using a working copy owner
 */
public void testMethodWithError09() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getCompilationUnit("/Reconciler/src/p1/X1.js").getWorkingCopy(owner, null);
		workingCopy1.getBuffer().setContents(
			"package p1;\n" +
			"public abstract class X1 {\n" +
			"	public abstract void bar();	\n"+
			"}"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler/src/p/X.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package p;\n" +
			"public class X extends p1.X1 {\n" +
			"	public void bar(){}	\n"+
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n" // shouldn't report problem against p.X
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
	}
}
/*
 * Scenario of reconciling using a working copy owner  (68557)
 */
public void testMethodWithError10() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		createFolder("/Reconciler15/src/test/cheetah");
		workingCopy1 = getCompilationUnit("/Reconciler15/src/test/cheetah/NestedGenerics.js").getWorkingCopy(owner, null);
		workingCopy1.getBuffer().setContents(
			"package test.cheetah;\n"+
			"import java.util.List;\n"+
			"import java.util.Stack;\n"+
			"public class NestedGenerics {\n"+
			"    Stack< List<Object>> stack = new Stack< List<Object> >();\n"+
			"}\n"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler15/src/test/cheetah/NestedGenericsTest.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package test.cheetah;\n"+
			"import java.util.Stack;\n"+
			"public class NestedGenericsTest {\n"+
			"    void test() {  \n"+
			"        Stack s = new NestedGenerics().stack;  \n"+
			"    }\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
		deleteFolder("/Reconciler15/src/test");
	}
}
/*
 * Scenario of reconciling using a working copy owner (68557)
 */
public void testMethodWithError11() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		createFolder("/Reconciler15/src/test/cheetah");
		workingCopy1 = getCompilationUnit("/Reconciler15/src/test/cheetah/NestedGenerics.js").getWorkingCopy(owner, null);
		workingCopy1.getBuffer().setContents(
			"package test.cheetah;\n"+
			"import java.util.*;\n"+
			"public class NestedGenerics {\n"+
			"    Map<List<Object>,String> map = null;\n"+
			"    Stack<List<Object>> stack2 = null;\n"+
			"    Map<List<Object>,List<Object>> map3 = null;\n"+
			"}\n"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler15/src/test/cheetah/NestedGenericsTest.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package test.cheetah;\n"+
			"import java.util.*;\n"+
			"public class NestedGenericsTest {\n"+
			"    void test() {  \n"+
			"        Map m = new NestedGenerics().map;  \n"+
			"		 Stack s2 = new NestedGenerics().stack2;    \n"+
			"        Map m3 = new NestedGenerics().map3;    \n"+
			"    }\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
		deleteFolder("/Reconciler15/src/test");
	}
}
/*
 * Scenario of reconciling using a working copy owner (68557 variation with wildcards)
 */
public void testMethodWithError12() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		createFolder("/Reconciler15/src/test/cheetah");
		workingCopy1 = getCompilationUnit("/Reconciler15/src/test/cheetah/NestedGenerics.js").getWorkingCopy(owner, null);
		workingCopy1.getBuffer().setContents(
			"package test.cheetah;\n"+
			"import java.util.*;\n"+
			"public class NestedGenerics {\n"+
			"    Map<List<?>,? super String> map = null;\n"+
			"    Stack<List<? extends Object>> stack2 = null;\n"+
			"    Map<List<Object[]>,List<Object>[]> map3 = null;\n"+
			"}\n"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler15/src/test/cheetah/NestedGenericsTest.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package test.cheetah;\n"+
			"import java.util.*;\n"+
			"public class NestedGenericsTest {\n"+
			"    void test() {  \n"+
			"        Map m = new NestedGenerics().map;  \n"+
			"		 Stack s2 = new NestedGenerics().stack2;    \n"+
			"        Map m3 = new NestedGenerics().map3;    \n"+
			"    }\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
		deleteFolder("/Reconciler15/src/test");
	}
}
/*
 * Scenario of reconciling using a working copy owner (68730)
 */
public void testMethodWithError13() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getCompilationUnit("/Reconciler15/src/test/X.js").getWorkingCopy(owner, null);
		createFolder("/Reconciler15/src/test");
		workingCopy1.getBuffer().setContents(
			"package test;\n"+
			"public class X <T extends String, U> {\n"+
			"	<Y1> void bar(Y1[] y) {}\n"+
			"	void bar2(Y<E3[]>[] ye[]) {}\n"+
			"    void foo(java.util.Map<Object[],String>.MapEntry<p.K<T>[],? super q.r.V8> m){}\n"+
			"    Class<? extends Object> getClass0() {}\n"+
			"    <E extends String> void pair (X<? extends E, U> e, T t){}\n"+
			"}\n"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler15/src/test/Y.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package test;\n"+
			"public class Y {\n"+
			"	void foo(){\n"+
			"		X someX = new X();\n"+
			"		someX.bar(null);\n"+
			"	}\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
		"----------\n" + 
		"1. WARNING in /Reconciler15/src/test/Y.java (at line 5)\n" + 
		"	someX.bar(null);\n" + 
		"	^^^^^^^^^^^^^^^\n" + 
		"Type safety: The method bar(Object[]) belongs to the raw type X. References to generic type X<T,U> should be parameterized\n" + 
		"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
		deleteFolder("/Reconciler15/src/test");
	}
}
/*
 * Scenario of reconciling using a working copy owner (66424)
 */
public void testMethodWithError14() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getCompilationUnit("/Reconciler15/src/test/X.js").getWorkingCopy(owner, null);
		createFolder("/Reconciler15/src/test");
		workingCopy1.getBuffer().setContents(
			"package test;\n"+
			"public class X <T> {\n"+
			"	<U> void bar(U u) {}\n"+
			"}\n"
		);
		workingCopy1.makeConsistent(null);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler15/src/test/Y.js").getWorkingCopy(owner, null);
		setWorkingCopyContents(
			"package test;\n"+
			"public class Y {\n"+
			"	void foo(){\n"+
			"		X someX = new X();\n"+
			"		someX.bar();\n"+
			"	}\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler15/src/test/Y.java (at line 5)\n" + 
			"	someX.bar();\n" + 
			"	      ^^^\n" + 
			"The method bar(Object) in the type X is not applicable for the arguments ()\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
		deleteFolder("/Reconciler15/src/test");
	}
}
/**
 * Ensures that the reconciler handles member move correctly.
 */
public void testMoveMember() throws JavaScriptModelException {
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"  }\n" +
		"  public void bar() {\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	clearDeltas();
	
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"  }\n" +
		"  public void foo() {\n" +
		"  }\n" +
		"}");
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta", 
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	bar()[*]: {REORDERED}\n" + 
		"	foo()[*]: {REORDERED}"
	);
}
/**
 * Ensures that the reconciler does nothing when the source
 * to reconcile with is the same as the current contents.
 */
public void testNoChanges1() throws JavaScriptModelException {
	setWorkingCopyContents(this.workingCopy.getSource());
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta",
		"[Working copy] X.java[*]: {CONTENT | FINE GRAINED}"
	);
}
/**
 * Ensures that the reconciler does nothing when the source
 * to reconcile with has the same structure as the current contents.
 */
public void testNoChanges2() throws JavaScriptModelException {
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"    System.out.println()\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta",
		"[Working copy] X.java[*]: {CONTENT | FINE GRAINED}"
	);
}
/*
 * Ensures that using a non-generic method with no parameter and with a raw receiver type doesn't create a type safety warning
 * (regression test for bug 105756 [1.5][model] Incorrect warning on using raw types)
 */
public void testRawUsage() throws CoreException {
	IJavaScriptUnit otherCopy = null;
	try {
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		otherCopy = getWorkingCopy(
			"Reconciler15/src/Generic105756.js", 
			"public class Generic105756<T> {\n" +
			"  void foo() {}\n" +
			"}",
			owner,
			false/*don't compute problems*/);
		setUp15WorkingCopy("/Reconciler15/src/X.js", owner);
		setWorkingCopyContents(
			"public class X {\n" +
			"  void bar(Generic105756 g) {\n" +
			"    g.foo();\n" +
			"  }\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (otherCopy != null)
			otherCopy.discardWorkingCopy();
	}
}
/*
 * Ensures that a reconcile participant is notified when a working copy is reconciled.
 */
public void testReconcileParticipant01() throws CoreException {
	ReconcileParticipant participant = new ReconcileParticipant();
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"    System.out.println()\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected participant delta",
		"[Working copy] X.java[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"		bar()[+]: {}\n" + 
		"		foo()[-]: {}",
		participant.delta
	);
}
/*
 * Ensures that a reconcile participant is not notified if not participating.
 */
public void testReconcileParticipant02() throws CoreException {
	ReconcileParticipant participant = new ReconcileParticipant(){
		public boolean isActive(IJavaScriptProject project) {
			return false;
		}
	};
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"    System.out.println()\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected participant delta",
		"<null>",
		participant.delta
	);
}
/*
 * Ensures that a reconcile participant is notified with the correct AST.
 */
public void testReconcileParticipant03() throws CoreException {
	ReconcileParticipant participant = new ReconcileParticipant();
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"    System.out.println()\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertASTNodeEquals(
		"Unexpected participant delta",
		"package p1;\n" + 
		"import p2.*;\n" + 
		"public class X {\n" + 
		"  public void bar(){\n" + 
		"  }\n" + 
		"}\n",
		participant.ast
	);
}
/*
 * Ensures that the same AST as the one a reconcile participant requested is reported.
 */
public void testReconcileParticipant04() throws CoreException {
	ReconcileParticipant participant = new ReconcileParticipant();
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"    System.out.println()\n" +
		"  }\n" +
		"}"
	);
	org.eclipse.wst.jsdt.core.dom.JavaScriptUnit ast = this.workingCopy.reconcile(AST.JLS3, false, null, null);
	assertSame(
		"Unexpected participant delta",
		participant.ast,
		ast
	);
}
/*
 * Ensures that a participant can fix an error during reconcile.
 */
public void testReconcileParticipant05() throws CoreException {
	new ReconcileParticipant() {
		public void reconcile(ReconcileContext context) {
			try {
				setWorkingCopyContents(
					"package p1;\n" +
					"public class X {\n" +
					"  public void bar() {\n" +
					"  }\n" +
					"}"
				);
				context.resetAST();
			} catch (JavaScriptModelException e) {
				e.printStackTrace();
			}
		}
	};
	setWorkingCopyContents(
		"package p1;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"    toString()\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"----------\n"
	);
}
/*
 * Ensures that a participant can introduce an error during reconcile.
 */
public void testReconcileParticipant06() throws CoreException {
	new ReconcileParticipant() {
		public void reconcile(ReconcileContext context) {
			try {
				setWorkingCopyContents(
					"package p1;\n" +
					"public class X {\n" +
					"  public void bar() {\n" +
					"    toString()\n" +
					"  }\n" +
					"}"
				);
				context.resetAST();
			} catch (JavaScriptModelException e) {
				e.printStackTrace();
			}
		}
	};
	setWorkingCopyContents(
		"package p1;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 4)\n" + 
		"	toString()\n" + 
		"	         ^\n" + 
		"Syntax error, insert \";\" to complete BlockStatements\n" + 
		"----------\n"
	);
}
/*
 * Ensures that a reconcile participant is NOT notified when a working copy is reconciled
 * in a project with insufficient source level.
 * (regression test for bug 125291 Enable conditional loading of APT)
 */
public void testReconcileParticipant07() throws CoreException {
	IJavaScriptProject project = this.workingCopy.getJavaScriptProject();
	String originalSourceLevel = project.getOption(JavaScriptCore.COMPILER_SOURCE, true);
	try {
		project.setOption(JavaScriptCore.COMPILER_SOURCE, JavaScriptCore.VERSION_1_1);
		ReconcileParticipant participant = new ReconcileParticipant();
		setWorkingCopyContents(
			"package p1;\n" +
			"import p2.*;\n" +
			"public class X {\n" +
			"  public void bar() {\n" +
			"    System.out.println()\n" +
			"  }\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertDeltas(
			"Unexpected participant delta",
			"<null>",
			participant.delta
		);
	} finally {
		project.setOption(JavaScriptCore.COMPILER_SOURCE, originalSourceLevel);
	}
}
/*
 * Ensures that a problem reporting session is not started during reconcile if a participant reports an error
 * and if the working copy is already consistent and the forceProblemDetection flag is false.
 * (regression test for bug 154170 Printing warnings breaks in-editor quick fixes)
 */
public void testReconcileParticipant08() throws CoreException {
	// set working copy contents and ensure it is consistent
	String contents = 
		"package p1;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"  }\n" +
		"}";
	setWorkingCopyContents(contents);
	this.workingCopy.makeConsistent(null);
	this.problemRequestor.initialize(contents.toCharArray());
	
	// reconcile with a participant adding a list of problems
	new ReconcileParticipant() {
		public void reconcile(ReconcileContext context) {
			context.putProblems("test.marker", new CategorizedProblem[] {});
		}
	};
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		""
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents, updating the structure of this reconciler's compilation
 * unit, and fires the Java element deltas for the structural changes
 * of a renaming a method; the original method deleted and the new method added structurally.
 */
public void testRenameMethod1() throws JavaScriptModelException {
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar() {\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta",
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	bar()[+]: {}\n" + 
		"	foo()[-]: {}"
	);
}
/**
 * Ensures that the reconciler reconciles the new contents with the current
 * contents,updating the structure of this reconciler's compilation
 * unit, and fires the Java element delta for the structural changes
 * of the addition of a portion of a new method.
 */
public void testRenameWithSyntaxError() throws JavaScriptModelException {
	setWorkingCopyContents(
		"package p1;\n" +
		"import p2.*;\n" +
		"public class X {\n" +
		"  public void bar( {\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Unexpected delta",
		"X[*]: {CHILDREN | FINE GRAINED}\n" + 
		"	bar()[+]: {}\n" + 
		"	foo()[-]: {}"
	);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 4)\n" + 
		"	public void bar( {\n" + 
		"	               ^\n" + 
		"Syntax error, insert \")\" to complete FunctionDeclaration\n" + 
		"----------\n"
	);
}
/*
 * Ensure that warning are suppressed by an @SuppressWarnings annotation.
 */
//https://bugs.eclipse.org/bugs/show_bug.cgi?id=95056
public void testSuppressWarnings1() throws JavaScriptModelException {
	IJavaScriptUnit otherCopy = null;
	try {
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		otherCopy = getWorkingCopy(
			"/Reconciler15/src/X.js",
	        "@Deprecated\n" + 
	        "public class X {\n" + 
	        "   void foo(){}\n" +
	        "}\n",
			owner,
			false/*don't compute problems*/);
		setUp15WorkingCopy("/Reconciler15/src/Y.js", owner);
		setWorkingCopyContents(
	        "public class Y extends X {\n" + 
	        "  @SuppressWarnings(\"all\")\n" +
	        "   void foo(){ super.foo(); }\n" +
	        "   Zork z;\n" +
	        "}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. WARNING in /Reconciler15/src/Y.java (at line 1)\n" + 
			"	public class Y extends X {\n" + 
			"	                       ^\n" + 
			"The type X is deprecated\n" + 
			"----------\n" + 
			"2. ERROR in /Reconciler15/src/Y.java (at line 4)\n" + 
			"	Zork z;\n" + 
			"	^^^^\n" + 
			"Zork cannot be resolved to a type\n" + 
			"----------\n");
	} finally {
		if (otherCopy != null)
			otherCopy.discardWorkingCopy();
	}
}
/*
 * Ensure that warning are suppressed by an @SuppressWarning annotation.
 */
public void testSuppressWarnings2() throws JavaScriptModelException {
	IJavaScriptUnit otherCopy = null;
	try {
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		otherCopy = getWorkingCopy(
			"/Reconciler15/src/java/util/List.js",
			"package java.util;\n" +
	        "public interface List<E> {\n" + 
	        "}\n",
			owner,
			false/*don't compute problems*/);
		setUp15WorkingCopy("/Reconciler15/src/X.js", owner);
		setWorkingCopyContents(
            "import java.util.List;\n" + 
            "\n" + 
            "public class X {\n" + 
            "    void foo(List list) {\n" + 
            "        List<String> ls1 = list;\n" + 
            "    }\n" + 
            "    @SuppressWarnings(\"unchecked\")\n" + 
            "    void bar(List list) {\n" + 
            "        List<String> ls2 = list;\n" + 
            "    }\n" + 
            "   Zork z;\n" +
            "}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. WARNING in /Reconciler15/src/X.java (at line 5)\n" + 
			"	List<String> ls1 = list;\n" + 
			"	                   ^^^^\n" + 
			"Type safety: The expression of type List needs unchecked conversion to conform to List<String>\n" + 
			"----------\n" + 
			"2. ERROR in /Reconciler15/src/X.java (at line 11)\n" + 
			"	Zork z;\n" + 
			"	^^^^\n" + 
			"Zork cannot be resolved to a type\n" + 
			"----------\n"
		);
	} finally {
		if (otherCopy != null)
			otherCopy.discardWorkingCopy();
	}
}
/*
 * Ensure that warning are suppressed by an @SuppressWarning annotation.
 */
public void testSuppressWarnings3() throws JavaScriptModelException {
	IJavaScriptUnit otherCopy = null;
	try {
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		otherCopy = getWorkingCopy(
			"/Reconciler15/src/java/util/HashMap.js",
			"package java.util;\n" +
	        "public class HashMap implements Map {\n" + 
	        "}\n",
			owner,
			false/*don't compute problems*/);
		setUp15WorkingCopy("/Reconciler15/src/X.js", owner);
		setWorkingCopyContents(
			"import java.util.*;\n" + 
			"@SuppressWarnings(\"unchecked\")\n" + 
			"public class X {\n" + 
			"	void foo() {\n" + 
			"		Map<String, String>[] map = new HashMap[10];\n" + 
			"	}\n" + 
            "   Zork z;\n" +				
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. ERROR in /Reconciler15/src/X.java (at line 7)\n" + 
			"	Zork z;\n" + 
			"	^^^^\n" + 
			"Zork cannot be resolved to a type\n" + 
			"----------\n"
		);
	} finally {
		if (otherCopy != null)
			otherCopy.discardWorkingCopy();
	}
}
/*
 * Ensure that warning are suppressed by an @SuppressWarnings annotation.
 */
public void testSuppressWarnings4() throws JavaScriptModelException {
	IJavaScriptUnit otherCopy = null;
	try {
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		otherCopy = getWorkingCopy(
			"/Reconciler15/src/X.js",
	        "/** @deprecated */\n" + 
	        "public class X {\n" + 
	        "   void foo(){}\n" +
	        "}\n",
			owner,
			false/*don't compute problems*/);
		setUp15WorkingCopy("/Reconciler15/src/Y.js", owner);
		setWorkingCopyContents(
	        "public class Y extends X {\n" + 
	        "  @SuppressWarnings(\"all\")\n" +
	        "   void foo(){ super.foo(); }\n" +
	        "   Zork z;\n" +
	        "}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"1. WARNING in /Reconciler15/src/Y.java (at line 1)\n" + 
			"	public class Y extends X {\n" + 
			"	                       ^\n" + 
			"The type X is deprecated\n" + 
			"----------\n" + 
			"2. ERROR in /Reconciler15/src/Y.java (at line 4)\n" + 
			"	Zork z;\n" + 
			"	^^^^\n" + 
			"Zork cannot be resolved to a type\n" + 
			"----------\n");
	} finally {
		if (otherCopy != null)
			otherCopy.discardWorkingCopy();
	}
}
/**
 * Ensure that an unhandled exception is detected.
 */
public void testUnhandledException() throws JavaScriptModelException {
	setWorkingCopyContents(
		"package p1;\n" +
		"public class X {\n" +
		"  public void foo() {\n" +
		"    throw new Exception();\n" +
		"  }\n" +
		"}"
	);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"1. ERROR in /Reconciler/src/p1/X.java (at line 4)\n" + 
		"	throw new Exception();\n" + 
		"	^^^^^^^^^^^^^^^^^^^^^^\n" + 
		"Unhandled exception type Exception\n" + 
		"----------\n"
	);
}
/**
 * Check that forcing a make consistent action is leading the next reconcile to not notice changes.
 */
public void testMakeConsistentFoolingReconciler() throws JavaScriptModelException {
	setWorkingCopyContents("");
	this.workingCopy.makeConsistent(null);
	this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertDeltas(
		"Should have got NO delta", 
		""
	);
}
/**
 * Test bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=60689
 * AST on reconcile: AST without Javadoc comments created
 * @deprecated using deprecated code
 */
public void testBug60689() throws JavaScriptModelException {
	setWorkingCopyContents("public class X {\n" +
		"	/**\n" +
		"	 * Returns the length of the string representing the number of \n" +
		"	 * indents in the given string <code>line</code>. Returns \n" +
		"	 * <code>-1<code> if the line isn't prefixed with an indent of\n" +
		"	 * the given number of indents. \n" +
		"	 */\n" +
		"	public static int computeIndentLength(String line, int numberOfIndents, int tabWidth) {\n" +
		"		return 0;\n" +
		"}"
	);
	org.eclipse.wst.jsdt.core.dom.JavaScriptUnit testCU = this.workingCopy.reconcile(AST.JLS2, true, null, null);
	assertNotNull("We should have a comment!", testCU.getCommentList());
	assertEquals("We should have 1 comment!", 1, testCU.getCommentList().size());
	testCU = this.workingCopy.reconcile(AST.JLS2, true, null, null);
	assertNotNull("We should have a comment!", testCU.getCommentList());
	assertEquals("We should have one comment!", 1, testCU.getCommentList().size());
}
/*
 * Ensures that a working copy in a 1.4 project that references a 1.5 project can be reconciled without error.
 * (regression test for bug 98434 A non-1.5 project with 1.5 projects in the classpath does not show methods with generics)
 */
public void testTwoProjectsWithDifferentCompliances() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	try {
		createJavaProject("P1", new String[] {""}, new String[] {"JCL15_LIB"}, "", "1.5");
		createFolder("/P1/p");
		createFile(
			"/P1/p/X.js",
			"package p;\n" +
			"public class X {\n" +
			"  void foo(Class<String> c) {\n" +
			"  }\n" +
			"}"
		);
		
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] {"/P1"}, "", "1.4");
		createFolder("/P2/p");
		WorkingCopyOwner owner = new WorkingCopyOwner() {};
		this.workingCopy = getWorkingCopy("/P2/p/Y.js", "", owner, this.problemRequestor);
		setWorkingCopyContents(
			"package p;\n" +
			"public class Y {\n" +
			"  void bar(Class c) {\n" +
			"    new X().foo(c);\n" +
			"  }\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true/*force pb detection*/, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		deleteProjects(new String[] {"P1", "P2"});
	}
}
/*
 * Ensures that a method that has a type parameter with bound can be overriden in another working copy.
 * (regression test for bug 76780 [model] return type not recognized correctly on some generic methods)
 */
public void testTypeParameterWithBound() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getWorkingCopy(
			"/Reconciler15/src/test/I.js",
			"package test;\n"+
			"public interface I {\n"+
			"	<T extends I> void foo(T t);\n"+
			"}\n",
			owner,
			null /*no problem requestor*/
		);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getWorkingCopy("Reconciler15/src/test/X.js", "", owner, this.problemRequestor);
		setWorkingCopyContents(
			"package test;\n"+
			"public class X implements I {\n"+
			"	public <T extends I> void foo(T t) {\n"+
			"	}\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
	}
}
/*
 * Ensures that a method that has a type parameter starting with $ can be reconciled against.
 * (regression test for bug 91709 [1.5][model] Quick Fix Error but no Problem Reported)
 */
public void testTypeParameterStartingWithDollar() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getWorkingCopy(
			"/Reconciler15/src/test/Y.js",
			"package test;\n"+
			"public class Y<$T> {\n"+
			"	void foo($T t);\n"+
			"}\n",
			owner,
			null /*no problem requestor*/
		);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getWorkingCopy("Reconciler15/src/test/X.js", "", owner, this.problemRequestor);
		setWorkingCopyContents(
			"package test;\n"+
			"public class X {\n"+
			"	public void bar() {\n"+
			"    new Y<String>().foo(\"\");\n" +
			"	}\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
	}
}
/*
 * Ensures that a working copy with a type with a dollar name can be reconciled without errors.
 * (regression test for bug 117121 Can't create class called A$B in eclipse)
 */
public void testTypeWithDollarName() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	try {
		String contents =
			"package p1;\n" +
			"public class Y$Z {\n" +
			"}";
		createFile(
			"/Reconciler/src/p1/Y$Z.js", 
			contents
		);
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getCompilationUnit("Reconciler/src/p1/Y$Z.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		
		this.problemRequestor.initialize(contents.toCharArray());
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, true, null, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		deleteFile("/Reconciler/src/p1/Y$Z.js");
	}
}
/*
 * Ensures that a working copy with a type with a dollar name can be reconciled against without errors.
 * (regression test for bug 125301 Handling of classes with $ in class name.)
 */
public void testTypeWithDollarName2() throws CoreException {
	IJavaScriptUnit workingCopy2 = null; 
	try {
		WorkingCopyOwner owner = this.workingCopy.getOwner();
		workingCopy2 = getWorkingCopy(
			"/Reconciler/src/p1/Y$Z.js",
			"package p1;\n" +
			"public class Y$Z {\n" +
			"}",
			owner,
			false/*don't compute problems*/
		);
		setWorkingCopyContents(
			"package p1;\n" +
			"public class X {\n" +
			"  Y$Z field;\n" +
			"}"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);
		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy2 != null)
			workingCopy2.discardWorkingCopy();
	}
}
/*
 * Ensures that a varargs method can be referenced from another working copy.
 */
public void testVarargs() throws CoreException {
	this.workingCopy.discardWorkingCopy(); // don't use the one created in setUp()
	this.workingCopy = null;
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit workingCopy1 = null;
	try {
		workingCopy1 = getWorkingCopy(
			"/Reconciler15/src/test/X.js",
			"package test;\n"+
			"public class X {\n"+
			"	void bar(String ... args) {}\n"+
			"}\n",
			owner,
			null /*no problem requestor*/
		);
		
		this.problemRequestor =  new ProblemRequestor();
		this.workingCopy = getWorkingCopy("Reconciler15/src/test/Y.js", "", owner, this.problemRequestor);
		setWorkingCopyContents(
			"package test;\n"+
			"public class Y {\n"+
			"	void foo(){\n"+
			"		X someX = new X();\n"+
			"		someX.bar(\"a\", \"b\");\n"+
			"	}\n"+
			"}\n"
		);
		this.workingCopy.reconcile(IJavaScriptUnit.NO_AST, false, owner, null);

		assertProblems(
			"Unexpected problems",
			"----------\n" + 
			"----------\n"
		);
	} finally {
		if (workingCopy1 != null) {
			workingCopy1.discardWorkingCopy();
		}
	}
}

/**
 * Bug 114338:[javadoc] Reconciler reports wrong javadoc warning (missing return type)
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=114338"
 *
 */
public void testBug114338() throws CoreException {
	// Set initial CU content
	setWorkingCopyContents(
		"package p1;\n" +
		"public class X {\n" + 
		"	/**\n" + 
		"	 * @return a\n" + 
		"	 */\n" + 
		"	boolean get() {\n" + 
		"		return false;\n" + 
		"	}\n" + 
		"}");
	this.workingCopy.reconcile(AST.JLS3, true, this.wcOwner, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"----------\n"
	);

	// Modify content
	String contents =
		"package p1;\n" +
		"public class X {\n" + 
		"	/**\n" + 
		"	 * @return boolean\n" + 
		"	 */\n" + 
		"	boolean get() {\n" + 
		"		return false;\n" + 
		"	}\n" + 
		"}";
	setWorkingCopyContents(contents);
	this.workingCopy.reconcile(AST.JLS3, true, this.wcOwner, null);
	assertProblems(
		"Unexpected problems",
		"----------\n" + 
		"----------\n"
	);
}

/**
 * Bug 36032:[plan] JavaProject.findType() fails to find second type in source file
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=36032"
 */
public void testBug36032a() throws CoreException, InterruptedException {
	try {
		// Resources creation
		createJavaProject("P", new String[] {""}, new String[] {"JCL_LIB"});
		String source = 
			"public class Test {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new SFoo().foo();\n" + 
			"	}\n" + 
			"}\n";
		this.createFile(
			"/P/Foo.js", 
			"class SFoo { void foo() {} }\n"
		);
		this.createFile(
			"/P/Test.js", 
			source
		);
		
		// Get compilation unit and reconcile it
		char[] sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopy = getCompilationUnit("/P/Test.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		this.workingCopy.getBuffer().setContents(source);
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertNoProblem(sourceChars, this.workingCopy);

		// Add new secondary type
		this.createFile(
			"/P/Bar.js", 
			"class SBar{ void bar() {} }\n"
		);
		source = 
			"public class Test {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new SFoo().foo();\n" + 
			"		new SBar().bar();\n" + 
			"	}\n" + 
			"}\n";
		
		// Reconcile with modified source
		sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopy.getBuffer().setContents(source);
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertNoProblem(sourceChars, this.workingCopy);
	} finally {
		deleteProject("P");
	}
}
public void testBug36032b() throws CoreException, InterruptedException {
	try {
		// Resources creation
		createJavaProject("P", new String[] {""}, new String[] {"JCL_LIB"});
		String source = 
			"public class Test {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new SFoo().foo();\n" + 
			"		new SBar().bar();\n" + 
			"	}\n" + 
			"}\n";
		createFile(
			"/P/Foo.js", 
			"class SFoo { void foo() {} }\n"
		);
		createFile(
			"/P/Test.js", 
			source
		);
		createFile(
			"/P/Bar.js", 
			"class SBar{ void bar() {} }\n"
		);
		
		// Get compilation unit and reconcile it
		char[] sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopy = getCompilationUnit("/P/Test.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		this.workingCopy.getBuffer().setContents(source);
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertNoProblem(sourceChars, this.workingCopy);

		// Delete secondary type => should get a problem
		waitUntilIndexesReady();
		deleteFile("/P/Bar.js");
		this.problemRequestor.initialize(source.toCharArray());
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertEquals("Working copy should not find secondary type 'Bar'!", 1, this.problemRequestor.problemCount);
		assertProblems("Working copy should have problem!",
			"----------\n" +
			"1. ERROR in /P/Test.java (at line 4)\n" +
			"	new SBar().bar();\n" +
			"	    ^^^^\n" +
			"SBar cannot be resolved to a type\n" +
			"----------\n"
		);

		// Fix the problem
		source = 
			"public class Test {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new SFoo().foo();\n" + 
			"	}\n" + 
			"}\n";
		sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopy.getBuffer().setContents(source);
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertNoProblem(sourceChars, this.workingCopy);
	} finally {
		deleteProject("P");
	}
}
// Secondary types used through multiple projects
public void testBug36032c() throws CoreException, InterruptedException {
	try {
		// Create first project
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		createFolder("/P1/test");
		createFile(
			"/P1/test/Foo.js", 
			"package test;\n" +
			"class Secondary{ void foo() {} }\n"
		);
		createFile(
			"/P1/test/Test1.js", 
			"package test;\n" +
			"public class Test1 {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new Secondary().foo();\n" + 
			"	}\n" + 
			"}\n"
		);

		// Create second project
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		String source = 
			"package test;\n" +
			"public class Test2 {\n" + 
			"	public static void main(String[] args) {\n" + 
			"		new Secondary().foo();\n" + 
			"	}\n" + 
			"}\n";
		createFolder("/P2/test");
		createFile(
			"/P2/test/Test2.js", 
			source
		);
		
		// Get compilation unit and reconcile it => expect no error
		char[] sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopy = getCompilationUnit("/P2/test/Test2.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		this.workingCopy.getBuffer().setContents(source);
		this.workingCopy.reconcile(AST.JLS3, true, null, null);
		assertNoProblem(sourceChars, this.workingCopy);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
/**
 * Bug 118823: [model] Secondary types cache not reset while removing _all_ secondary types from CU
 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=118823"
 */
public void testBug118823() throws CoreException, InterruptedException, IOException {
	try {
		// Resources creation
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		String source = "class Test {}\n";
		createFile(
			"/P1/Test.js", 
			source
		);
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		String source2 = 
			"class A {\n" +
			"	Secondary s;\n" +
			"}\n";
		createFile(
			"/P2/A.js",
			source2
		);
		waitUntilIndexesReady();
		this.workingCopies = new IJavaScriptUnit[2];
		this.wcOwner = new WorkingCopyOwner() {};

		// Get first working copy and verify that there's no error
		char[] sourceChars = source.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0] = getCompilationUnit("/P1/Test.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's one error (missing secondary type)
		this.problemRequestor.initialize(source2.toCharArray());
		this.workingCopies[1] = getCompilationUnit("/P2/A.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertEquals("Working copy should not find secondary type 'Secondary'!", 1, this.problemRequestor.problemCount);
		assertProblems("Working copy should have problem!",
			"----------\n" +
			"1. ERROR in /P2/A.java (at line 2)\n" +
			"	Secondary s;\n" +
			"	^^^^^^^^^\n" +
			"Secondary cannot be resolved to a type\n" +
			"----------\n"
		);

		// Delete file and recreate it with secondary
		final String source1 = 
			"public class Test {}\n" + 
			"class Secondary{}\n";
		getWorkspace().run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					deleteFile("/P1/Test.js");
					createFile(
						"/P1/Test.js", 
						source1
					);
				}
			},
			null
		);

		// Get first working copy and verify that there's still no error
		sourceChars = source1.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0].getBuffer().setContents(source1);
		this.workingCopies[0].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's any longer error
		sourceChars = source2.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[1].getBuffer().setContents(source2);
		this.workingCopies[1].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		assertNoProblem(sourceChars, this.workingCopies[1]);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
public void testBug118823b() throws CoreException, InterruptedException {
	try {
		// Resources creation
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		String source1 = "class Test {}\n";
		createFile(
			"/P1/Test.js", 
			source1
		);
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		String source2 = 
			"class A {\n" +
			"	Secondary s;\n" +
			"}\n";
		createFile(
			"/P2/A.js",
			source2
		);
		waitUntilIndexesReady();
		this.workingCopies = new IJavaScriptUnit[2];

		// Get first working copy and verify that there's no error
		char[] sourceChars = source1.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0] = getCompilationUnit("/P1/Test.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's one error (missing secondary type)
		this.problemRequestor.initialize(source2.toCharArray());
		this.workingCopies[1] = getCompilationUnit("/P2/A.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertEquals("Working copy should not find secondary type 'Secondary'!", 1, this.problemRequestor.problemCount);
		assertProblems("Working copy should have problem!",
			"----------\n" +
			"1. ERROR in /P2/A.java (at line 2)\n" +
			"	Secondary s;\n" +
			"	^^^^^^^^^\n" +
			"Secondary cannot be resolved to a type\n" +
			"----------\n"
		);

		// Modify first working copy and verify that there's still no error
		source1 = 
			"public class Test {}\n" + 
			"class Secondary{}\n";
		sourceChars = source1.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0].getBuffer().setContents(source1);
		this.workingCopies[0].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		this.workingCopies[0].commitWorkingCopy(true, null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's any longer error
		sourceChars = source2.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[1].getBuffer().setContents(source2);
		this.workingCopies[1].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		assertNoProblem(sourceChars, this.workingCopies[1]);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}
public void testBug118823c() throws CoreException, InterruptedException {
	try {
		// Resources creation
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		String source1 = "class Test {}\n";
		createFile(
			"/P1/Test.js", 
			source1
		);
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		String source2 = 
			"class A {\n" +
			"	Secondary s;\n" +
			"}\n";
		createFile(
			"/P2/A.js",
			source2
		);
		waitUntilIndexesReady();
		this.workingCopies = new IJavaScriptUnit[2];
		this.wcOwner = new WorkingCopyOwner() {};

		// Get first working copy and verify that there's no error
		char[] sourceChars = source1.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0] = getCompilationUnit("/P1/Test.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's one error (missing secondary type)
		this.problemRequestor.initialize(source2.toCharArray());
		this.workingCopies[1] = getCompilationUnit("/P2/A.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertEquals("Working copy should not find secondary type 'Secondary'!", 1, this.problemRequestor.problemCount);
		assertProblems("Working copy should have problem!",
			"----------\n" +
			"1. ERROR in /P2/A.java (at line 2)\n" +
			"	Secondary s;\n" +
			"	^^^^^^^^^\n" +
			"Secondary cannot be resolved to a type\n" +
			"----------\n"
		);

		// Delete file and recreate it with secondary
		deleteFile("/P1/Test.js");
		source1 = 
			"public class Test {}\n" + 
			"class Secondary{}\n";
		createFile(
			"/P1/Test.js", 
			source1
		);

		// Get first working copy and verify that there's still no error
		sourceChars = source1.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[0].getBuffer().setContents(source1);
		this.workingCopies[0].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		this.workingCopies[0].commitWorkingCopy(true, null);
		assertNoProblem(sourceChars, this.workingCopies[0]);

		// Get second working copy and verify that there's any longer error
		sourceChars = source2.toCharArray();
		this.problemRequestor.initialize(sourceChars);
		this.workingCopies[1].getBuffer().setContents(source2);
		this.workingCopies[1].reconcile(AST.JLS3,
			true, // force problem detection to see errors if any
			null,	// do not use working copy owner to not use working copies in name lookup
			null);
		assertNoProblem(sourceChars, this.workingCopies[1]);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
	}
}

// https://bugs.eclipse.org/bugs/show_bug.cgi?id=107931
// won't be fixed; this test watches the current behavior in case we change
// our mind
public void test1001() throws CoreException, InterruptedException, IOException {
	try {
		// Resources creation
		String sources[] = new String[3];
		char[] sourcesAsCharArrays[] = new char[3][];
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		sources[0] = "class X {}\n";
		createFile(
			"/P1/X.js", 
			sources[0]
		);
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		sources[1] = 
			"interface I {\n" +
			"  void foo();\n" +
			"  void bar(X p);\n" +
			"}\n";
		createFile(
			"/P2/I.js",
			sources[1]
		);
		createJavaProject("P3", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P2" });
		sources[2] = 
			"class Y implements I {\n" +
			"  // public void foo() { }\n" +
			"  // public void bar(X p) { }\n" +
			"}\n";
		createFile(
			"/P3/Y.js",
			sources[2]
		);
		for (int i = 0 ; i < sources.length ; i++) {
			sourcesAsCharArrays[i] = sources[i].toCharArray();
		}
		waitUntilIndexesReady();
		this.workingCopies = new IJavaScriptUnit[3];
		this.wcOwner = new WorkingCopyOwner() {};

		// Get first working copy and verify that there's no error
		this.problemRequestor.initialize(sourcesAsCharArrays[0]);
		this.workingCopies[0] = getCompilationUnit("/P1/X.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourcesAsCharArrays[0], this.workingCopies[0]);

		// Get second working copy and verify that there's no error
		this.problemRequestor.initialize(sourcesAsCharArrays[1]);
		this.workingCopies[1] = getCompilationUnit("/P2/I.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourcesAsCharArrays[1], this.workingCopies[1]);

		// Get third working copy and verify that all expected errors are here
		this.problemRequestor.initialize(sourcesAsCharArrays[2]);
		this.workingCopies[2] = getCompilationUnit("/P3/Y.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertProblems("Working copy should have problems:",
			"----------\n" + 
			"1. ERROR in /P3/Y.java (at line 1)\n" + 
			"	class Y implements I {\n" + 
			"	      ^\n" +
// we miss the first diagnostic - see justification in bugzilla
//			"The type Y must implement the inherited abstract method I.bar(X)\n" + 
//			"----------\n" + 
//			"2. ERROR in /P3/Y.java (at line 1)\n" + 
//			"	class Y implements I {\n" + 
//			"	      ^\n" + 
			"The type Y must implement the inherited abstract method I.foo()\n" + 
			"----------\n"
		);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
		deleteProject("P3");
	}
}

// https://bugs.eclipse.org/bugs/show_bug.cgi?id=107931
// variant: having all needed projects on the classpath solves the issue
public void test1002() throws CoreException, InterruptedException, IOException {
	try {
		// Resources creation
		String sources[] = new String[3];
		char[] sourcesAsCharArrays[] = new char[3][];
		createJavaProject("P1", new String[] {""}, new String[] {"JCL_LIB"});
		sources[0] = "class X {}\n";
		createFile(
			"/P1/X.js", 
			sources[0]
		);
		createJavaProject("P2", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" });
		sources[1] = 
			"interface I {\n" +
			"  void foo();\n" +
			"  void bar(X p);\n" +
			"}\n";
		createFile(
			"/P2/I.js",
			sources[1]
		);
		createJavaProject("P3", new String[] {""}, new String[] {"JCL_LIB"}, new String[] { "/P1" /* compare with test1001 */, "/P2" });
		sources[2] = 
			"class Y implements I {\n" +
			"  // public void foo() { }\n" +
			"  // public void bar(X p) { }\n" +
			"}\n";
		createFile(
			"/P3/Y.js",
			sources[2]
		);
		for (int i = 0 ; i < sources.length ; i++) {
			sourcesAsCharArrays[i] = sources[i].toCharArray();
		}
		waitUntilIndexesReady();
		this.workingCopies = new IJavaScriptUnit[3];
		this.wcOwner = new WorkingCopyOwner() {};

		// Get first working copy and verify that there's no error
		this.problemRequestor.initialize(sourcesAsCharArrays[0]);
		this.workingCopies[0] = getCompilationUnit("/P1/X.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourcesAsCharArrays[0], this.workingCopies[0]);

		// Get second working copy and verify that there's no error
		this.problemRequestor.initialize(sourcesAsCharArrays[1]);
		this.workingCopies[1] = getCompilationUnit("/P2/I.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertNoProblem(sourcesAsCharArrays[1], this.workingCopies[1]);

		// Get third working copy and verify that all expected errors are here
		this.problemRequestor.initialize(sourcesAsCharArrays[2]);
		this.workingCopies[2] = getCompilationUnit("/P3/Y.js").getWorkingCopy(new WorkingCopyOwner() {}, null);
		assertProblems("Working copy should have problems:",
			"----------\n" + 
			"1. ERROR in /P3/Y.java (at line 1)\n" + 
			"	class Y implements I {\n" + 
			"	      ^\n" + 
			"The type Y must implement the inherited abstract method I.bar(X)\n" + 
			"----------\n" + 
			"2. ERROR in /P3/Y.java (at line 1)\n" + 
			"	class Y implements I {\n" + 
			"	      ^\n" + 
			"The type Y must implement the inherited abstract method I.foo()\n" + 
			"----------\n"
		);
	} finally {
		deleteProject("P1");
		deleteProject("P2");
		deleteProject("P3");
	}
}
}
