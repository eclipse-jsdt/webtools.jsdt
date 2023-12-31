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

public interface IBinaryNestedType {
/**
 * Answer the resolved name of the enclosing type in the
 * class file format as specified in section 4.2 of the Java 2 VM spec.
 *
 * For example, java.lang.String is java/lang/String.
 */

char[] getEnclosingTypeName();
/**
 * Answer an int whose bits are set according the access constants
 * defined by the VM spec.
 */

// We have added AccDeprecated & AccSynthetic.

int getModifiers();
/**
 * Answer the resolved name of the member type in the
 * class file format as specified in section 4.2 of the Java 2 VM spec.
 *
 * For example, p1.p2.A.M is p1/p2/A$M.
 */

char[] getName();
}
