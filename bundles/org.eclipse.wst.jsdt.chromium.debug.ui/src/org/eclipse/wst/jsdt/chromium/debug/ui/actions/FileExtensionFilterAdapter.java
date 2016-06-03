/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.wst.jsdt.chromium.debug.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.ui.IActionFilter;

/**
 * This action filter is used to test the file extension for ResourceMapping objects. To use it you
 * must first declare it in your plugin.xml:
 * 
 * <extension point="org.eclipse.core.runtime.adapters">
 *    <factory
 *       adaptableType="org.eclipse.core.resources.mapping.ResourceMapping"
 *       class="org.eclipse.wst.jsdt.chromium.debug.ui.actions.FileExtensionFilterAdapterFactory">
 *       <adapter 
 *           type="org.eclipse.ui.IActionFilter"/>
 *   </factory>
 * </extension>
 * 
 * Once that's done it may be used to filter as per the <visibility> element in the following example:
 * 
 *     <objectContribution
 *         objectClass="org.eclipse.core.resources.mapping.ResourceMapping"
 *         adaptable="true"
 *         id="org.eclipse.wst.jsdt.chromium.debug.ui.ExampleActionId">
 *           
 *         <visibility>
 *              <or>
 *                  <objectState name="fileExtension" value="js"/>
 *                  <objectState name="fileExtension" value="chromium"/>
 *              </or>
 *         </visibility>
 * 
 * @author Shane Bryzak
 */
public class FileExtensionFilterAdapter implements IActionFilter {

	private static final Object FILE_EXTENSION = "fileExtension";
	
	private static FileExtensionFilterAdapter INSTANCE = new FileExtensionFilterAdapter();
	
	private FileExtensionFilterAdapter() {}
	
	@Override
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof ResourceMapping) {
			ResourceMapping obj = (ResourceMapping) target;
			
			if (FILE_EXTENSION.equals(name) && obj.getModelObject() instanceof IFile) {
				String fileExtension = ((IFile) obj.getModelObject()).getFileExtension();
				return value != null && fileExtension != null && value.toLowerCase().equals(fileExtension.toLowerCase());
			}
		}
		
		return false;
	}

	public static FileExtensionFilterAdapter getInstance() {
		return INSTANCE;
	}
}
