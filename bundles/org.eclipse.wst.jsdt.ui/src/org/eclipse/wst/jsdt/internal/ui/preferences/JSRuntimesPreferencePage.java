/*******************************************************************************
* Copyright (c) 2016 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.Logger;

/**
 * @author orlandor@mx1.ibm.com
 *
 */
@SuppressWarnings("restriction")
public class JSRuntimesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	/**
	 * ID for the page
	 */
	public static final String ID = "org.eclipse.wst.jsdt.ui.preferences.RuntimesPreferencePage"; //$NON-NLS-1$
	
	private String[] runtimeNamesArray;
	private String[] runtimeIndexToIdsMap;
	
	private Map <String, IJSRuntimeInstall> defaultInstallByType; 
	private Map <String, IJSRuntimeInstall[]> fRuntimeInstallsByType;
	private Map <String, String> fOriginalRuntimesTimeStamp;
	private Map <String, String> fLastRuntimesTimeStamp;
	
	private String currentRuntimeTypeId;
	
	private Combo fJSTypeCombo;
	private InstalledJSRuntimesBlock fJSRuntimesBlock;
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		defaultInstallByType = new HashMap<String, IJSRuntimeInstall> ();
		fRuntimeInstallsByType = new HashMap <String, IJSRuntimeInstall[]>();
		fOriginalRuntimesTimeStamp = new HashMap <String, String> ();
		fLastRuntimesTimeStamp = new HashMap <String, String> ();
	}
	
	private void initRuntimeTypes () {
		// We don't know anything about the runtime types, so let's gather
		// the information we need.
		if (runtimeNamesArray == null) {
			Collection <IJSRuntimeType> runtimeTypes = JSRuntimeManager.getJSRuntimeTypes();
			IJSRuntimeType[] jsRuntimeTypes = new IJSRuntimeType[runtimeTypes.size()];
			runtimeTypes.toArray(jsRuntimeTypes);
			
			// Sort the elements for display... just once
			Arrays.sort(jsRuntimeTypes, new Comparator<IJSRuntimeType>() {
				public int compare(IJSRuntimeType runtime1, IJSRuntimeType runtime2) {
					return runtime1.getName().compareToIgnoreCase(runtime2.getName());
				}
			});
			
			runtimeIndexToIdsMap = new String[jsRuntimeTypes.length];
			runtimeNamesArray = new String[jsRuntimeTypes.length];
			
			for (int i = 0; i < jsRuntimeTypes.length; ++i) {
				// This is the list of names that will be shown in our combo.
				// We can safely use this here because we know they are already
				// ordered
				runtimeNamesArray[i] = jsRuntimeTypes[i].getName();
				
				// Build a map from index (in the array, which is the same will be used in the combo)
				// to ids, which is the correct and easiest way to handle the runtime types.
				runtimeIndexToIdsMap[i] = jsRuntimeTypes[i].getId();
			}
		}
	}
	
	private void initDefaultRuntimeInstallsMap () {
		defaultInstallByType.clear();
		Collection <String> runtimeTypeIds = JSRuntimeManager.getJSRuntimeTypesIds();
		for (String runtimeTypeId : runtimeTypeIds) {
			IJSRuntimeInstall runtimeInstall = JSRuntimeManager.getDefaultRuntimeInstall (runtimeTypeId);
			defaultInstallByType.put(runtimeTypeId, runtimeInstall);
		}
	}
	
	private void initRuntimeInstallsByTypeMap () {
		fRuntimeInstallsByType.clear();
		Collection <String> runtimeTypeIds = JSRuntimeManager.getJSRuntimeTypesIds();
		for (String runtimeTypeId : runtimeTypeIds) {
			IJSRuntimeInstall[] runtimeInstalls = 
						JSRuntimeManager.getJSRuntimeInstallsByType(runtimeTypeId);
			fRuntimeInstallsByType.put(runtimeTypeId, runtimeInstalls);
		}
	}
	
	private String getRuntimeTypeId (int selection) {
		if (selection != -1) {
			return runtimeIndexToIdsMap[selection];
		}
		
		return null;
	}
	
	private void backupTimeStamp () {
		String oldTimeStamp = fOriginalRuntimesTimeStamp.get (currentRuntimeTypeId);
		if (oldTimeStamp == null) {
			// Never seen, just set the current baseline
			fOriginalRuntimesTimeStamp.put(currentRuntimeTypeId, fJSRuntimesBlock.getTimeStamp());
		}
		fLastRuntimesTimeStamp.put(currentRuntimeTypeId, fJSRuntimesBlock.getTimeStamp());
	}
	
	private void clearEnvironment () {
		initRuntimeInstallsByTypeMap ();
		initDefaultRuntimeInstallsMap();
		fOriginalRuntimesTimeStamp.clear();
		fLastRuntimesTimeStamp.clear();
		fJSTypeCombo.select(0);
		currentRuntimeTypeId = getRuntimeTypeId(0);
		fJSRuntimesBlock.setRuntimeType (JSRuntimeManager.getJSRuntimeType(currentRuntimeTypeId));
		initDefaultRuntime ();
		backupTimeStamp();
	}

	protected Control createContents(Composite ancestor) {
		initializeDialogUnits(ancestor);
		
		noDefaultButton();
		
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		ancestor.setLayout(layout);
		
		SWTFactory.createWrapLabel(ancestor, PreferencesMessages.JSRuntimesPreferencePage_JSRuntimes_Title, 1, 300);
		SWTFactory.createVerticalSpacer(ancestor, 1);
		
		// TODO: Test with empty combo (i.e. no runtime types)
		initRuntimeTypes();
		
		fJSTypeCombo = SWTFactory.createCombo(ancestor, SWT.BORDER | SWT.READ_ONLY, 1, runtimeNamesArray);
		fJSTypeCombo.addSelectionListener(new SelectionAdapter() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				// When combo changes, backup the current runtime installs because those are about
				// to be lost, then make sure to re-populate the runtime installs list with the
				// new values
				fRuntimeInstallsByType.put(currentRuntimeTypeId, fJSRuntimesBlock.getRuntimeInstalls());
				// Backup timestamp for the old type
				backupTimeStamp ();
				currentRuntimeTypeId = getRuntimeTypeId (fJSTypeCombo.getSelectionIndex());
				// Inform the runtimes block that the type has change so major updates will happen
				fJSRuntimesBlock.setRuntimeType (JSRuntimeManager.getJSRuntimeType(currentRuntimeTypeId), 
							fRuntimeInstallsByType.get(currentRuntimeTypeId));
				updateDefaultRuntimeInstall (defaultInstallByType.get(currentRuntimeTypeId));
				// Backup timestamp for the new runtime type we are about to work with
				backupTimeStamp();
				super.widgetSelected(e);
			}
			
		});
		
		fJSRuntimesBlock = new InstalledJSRuntimesBlock();
		fJSRuntimesBlock.createControl(ancestor);
		Control control = fJSRuntimesBlock.getControl();
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 1;
		control.setLayoutData(data);
		
		fJSRuntimesBlock.restoreColumnSettings(JavaScriptPlugin.getDefault().getDialogSettings(), ID);
		
		fJSRuntimesBlock.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// Use this moment to update the default runtime for a given runtime type
				// and to retrieve/store the current list of runtime installs that
				// probably are out of sync with the model.
				IJSRuntimeInstall install = fJSRuntimesBlock.getCheckedRuntimeInstall();
				if (install == null) {
					setValid(false);
					if(fJSRuntimesBlock.getRuntimeInstalls().length < 1) {
						setErrorMessage(PreferencesMessages.JSRuntimesPreferencePage_NoRuntimes_Error);
					}
					else {
						setErrorMessage(PreferencesMessages.JSRuntimesPreferencePage_NoDefaultRuntime_Error); 
					}
				} else {
					setMessage(null);
					setValid(true);
					setErrorMessage(null);
					updateDefaultRuntimeInstall (install);
				}
			}
		});
		applyDialogFont(ancestor);
		
		clearEnvironment();
		
		return ancestor;
	}
	
	/**
	 * 
	 */
	private void initDefaultRuntime() {
		IJSRuntimeInstall realDefault= JSRuntimeManager.getDefaultRuntimeInstall(currentRuntimeTypeId);
		if (realDefault != null) {
			IJSRuntimeInstall[] vms= fJSRuntimesBlock.getRuntimeInstalls();
			for (int i = 0; i < vms.length; i++) {
				IJSRuntimeInstall fakeRuntime = vms[i];
				if (fakeRuntime.equals(realDefault)) {
					fJSRuntimesBlock.setCheckedRuntimeInstall(fakeRuntime);
					break;
				}
			}
		}
	}

	private void updateDefaultRuntimeInstall(IJSRuntimeInstall install) {
		if (currentRuntimeTypeId != null) {
			defaultInstallByType.put(currentRuntimeTypeId, install);
			fJSRuntimesBlock.setCheckedRuntimeInstall(install);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		final boolean[] canceled = new boolean[] {false};
		BusyIndicator.showWhile(null, new Runnable() {
			@Override
			public void run() {
				// Backup current block state
				fRuntimeInstallsByType.put(currentRuntimeTypeId, fJSRuntimesBlock.getRuntimeInstalls());
				
				Collection<String> runtimeTypesIds = fRuntimeInstallsByType.keySet();
				for (String runtimeTypeId : runtimeTypesIds) {
					IJSRuntimeInstall [] currentInstalls = fRuntimeInstallsByType.get(runtimeTypeId);
					List <String> currentInstallIds = new ArrayList <String> ();
					for (int i = 0; i < currentInstalls.length; ++i) {
						if (currentInstalls[i] instanceof JSRuntimeWorkingCopy) {
							JSRuntimeWorkingCopy standin = (JSRuntimeWorkingCopy) currentInstalls[i];
							standin.convertToRealRuntime();
						}
						currentInstallIds.add(currentInstalls[i].getId());
					}
					
					// Now, need to iterate the other way around to identify 
					// removed entries
					IJSRuntimeInstall [] allInstalls = JSRuntimeManager.getJSRuntimeInstallsByType(runtimeTypeId);
					for (int i = 0; i < allInstalls.length; ++i) {
						// This entry was removed, so persist the change
						if (!currentInstallIds.contains(allInstalls[i].getId())) {
							JSRuntimeManager.removeJSRuntimeInstall(allInstalls[i].getId());
						}
					}
					
					IJSRuntimeInstall defaultInstall = defaultInstallByType.get(runtimeTypeId);
					if (defaultInstall instanceof JSRuntimeWorkingCopy) {
						defaultInstall = ((JSRuntimeWorkingCopy) defaultInstall).convertToRealRuntime();
					}
					JSRuntimeManager.setDefaultRuntimeInstall(runtimeTypeId, defaultInstall);
				}
				
				// Reset timestamps
				fOriginalRuntimesTimeStamp.clear();
				fLastRuntimesTimeStamp.clear();
				
				try {
					JSRuntimeManager.saveRuntimesConfiguration ();
				}
				catch (CoreException e) {
					Logger.log(Logger.ERROR, "Error saving preferences for JavaScript runtimes.", e); //$NON-NLS-1$
				}	
			}
		});
		
		if(canceled[0]) {
			return false;
		}
		
		// save column widths
		IDialogSettings settings = JavaScriptPlugin.getDefault().getDialogSettings();
		fJSRuntimesBlock.saveColumnSettings(settings, ID);
				
		return super.performOk();
	}
	
	/*
	 * @see org.eclipse.jface.preference.PreferencePage#okToLeave()
	 */
	@Override
	public boolean okToLeave() {
		// Backup current block state so you can confidently iterate over
		// updated collections;
		backupTimeStamp ();
		
		boolean hasChanges = false;
		Set <String> keys = fOriginalRuntimesTimeStamp.keySet();
		for (String key : keys) {
			String originalts = fOriginalRuntimesTimeStamp.get(key);
			String finalts = fLastRuntimesTimeStamp.get(key);
			if (!originalts.equals(finalts)) {
				hasChanges = true;
				break;
			}
		}
		
		if (hasChanges) {
			String title = PreferencesMessages.JSRuntimesPreferencePage_JSRuntimes_Title;
			String message = PreferencesMessages.JSRuntimesPreferencePage_UnsavedChanges_Message;
			// Just give options to apply or discard which simplifies the code and
			// allows us to reuse when switching the runtime types combo box selection.
			String[] buttonLabels = new String[] { 
						PreferencesMessages.JSRuntimesPreferencePage_ApplyButton_Label, 
						PreferencesMessages.JSRuntimesPreferencePage_DiscardButton_Label, 
						PreferencesMessages.JSRuntimesPreferencePage_ApplyLaterButton_Label };
			MessageDialog dialog = new MessageDialog(getShell(), title, null, message, MessageDialog.QUESTION, buttonLabels, 0);
			int res = dialog.open();
			if (res == 0) { // apply
				return performOk() && super.okToLeave();
			} else if (res == 1) { // discard
				clearEnvironment();
				fJSRuntimesBlock.restoreColumnSettings(JavaScriptPlugin.getDefault().getDialogSettings(), ID);
			} else { // apply later
			}
		}
		return super.okToLeave();
	}

}
