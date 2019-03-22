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
package org.eclipse.wst.jsdt.debug.internal.ui.adapters;

import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.internal.ui.viewers.update.DebugEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetProxy;

/**
 * Model proxy for our {@link IJavaScriptDebugTarget}
 * 
 * @since 1.0
 */
public class JavaScriptDebugTargetProxy extends DebugTargetProxy {

	/**
	 * Constructor
	 * @param target
	 */
	public JavaScriptDebugTargetProxy(IDebugTarget target) {
		super(target);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetProxy#createEventHandlers()
	 */
	protected DebugEventHandler[] createEventHandlers() {
		return new DebugEventHandler[] {new JavaScriptDebugTargetEventHandler(this), new JavaScriptThreadHandler(this)};
	}
}
