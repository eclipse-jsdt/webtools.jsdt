/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.transport.exception;

import org.eclipse.wst.jsdt.debug.transport.packet.Packet;
import org.eclipse.wst.jsdt.debug.transport.packet.PacketReceiveManager;
import org.eclipse.wst.jsdt.debug.transport.packet.PacketSendManager;

/**
 * This exception is thrown if the {@link PacketSendManager} or {@link PacketReceiveManager}
 * times out while waiting for a {@link Packet}
 * 
 * @since 1.0
 */
public final class TimeoutException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message the message for the exception
	 */
	public TimeoutException(String message) {
		super(message);
	}
}
