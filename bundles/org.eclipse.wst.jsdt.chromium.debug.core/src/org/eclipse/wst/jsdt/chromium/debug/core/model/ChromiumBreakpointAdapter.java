// Copyright (c) 2010, 2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/
//
// Contributors:
//      Ilya Buziuk <ilyabuziuk@gmail.com> - https://bugs.eclipse.org/bugs/show_bug.cgi?id=486061

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.wst.jsdt.chromium.debug.core.ChromiumDebugPlugin;

/**
 * Implements breakpoint adapter for breakpoints provided by
 * org.eclipse.wst.jsdt.chromium.debug.* and org.eclipse.wst.jsdt.debug.*
 */
public class ChromiumBreakpointAdapter {
	private static final String JSDT_BREAKPOINT_MODEL_ID = "org.eclipse.wst.jsdt.debug.model"; //$NON-NLS-1$
	
	/**
	 * Storage for mapping {@link JavaScriptBreakpoint} to {@link ChromiumLineBreakpoint}
	 */
	private static final Map<IBreakpoint, ChromiumLineBreakpoint> JSDT_TO_CHROMIUM_STORAGE = new HashMap<IBreakpoint, ChromiumLineBreakpoint>();

	/**
	 * The method was updated for org.eclipse.wst.jsdt.debug.* breakpoint
	 * support. For {@link ChromiumLineBreakpoint} it works just as it used to - 
	 * casts {@link IBreakpoint} to {@link ChromiumLineBreakpoint}. For
	 * {@link JavaScriptBreakpoint} - retrieves {@link ChromiumLineBreakpoint}
	 * from the storage
	 * 
	 * @see {@link #tryCastBreakpointOnAddition tryCastBreakpointOnAddition}
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=486061
	 * @throws NullPointerException if {@link JavaScriptBreakpoint} is null
	 */
	public static ChromiumLineBreakpoint tryCastBreakpoint(IBreakpoint breakpoint) {
		ChromiumLineBreakpoint chromiumBreakpoint = null;
		if (isChromiumLineBreakPoint(breakpoint)) {
			chromiumBreakpoint = (ChromiumLineBreakpoint) breakpoint;
		} else if (isJSDTBreakpoint(breakpoint)) {
			// Retrieving Chromium breakpoint from the storage
			chromiumBreakpoint = JSDT_TO_CHROMIUM_STORAGE.get(breakpoint);
		}

		return chromiumBreakpoint;
	}

	/**
	 * The method was created for org.eclipse.wst.jsdt.debug.* breakpoint
	 * support. On {@link JavaScriptBreakpoint} creation new
	 * {@link ChromiumLineBreakpoint} would be created and added to the storage.
	 * 
	 * @author Ilya Buziuk <ilyabuziuk@gmail.com>
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=486061
	 * @throws NullPointerException if {@link JavaScriptBreakpoint} is null
	 */
	public static ChromiumLineBreakpoint tryCastBreakpointOnAddition(IBreakpoint breakpoint) {
		ChromiumLineBreakpoint chromiumBreakpoint = null;
		if (isChromiumLineBreakPoint(breakpoint)) {
			chromiumBreakpoint = (ChromiumLineBreakpoint) breakpoint;
		} else if (isJSDTBreakpoint(breakpoint)) {
			try {
				chromiumBreakpoint = new ChromiumLineBreakpoint(breakpoint);
				// Adding newly created Chromium breakpoint to the storage
				JSDT_TO_CHROMIUM_STORAGE.put(breakpoint, chromiumBreakpoint);
			} catch (CoreException e) {
				ChromiumDebugPlugin.logError(e.getMessage());
			}
		}

		return chromiumBreakpoint;
	}

	/**
	 * The method was created for org.eclipse.wst.jsdt.debug.* breakpoint
	 * support. On {@link JavaScriptBreakpoint} removal
	 * {@link ChromiumLineBreakpoint} would be removed from the storage.
	 * 
	 * @author Ilya Buziuk <ilyabuziuk@gmail.com>
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=486061
	 * @throws NullPointerException if {@link JavaScriptBreakpoint} is null
	 */
	public static ChromiumLineBreakpoint tryCastBreakpointOnRemoval(IBreakpoint breakpoint) {
		ChromiumLineBreakpoint chromiumBreakpoint = null;
		if (isChromiumLineBreakPoint(breakpoint)) {
			chromiumBreakpoint = (ChromiumLineBreakpoint) breakpoint;
		} else if (isJSDTBreakpoint(breakpoint)) {
			chromiumBreakpoint = JSDT_TO_CHROMIUM_STORAGE.get(breakpoint);
			// JSDT breakpoint was removed - removing Chromium breakpoint from the storage
			JSDT_TO_CHROMIUM_STORAGE.remove(breakpoint);
		}

		return chromiumBreakpoint;
	}

	private static boolean isChromiumLineBreakPoint(IBreakpoint breakpoint) {
		return (breakpoint instanceof ChromiumLineBreakpoint
				&& VProjectWorkspaceBridge.DEBUG_MODEL_ID.equals(breakpoint.getModelIdentifier()));
	}
	
	private static boolean isJSDTBreakpoint(IBreakpoint breakpoint) {
		return (JSDT_BREAKPOINT_MODEL_ID.equals(breakpoint.getModelIdentifier()));
	}

}
