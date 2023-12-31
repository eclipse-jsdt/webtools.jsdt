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

public interface ISourceMethod extends IGenericMethod {

/**
 * Answer the source end position of the method's declaration.
 */

int getDeclarationSourceEnd();
/**
 * Answer the source start position of the method's declaration.
 */

int getDeclarationSourceStart();

/**
 * Answer the source end position of the method's selector.
 */
int getNameSourceEnd();
/**
 * Answer the source start position of the method's selector.
 */

int getNameSourceStart();
/**
 * Answer the unresolved name of the return type
 * or null if receiver is a constructor or clinit.
 *
 * The name is a simple name or a qualified, dot separated name.
 * For example, Hashtable or java.util.Hashtable.
 */

char[] getReturnTypeName();
}
