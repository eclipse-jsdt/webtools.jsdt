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
package org.eclipse.wst.jsdt.debug.core.jsdi.event;

import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * Description of a JSDI event indicating a {@link VirtualMachine} has been stopped in an unexpected way.<br><br>
 * 
 * There are no guarantees this event can be received.<br><br>
 * 
 * This event is sent in a "fire and forget" fashion, such that if the underlying communication channel
 * has not been interrupted the event can be received.
 * 
 * @see Event
 * @see EventQueue
 * @see EventSet
 * @since 1.0
 * @noextend This interface is not intended to be extended by clients.
 */
public interface VMDeathEvent extends Event {

}
