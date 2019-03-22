/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.opera.internal.jsdi;

import org.eclipse.wst.jsdt.debug.core.jsdi.UndefinedValue;

/**
 * Default {@link UndefinedValue} Opera implementation
 * 
 * @since 0.1
 */
public class UndefinedImpl extends MirrorImpl implements UndefinedValue {

	public static String VALUE = "undefined"; //$NON-NLS-1$
	
	public UndefinedImpl(VirtualMachineImpl vm) {
		super(vm);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.Value#valueString()
	 */
	public String valueString() {
		return VALUE;
	}
}
