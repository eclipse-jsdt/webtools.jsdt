/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.tests.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.core.tests.internal.runtime.TestJSRuntimeProvider1;
import org.eclipse.wst.jsdt.core.tests.internal.runtime.TestRuntimeType1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JSRuntimeManagerTest {
	public static final String RUNTIME_TEST_TYPE_ID = "js.runtime.test.type";  //$NON-NLS-1$
	public static final String FAKE_RUNTIME_ID = "new-test-runtime-id"; //$NON-NLS-1$
	public static final String FAKE_RUNTIME_NAME = "new-test-runtime-name"; //$NON-NLS-1$
	public static final String FAKE_RUNTIME_LOCATION = "/home/new/test/runtime/location"; //$NON-NLS-1$
	public static final String FAKE_RUNTIME_ARGS = "arg1, arg2"; //$NON-NLS-1$
	public static final String UNREGISTERED_RUNTIME_TYPE = "UNREGISTERED_RUNTIME_TYPE"; //$NON-NLS-1$
	
	@Before
	public void clearManager () {
		JSRuntimeManager.reset();
	}
	
	private IJSRuntimeInstall createFakeRuntime () {
		TestRuntimeType1 rt1 = new TestRuntimeType1();
		JSRuntimeWorkingCopy runtimeInstall = new JSRuntimeWorkingCopy(rt1, FAKE_RUNTIME_ID);
		runtimeInstall.setName(FAKE_RUNTIME_NAME);
		runtimeInstall.setInstallLocation(new File (FAKE_RUNTIME_LOCATION));
		runtimeInstall.setJSRuntimeArguments(FAKE_RUNTIME_ARGS);
		
		return runtimeInstall;
	}
	
	@Test
	public void test_01_managerDefaultContents () {
		// Initial test, confirm the readings give you the default 
		// content which should be provided by the test ext-point
		
		IJSRuntimeInstall jsri1 = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		Assert.assertNotNull(jsri1);
		Assert.assertEquals(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1, jsri1.getId());
		
		IJSRuntimeInstall jsri2 = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2);
		Assert.assertNotNull(jsri2);
		Assert.assertEquals(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2, jsri2.getId());
		
		IJSRuntimeInstall[] storedRuntimes = JSRuntimeManager.getJSRuntimeInstallsByType(RUNTIME_TEST_TYPE_ID);
		Assert.assertArrayEquals(new IJSRuntimeInstall[]{ jsri1, jsri2 }, storedRuntimes);
	}
	
	@Test
	public void test_02_managerValidAdd () {	
		IJSRuntimeInstall runtimeInstall = createFakeRuntime ();
		
		JSRuntimeManager.addJSRuntimeInstall(runtimeInstall);
		
		IJSRuntimeInstall storedRuntime = JSRuntimeManager.getJSRuntimeInstall(FAKE_RUNTIME_ID);
		Assert.assertNotNull("Manager must return a valid runtime for this ID.", runtimeInstall); //$NON-NLS-1$
		
		Assert.assertEquals(FAKE_RUNTIME_ID, storedRuntime.getId());
		Assert.assertEquals(FAKE_RUNTIME_NAME, storedRuntime.getName());
		Assert.assertEquals(FAKE_RUNTIME_LOCATION, storedRuntime.getInstallLocation().getAbsolutePath());
		Assert.assertArrayEquals(FAKE_RUNTIME_ARGS.split(" "), storedRuntime.getJSRuntimeArguments()); //$NON-NLS-1$
		Assert.assertEquals(RUNTIME_TEST_TYPE_ID, storedRuntime.getRuntimeType().getId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_03_managerInvalidAdd_DuplicateRuntime () {
		IJSRuntimeInstall runtimeInstall = createFakeRuntime ();
		// First addition should work flawlessly while second must fail
		JSRuntimeManager.addJSRuntimeInstall(runtimeInstall);
		JSRuntimeManager.addJSRuntimeInstall(runtimeInstall);
	}
	
	@Test
	public void test_04_managerClear () {
		IJSRuntimeInstall runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		Assert.assertNotNull("Runtime install must be present in JSRuntimeManager.", runtimeInstall); //$NON-NLS-1$
		
		runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2);
		Assert.assertNotNull("Runtime install must be present in JSRuntimeManager.", runtimeInstall); //$NON-NLS-1$
		
		JSRuntimeManager.clear();
		
		runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		Assert.assertNull("Manager must have no runtime ids.", runtimeInstall); //$NON-NLS-1$
		
		runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2);
		Assert.assertNull("Manager must have no runtime ids.", runtimeInstall); //$NON-NLS-1$
	}
	
	@Test
	public void test_05_managerUpdate () {
		String updatedPrefix = "UPDATED"; //$NON-NLS-1$
		String updatedArgs = updatedPrefix + "arg1 " + updatedPrefix + "arg 2"; //$NON-NLS-1$ //$NON-NLS-2$
		
		IJSRuntimeInstall install = createFakeRuntime ();
		JSRuntimeManager.addJSRuntimeInstall(install);
		
		install.setName(updatedPrefix + FAKE_RUNTIME_NAME);
		install.setInstallLocation(new File (updatedPrefix + FAKE_RUNTIME_LOCATION));
		install.setJSRuntimeArguments(updatedArgs);
		
		// So far so good, now try to update the manager
		JSRuntimeManager.updateJSRuntimeInstall(install);
		
		IJSRuntimeInstall updatedRuntime = JSRuntimeManager.getJSRuntimeInstall(FAKE_RUNTIME_ID);
		
		Assert.assertEquals(FAKE_RUNTIME_ID, updatedRuntime.getId());
		Assert.assertEquals(updatedPrefix + FAKE_RUNTIME_NAME, updatedRuntime.getName());
		Assert.assertEquals(new File(updatedPrefix + FAKE_RUNTIME_LOCATION).getAbsolutePath(), 
					updatedRuntime.getInstallLocation().getAbsolutePath());
		Assert.assertArrayEquals(updatedArgs.split(" "), updatedRuntime.getJSRuntimeArguments()); //$NON-NLS-1$
		Assert.assertEquals(RUNTIME_TEST_TYPE_ID, updatedRuntime.getRuntimeType().getId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_06_managerInvalidUpdate () {
		IJSRuntimeInstall install = createFakeRuntime ();
	
		// Updating a runtime install that was not previously added
		// must throw an exception.
		JSRuntimeManager.updateJSRuntimeInstall(install);
	}
	
	@Test
	public void test_07_managerRemove () {
		IJSRuntimeInstall runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		Assert.assertNotNull("JS Runtime Install missing.", runtimeInstall); //$NON-NLS-1$
		
		JSRuntimeManager.removeJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		
		runtimeInstall = JSRuntimeManager.getJSRuntimeInstall(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		Assert.assertNull("JS Runtime unsuccessfully removed.", runtimeInstall); //$NON-NLS-1$
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_08_managerInvalidRemove () {
		JSRuntimeManager.removeJSRuntimeInstall(FAKE_RUNTIME_ID);
	}
	
	@Test
	public void test_09_obtainRuntimeInstallsByType () {
		IJSRuntimeInstall install = createFakeRuntime ();
		JSRuntimeManager.addJSRuntimeInstall(install);
		
		IJSRuntimeInstall[] installs = JSRuntimeManager.getJSRuntimeInstallsByType(RUNTIME_TEST_TYPE_ID);
		
		Assert.assertEquals("Manager has more installs than expected.", 3, installs.length); //$NON-NLS-1$
		
		List <String> expectedIds = Arrays.asList (
					TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1, 
					TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2,
					FAKE_RUNTIME_ID);
		List <String> storedIds = new ArrayList<String>();
		
		for (int i = 0; i < installs.length; ++i) {
			storedIds.add(installs[i].getId());
		}
		
		Assert.assertTrue(expectedIds.containsAll(storedIds));
		Assert.assertTrue(storedIds.containsAll(expectedIds));
		
		installs = JSRuntimeManager.getJSRuntimeInstallsByType(UNREGISTERED_RUNTIME_TYPE);
		Assert.assertEquals("No installs must be found for unknown runtime type.", 0, installs.length); //$NON-NLS-1$
	}
}
