/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* https://www.eclipse.org/legal/epl-2.0/
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.tests.runtime;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.internal.core.runtime.JSRuntimeTypeRegistryReader;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("restriction")
public class JSRuntimeTypeRegistryReaderTest {
	private static final String RUNTIME_TYPE_1 = "js.runtime.test.type"; //$NON-NLS-1$
	private static final String RUNTIME_TYPE_2 = "js.runtime.test.type.2"; //$NON-NLS-1$
	
	private static final String RUNTIME_TYPE_NAME_1 = "jsruntimetesttype"; //$NON-NLS-1$
	private static final String RUNTIME_TYPE_NAME_2 = "jsruntimetesttype2"; //$NON-NLS-1$
	
	private Set <String> getExpectedRuntimeTypesIds () {
		Set <String> s = new HashSet<String> ();
		s.add(RUNTIME_TYPE_1);
		s.add(RUNTIME_TYPE_2);
		return s;
	}
	
	@Test
	public void test_01_RegistryReaderGetRuntimeTypes () {
		Collection <IJSRuntimeType> runtimeTypes = JSRuntimeTypeRegistryReader.getJSRuntimeTypes();
		
		// Create a temporary set to remove from there, so we can validate after
		// that remaining set is empty
		Set <String> expectedRuntimeTypesIds = getExpectedRuntimeTypesIds();
		
		for (IJSRuntimeType runtimeType : runtimeTypes) {
			if (runtimeType.getId().equals(RUNTIME_TYPE_1)) {
				Assert.assertEquals(RUNTIME_TYPE_NAME_1, runtimeType.getName());
			} else if (runtimeType.getId().equals(RUNTIME_TYPE_2)) {
				Assert.assertEquals(RUNTIME_TYPE_NAME_2, runtimeType.getName());
			} else {
				continue;
				// Do nothing, this is fine because our platform may have some extra
				// contributions, but at least we are focusing on the ones our testing
				// is providing
			}
			
			expectedRuntimeTypesIds.remove(runtimeType.getId());
		}
		
		Assert.assertEquals(0, expectedRuntimeTypesIds.size());
	}
	
	@Test
	public void test_02_RegistryReaderGetExistingRuntimeById () {
		IJSRuntimeType runtimeType = JSRuntimeTypeRegistryReader.getJSRuntimeType(RUNTIME_TYPE_1);
		Assert.assertNotNull(runtimeType);
		Assert.assertEquals(RUNTIME_TYPE_NAME_1, runtimeType.getName());
		
		runtimeType = JSRuntimeTypeRegistryReader.getJSRuntimeType(RUNTIME_TYPE_2);
		Assert.assertNotNull(runtimeType);
		Assert.assertEquals(RUNTIME_TYPE_NAME_2, runtimeType.getName());
	}
	
	@Test
	public void test_03_RegistryReaderGetUnexistingRuntimeById () {
		IJSRuntimeType runtimeType = JSRuntimeTypeRegistryReader.getJSRuntimeType("Unexisting runtime"); //$NON-NLS-1$
		Assert.assertNull(runtimeType);
	}
	
	@Test
	public void test_04_RegistryReaderGetUnexistingRuntimeById () {
		Collection<String> runtimeTypeIds = JSRuntimeTypeRegistryReader.getJSRuntimeTypesIds();
		Set <String> expectedRuntimeTypesIds = getExpectedRuntimeTypesIds();
//		Assert.assertTrue(expectedRuntimeTypesIds.containsAll(runtimeTypeIds));
		Assert.assertTrue(runtimeTypeIds.containsAll(expectedRuntimeTypesIds));
	}
	
	
}
