/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.chrome.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.request.VMDeathRequest;
import org.eclipse.wst.jsdt.debug.internal.chrome.jsdi.VMImpl;

/**
 * {@link VMDeathRequest} impl
 * 
 * @since 1.0
 */
public class VMDeathReqImpl extends EventReqImpl implements VMDeathRequest {

	/**
	 * Constructor
	 * @param vm
	 * @param enabled
	 */
	public VMDeathReqImpl(VMImpl vm, boolean enabled) {
		super(vm, enabled);
	}
}
