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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.jsdt.core.runtime.AbstractJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;

public class TestRuntimeType1 extends AbstractJSRuntimeType {
	public static final String TEST_RUNTIME_TYPE_ID = "js.runtime.test.type"; //$NON-NLS-1$
	
	@Override
	public String getName() {
		return "jsruntimetesttype"; //$NON-NLS-1$
	}

	@Override
	protected IJSRuntimeInstall doCreateRuntimeInstall(String id) {
		return new AbstractTestJSRuntimeInstall(this, id) {
		};
	}

	public String getId() {
		if (fId == null) {
			fId = TEST_RUNTIME_TYPE_ID;
		}
		return super.getId();
	}

	@Override
	protected IStatus doValidateInstallLocation(File tempFile) {
		return Status.OK_STATUS;
	}
}
