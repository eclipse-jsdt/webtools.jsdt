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
 * Test provider for an additional "test" runtime installs.
 *
 */
public class TestJSRuntimeProvider2 implements IJSRuntimeInstallProvider {
	public static String FAKE_RUNTIME_ID_3 = "RUNTIME_ID_3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_NAME_3 = "RUNTIME_NAME_3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_INSTALL_LOCATION_3 = "/home/fake/location/3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_ARGS_3 = "args for test"; //$NON-NLS-1$
	
	public Collection<IJSRuntimeInstall> getJSRuntimeInstallContributions(IJSRuntimeType runtimeTypeId) {
		List <IJSRuntimeInstall> runtimes = new ArrayList<IJSRuntimeInstall>();
		
		IJSRuntimeInstall rjs1 = runtimeTypeId.createRuntimeInstall(FAKE_RUNTIME_ID_3);
		rjs1.setName(FAKE_RUNTIME_NAME_3); 
		rjs1.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_3);
		rjs1.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_3)); 
		
		runtimes.add(rjs1);
		return runtimes;
	}

}
