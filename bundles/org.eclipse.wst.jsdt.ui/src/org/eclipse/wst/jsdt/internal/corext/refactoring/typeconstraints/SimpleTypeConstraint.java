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
package org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints;

import org.eclipse.core.runtime.Assert;

public final class SimpleTypeConstraint implements ITypeConstraint {
	
	private final ConstraintVariable fLeft;
	private final ConstraintVariable fRight;
	private final ConstraintOperator fOperator;
	
	/* package */ SimpleTypeConstraint(ConstraintVariable left, ConstraintVariable right, ConstraintOperator operator) {
		Assert.isNotNull(left);
		Assert.isNotNull(right);
		Assert.isNotNull(operator);
		fLeft= left;
		fRight= right;
		fOperator= operator;
	}
	
	public  ConstraintVariable getLeft() {
		return fLeft;
	}

	public  ConstraintVariable getRight() {
		return fRight;
	}

	public ConstraintOperator getOperator() {
		return fOperator;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public  String toString(){
		return getLeft().toString() + " " + fOperator.toString() + " " + getRight().toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.experiments.TypeConstraint#toResolvedString()
	 */
	public  String toResolvedString() {
		return getLeft().toResolvedString() + " " + fOperator.toString() + " " + getRight().toResolvedString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.experiments.ITypeConstraint#isSimpleTypeConstraint()
	 */
	public  boolean isSimpleTypeConstraint() {
		return true;
	}
	
	public boolean isSubtypeConstraint(){
		return fOperator.isSubtypeOperator();
	}

	public boolean isStrictSubtypeConstraint(){
		return fOperator.isStrictSubtypeOperator();
	}

	public boolean isEqualsConstraint(){
		return fOperator.isEqualsOperator();
	}

	public boolean isDefinesConstraint(){
		return fOperator.isDefinesOperator();
	}	
}
