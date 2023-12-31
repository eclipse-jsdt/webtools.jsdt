/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.tests.internal;

import org.eclipse.core.runtime.Plugin;

/**
 * @author nitin
 * 
 */
public class Activator extends Plugin {

	/** The shared instance. */
	private static Activator plugin;

	/**
	 * 
	 */
	public Activator() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
