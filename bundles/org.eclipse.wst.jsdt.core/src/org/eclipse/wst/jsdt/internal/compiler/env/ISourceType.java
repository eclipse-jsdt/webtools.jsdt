/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.env;

public interface ISourceType extends IGenericType {

/**
 * Answer the source end position of the type's declaration.
 */
int getDeclarationSourceEnd();

/**
 * Answer the source start position of the type's declaration.
 */
int getDeclarationSourceStart();

/**
 * Answer the enclosing type
 * or null if the receiver is a top level type.
 */
ISourceType getEnclosingType();

/**
 * Answer the receiver's fields.
 *
 * NOTE: Multiple fields with the same name can exist in the result.
 */
ISourceField[] getFields();

/**
 * Answer the unresolved names of the receiver's interfaces
 * or null if the array is empty.
 *
 * A name is a simple name or a qualified, dot separated name.
 * For example, Hashtable or java.util.Hashtable.
 */
char[][] getInterfaceNames();

/**
 * Answer the receiver's member types.
 */
ISourceType[] getMemberTypes();

/**
 * Answer the receiver's methods.
 *
 * NOTE: Multiple methods with the same name & parameter types can exist in the result.
 */
ISourceMethod[] getMethods();

/**
 * Answer the simple source name of the receiver.
 */
char[] getName();

/**
 * Answer the source end position of the type's name.
 */
int getNameSourceEnd();

/**
 * Answer the source start position of the type's name.
 */
int getNameSourceStart();

/**
 * Answer the unresolved name of the receiver's superclass
 * or null if it does not have one.
 *
 * The name is a simple name or a qualified, dot separated name.
 * For example, Hashtable or java.util.Hashtable.
 */
char[] getSuperclassName();

char [][]getPackageName();
}
