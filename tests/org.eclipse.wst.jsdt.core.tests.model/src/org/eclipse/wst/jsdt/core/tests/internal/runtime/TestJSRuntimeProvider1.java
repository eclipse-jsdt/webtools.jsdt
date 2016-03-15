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
	
	public Collection<IBaseJSRuntimeInstall> getJSRuntimeInstallContributions() {
		List <IBaseJSRuntimeInstall> runtimes = new ArrayList<IBaseJSRuntimeInstall>();
		
		IBaseJSRuntimeInstall rjs1 = new AbstractTestJSRuntimeInstall() {			
			public String getId() {
				return FAKE_RUNTIME_ID_1;
			}
		};
		rjs1.setName(FAKE_RUNTIME_NAME_1); 
		rjs1.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_1);
		rjs1.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_1)); 
		
		IBaseJSRuntimeInstall rjs2 = new AbstractTestJSRuntimeInstall() {			
			public String getId() {
				return FAKE_RUNTIME_ID_2;
			}
		};
		rjs2.setName(FAKE_RUNTIME_NAME_2); 
		rjs2.setJSRuntimeArguments(FAKE_RUNTIME_ARGS_2);
		rjs2.setInstallLocation(new File (FAKE_RUNTIME_INSTALL_LOCATION_2)); 
		
		runtimes.add(rjs1);
		runtimes.add(rjs2);
		return runtimes;
	}

}
