/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Robert M. Fuhrer (rfuhrer@watson.ibm.com), IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets;

import java.util.Iterator;

import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.types.ArrayType;
import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.types.TType;
import org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints2.TTypes;

/**
 * Represents the super-types of a set of array types. Special because this set
 * always includes Object.
 */
public class ArraySuperTypeSet extends ArrayTypeSet {
	public ArraySuperTypeSet(TypeSet s) {
		super(s.getTypeSetEnvironment());
		if (s instanceof SuperTypesOfSingleton || s instanceof SuperTypesSet)
			fElemTypeSet= s.lowerBound(); // optimization: array-super(super(s)) == array-super(s)
		else
			fElemTypeSet= s;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#anyMember()
	 */
	public TType anyMember() {
		return getJavaLangObject();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#contains(TType)
	 */
	public boolean contains(TType t) {
		if (t.equals(getJavaLangObject())) return true;
		if (!(t instanceof ArrayType))
			return false;

		ArrayType at= (ArrayType) t;
		TType atElemType= at.getComponentType();

		if (fElemTypeSet.contains(atElemType)) // try to avoid enumeration
			return true;

		for(Iterator iter= fElemTypeSet.iterator(); iter.hasNext(); ) {
			TType elemType= (TType) iter.next();

			if (TTypes.canAssignTo(elemType, atElemType))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#containsAll(org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.TypeSet)
	 */
	public boolean containsAll(TypeSet s) {
		if (s instanceof ArraySuperTypeSet) {
			ArraySuperTypeSet ats= (ArraySuperTypeSet) s;

			return fElemTypeSet.containsAll(ats.fElemTypeSet);
		} else if (s instanceof ArrayTypeSet) {
			ArrayTypeSet ats= (ArrayTypeSet) s;

			return fElemTypeSet.containsAll(ats.fElemTypeSet);
		} else
			return enumerate().containsAll(s);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.TypeSet#specialCasesIntersectedWith(org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.TypeSet)
	 */
	protected TypeSet specialCasesIntersectedWith(TypeSet s2) {
		if (s2 instanceof ArraySuperTypeSet) {
			ArraySuperTypeSet ats2= (ArraySuperTypeSet) s2;

			if (ats2.fElemTypeSet.isUniverse())
				return new ArraySuperTypeSet(fElemTypeSet);
		} else if (s2 instanceof ArrayTypeSet) {
			ArrayTypeSet ats2= (ArrayTypeSet) s2;

			if (ats2.fElemTypeSet.isUniverse())
				return new ArrayTypeSet(fElemTypeSet); // intersection doesn't include Object, which is in 'this'
		}
		return super.specialCasesIntersectedWith(s2);
	}

	private EnumeratedTypeSet fEnumCache= null;

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#enumerate()
	 */
	public EnumeratedTypeSet enumerate() {
		if (fEnumCache == null) {
			fEnumCache= new EnumeratedTypeSet(getTypeSetEnvironment());
			TypeSet elemSupers= fElemTypeSet.superTypes();

			for(Iterator iter= elemSupers.iterator(); iter.hasNext(); ) {
				TType elemSuper= (TType) iter.next();

				fEnumCache.add(TTypes.createArrayType(elemSuper, 1));
			}

			fEnumCache.add(getJavaLangObject());
			fEnumCache.initComplete();
		}
		return fEnumCache;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#hasUniqueUpperBound()
	 */
	public boolean hasUniqueUpperBound() {
		return true; // Object is the unique upper bound of any set of array types
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#isSingleton()
	 */
	public boolean isSingleton() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#isUniverse()
	 */
	public boolean isUniverse() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#iterator()
	 */
	public Iterator iterator() {
		return enumerate().iterator();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#makeClone()
	 */
	public TypeSet makeClone() {
		return new ArraySuperTypeSet(fElemTypeSet);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.TypeSet#superTypes()
	 */
	public TypeSet superTypes() {
		return makeClone();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#uniqueUpperBound()
	 */
	public TType uniqueUpperBound() {
		return getJavaLangObject();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#upperBound()
	 */
	public TypeSet upperBound() {
		return new SingletonTypeSet(getJavaLangObject(), getTypeSetEnvironment());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.ArrayTypeSet#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof ArraySuperTypeSet) {
			ArraySuperTypeSet other= (ArraySuperTypeSet) obj;

			return fElemTypeSet.equals(other.fElemTypeSet);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.typeconstraints.typesets.TypeSet#subTypes()
	 */
	public TypeSet subTypes() {
		return getTypeSetEnvironment().getUniverseTypeSet();
	}

	public String toString() {
		return "{" + fID + ": array-super(" + fElemTypeSet + ")}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
