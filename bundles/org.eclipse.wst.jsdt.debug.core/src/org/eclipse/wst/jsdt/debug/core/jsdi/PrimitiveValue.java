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
 * Abstract representation of a primitive value with-respect-to JavaScript debugging
 * 
 * @see Value
 * @see BooleanValue
 * @see NumberValue
 * @see StringValue
 * @since 1.0
 * @noextend This interface is not intended to be extended by clients.
 */
public interface PrimitiveValue extends Value {

	/**
	 * Returns the integer for the value.
	 * 
	 * @return the integer value
	 */
	public int intValue();
	
	/**
	 * Returns the double for the value.
	 * 
	 * @return the double value
	 */
	public double doubleValue();
	
	/**
	 * Returns the boolean for the value.
	 * 
	 * @return the boolean value
	 */
	public boolean booleanValue();
	
	/**
	 * Returns the String value for the value.<br>
	 * <br>
	 * This method can return <code>null</code>
	 * 
	 * @return the {@link String} value
	 */
	public String stringValue();
}
