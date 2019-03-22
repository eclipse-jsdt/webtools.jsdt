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

package org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints2;

import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.types.ArrayType;

/**
 * An ArrayTypeVariable2 is a ConstraintVariable which stands for an array type.
 */
public class ArrayTypeVariable2 extends ConstraintVariable2 {

	public ArrayTypeVariable2(ArrayType type) {
		super(type);
	}
	
	// hashCode() and equals(..) not necessary (unique per construction)
	
}
