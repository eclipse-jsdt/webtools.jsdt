/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.jsd2.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;

/**
 * JSD2 implementation of {@link NullValue}
 * 
 * @since 1.0
 */
public class NullValueImpl extends MirrorImpl implements NullValue {

	/**
	 * String representation of the value<br><br>
	 * Value is: <code>null</code>
	 */
	public static final String NULL = "null"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param vm the underlying {@link VirtualMachine}
	 */
	public NullValueImpl(VirtualMachine vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return NULL;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return valueString();
	}
}
