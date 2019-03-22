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
package org.eclipse.wst.jsdt.internal.compiler.impl;

public class BooleanConstant extends Constant {

private boolean value;

private static final BooleanConstant TRUE = new BooleanConstant(true);
private static final BooleanConstant FALSE = new BooleanConstant(false);

public static BooleanConstant fromValue(boolean value) {
	return value ? BooleanConstant.TRUE : BooleanConstant.FALSE;
}
private BooleanConstant(boolean value) {
	this.value = value;
}

public boolean booleanValue() {
	return value;
}

public String stringValue() {
	//spec 15.17.11
	return String.valueOf(this.value);
}

public String toString(){
	return "(boolean)" + value ;  //$NON-NLS-1$
}

public int typeID() {
	return T_boolean;
}
}
