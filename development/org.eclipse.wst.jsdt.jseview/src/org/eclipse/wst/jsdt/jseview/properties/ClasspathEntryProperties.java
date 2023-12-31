/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.jseview.JEViewPlugin;

public class ClasspathEntryProperties implements IPropertySource {
	
	private static abstract class Property extends GenericProperty<IIncludePathEntry> {
		public Property(String name) {
			super(IIncludePathEntry.class, name);
		}
	}
	
	private static HashMap<String, Property> fgIdToProperty= new HashMap<String, Property>();
	private static LinkedHashMap<Class<?>, List<Property>> fgTypeToProperty= new LinkedHashMap<Class<?>, List<Property>>();
	
	static {
		addProperty(new Property("combineAccessRules") {
			@Override public Object compute(IIncludePathEntry entry) {
				return entry.combineAccessRules();
			}
		});
		addProperty(new Property("getContentKind") {
			@Override public Object compute(IIncludePathEntry entry) {
				return getContentKindString(entry.getContentKind());
			}
		});
		addProperty(new Property("getEntryKind") {
			@Override public Object compute(IIncludePathEntry entry) {
				return getEntryKindString(entry.getEntryKind());
			}
		});
//		addProperty(new Property("getOutputLocation") {
//			@Override public Object compute(IIncludePathEntry entry) {
//				return entry.getOutputLocation();
//			}
//		});
		addProperty(new Property("getPath") {
			@Override public Object compute(IIncludePathEntry entry) {
				return entry.getPath();
			}
		});
		addProperty(new Property("getSourceAttachmentPath") {
			@Override public Object compute(IIncludePathEntry entry) {
				return entry.getSourceAttachmentPath();
			}
		});
		addProperty(new Property("getSourceAttachmentRootPath") {
			@Override public Object compute(IIncludePathEntry entry) {
				return entry.getSourceAttachmentRootPath();
			}
		});
		addProperty(new Property("isExported") {
			@Override public Object compute(IIncludePathEntry entry) {
				return entry.isExported();
			}
		});
	}
	
	private static void addProperty(Property property) {
		fgIdToProperty.put(property.getId(), property);
		List<Property> properties= fgTypeToProperty.get(property.getType());
		if (properties == null) {
			properties= new ArrayList<Property>();
			fgTypeToProperty.put(property.getType(), properties);
		}
		properties.add(property);
	}
	
	static String getContentKindString(int kind) {
		String name;
		switch (kind) {
			case IPackageFragmentRoot.K_SOURCE :
				name= "K_SOURCE";
				break;
			case IPackageFragmentRoot.K_BINARY :
				name= "K_BINARY";
				break;
			default :
				name= "UNKNOWN";
				break;
		}
		return kind + " (" + name + ")";
	}
	
	static String getEntryKindString(int kind) {
		String name;
		switch (kind) {
			case IIncludePathEntry.CPE_CONTAINER :
				name= "CPE_CONTAINER";
				break;
			case IIncludePathEntry.CPE_LIBRARY :
				name= "CPE_LIBRARY";
				break;
			case IIncludePathEntry.CPE_PROJECT :
				name= "CPE_PROJECT";
				break;
			case IIncludePathEntry.CPE_SOURCE :
				name= "CPE_SOURCE";
				break;
			case IIncludePathEntry.CPE_VARIABLE :
				name= "CPE_VARIABLE";
				break;
			default :
				name= "UNKNOWN";
				break;
		}
		return kind + " (" + name + ")";
	}
	
	
	protected IIncludePathEntry fEntry;

	public ClasspathEntryProperties(IIncludePathEntry entry) {
		fEntry= entry;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> result= new ArrayList<IPropertyDescriptor>();
		for (Entry<Class<?>, List<Property>> entry : fgTypeToProperty.entrySet()) {
			if (entry.getKey().isAssignableFrom(fEntry.getClass())) {
				for (Property property : entry.getValue()) {
					result.add(property.getDescriptor());
				}
			}
		}
		return result.toArray(new IPropertyDescriptor[result.size()]);
	}
	
	public Object getPropertyValue(Object id) {
		Property property= fgIdToProperty.get(id);
		if (property == null) {
			return null;
		} else {
			try {
				return property.compute(fEntry);
			} catch (JavaScriptModelException e) {
				if (e.isDoesNotExist()) {
					return "JavaScriptModelException: " + e.getLocalizedMessage();
				} else {
					JEViewPlugin.log("error calculating property '" + property.getType().getSimpleName() + '#' + property.getName() + '\'', e);
					return "Error: " + e.getLocalizedMessage();
				}
			}
		}
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
