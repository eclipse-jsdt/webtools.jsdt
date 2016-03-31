/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 	    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.internal.launch.ui;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.internal.Messages;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;
import org.eclipse.wst.jsdt.js.node.internal.ui.ImageResource;

/**
 * Main tab for node application launch configuration
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeLaunchMainTab extends AbstractLaunchConfigurationTab {

    private Text scriptText;
    private Button searchButton;
	private boolean mainTabEntriesValid;

	// Debug variables
	private Label debugPortLabel;
	private Text debugPortText;
	private Label debugHostLabel;
	private Text debugHostText;
	private Button debugAddNetworkConsoleButton;
	private Button debugBreakButton;

	// Generic Selection Listener
	private SelectionAdapter selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			updateLaunchConfigurationDialog();
		}
	};

    //Generic Modify Listener
	private ModifyListener modifyListener = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
            updateLaunchConfigurationDialog();
        }
    };

	//
	private VerifyListener onlyDigitsListener = new VerifyListener() {
		@Override
		public void verifyText(VerifyEvent e) {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9')) {
					e.doit = false;
					return;
				}
			}
		}
	};

	@Override
	public void createControl(Composite parent) {
		String mode = getLaunchConfigurationDialog().getMode();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite = new Composite(scrolledComposite, SWT.NULL);
		scrolledComposite.setContent(composite);

		Point minSize = composite.computeSize(0, 350);
		composite.setSize(minSize);
		if (composite.getParent() instanceof ScrolledComposite) {
			ScrolledComposite sc1 = (ScrolledComposite)composite.getParent();
			sc1.setMinSize(minSize);
			sc1.setExpandHorizontal(true);
			sc1.setExpandVertical(true);
		}

		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		layout.numColumns = 1;
		composite.setLayout(layout);

        createProgramGroup(composite);
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			createDebugSpecificsGroup(composite);
		}

		setControl(scrolledComposite);
	}

	private void createDebugSpecificsGroup(Composite parent) {
		Group connectionGroup = new Group(parent, SWT.NONE);
		connectionGroup.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_CONNECTION_TEXT);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		connectionGroup.setLayoutData(gd);
		connectionGroup.setLayout(new GridLayout(1, false));
		connectionGroup.setFont(parent.getFont());

		// Create
		debugHostLabel = new Label(connectionGroup, SWT.NONE);
		debugHostLabel.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_HOST_TEXT);
		debugHostText = new Text(connectionGroup, SWT.SINGLE | SWT.BORDER);
		debugHostText.setLayoutData(gd);
		debugHostText.setFont(parent.getFont());
		debugHostText.addModifyListener(modifyListener);

		debugPortLabel = new Label(connectionGroup, SWT.NONE);
		debugPortLabel.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_PORT_TEXT);
		debugPortText = new Text(connectionGroup, SWT.SINGLE | SWT.BORDER);
		debugPortText.setTextLimit(5);
		debugPortText.setLayoutData(gd);
		debugPortText.setFont(parent.getFont());
		debugPortText.addModifyListener(modifyListener);
		debugPortText.addVerifyListener(onlyDigitsListener);

		debugBreakButton = createCheckButton(connectionGroup, Messages.LAUNCH_CONFIGURATION_MAIN_TAB_BREAK_TEXT);
		debugBreakButton.addSelectionListener(selectionListener);

		debugAddNetworkConsoleButton = createCheckButton(connectionGroup,
				Messages.LAUNCH_CONFIGURATION_MAIN_TAB_DEBUGGER_NETWORK_CONSOLE_TEXT);
		debugAddNetworkConsoleButton.addSelectionListener(selectionListener);
	}

	private void createProgramGroup(Composite parent) {
        Group programGroup = new Group(parent, SWT.NONE);
        programGroup.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_MAIN_FILE_TEXT);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        programGroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        programGroup.setLayout(layout);
        programGroup.setFont(parent.getFont());

        //Script part
        scriptText = new Text(programGroup, SWT.SINGLE | SWT.BORDER);
        scriptText.setLayoutData(gd);
        scriptText.setFont(parent.getFont());
        scriptText.addModifyListener(modifyListener);
        searchButton = createPushButton(programGroup, Messages.LAUNCH_CONFIGURATION_MAIN_TAB_WORKSPACE_BUTTON, null);
        searchButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
				String scriptFile = browseWorkspace(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_DESCRIPTION);
				if (scriptFile != null && !scriptFile.equals(NodeConstants.EMPTY)) {
					scriptText.setText(scriptFile);
					updateLaunchConfigurationDialog();
				}
            }
        });
	}

	protected String browseWorkspace(String description) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getControl().getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject || element instanceof IFolder) {
					return true;
			    }
			    if(element instanceof IFile) {
			    	return true;
			    }
			    return false;
			}
		});
		dialog.setTitle(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_TITLE);
		dialog.setMessage(description);
		dialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if (selection != null && selection.length > 0 && selection[0] instanceof IFile){
					return new Status(IStatus.OK, NodePlugin.PLUGIN_ID, IStatus.OK, NodeConstants.EMPTY, null);
				}
				return new Status(IStatus.ERROR, NodePlugin.PLUGIN_ID, IStatus.ERROR, NodeConstants.EMPTY, null);
			}
		});

		if (dialog.open() == Window.OK) {
			IFile file = (IFile)dialog.getFirstResult();
			if(file != null){
				String fullFileName = file.getLocation().toOSString();
				return fullFileName;
			}
		}
		return null;
	}

    @Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    }

    @Override
	public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            scriptText.setText(configuration.getAttribute(NodeConstants.ATTR_APP_PATH, NodeConstants.EMPTY));
			if (getLaunchConfigurationDialog().getMode().equals(ILaunchManager.DEBUG_MODE)) {
				debugHostText
						.setText(configuration.getAttribute(NodeConstants.ATTR_HOST_FIELD, NodeConstants.DEFAULT_HOST));
				debugPortText.setText(configuration.getAttribute(NodeConstants.ATTR_PORT_FIELD,
						String.valueOf(NodeConstants.DEFAULT_PORT)));
				debugAddNetworkConsoleButton
						.setSelection(configuration.getAttribute(NodeConstants.ATTR_ADD_NETWORK_CONSOLE_FIELD, false));
				debugBreakButton.setSelection(configuration.getAttribute(NodeConstants.ATTR_BREAK_FIELD, true));
			}
        } catch (CoreException e) {
            setErrorMessage(e.getMessage());
        }
    }

    @Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    	configuration.setAttribute(NodeConstants.ATTR_APP_PATH, scriptText.getText().trim());
		if (getLaunchConfigurationDialog().getMode().equals(ILaunchManager.DEBUG_MODE)) {
			configuration.setAttribute(NodeConstants.ATTR_HOST_FIELD, debugHostText.getText().trim());
			configuration.setAttribute(NodeConstants.ATTR_PORT_FIELD, debugPortText.getText().trim());
			configuration.setAttribute(NodeConstants.ATTR_ADD_NETWORK_CONSOLE_FIELD,
					debugAddNetworkConsoleButton.getSelection());
			configuration.setAttribute(NodeConstants.ATTR_BREAK_FIELD, debugBreakButton.getSelection());
		}

    	//Set default working directory
    	if(!scriptText.getText().trim().equals(NodeConstants.EMPTY)) {
    		File file = new File(scriptText.getText().trim());
        	IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(Path.fromOSString(file.getAbsolutePath()));
        	if(iFile != null && iFile.exists()) {
        		String project = iFile.getProject().getLocation().toOSString();
        		configuration.setAttribute(NodeConstants.ATTR_APP_PROJECT, project);
        	}
    	}
    }

    @Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
    	validateEntries();
    	return mainTabEntriesValid;
    }

	protected void validateEntries(){
        setErrorMessage(null);
		mainTabEntriesValid = true;

		String scriptFile = scriptText.getText();

        if (mainTabEntriesValid && scriptFile.length() > 0) {
            File file = new File(scriptFile);
            if (!file.exists()) {
                setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_DOES_NOT_EXIST);
                mainTabEntriesValid = false;
            }
        } else if(mainTabEntriesValid && scriptFile.length() <= 0){
            setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_MAIN_FILE);
            mainTabEntriesValid = false;
        }

        if(getLaunchConfigurationDialog().getMode().equals(ILaunchManager.DEBUG_MODE)){
    		String host = debugHostText.getText();

    		if (mainTabEntriesValid && host.length() <= 0) {
    			setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_HOST);
    			mainTabEntriesValid = false;
    		}

			// Validate port range
    		String port = debugPortText.getText();
			int portNumber = 0;
			if (!port.isEmpty()) {
				portNumber = Integer.valueOf(port);
			}

			if (mainTabEntriesValid && (port.length() <= 0 || portNumber < 1024 || portNumber > 65535)) {
				setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_INVALID_PORT);
				mainTabEntriesValid = false;
			}
        }
	}

    public Composite createComposite(Composite parent, Font font, int columns, int hspan, int fill) {
        Composite g = new Composite(parent, SWT.NONE);
        g.setLayout(new GridLayout(columns, false));
        g.setFont(font);
        GridData gd = new GridData(fill);
        gd.horizontalSpan = hspan;
        g.setLayoutData(gd);
        return g;
    }

	@Override
	public Image getImage() {
		return ImageResource.getImage(ImageResource.IMG_NODEJS);
	}

    @Override
	public String getName() {
        return Messages.LAUNCH_CONFIGURATION_MAIN_TAB;
    }
}