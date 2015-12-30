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
package org.eclipse.wst.jsdt.js.common.build.system.launch.ui;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.js.common.CommonPlugin;
import org.eclipse.wst.jsdt.js.common.Messages;
import org.eclipse.wst.jsdt.js.common.build.system.ui.IFileSelectionDialog;
import org.eclipse.wst.jsdt.js.common.util.WorkbenchResourceUtil;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
@SuppressWarnings("restriction")
public abstract class GenericBuildSystemTab extends AbstractLaunchConfigurationTab {
	private WidgetListener defaultListener = new WidgetListener();
	protected Text buildFileText;
	protected Combo tasksCommbo;
	
	protected abstract String[] getTasksFromFile(IFile file) throws JavaScriptModelException;
	
		
	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		((GridLayout) comp.getLayout()).verticalSpacing = 0;		
		createBuildFileEditor(comp);
		createTaskComboEditor(comp);
		setControl(comp);
	}
	
	private void createTaskComboEditor(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.LaunchTab_Tasks, 1, 1, GridData.FILL_HORIZONTAL);
		tasksCommbo = SWTFactory.createCombo(group, SWT.BORDER|SWT.H_SCROLL, 0, null);
		tasksCommbo.addModifyListener(defaultListener);
	}

	private void createBuildFileEditor(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.LaunchTab_BuildFile, 2, 1, GridData.FILL_HORIZONTAL);
		buildFileText = SWTFactory.createSingleText(group, 1);
		buildFileText.addModifyListener(defaultListener);
		Button fileButton = createPushButton(group, Messages.LaunchTab_Browse, null); 
		fileButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBuildFileButtonSelected();				
			}
		});
	}
	
	@Override
	public String getName() {
		return Messages.LaunchTab_Main;
	}
	
	protected void updateTasks(String[] tasks) {
		if (tasksCommbo != null && !tasksCommbo.isDisposed()) {
			tasksCommbo.removeAll();
			if (tasks != null && tasks.length > 0) {
				tasksCommbo.setItems(tasks);
				tasksCommbo.setText(tasks[0]);
			}
		}
	}
	
	protected void handleBuildFileButtonSelected() {
		IFileSelectionDialog dialog = new IFileSelectionDialog(Messages.LaunchTab_DialogTitle,
				Messages.LaunchTab_DialogMessage, new String[] { "js" }); //$NON-NLS-1$
		dialog.open();
		Object result = dialog.getFirstResult();	
		if (result instanceof IFile) {
			buildFileText.setText(((IFile) result).getLocation().toOSString());			
		}
	}
	
	IDialogSettings getDialogBoundsSettings(String id) {
		IDialogSettings settings = CommonPlugin.getDefault().getDialogSettings();
		IDialogSettings section = settings.getSection(id);
		if (section == null) {
			section = settings.addNewSection(id);
		} 
		return section;
	}
	
	private class WidgetListener implements ModifyListener, SelectionListener {
		
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}
		
		public void widgetDefaultSelected(SelectionEvent e) {/*do nothing*/}
		
		public void widgetSelected(SelectionEvent e) {
			updateLaunchConfigurationDialog();
		}
	}
	
	protected IFile getBuildFile() {
		if (buildFileText != null && !buildFileText.isDisposed()) {
			String path = buildFileText.getText();
			File file = WorkbenchResourceUtil.getFile(path);
			if (file != null) {
				return WorkbenchResourceUtil.getFileForLocation(file.getAbsolutePath());
			}
		}
		return null;
	}
	

}
