/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.common.tests.suites;

/**
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
import org.eclipse.wst.jsdt.js.node.common.util.tests.JsonUtilTest;
import org.eclipse.wst.jsdt.js.node.common.util.tests.PackageJsonUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ JsonUtilTest.class, PackageJsonUtilTest.class })
public class UtilTests {
}