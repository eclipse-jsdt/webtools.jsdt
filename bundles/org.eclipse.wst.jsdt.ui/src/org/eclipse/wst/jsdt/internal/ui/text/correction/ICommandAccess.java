/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.text.correction;

/**
 * Correction proposals implement this interface to by invokable by a command. 
 * (e.g. keyboard shortcut) 
 */
public interface ICommandAccess {

	/**
	 * Returns the id of the command that should invoke this correction proposal
	 * @return the id of the command. This id must start with {@link CorrectionCommandInstaller#COMMAND_PREFIX}
	 * to be recognixes as correction command.
	 */
	String getCommandId();
	
}
