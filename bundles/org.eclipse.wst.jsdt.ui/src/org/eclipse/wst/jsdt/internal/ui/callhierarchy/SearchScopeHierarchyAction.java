/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *   Jesper Kamstrup Linnet (eclipse@kamstrup-linnet.dk) - initial API and implementation 
 *          (report 36180: Callers/Callees view)
 *   Michael Fraenkel (fraenkel@us.ibm.com) - patch
 *          (report 60714: Call Hierarchy: display search scope in view title)
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.callhierarchy;

import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.search.JavaSearchScopeFactory;


class SearchScopeHierarchyAction extends SearchScopeAction {
	private final SearchScopeActionGroup fGroup;
	
	public SearchScopeHierarchyAction(SearchScopeActionGroup group) {
		super(group, CallHierarchyMessages.SearchScopeActionGroup_hierarchy_text); 
		this.fGroup = group;
		setToolTipText(CallHierarchyMessages.SearchScopeActionGroup_hierarchy_tooltip); 
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.CALL_HIERARCHY_SEARCH_SCOPE_ACTION);
	}
	
	public IJavaScriptSearchScope getSearchScope() {
		try {
			IFunction method = this.fGroup.getView().getMethod();
			
			if (method != null) {
				return SearchEngine.createHierarchyScope(method.getDeclaringType());
			} else {
				return null;
			}
		} catch (JavaScriptModelException e) {
			JavaScriptPlugin.log(e);
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.callhierarchy.SearchScopeActionGroup.SearchScopeAction#getSearchScopeType()
	 */
	public int getSearchScopeType() {
		return SearchScopeActionGroup.SEARCH_SCOPE_TYPE_HIERARCHY;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.callhierarchy.SearchScopeAction#getFullDescription()
	 */
	public String getFullDescription() {
		IFunction method = this.fGroup.getView().getMethod();
		return JavaSearchScopeFactory.getInstance().getHierarchyScopeDescription(method.getDeclaringType());
	}

}
