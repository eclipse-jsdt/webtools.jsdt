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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.jsdt.js.node.internal.NodeConstants;
import org.eclipse.wst.jsdt.js.node.internal.ui.ImageResource;
import org.eclipse.wst.jsdt.js.node.internal.Messages;

/**
 * Arguments tab for node application launch configuration
 * 
 * @author "Adalberto Lopez Venegas (adalbert)"
 */
public class NodeLaunchArgumentsTab extends AbstractLaunchConfigurationTab {
    
	private Text nodeArgumentsText;
	private Text appArgumentsText;
	private Button nodeVariablesButton;
	private Button appVariablesButton;
	
    NodeWorkingDirectoryBlock workingDirectoryBlock = new NodeWorkingDirectoryBlock(NodeConstants.ATTR_WORKING_DIRECTORY);

	private ModifyListener modifyListener = new ModifyListener() {
        @Override
		public void modifyText(ModifyEvent e) {
            updateLaunchConfigurationDialog();
        }
    };
    
	private SelectionListener nodeArgsSelectionListener = new SelectionListener() {
        @Override
		public void widgetSelected(SelectionEvent e) {
            StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
            dialog.open();
            String variable = dialog.getVariableExpression();
            if (variable != null) {
            	nodeArgumentsText.insert(variable);
            }
        }

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		}
    };
    
    private SelectionListener appArgsSelectionListener = new SelectionListener() {
        @Override
		public void widgetSelected(SelectionEvent e) {
            StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
            dialog.open();
            String variable = dialog.getVariableExpression();
            if (variable != null) {
            	appArgumentsText.insert(variable);
            }
        }

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {				
		}            
    };

	@Override
	public void createControl(Composite parent) {        
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
		
		createNodeArgumentsGroup(composite);
        createAppArgumentsGroup(composite);
        createWorkingDirectoryGroup(composite);
		
		setControl(scrolledComposite);		
	}
	
	private void createNodeArgumentsGroup(Composite parent) {
        Group nodeJSOptionsGroup = new Group(parent, SWT.NONE);
        nodeJSOptionsGroup.setText(Messages.LAUNCH_CONFIGURATION_ARGUMENTS_TAB_NODE_ARGUMENTS_TEXT);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        nodeJSOptionsGroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        nodeJSOptionsGroup.setLayout(layout);
        nodeJSOptionsGroup.setFont(parent.getFont());
        
        nodeArgumentsText = new Text(nodeJSOptionsGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 40;
		gd.widthHint = 100;
		nodeArgumentsText.setLayoutData(gd);
		nodeArgumentsText.addModifyListener(modifyListener);
		
		nodeVariablesButton = createPushButton(nodeJSOptionsGroup, Messages.LAUNCH_CONFIGURATION_ARGUMENTS_TAB_VARIABLES_TEXT, null);
		nodeVariablesButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		nodeVariablesButton.addSelectionListener(nodeArgsSelectionListener);
	}
	
	private void createAppArgumentsGroup(Composite parent) {
        Group scriptOptionsGroup = new Group(parent, SWT.NONE);
        scriptOptionsGroup.setText(Messages.LAUNCH_CONFIGURATION_ARGUMENTS_TAB_APP_ARGUMENTS_TEXT);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        scriptOptionsGroup.setLayoutData(gd);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        scriptOptionsGroup.setLayout(layout);
        scriptOptionsGroup.setFont(parent.getFont());
        
        appArgumentsText = new Text(scriptOptionsGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 40;
		gd.widthHint = 100;
		appArgumentsText.setLayoutData(gd);
		appArgumentsText.addModifyListener(modifyListener);
		
		appVariablesButton = createPushButton(scriptOptionsGroup, Messages.LAUNCH_CONFIGURATION_ARGUMENTS_TAB_VARIABLES_TEXT, null);
		appVariablesButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		appVariablesButton.addSelectionListener(appArgsSelectionListener);
	}
	
	protected void createWorkingDirectoryGroup(Composite parent) {
		workingDirectoryBlock.createControl(parent);
	}

    @Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    	workingDirectoryBlock.setDefaults(configuration);
    }

    @Override
	public void initializeFrom(ILaunchConfiguration configuration) {
        try {
        	nodeArgumentsText.setText(configuration.getAttribute(NodeConstants.ATTR_NODE_ARGUMENTS, NodeConstants.EMPTY));
        	appArgumentsText.setText(configuration.getAttribute(NodeConstants.ATTR_APP_ARGUMENTS, NodeConstants.EMPTY));
        	workingDirectoryBlock.initializeFrom(configuration);
            
        } catch (CoreException e) {
            setErrorMessage(e.getMessage());
        }
    }

    @Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {    	
    	configuration.setAttribute(NodeConstants.ATTR_NODE_ARGUMENTS, nodeArgumentsText.getText().trim());
    	configuration.setAttribute(NodeConstants.ATTR_APP_ARGUMENTS, appArgumentsText.getText().trim());
    	workingDirectoryBlock.performApply(configuration);
    }

    
    @Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
        setErrorMessage(null);
        setMessage(null);
    	return workingDirectoryBlock.isValid(launchConfig);
    }
    
	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog){
		super.setLaunchConfigurationDialog(dialog);
		workingDirectoryBlock.setLaunchConfigurationDialog(dialog);
	}
	
    @Override
	public String getErrorMessage() {
        String m = super.getErrorMessage();
        if (m == null) {
                return workingDirectoryBlock.getErrorMessage();
        }
        return m;
    }
    
    @Override
	public String getMessage() {
        String m = super.getMessage();
        if (m == null) {
            return workingDirectoryBlock.getMessage();
        }
        return m;
    }

	@Override
	public Image getImage() {
		return ImageResource.getImage(ImageResource.IMG_ARGUMENTS);
	}

	@Override
	public String getName() {
        return Messages.LAUNCH_CONFIGURATION_ARGUMENTS_TAB;
    }

}
