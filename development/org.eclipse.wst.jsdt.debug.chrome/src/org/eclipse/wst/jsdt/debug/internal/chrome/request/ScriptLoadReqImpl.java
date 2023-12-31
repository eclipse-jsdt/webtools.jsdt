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
package org.eclipse.wst.jsdt.debug.internal.chrome.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.request.ScriptLoadRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * {@link ScriptLoadRequest} impl
 * 
 * @since 1.0
 */
public class ScriptLoadReqImpl extends EventReqImpl implements ScriptLoadRequest {

	/**
	 * Constructor
	 * @param vm
	 * @param enabled
	 */
	public ScriptLoadReqImpl(VMImpl vm, boolean enabled) {
		super(vm, enabled);
	}

}
