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

import java.util.HashSet;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ThreadReferenceImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.VirtualMachineImpl;

/**
 * Rhino implementation of {@link EventSet}
 * 
 * @since 1.0
 */
public final class EventSetImpl extends HashSet implements EventSet {

	private static final long serialVersionUID = 1L;
	private VirtualMachineImpl vm;
	private ThreadReferenceImpl thread;

	/**
	 * Constructor
	 * @param vm
	 */
	public EventSetImpl(VirtualMachineImpl vm) {
		this.vm = vm;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet#resume()
	 */
	public void resume() {
		if (thread != null) {
			thread.resume();
		}
		// Context.enter() & Context.exit() without starting a script causes
		// thread to be null here.  In this case we want to do nothing.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.event.EventSet#suspended()
	 */
	public boolean suspended() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Mirror#virtualMachine()
	 */
	public VirtualMachine virtualMachine() {
		return this.vm;
	}

	/**
	 * @param thread
	 */
	public void setThread(ThreadReferenceImpl thread) {
		this.thread = thread;
	}
}
