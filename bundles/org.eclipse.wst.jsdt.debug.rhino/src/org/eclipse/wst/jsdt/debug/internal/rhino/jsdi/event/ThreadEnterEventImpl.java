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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.event;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.ThreadEnterEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.ThreadEnterRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link ThreadEnterEvent}
 * 
 * @since 1.0
 */
public final class ThreadEnterEventImpl extends LocatableEventImpl implements ThreadEnterEvent {

	/**
	 * Constructor
	 * @param vm
	 * @param thread
	 * @param request
	 */
	public ThreadEnterEventImpl(VirtualMachineImpl vm, ThreadReference thread, ThreadEnterRequest request) {
		super(vm, thread, null, request);
	}
}