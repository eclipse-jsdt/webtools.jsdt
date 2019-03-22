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

package org.eclipse.wst.jsdt.core.tests.internal.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstallProvider;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * Test provider for two "test" runtime installs.
 *
 */
public class TestJSRuntimeProvider1 implements IJSRuntimeInstallProvider {
	public static String FAKE_RUNTIME_ID_1 = "RUNTIME_ID_1"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_NAME_1 = "RUNTIME_NAME_1"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_INSTALL_LOCATION_1 = "/home/fake/location/1"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_ARGS_1 = "1 2 3"; //$NON-NLS-1$
	
	public static String FAKE_RUNTIME_ID_2 = "RUNTIME_ID_2"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_NAME_2 = "RUNTIME_NAME_2"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_INSTALL_LOCATION_2 = "/home/fake/location/2"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_ARGS_2 = "test1 test2 test3"; //$NON-NLS-1$
	
	public Collection<IJSRuntimeInstall> getJSRuntimeInstallContributions(IJSRuntimeType runtimeTypeId) {
		List <IJSRuntimeInstall> runtimes = new ArrayList<IJSRuntimeInstall>();
		
		IJSRuntimeInstall rjs1 = runtimeTypeId.createRuntimeInstall(FAKE_RUNTIME_ID_1);
		rjs1.setName(FAKE_RUNTIME_NAME_1); 
		rjs1.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_1);
		rjs1.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_1)); 
		
		IJSRuntimeInstall rjs2 = runtimeTypeId.createRuntimeInstall(FAKE_RUNTIME_ID_2);
		rjs2.setName(FAKE_RUNTIME_NAME_2); 
		rjs2.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_2);
		rjs2.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_2)); 
		
		runtimes.add(rjs1);
		runtimes.add(rjs2);
		return runtimes;
	}

}
