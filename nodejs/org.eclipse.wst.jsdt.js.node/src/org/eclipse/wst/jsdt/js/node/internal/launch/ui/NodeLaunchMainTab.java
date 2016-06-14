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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import org.eclipse.wst.jsdt.js.node.internal.util.LaunchConfigurationUtil;

/**
 * Main tab for node application launch configuration
 *
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeLaunchMainTab extends AbstractLaunchConfigurationTab {

    private Text scriptText;
    private Button workspaceButton;
    private Text projectText;
    private Button browseButton;

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

		createProjectGroup(composite);
        createMainFileGroup(composite);
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
	
	private void createProjectGroup(Composite parent) {
        Group projectGroup = new Group(parent, SWT.NONE);
        projectGroup.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_PROJECT_TEXT);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectGroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setFont(parent.getFont());

        projectText = new Text(projectGroup, SWT.SINGLE | SWT.BORDER);
        projectText.setLayoutData(gd);
        projectText.setFont(parent.getFont());
        projectText.addModifyListener(modifyListener);
        browseButton = createPushButton(projectGroup, Messages.LAUNCH_CONFIGURATION_MAIN_TAB_BROWSE_BUTTON, null);
        browseButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
				String project = browseProjects(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_PROJECT_DESCRIPTION);
				if (project != null && !project.equals(NodeConstants.EMPTY)) {
					projectText.setText(project);
					updateLaunchConfigurationDialog();
				}
            }
        });
	}

	private void createMainFileGroup(Composite parent) {
        Group mainFileGroup = new Group(parent, SWT.NONE);
        mainFileGroup.setText(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_MAIN_FILE_TEXT);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        mainFileGroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        mainFileGroup.setLayout(layout);
        mainFileGroup.setFont(parent.getFont());

        scriptText = new Text(mainFileGroup, SWT.SINGLE | SWT.BORDER);
        scriptText.setLayoutData(gd);
        scriptText.setFont(parent.getFont());
        scriptText.addModifyListener(modifyListener);
        workspaceButton = createPushButton(mainFileGroup, Messages.LAUNCH_CONFIGURATION_MAIN_TAB_WORKSPACE_BUTTON, null);
        workspaceButton.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
				String script = browseWorkspace(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_MAIN_FILE_DESCRIPTION);
				if (script != null && !script.equals(NodeConstants.EMPTY)) {
					scriptText.setText(script);
					// Set project to be the same as the main file
					String scriptLocation = null;
					try {
						scriptLocation = LaunchConfigurationUtil.resolveValue(script);
					} catch (CoreException ex) { // Do nothing
					}
					
					if(scriptLocation != null){
						IPath location= Path.fromOSString(scriptLocation);
						IFile iFile= ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
						projectText.setText(iFile.getProject().getName());
					}
				}
				updateLaunchConfigurationDialog();
            }
        });
	}
	
	private String browseProjects(String description) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getControl().getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject) {
					IProject project = (IProject) element;
					if (project != null && project.exists() && project.isOpen()){
						return true;
					}
			    }
			    return false;
			}
		});
		dialog.setTitle(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_SELECT_PROJECT_TITLE);
		dialog.setMessage(description);
		dialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if (selection != null && selection.length > 0 && selection[0] instanceof IProject){
					return new Status(IStatus.OK, NodePlugin.PLUGIN_ID, IStatus.OK, NodeConstants.EMPTY, null);
				}
				return new Status(IStatus.ERROR, NodePlugin.PLUGIN_ID, IStatus.ERROR, NodeConstants.EMPTY, null);
			}
		});

		if (dialog.open() == Window.OK) {
			IProject project = (IProject) dialog.getFirstResult();
			if (project != null) {
				return project.getName();
			}
		}
		return null;
	}

	private String browseWorkspace(String description) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getControl().getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.setAllowMultiple(false);
		dialog.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IProject || element instanceof IFolder || element instanceof IFile) {
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
			IFile file = (IFile) dialog.getFirstResult();
			if (file != null) {
				return VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", //$NON-NLS-1$
						file.getFullPath().toString());
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
        	projectText.setText(configuration.getAttribute(NodeConstants.ATTR_APP_PROJECT, NodeConstants.EMPTY));
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
    	configuration.setAttribute(NodeConstants.ATTR_APP_PROJECT, projectText.getText().trim());
    	configuration.setAttribute(NodeConstants.ATTR_APP_PATH, scriptText.getText().trim());
		if (getLaunchConfigurationDialog().getMode().equals(ILaunchManager.DEBUG_MODE)) {
			configuration.setAttribute(NodeConstants.ATTR_HOST_FIELD, debugHostText.getText().trim());
			configuration.setAttribute(NodeConstants.ATTR_PORT_FIELD, debugPortText.getText().trim());
			configuration.setAttribute(NodeConstants.ATTR_ADD_NETWORK_CONSOLE_FIELD,
					debugAddNetworkConsoleButton.getSelection());
			configuration.setAttribute(NodeConstants.ATTR_BREAK_FIELD, debugBreakButton.getSelection());
			LaunchConfigurationUtil.addSourceLookupAttr(configuration);
		}
		
		//Set mapped resources
        String projectName = projectText.getText().trim();
		configuration.setMappedResources(getResource(projectName));
    }
        
    private IResource[] getResource(String projectName){
        if (projectName.length() > 0) {
        	IStatus status = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
        	if(status.isOK()){
            	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            	return new IResource[] {project}; 
        	}
        }
		return null;
    }

    @Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
    	return validateEntries();
    }

	private boolean validateEntries(){
        setErrorMessage(null);

        // Validate project
        String projectName = projectText.getText().trim();
        if (projectName.length() > 0) {
        	IStatus status = ResourcesPlugin.getWorkspace().validateName(projectName, IResource.PROJECT);
        	if(status.isOK()){
            	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    			if (project == null || !project.exists() || !project.isOpen()){
    				setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_PROJECT_DOES_NOT_EXIST);
    				return false;
    			}
        	} else {
				setErrorMessage(status.getMessage());
				return false;
        	}
        } else {
            setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_PROJECT);
            return false;
        }
        
        // Validate main file
		String scriptFile = scriptText.getText().trim();
        if (scriptFile.length() > 0) {
			// Resolve possible eclipse variables
			try {
				scriptFile = LaunchConfigurationUtil.resolveValue(scriptFile);
			} catch (CoreException e) { // Do nothing
			}

			File file = null;
			if (scriptFile != null) {
				file = new File(scriptFile);
			}

			if (file == null || !file.exists() || file.isDirectory()) {
				setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_DOES_NOT_EXIST);
				return false;
			}
			
			// Validate main file is contained in selected project
			IWorkspace workspace= ResourcesPlugin.getWorkspace();    
			IPath location= Path.fromOSString(file.getAbsolutePath());
			IFile ifile= workspace.getRoot().getFileForLocation(location);
			if(!ifile.getProject().getName().equals(projectName)){
				setErrorMessage(NLS.bind(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_MAIN_FILE_NOT_IN_PROJECT, projectName));
				return false;
			}
        } else {
            setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_MAIN_FILE);
            return false;
        }

        // Debug mode validations
        if(getLaunchConfigurationDialog().getMode().equals(ILaunchManager.DEBUG_MODE)){
        	//Validate host
    		String host = debugHostText.getText();
    		if (host.length() <= 0) {
    			setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_SPECIFY_HOST);
    			return false;
    		}

			// Validate port range
    		String port = debugPortText.getText();
			int portNumber = 0;
			if (!port.isEmpty()) {
				portNumber = Integer.valueOf(port);
			}

			if (port.length() <= 0 || portNumber < 1024 || portNumber > 65535) {
				setErrorMessage(Messages.LAUNCH_CONFIGURATION_MAIN_TAB_ERROR_INVALID_PORT);
				return false;
			}
        }
		return true;
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
