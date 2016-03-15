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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.tests.internal.runtime.TestJSRuntimeProvider1;
import org.eclipse.wst.jsdt.core.tests.internal.runtime.TestJSRuntimeProvider2;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeInstallRegistryReader;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@SuppressWarnings("restriction")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JSRuntimeInstallRegistryReaderTest {
	private static final String RUNTIME_TYPE_1 = "js.runtime.test.type"; //$NON-NLS-1$
	private static final String RUNTIME_TYPE_2 = "js.runtime.test.type.2"; //$NON-NLS-1$
	
	private static final String RUNTIME_TYPE_NAME_1 = "jsruntimetesttype"; //$NON-NLS-1$
	private static final String RUNTIME_TYPE_NAME_2 = "jsruntimetesttype2"; //$NON-NLS-1$
	
	private Set <String> getExpectedRuntimesIds () {
		Set <String> s = new HashSet<String> ();
		s.add(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1);
		s.add(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2);
		s.add(TestJSRuntimeProvider2.FAKE_RUNTIME_ID_3);
		return s;
	}
	
	@Test
	public void test_01_extensionReading () {
		// This will read the contributions from the extension point
		Collection <IJSRuntimeInstall> runtimes = JSRuntimeInstallRegistryReader.getJSRuntimeInstalls();
		
		// Create a temporary set to remove from there, so we can validate after
		// that remaining set is empty
		Set <String> expectedRuntimesIds = getExpectedRuntimesIds();
		
		for (IJSRuntimeInstall runtime : runtimes) {
			String runtimeName;
			String runtimeArgs;
			String installLocation;
			String runtimeTypeId;
			String runtimeTypeName;
	
			if (runtime.getId().equals(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_1)) {
				runtimeName = TestJSRuntimeProvider1.FAKE_RUNTIME_NAME_1;
				runtimeArgs = TestJSRuntimeProvider1.FAKE_RUNTIME_ARGS_1;
				installLocation = TestJSRuntimeProvider1.FAKE_RUNTIME_INSTALL_LOCATION_1;
				runtimeTypeId = RUNTIME_TYPE_1;
				runtimeTypeName = RUNTIME_TYPE_NAME_1;
			} else if (runtime.getId().equals(TestJSRuntimeProvider1.FAKE_RUNTIME_ID_2)) {
				runtimeName = TestJSRuntimeProvider1.FAKE_RUNTIME_NAME_2;
				runtimeArgs = TestJSRuntimeProvider1.FAKE_RUNTIME_ARGS_2;
				installLocation = TestJSRuntimeProvider1.FAKE_RUNTIME_INSTALL_LOCATION_2;
				runtimeTypeId = RUNTIME_TYPE_1;
				runtimeTypeName = RUNTIME_TYPE_NAME_1;
			} else if (runtime.getId().equals(TestJSRuntimeProvider2.FAKE_RUNTIME_ID_3)) {
				runtimeName = TestJSRuntimeProvider2.FAKE_RUNTIME_NAME_3;
				runtimeArgs = TestJSRuntimeProvider2.FAKE_RUNTIME_ARGS_3;
				installLocation = TestJSRuntimeProvider2.FAKE_RUNTIME_INSTALL_LOCATION_3;
				runtimeTypeId = RUNTIME_TYPE_2;
				runtimeTypeName = RUNTIME_TYPE_NAME_2;
			} else {
				continue;
				// Do nothing, this is fine because our platform may have some extra
				// contributions, but at least we are focusing on the ones our testing
				// is providing
			}
			
			Assert.assertEquals(runtimeName, runtime.getName());
			Assert.assertArrayEquals(runtimeArgs.split(" "), runtime.getJSRuntimeArguments()); //$NON-NLS-1$
			Assert.assertEquals(installLocation, runtime.getInstallLocation().getAbsolutePath());
			Assert.assertEquals(runtimeTypeId, runtime.getRuntimeType().getId());
			Assert.assertEquals(runtimeTypeName, runtime.getRuntimeType().getName());
			
			expectedRuntimesIds.remove(runtime.getId());
		}
		
		Assert.assertEquals(0, expectedRuntimesIds.size());
	}
}
