/*******************************************************************************
 * Licensed Materials - Property of IBM
 * © Copyright 2016 IBM Corporation and others. All Rights Reserved.
 * U.S. Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.IExportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public class ExportDeclaration extends SourceRefElement implements IExportDeclaration {

	protected String name;

	/**
	 * Constructs an ExportDeclaration in the given export container with the
	 * given name.
	 */
	protected ExportDeclaration(ExportContainer parent, String name) {
		super(parent);
		this.name = name;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ExportDeclaration))
			return false;
		return super.equals(o);
	}

	public String getElementName() {
		return this.name;
	}

	/**
	 * @see IJavaScriptElement
	 */
	public int getElementType() {
		return EXPORT_DECLARATION;
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.IExportDeclaration#getFlags()
	 */
	public int getFlags() throws JavaScriptModelException {
		ExportDeclarationElementInfo info = (ExportDeclarationElementInfo) getElementInfo();
		return info.getModifiers();
	}

	/**
	 * @see JavaElement#getHandleMemento(StringBuffer) For export
	 *      declarations, the handle delimiter is associated to the export
	 *      container already
	 */
	protected void getHandleMemento(StringBuffer buff) {
		((JavaElement) getParent()).getHandleMemento(buff);
		escapeMementoName(buff, getElementName());
		if (this.occurrenceCount > 1) {
			buff.append(JEM_COUNT);
			buff.append(this.occurrenceCount);
		}
	}

	/**
	 * @see JavaElement#getHandleMemento()
	 */
	protected char getHandleMementoDelimiter() {
		// For export declarations, the handle delimiter is associated to the
		// export container already
		Assert.isTrue(false, "Should not be called"); //$NON-NLS-1$
		return 0;
	}

	/*
	 * @see JavaElement#getPrimaryElement(boolean)
	 */
	public IJavaScriptElement getPrimaryElement(boolean checkOwner) {
		CompilationUnit cu = (CompilationUnit) this.parent.getParent();
		if (checkOwner && cu.isPrimary())
			return this;
		return cu.getExport(getElementName());
	}

	/**
	 */
	public String readableName() {

		return null;
	}

	/**
	 * @private Debugging purposes
	 */
	protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
		buffer.append(this.tabString(tab));
		buffer.append("export "); //$NON-NLS-1$
		toStringName(buffer);
		if (info == null) {
			buffer.append(" (not open)"); //$NON-NLS-1$
		}
	}
}
