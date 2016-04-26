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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.internal.ui.dialogs.StatusInfo;

/**
 * Page used to edit a standard runtime install.
 */
@SuppressWarnings("restriction")
public class StandardRuntimeInstallPage extends AbstractRuntimeInstallPage {
	
	// Runtime being edited or created
	private JSRuntimeWorkingCopy fRuntimeInstall;
	private Text fRuntimeName;
	private Text fRuntimeArgs;
	private Text fRuntimeLocation;
	private IStatus[] fFieldStatus = new IStatus[1];
	
	/**
	 * 
	 */
	public StandardRuntimeInstallPage() {
		super(PreferencesMessages.StandardRuntimeInstallPage_PageTitle);
		for (int i = 0; i < fFieldStatus.length; i++) {
			fFieldStatus[i] = Status.OK_STATUS;
		}
	}	

	@Override
	public void createControl(Composite p) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(p, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// Runtime location
		SWTFactory.createLabel(composite, PreferencesMessages.StandardRuntimeInstallPage_RuntimeLocationField_Label, 1);
		fRuntimeLocation = SWTFactory.createSingleText(composite, 1);
		Button folders = SWTFactory.createPushButton(composite, PreferencesMessages.StandardRuntimeInstallPage_RuntimeLocationFileButton_Label, null);
		GridData data = (GridData) folders.getLayoutData();
		data.horizontalAlignment = GridData.END;
		//Runtime name
		SWTFactory.createLabel(composite, PreferencesMessages.StandardRuntimeInstallPage_RuntimeLocationNameField_Label, 1);
		fRuntimeName = SWTFactory.createSingleText(composite, 2);
		//Runtime arguments
		SWTFactory.createLabel(composite, PreferencesMessages.StandardRuntimeInstallPage_RuntimeArgumentsField_Label, 1);
		fRuntimeArgs = SWTFactory.createSingleText(composite, 1);
		Button variables = SWTFactory.createPushButton(composite, PreferencesMessages.StandardRuntimeInstallPage_RuntimeLocationVariablesButton_Label, null);
		data = (GridData) variables.getLayoutData();
		data.horizontalAlignment = GridData.END;
		
		//add the listeners now to prevent them from monkeying with initialized settings
		fRuntimeName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validateRuntimeName();
			}
		});
		fRuntimeLocation.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validateRuntimeLocation();
			}
		});
		folders.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				File file = new File(fRuntimeLocation.getText());
				String text = fRuntimeLocation.getText();
				if (file.isFile()) {
					text = file.getAbsolutePath();
				}
				dialog.setFilterPath(text); 
				String newPath = dialog.open();
				if (newPath != null) {
					fRuntimeLocation.setText(newPath);
				}
			}
		});
		variables.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
				if (dialog.open() == Window.OK) {
					String expression = dialog.getVariableExpression();
					if (expression != null) {
						fRuntimeArgs.insert(expression);
					}
				}
			}
		});
		Dialog.applyDialogFont(composite);
		setControl(composite);
		initializeFields();
	}	
	
	/**
	 * Validates the runtime location
	 * @return the status after validating the runtime location
	 */
	private void validateRuntimeLocation() {
		String locationName = fRuntimeLocation.getText();
		IStatus s = null;
		File file = null; 
		if (locationName.length() == 0) {
			s = new StatusInfo(IStatus.ERROR, PreferencesMessages.StandardRuntimeInstallPage_RuntimeEmptyLocation_Error); 
		} 
		else {
			file = new File(locationName);
			if (!file.exists()) {
				s = new StatusInfo(IStatus.ERROR, PreferencesMessages.StandardRuntimeInstallPage_RuntimeUnexistingLocation_Error); 
			} 
			else {
				final IStatus[] temp = new IStatus[1];
				final File tempFile = file; 
				Runnable r = new Runnable() {
					@Override
					public void run() {
						temp[0] = fRuntimeInstall.getRuntimeType().validateInstallLocation(tempFile);
					}
				};
				BusyIndicator.showWhile(getShell().getDisplay(), r);
				s = temp[0];
			}
		}
		if (file != null) {
			fRuntimeInstall.setInstallLocation(file);
		}
		if (s.isOK() && file != null) {
			String name = fRuntimeName.getText();
			if (name == null || name.trim().length() == 0) {
				// auto-generate runtime name
				if (file.isFile()) {
					String fileName = file.getName();
					fRuntimeName.setText(fileName);
				} else {
					try {
						String genName = null;
						IPath path = new Path(file.getCanonicalPath());
						int segs = path.segmentCount();
						if (segs == 1) {
							genName = path.segment(0);
						} 
						else if (segs >= 2) {
							genName = path.lastSegment();
						}
						if (genName != null) {
							fRuntimeName.setText(genName);
						}
					} catch (IOException e) {}
				}
			}
		}
		setRuntimeInstallLocationStatus(s);
		updatePageStatus();
	}
	
	/**
	 * Returns the installation location as a file from the runtime root text control
	 * @return the installation location as a file
	 */
	protected File getInstallLocation() {
		return new File(fRuntimeLocation.getText());
	}	

	/**
	 * Validates the entered name of the runtime install
	 * @return the status of the name validation
	 */
	private void validateRuntimeName() {
		nameChanged(fRuntimeName.getText());
	}
	
	@Override
	public boolean finish() {
		setFieldValuesToRuntime(fRuntimeInstall);
		return true;
	}

	@Override
	public JSRuntimeWorkingCopy getSelection() {
		return fRuntimeInstall;
	}
	
	@Override
	public void setSelection(JSRuntimeWorkingCopy runtime) {
		super.setSelection(runtime);
		fRuntimeInstall = runtime;
		setTitle(PreferencesMessages.StandardRuntimeInstallPage_RuntimeDefinition_Title);
		setDescription(PreferencesMessages.StandardRuntimeInstallPage_RuntimeAttributes_Message);
	}
	
	/**
	 * initialize fields to the specified runtime
	 * @param runtime the runtime to initialize from
	 */
	protected void setFieldValuesToRuntime(JSRuntimeWorkingCopy runtime) {
		File dir = new File(fRuntimeLocation.getText());
		File file = dir.getAbsoluteFile();
		runtime.setInstallLocation(file);
		runtime.setName(fRuntimeName.getText());
		
		String argString = fRuntimeArgs.getText().trim();
		if (argString != null && argString.length() > 0) {
			runtime.setJSRuntimeArguments(argString);			
		} 
		else {
			runtime.setJSRuntimeArguments(null);
		} 
	}
	
	/**
	 * Creates a unique name for the runtime type
	 * @param runtimeType the runtime install type
	 * @return a unique name
	 */
	protected static String createUniqueId(IJSRuntimeType runtimeType) {
		String id = null;
		do {
			id = String.valueOf(System.currentTimeMillis());
		} while (JSRuntimeManager.getJSRuntimeInstall(id) != null);
		return id;
	}	
	
	/**
	 * Initialize the dialogs fields
	 */
	private void initializeFields() {
		fRuntimeName.setText(fRuntimeInstall.getName());
		File installLocation = fRuntimeInstall.getInstallLocation();
		if (installLocation != null) {
			fRuntimeLocation.setText(installLocation.getAbsolutePath());
		}
		String runtimeArgs = fRuntimeInstall.getJSRuntimeArgumentsAsString();
		if (runtimeArgs != null) {
			fRuntimeArgs.setText(runtimeArgs);
		}
		validateRuntimeName();
		validateRuntimeLocation();
	}	
	
	/**
	 * Sets the status of the JRE location field.
	 * 
	 * @param status JRE location status
	 */
	private void setRuntimeInstallLocationStatus(IStatus status) {
		fFieldStatus[0] = status;
	}
	
	@Override
	public String getErrorMessage() {
		String message = super.getErrorMessage();
		return message;
	}

	@Override
	public boolean isPageComplete() {
		boolean complete = super.isPageComplete();
		return complete;
	}

	@Override
	protected IStatus[] getRuntimeStatus() {
		return fFieldStatus;
	}
	
}
