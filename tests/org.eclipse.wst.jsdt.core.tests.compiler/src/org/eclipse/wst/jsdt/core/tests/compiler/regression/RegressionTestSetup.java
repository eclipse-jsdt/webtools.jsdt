/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.core.tests.util.CompilerTestSetup;
import org.eclipse.wst.jsdt.core.tests.util.TestVerifier;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.compiler.env.INameEnvironment;
import org.eclipse.wst.jsdt.internal.compiler.batch.FileSystem;

import junit.framework.*;

public class RegressionTestSetup extends CompilerTestSetup {
	
	TestVerifier verifier = new TestVerifier(true);
	INameEnvironment javaClassLib;
	
	public RegressionTestSetup(Test test, String complianceLevel) {
		super(test, complianceLevel);
	}

	protected void setUp() {
		if (this.javaClassLib == null) {
			// Create name environment
			this.javaClassLib = new FileSystem(Util.getJavaClassLibs(), new String[0], null);
		}
		super.setUp();
	}
}
