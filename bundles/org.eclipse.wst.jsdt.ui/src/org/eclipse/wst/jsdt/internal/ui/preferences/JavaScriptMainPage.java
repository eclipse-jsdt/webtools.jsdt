/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
package org.eclipse.wst.jsdt.internal.ui.preferences;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
//import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * @author childsb
 *

 */


public class JavaScriptMainPage extends PropertyPage implements IWorkbenchPropertyPage{
	
	public static final String PROP_ID= "org.eclipse.wst.jsdt.internal.ui.preferences.JavaScriptMainPage"; //$NON-NLS-1$
	
	public JavaScriptMainPage() {}
	
	protected Control createContents(Composite parent) {
		final Composite composite= new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout());
		
//		Link link= new Link(composite, SWT.WRAP);
//		GridData data= new GridData(SWT.FILL, SWT.BEGINNING, true, false);
//		data.widthHint= 300;
//		link.setLayoutData(data);
//		link.setText(PreferencesMessages.JavaEditorPropertyPage_SaveActionLink_Text);
//		link.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				IWorkbenchPreferenceContainer container= (IWorkbenchPreferenceContainer)getContainer();
//				container.openPage(SaveParticipantPreferencePage.PROPERTY_PAGE_ID, null);
//			}
//		});
		
		return composite;
	}
}
