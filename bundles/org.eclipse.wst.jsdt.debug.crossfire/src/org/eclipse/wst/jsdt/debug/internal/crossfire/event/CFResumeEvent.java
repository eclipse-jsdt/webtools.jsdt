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
package org.eclipse.wst.jsdt.debug.internal.crossfire.event;

import org.eclipse.wst.jsdt.debug.core.jsdi.Location;
import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.ResumeEvent;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.EventRequest;

/**
 * Crossfire implementation of {@link ResumeEvent}
 * 
 * @since 1.0
 */
public class CFResumeEvent extends CFLocatableEvent implements ResumeEvent {

	/**
	 * Constructor
	 * @param vm
	 * @param request
	 * @param thread
	 * @param location
	 */
	public CFResumeEvent(VirtualMachine vm, EventRequest request, ThreadReference thread, Location location) {
		super(vm, request, thread, location);
	}
}
