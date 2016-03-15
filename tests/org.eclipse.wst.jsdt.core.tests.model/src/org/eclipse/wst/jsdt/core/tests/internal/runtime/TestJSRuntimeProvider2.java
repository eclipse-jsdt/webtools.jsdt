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

package org.eclipse.wst.jsdt.core.tests.internal.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.wst.jsdt.core.runtime.IBaseJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstallProvider;

/**
 * Test provider for an additional "test" runtime installs.
 *
 */
public class TestJSRuntimeProvider2 implements IJSRuntimeInstallProvider {
	public static String FAKE_RUNTIME_ID_3 = "RUNTIME_ID_3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_NAME_3 = "RUNTIME_NAME_3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_INSTALL_LOCATION_3 = "/home/fake/location/3"; //$NON-NLS-1$
	public static String FAKE_RUNTIME_ARGS_3 = "args for test"; //$NON-NLS-1$
	
	public Collection<IBaseJSRuntimeInstall> getJSRuntimeInstallContributions() {
		List <IBaseJSRuntimeInstall> runtimes = new ArrayList<IBaseJSRuntimeInstall>();
		
		IBaseJSRuntimeInstall rjs1 = new AbstractTestJSRuntimeInstall() {			
			public String getId() {
				return FAKE_RUNTIME_ID_3;
			}
		};
		rjs1.setName(FAKE_RUNTIME_NAME_3); 
		rjs1.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_3);
		rjs1.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_3)); 
		
		runtimes.add(rjs1);
		return runtimes;
	}

}
