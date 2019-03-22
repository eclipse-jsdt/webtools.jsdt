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
package org.eclipse.wst.jsdt.internal.ui.search;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;


public class SortAction extends Action {
	private int fSortOrder;
	private JavaSearchResultPage fPage;
	
	public SortAction(String label, JavaSearchResultPage page, int sortOrder) {
		super(label);
		fPage= page;
		fSortOrder= sortOrder;
	}

	public void run() {
		BusyIndicator.showWhile(fPage.getViewer().getControl().getDisplay(), new Runnable() {
			public void run() {
				fPage.setSortOrder(fSortOrder);
			}
		});
	}

	public int getSortOrder() {
		return fSortOrder;
	}
}
