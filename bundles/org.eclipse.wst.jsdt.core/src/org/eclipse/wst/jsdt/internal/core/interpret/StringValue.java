/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.interpret;

public class StringValue extends Value {

	String stringValue;

	public StringValue(String value) {
		super(Value.STRING);
		this.stringValue=value;
	}


	public boolean booleanValue() {
		return stringValue.length()!=0;
	}

	public int numberValue() {
		return Integer.valueOf(stringValue).intValue();
	}

	public String stringValue() {
		return stringValue;
	}
	
}
