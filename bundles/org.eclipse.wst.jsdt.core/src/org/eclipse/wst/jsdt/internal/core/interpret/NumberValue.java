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

public class NumberValue extends Value{

	int intValue;
	
	public NumberValue(int value)
	{
		super(NUMBER);
		this.intValue=value;
	}


	public  int numberValue()
	{
		return intValue;
	}
	public  String stringValue() 
	{
		return String.valueOf(intValue);
	}
	
	public  boolean booleanValue() { return (intValue==0) ?  false:true;}
	

}
