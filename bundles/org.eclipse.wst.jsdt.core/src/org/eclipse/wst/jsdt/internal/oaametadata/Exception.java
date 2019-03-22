/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.oaametadata;

public class Exception  extends VersionableElement{

	
//	exception_element = element exception {
//		  exception_content  &  exception_attributes  &  foreign_nodes
//		}
//		exception_content = (
//		  parameters_element?  &  returns_element?  &  
//		  descriptive_elements  &  compatibility_elements
//		  # Research the above, make consistent with the spec  
//		  # <parameters>? <returns>?
//		)
//		exception_attributes = (
//		  empty
//		)

	public Parameter[] parameters;
	public ReturnsData  returns;
	public String type;
}
