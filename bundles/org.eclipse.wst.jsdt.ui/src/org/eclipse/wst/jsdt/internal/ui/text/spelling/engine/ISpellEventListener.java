/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.spelling.engine;


/**
 * Interface for spell event listeners.
 *
 * 
 */
public interface ISpellEventListener {

	/**
	 * Handles a spell event.
	 *
	 * @param event
	 *                  Event to handle
	 */
	public void handle(ISpellEvent event);
}
