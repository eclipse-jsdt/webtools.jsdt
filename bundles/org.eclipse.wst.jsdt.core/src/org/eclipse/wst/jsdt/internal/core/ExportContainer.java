/*******************************************************************************
 * Licensed Materials - Property of IBM
 * © Copyright 2016 IBM Corporation and others. All Rights Reserved.
 * U.S. Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.wst.jsdt.core.IExportContainer;
import org.eclipse.wst.jsdt.core.IExportDeclaration;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.ISourceReference;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.core.util.MementoTokenizer;

public class ExportContainer extends SourceRefElement implements IExportContainer {
	protected ExportContainer(CompilationUnit parent) {
			super(parent);
		}

	public boolean equals(Object o) {
		if (!(o instanceof ExportContainer))
			return false;
		return super.equals(o);
	}

	/**
	 * @see IJavaScriptElement
	 */
	public int getElementType() {
		return EXPORT_CONTAINER;
	}

	/*
	 * @see JavaElement
	 */
	public IJavaScriptElement getHandleFromMemento(String token, MementoTokenizer memento, WorkingCopyOwner workingCopyOwner) {
		switch (token.charAt(0)) {
			case JEM_COUNT :
				return getHandleUpdatingCountFromMemento(memento, workingCopyOwner);
			case JEM_IMPORTDECLARATION :
				if (memento.hasMoreTokens()) {
					String exportName = memento.nextToken();
					JavaElement exportDecl = (JavaElement) getExport(exportName);
					return exportDecl.getHandleFromMemento(memento, workingCopyOwner);
				}
				else {
					return this;
				}
		}
		return null;
	}

	/**
	 * @see JavaElement#getHandleMemento()
	 */
	protected char getHandleMementoDelimiter() {
		return JavaElement.JEM_IMPORTDECLARATION;
	}

	/**
	 * @see IExportContainer
	 */
	public IExportDeclaration getExport(String exportName) {
		int index = exportName.indexOf(".*"); ///$NON-NLS-1$
		boolean isOnDemand = index != -1;
		if (isOnDemand)
			// make sure to copy the string (so that it doesn't hold on the
			// underlying char[] that might be much bigger than necessary)
			exportName = new String(exportName.substring(0, index));
		return new ExportDeclaration(this, exportName);
	}

	/*
	 * @see JavaElement#getPrimaryElement(boolean)
	 */
	public IJavaScriptElement getPrimaryElement(boolean checkOwner) {
		CompilationUnit cu = (CompilationUnit) this.parent;
		if (checkOwner && cu.isPrimary())
			return this;
		return cu.getExportContainer();
	}

	/**
	 * @see ISourceReference
	 */
	public ISourceRange getSourceRange() throws JavaScriptModelException {
		SourceRange range;
		IJavaScriptElement[] exports = getChildren();
		
		if (exports.length > 0) {
			ISourceRange firstRange = ((ISourceReference) exports[0]).getSourceRange();
			ISourceRange lastRange = ((ISourceReference) exports[exports.length - 1]).getSourceRange();			
			range = new SourceRange(firstRange.getOffset(), lastRange.getOffset() + lastRange.getLength() - firstRange.getOffset());
		} else {
			range = new SourceRange(0, 0);
		}

		return range;
	}

	/**
	 */
	public String readableName() {

		return null;
	}

	/**
	 * @private Debugging purposes
	 */
	protected void toString(int tab, StringBuffer buffer) {
		Object info = JavaModelManager.getJavaModelManager().peekAtInfo(this);
		if (info == null || !(info instanceof JavaElementInfo))
			return;
		IJavaScriptElement[] children = ((JavaElementInfo) info).getChildren();
		for (int i = 0; i < children.length; i++) {
			if (i > 0)
				buffer.append("\n"); //$NON-NLS-1$
			((JavaElement) children[i]).toString(tab, buffer);
		}
	}

	/**
	 * Debugging purposes
	 */
	protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
		buffer.append(this.tabString(tab));
		buffer.append("<export container>"); //$NON-NLS-1$
		if (info == null) {
			buffer.append(" (not open)"); //$NON-NLS-1$
		}
	}
}
