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
package org.eclipse.wst.jsdt.internal.ui.filters;


import org.eclipse.wst.jsdt.internal.ui.viewsupport.MemberFilter;


/**
 * Filters local types
 */
public class LocalTypesFilter extends MemberFilter {
	public LocalTypesFilter() {
		addFilter(MemberFilter.FILTER_LOCALTYPES);
	}
}
