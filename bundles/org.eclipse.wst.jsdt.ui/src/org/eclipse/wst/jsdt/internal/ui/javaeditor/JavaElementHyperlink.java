/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.javaeditor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;


/**
 * Java element hyperlink.
 *
 * 
 */
public class JavaElementHyperlink implements IHyperlink {

	private final IRegion fRegion;
	private final IAction fOpenAction;


	/**
	 * Creates a new Java element hyperlink.
	 */
	public JavaElementHyperlink(IRegion region, IAction openAction) {
		Assert.isNotNull(openAction);
		Assert.isNotNull(region);

		fRegion= region;
		fOpenAction= openAction;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.javaeditor.IHyperlink#getHyperlinkRegion()
	 * 
	 */
	public IRegion getHyperlinkRegion() {
		return fRegion;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.javaeditor.IHyperlink#open()
	 * 
	 */
	public void open() {
		fOpenAction.run();
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.javaeditor.IHyperlink#getTypeLabel()
	 * 
	 */
	public String getTypeLabel() {
		return null;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.ui.javaeditor.IHyperlink#getHyperlinkText()
	 * 
	 */
	public String getHyperlinkText() {
		return fOpenAction.getToolTipText();
	}
}
