/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;

/**
 * Default implementation of {@link ScriptLoadRequest} for Crossfire
 * 
 * @since 1.0
 */
public class CFScriptLoadRequest extends CFEventRequest implements ScriptLoadRequest {

	/**
	 * Constructor
	 * @param vm
	 */
	public CFScriptLoadRequest(VirtualMachine vm) {
		super(vm);
	}
}
