/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.jseview.properties;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import org.eclipse.wst.jsdt.core.IJarEntryResource;

public class JarEntryResourceProperties implements IPropertySource {
	
	private static final String C_JAR_ENTRY_RESOURCE= "IJarEntryResource";
	
	private static final String P_NAME= "org.eclipse.wst.jsdt.jseview.IJarEntryResource.name";
	private static final String P_FULL_PATH= "org.eclipse.wst.jsdt.jseview.IJarEntryResource.fullPath";
	private static final String P_IS_FILE= "org.eclipse.wst.jsdt.jseview.IJarEntryResource.isFile";
	private static final String P_IS_READ_ONLY= "org.eclipse.wst.jsdt.jseview.IJarEntryResource.isReadOnly";
	
	protected IJarEntryResource fJarEntryResource;
	
	private static final ArrayList<IPropertyDescriptor> JAR_ENTRY_RESOURCE_PROPERTY_DESCRIPTORS= new ArrayList<IPropertyDescriptor>();
	static {
		addResourceDescriptor(new PropertyDescriptor(P_NAME, "name"));
		addResourceDescriptor(new PropertyDescriptor(P_FULL_PATH, "fullPath"));
		addResourceDescriptor(new PropertyDescriptor(P_IS_FILE, "isFile"));
		addResourceDescriptor(new PropertyDescriptor(P_IS_READ_ONLY, "isReadOnly"));
	}

	private static void addResourceDescriptor(PropertyDescriptor descriptor) {
		descriptor.setAlwaysIncompatible(true);
		descriptor.setCategory(C_JAR_ENTRY_RESOURCE);
		JAR_ENTRY_RESOURCE_PROPERTY_DESCRIPTORS.add(descriptor);
	}
	

	public JarEntryResourceProperties(IJarEntryResource jarEntryResource) {
		fJarEntryResource= jarEntryResource;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> result= new ArrayList<IPropertyDescriptor>(JAR_ENTRY_RESOURCE_PROPERTY_DESCRIPTORS);
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}
	
	public Object getPropertyValue(Object name) {
		if (name.equals(P_NAME)) {
			return fJarEntryResource.getName();
		} else 	if (name.equals(P_FULL_PATH)) {
			return fJarEntryResource.getFullPath();
		} else 	if (name.equals(P_IS_FILE)) {
			return fJarEntryResource.isFile();
		} else 	if (name.equals(P_IS_READ_ONLY)) {
			return fJarEntryResource.isReadOnly();
		}
		
		return null;
	}
	
	public void setPropertyValue(Object name, Object value) {
		// do nothing
	}
	
	public Object getEditableValue() {
		return this;
	}
	
	public boolean isPropertySet(Object property) {
		return false;
	}
	
	public void resetPropertyValue(Object property) {
		// do nothing
	}
}
