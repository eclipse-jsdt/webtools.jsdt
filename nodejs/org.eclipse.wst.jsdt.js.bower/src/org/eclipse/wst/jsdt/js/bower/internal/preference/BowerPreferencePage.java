/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.bower.internal.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.jsdt.js.bower.internal.Messages;
import org.eclipse.wst.jsdt.js.bower.internal.preference.editor.BowerHomeFieldEditor;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class BowerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String PAGE_ID = "org.jboss.tools.jst.js.bower.preferences.BowerPreferencesPage"; //$NON-NLS-1$

	public BowerPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(BowerPreferenceHolder.getStore());
	}

	@Override
	protected void createFieldEditors() {
		BowerHomeFieldEditor bowerHomeEditor = new BowerHomeFieldEditor(BowerPreferenceHolder.PREF_BOWER_LOCATION,
				Messages.BowerPreferencePage_BowerLocationLabel, getFieldEditorParent());
		addField(bowerHomeEditor);
	}
	
}
