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
package org.eclipse.wst.jsdt.debug.internal.core.breakpoints;

import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpointParticipant;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;

/**
 * A default breakpoint participant
 * 
 * @since 1.1
 */
public class DefaultJavaScriptBreakpointParticipant implements IJavaScriptBreakpointParticipant {

	/**
	 * Constructor
	 */
	public DefaultJavaScriptBreakpointParticipant() {}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpointParticipant#breakpointHit(org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread, org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint)
	 */
	public int breakpointHit(IJavaScriptThread thread, IJavaScriptBreakpoint breakpoint) {
		return DONT_CARE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpointParticipant#scriptLoaded(org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread, org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference, org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint)
	 */
	public int scriptLoaded(IJavaScriptThread thread, ScriptReference script, IJavaScriptBreakpoint breakpoint) {
		return DONT_CARE;
	}
}
