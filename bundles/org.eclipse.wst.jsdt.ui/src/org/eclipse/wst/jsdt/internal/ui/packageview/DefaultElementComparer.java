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
package org.eclipse.wst.jsdt.internal.ui.packageview;

import org.eclipse.jface.viewers.IElementComparer;

public class DefaultElementComparer implements IElementComparer {
	
	public static final DefaultElementComparer INSTANCE= new DefaultElementComparer();
	
	public boolean equals(Object a, Object b) {
		return a.equals(b);
	}
	public int hashCode(Object element) {
		return element.hashCode();
	}
}
