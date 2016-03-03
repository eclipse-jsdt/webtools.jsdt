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

package org.eclipse.wst.jsdt.internal.core.runtime;

import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;

/**
 * Model instances of the JavaScript runtime types that will be read
 * from the <code>org.eclipse.wst.jsdt.core.JSRuntimeInstallTypes</code>
 * extension point.
 */
public class JSRuntimeType implements IJSRuntimeType {
	private String id;
	private String name;
	
	public JSRuntimeType (String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
