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
package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Binding;

/**
 * @see IField
 */

public class SourceField extends NamedMember implements IField {

/**
 * Constructs a handle to the field with the given name in the specified type.
 */
protected SourceField(JavaElement parent, String name) {
	super(parent, name);
}
public boolean equals(Object o) {
	if (!(o instanceof SourceField)) return false;
	return super.equals(o);
}
public ASTNode findNode(org.eclipse.wst.jsdt.core.dom.JavaScriptUnit ast) {
	// For field declarations, a variable declaration fragment is returned
	// Return the FieldDeclaration instead
	// For enum constant declaration, we return the node directly
	ASTNode node = super.findNode(ast);
	if (node == null) return null;
	return node.getParent();
}
/**
 * @see IField
 */
public Object getConstant() throws JavaScriptModelException {
	Object constant = null;
	SourceFieldElementInfo info = (SourceFieldElementInfo) getElementInfo();
	final char[] constantSourceChars = info.initializationSource;
	if (constantSourceChars == null) {
		return null;
	}

	String constantSource = new String(constantSourceChars);
	String signature = info.getTypeSignature();
	try {
//		if (signature.equals(Signature.SIG_INT)) {
//			constant = Integer.valueOf(constantSource);
//		} else if (signature.equals(Signature.SIG_SHORT)) {
//			constant = new Short(constantSource);
//		} else if (signature.equals(Signature.SIG_BYTE)) {
//			constant = new Byte(constantSource);
//		} else if (signature.equals(Signature.SIG_BOOLEAN)) {
//			constant = Boolean.valueOf(constantSource);
//		} else if (signature.equals(Signature.SIG_CHAR)) {
//			if (constantSourceChars.length != 3) {
//				return null;
//			}
//			constant = new Character(constantSourceChars[1]);
//		} else if (signature.equals(Signature.SIG_DOUBLE)) {
//			constant = new Double(constantSource);
//		} else if (signature.equals(Signature.SIG_FLOAT)) {
//			constant = new Float(constantSource);
//		} else if (signature.equals(Signature.SIG_LONG)) {
//			if (constantSource.endsWith("L") || constantSource.endsWith("l")) { //$NON-NLS-1$ //$NON-NLS-2$
//				int index = constantSource.lastIndexOf("L");//$NON-NLS-1$
//				if (index != -1) {
//					constant = new Long(constantSource.substring(0, index));
//				} else {
//					constant = new Long(constantSource.substring(0, constantSource.lastIndexOf("l")));//$NON-NLS-1$
//				}
//			} else {
//				constant = new Long(constantSource);
//			}
		/*} else*/ if (signature.equals("QString;")) {//$NON-NLS-1$
			constant = constantSource;
		}
	} catch (NumberFormatException e) {
		// not a parsable constant
		return null;
	}
	return constant;
}
/**
 * @see IJavaScriptElement
 */
public int getElementType() {
	return FIELD;
}
/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.IField#getKey()
 */
public String getKey() {
	try {
		return getKey(this, false/*don't open*/);
	} catch (JavaScriptModelException e) {
		// happen only if force open is true
		return null;
	}
}
/**
 * @see JavaElement#getHandleMemento()
 */
protected char getHandleMementoDelimiter() {
	return JavaElement.JEM_FIELD;
}
/*
 * @see JavaElement#getPrimaryElement(boolean)
 */
public IJavaScriptElement getPrimaryElement(boolean checkOwner) {
	if (checkOwner) {
		CompilationUnit cu = (CompilationUnit)getAncestor(JAVASCRIPT_UNIT);
		if (cu.isPrimary()) return this;
	}
	IJavaScriptElement primaryParent =this.parent.getPrimaryElement(false);
	if (primaryParent instanceof IType)
		return ((IType)primaryParent).getField(this.name);
	return ((IJavaScriptUnit)primaryParent).getField(this.name);
}
/**
 * @see IField
 */
public String getTypeSignature() throws JavaScriptModelException {
	SourceFieldElementInfo info = (SourceFieldElementInfo) getElementInfo();
	return info.getTypeSignature();
}
/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.IField#isResolved()
 */
public boolean isResolved() {
	return false;
}
public JavaElement resolved(Binding binding) {
	SourceRefElement resolvedHandle = new ResolvedSourceField(this.parent, this.name, new String(binding.computeUniqueKey()));
	resolvedHandle.occurrenceCount = this.occurrenceCount;
	return resolvedHandle;
}
/**
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
	buffer.append(this.tabString(tab));
	if (info == null) {
		toStringName(buffer);
		buffer.append(" (not open)"); //$NON-NLS-1$
	} else if (info == NO_INFO) {
		toStringName(buffer);
	} else {
//		try {
//			buffer.append(Signature.toString(this.getTypeSignature()));
			buffer.append("var "); //$NON-NLS-1$
			toStringName(buffer);
//		} catch (JavaScriptModelException e) {
//			buffer.append("<JavaScriptModelException in toString of " + getElementName()); //$NON-NLS-1$
//		}
	}
}
}
