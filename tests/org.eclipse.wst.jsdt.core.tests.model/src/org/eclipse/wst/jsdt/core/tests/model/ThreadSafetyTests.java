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

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.JavaProject;

import junit.framework.*;

/**
 * DO NOT RELEASE AS PART OF REGRESSION TEST - if failing, can cause testing hang
 */
public class ThreadSafetyTests extends ModifyingResourceTests {

public ThreadSafetyTests(String name) {
	super(name);
}

public static Test suite() {
	return buildModelTestSuite(ThreadSafetyTests.class);	
}
/**
 * 33231 - deadlocked if activating initializer while some concurrent action is populating the JavaModel
 */
public void testDeadlock01() throws CoreException {

	System.out.println("Test deadlock scenario");
	try {
		final IJavaScriptProject project = this.createJavaProject(
				"P", 
				new String[] {}, 
				new String[] {"org.eclipse.wst.jsdt.core.tests.model.TEST_CONTAINER"});
		
		// simulate state on startup (flush containers, and discard their previous values)
		waitUntilIndexesReady();
		project.getJavaScriptModel().close();
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		manager.previousSessionContainers = new HashMap(5);
		manager.containers = new HashMap(5);
		manager.removePerProjectInfo((JavaProject)project);

		// use a thread to hold the lock, so as to recreate potential deadlock situation
		final Semaphore step1 = new Semaphore("<1:permission to populate JavaModel inducing containers inits>", 0); // first acquisition will wait
		final Semaphore step2 = new Semaphore("<2:permission to perform resource modification >", 0); // first acquisition to wait
		final Semaphore hasCompleted = new Semaphore(0); 
		
		ContainerInitializer.setInitializer(new ClasspathInitializerTests.DefaultContainerInitializer(new String[] {"P", ""}){
			public void initialize(IPath containerPath, IJavaScriptProject javaProject) throws CoreException {
				step2.release();
				System.out.println(Thread.currentThread() + " initializer has started: attempting to acquire workspace lock");
				super.initialize(containerPath, javaProject);
				System.out.println(Thread.currentThread() + " initializer has finished");
			}
		});

		// trigger some delta notification in different thread
		Thread performJavaOperationInsideWorkspaceLock = new Thread(new Runnable(){
				public void run() {
					try {
						ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
							public void run(IProgressMonitor monitor)	throws CoreException {
								System.out.println(Thread.currentThread() + " got workspace lock");
								step1.release();
								step2.acquire(); // ensure the java model lock is taken
								ThreadSafetyTests.this.createFile("/P/X.js", "public class X {}");
								System.out.println(Thread.currentThread() + " created file X.js");
							}
						}, null);
					} catch (CoreException e) {
					}
					hasCompleted.release();
					System.out.println(Thread.currentThread() +" ResourceModification DONE");
				}
			},"ModifyResource");
		performJavaOperationInsideWorkspaceLock.setDaemon(true);
		performJavaOperationInsideWorkspaceLock.start();

		Thread attemptPopulateTheJavaModel = new Thread(new Runnable(){
				public void run() {
					try {
							step1.acquire(); // ensure workspace lock is taken already
							System.out.println(Thread.currentThread() + " about to populate Java model");
							// needs the JavaModel lock to populate the project
							project.getChildren(); // trigger classpath initializer activation (requires workspace lock)
							System.out.println(Thread.currentThread() + " done populating the model");
					} catch (JavaScriptModelException e) {
					}
					hasCompleted.release();
					System.out.println(Thread.currentThread() +" Populate JavaModel DONE");
				}
			},"PopulateModel");
		attemptPopulateTheJavaModel.setDaemon(true);
		attemptPopulateTheJavaModel.start();

		hasCompleted.acquire(); // ensure both actions did complete
		hasCompleted.acquire();
		System.out.println("SUCCESS - no deadlock encountered");
	} finally {
		// cleanup  
		this.deleteProject("P");
	}
}
}
