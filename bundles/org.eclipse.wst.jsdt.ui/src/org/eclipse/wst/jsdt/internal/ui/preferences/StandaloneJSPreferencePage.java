/*******************************************************************************
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2020. All Rights Reserved.
 * U.S. Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class StandaloneJSPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	/**
	 * 
	 */
	private static final String SCANNER_PROVIDING_BUNDLE = "org.eclipse.wst.jsdt.web.ui"; //$NON-NLS-1$
	private static final String TASKTAG_PREFERENCE_PAGE_ID = "org.eclipse.wst.sse.ui.preferences.tasktags"; //$NON-NLS-1$

	public StandaloneJSPreferencePage() {
		super();
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();

		PreferenceLinkArea link = null;
		if (hasTaskTagPage()) {
			link = new PreferenceLinkArea(parent, SWT.NONE, TASKTAG_PREFERENCE_PAGE_ID, PreferencesMessages.TaskTagLinkText, (IWorkbenchPreferenceContainer) getContainer(), null);
			return link.getControl();
		}
		else {
			Label label = new Label(parent, SWT.WRAP);
			label.setText(PreferencesMessages.TaskTagLinkAlternateText);
			return label;
		}
	}

	private boolean hasTaskTagPage() {
		return Platform.getBundle(SCANNER_PROVIDING_BUNDLE) != null;
	}

	public void init(IWorkbench workbench) {
		setDescription(PreferencesMessages.StandaloneJavaScriptPreferencePage_description);
	}
}
