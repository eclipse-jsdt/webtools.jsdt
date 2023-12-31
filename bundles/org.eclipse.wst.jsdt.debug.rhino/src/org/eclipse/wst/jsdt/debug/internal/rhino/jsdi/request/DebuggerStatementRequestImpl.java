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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request;

import org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.request.DebuggerStatementRequest;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link DebuggerStatementRequest}
 * 
 * @since 1.0
 */
public class DebuggerStatementRequestImpl extends EventRequestImpl implements DebuggerStatementRequest {

	private ThreadReference thread;

	/**
	 * Constructor
	 * @param vm
	 */
	public DebuggerStatementRequestImpl(VirtualMachineImpl vm) {
		super(vm);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.request.DebuggerStatementRequest#addThreadFilter(org.eclipse.wst.jsdt.debug.core.jsdi.ThreadReference)
	 */
	public synchronized void addThreadFilter(ThreadReference thread) {
		checkDeleted();
		this.thread = thread;
	}

	/**
	 * The underlying {@link ThreadReference}
	 * @return the underlying {@link ThreadReference}
	 */
	public synchronized ThreadReference thread() {
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.request.EventRequestImpl#setEnabled(boolean)
	 */
	public synchronized void setEnabled(boolean enabled) {
		checkDeleted();
		this.enabled = enabled;
	}
}