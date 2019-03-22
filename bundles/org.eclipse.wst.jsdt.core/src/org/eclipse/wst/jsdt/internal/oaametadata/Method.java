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

public class Method extends VersionableElement {

//	method_element = element method {
//		  method_content  &  method_attributes  &  foreign_nodes
//		}
//		method_content = (
//		  exceptions_element?  &  parameters_element?  &  returns_element?  &  
//		  descriptive_elements  &  compatibility_elements
//		  # Research the above, make consistent with the spec  
//		)
//		method_attributes = (
//		  name?  &  scope?  &  visibility?
//		)
	
	
	public String scope;
	public String visibility;
	public String name;
	
	public boolean isContructor;
	
	public Exception [] exceptions;
	public Parameter [] parameters;
	public ReturnsData returns;
	public boolean isStatic() {
		return IOAAMetaDataConstants.USAGE_STATIC.equals(scope);
	}
}
