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

package org.eclipse.wst.jsdt.core.tests.internal.runtime;

import org.eclipse.wst.jsdt.core.runtime.AbstractJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

public abstract class AbstractTestJSRuntimeInstall extends AbstractJSRuntimeInstall {
	public AbstractTestJSRuntimeInstall(IJSRuntimeType type, String id) {
		super(type, id);
	}
}
