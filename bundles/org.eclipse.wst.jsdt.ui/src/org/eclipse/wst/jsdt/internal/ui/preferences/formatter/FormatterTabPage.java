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
package org.eclipse.wst.jsdt.internal.ui.preferences.formatter;

import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;

public abstract class FormatterTabPage extends ModifyDialogTabPage {
	
	private final static String SHOW_INVISIBLE_PREFERENCE_KEY= JavaScriptUI.ID_PLUGIN + ".formatter_page.show_invisible_characters"; //$NON-NLS-1$
	
	private JavaPreview fPreview;
	private final IDialogSettings fDialogSettings;
	private Button fShowInvisibleButton;

	public FormatterTabPage(IModificationListener modifyListener, Map workingValues) {
		super(modifyListener, workingValues);
		
		fDialogSettings= JavaScriptPlugin.getDefault().getDialogSettings();
	}

	protected Composite doCreatePreviewPane(Composite composite, int numColumns) {
		
		createLabel(numColumns - 1, composite, FormatterMessages.ModifyDialogTabPage_preview_label_text);  
		
		fShowInvisibleButton= new Button(composite, SWT.CHECK);
		fShowInvisibleButton.setText(FormatterMessages.FormatterTabPage_ShowInvisibleCharacters_label);
		fShowInvisibleButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		fShowInvisibleButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fPreview.showInvisibleCharacters(fShowInvisibleButton.getSelection());
				fDialogSettings.put(SHOW_INVISIBLE_PREFERENCE_KEY, fShowInvisibleButton.getSelection());
			}
		});
		fShowInvisibleButton.setSelection(isShowInvisible());
		
		fPreview= doCreateJavaPreview(composite);
		fDefaultFocusManager.add(fPreview.getControl());
		fPreview.showInvisibleCharacters(fShowInvisibleButton.getSelection());
		
		final GridData gd= createGridData(numColumns, GridData.FILL_BOTH, 0);
		gd.widthHint= 0;
		gd.heightHint=0;
		fPreview.getControl().setLayoutData(gd);
		
		return composite;
	}

	private boolean isShowInvisible() {
		return fDialogSettings.getBoolean(SHOW_INVISIBLE_PREFERENCE_KEY);
	}
	
	protected void doUpdatePreview() {
		boolean showInvisible= isShowInvisible();
		fPreview.showInvisibleCharacters(showInvisible);
		fShowInvisibleButton.setSelection(showInvisible);
	}

}
