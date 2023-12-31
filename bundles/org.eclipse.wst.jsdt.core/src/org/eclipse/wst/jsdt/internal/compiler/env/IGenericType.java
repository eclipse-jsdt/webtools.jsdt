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
package org.eclipse.wst.jsdt.internal.compiler.env;

public interface IGenericType extends IDependent {

/**
 * Answer an int whose bits are set according the access constants
 * defined by the VM spec.
 * NOTE 1: We have added AccDeprecated & AccSynthetic.
 * NOTE 2: If the receiver represents a member type, the modifiers are extracted from its inner class attributes.
 */
int getModifiers();
/**
 * Answer whether the receiver contains the resolved binary form
 * or the unresolved source form of the type.
 */

boolean isBinaryType();
}
