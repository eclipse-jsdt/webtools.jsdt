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
package org.eclipse.wst.jsdt.debug.core.jsdi;

/**
 * Exception that is thrown when a request is made to a {@link VirtualMachine}
 * that is not allowed based on its current state.
 * 
 * Clients can instantiate this class.
 * 
 * @since 1.0
 */
public final class IncompatibleVmStateException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message the backing message for the exception
	 * @param cause the cause (if any) for this exception. <code>null</code> is
	 * accepted.
	 */
	public IncompatibleVmStateException(String message, Throwable cause) {
		super(message, cause);
	}
}
