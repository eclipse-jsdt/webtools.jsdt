/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui.navigator;


/**
 * @author childsb
 *
 */
public class ContainerFolder {
	

	
	
		Object parent;
		String name;
		public ContainerFolder(String fullPath, Object parent){

			this.parent = parent;
			name = fullPath;
		}
		
		public Object getParentObject() {
			return parent;
		}
		
		public String getName() {
			return name;
			
		}
		public String toString() { return name;}
	
}
