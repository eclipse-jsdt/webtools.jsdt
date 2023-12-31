/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.javaeditor;

/**
 * <code>ICompoundEditListener</code>s can be registered with a
 * {@link CompoundEditExitStrategy} to be notified if a compound edit session is ended.
 * 
 * 
 */
public interface ICompoundEditListener {
	/**
	 * Notifies the receiver that the sending <code>CompoundEditExitStrategy</code> has
	 * detected the end of a compound operation.
	 */
	void endCompoundEdit();
}
