/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/ 

package org.eclipse.wst.jsdt.core.tests;

import org.eclipse.wst.jsdt.core.tests.runtime.JSRuntimeInstallRegistryReaderTest;
import org.eclipse.wst.jsdt.core.tests.runtime.JSRuntimeManagerTest;
import org.eclipse.wst.jsdt.core.tests.runtime.JSRuntimeTypeRegistryReaderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({JSRuntimeManagerTest.class, 
	JSRuntimeInstallRegistryReaderTest.class,
	JSRuntimeTypeRegistryReaderTest.class})
public class RunJSRuntimeTests {

}
