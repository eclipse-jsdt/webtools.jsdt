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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeInstall;
import org.eclipse.wst.jsdt.core.runtime.IJSRuntimeType;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeManager;
import org.eclipse.wst.jsdt.core.runtime.JSRuntimeWorkingCopy;
import org.eclipse.wst.jsdt.internal.ui.util.SWTUtil;
import org.eclipse.wst.jsdt.ui.ISharedImages;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;

/**
 * A composite that displays installed runtime installs in a table. 
 * Runtime installs can be added, removed and edited.
 * <p>
 * This block implements ISelectionProvider - it sends selection change events
 * when the checked runtime in the table changes.
 * </p>
 */
@SuppressWarnings("restriction")
public class InstalledJSRuntimesBlock implements ISelectionProvider {
	/**
	 * This block's control
	 */
	private Composite fControl;
	
	/**
	 * Runtimes being displayed
	 */
	private List<IJSRuntimeInstall> fRuntimeInstalls = new ArrayList<IJSRuntimeInstall>(); 
	
	/**
	 * The main list control
	 */ 
	private CheckboxTableViewer fRuntimeList;
	
	// Action buttons
	private Button fAddButton;
	private Button fRemoveButton;
	private Button fEditButton;
	private Button fCopyButton;	
    
	// index of column used for sorting
	private int fSortColumn = 0;
	
	/**
	 * Selection listeners (checked runtime changes)
	 */
	private ListenerList<ISelectionChangedListener> fSelectionListeners = new ListenerList<>();
	
	/**
	 * Previous selection
	 */
	private ISelection fPrevSelection = new StructuredSelection();

    private Table fTable;
			
	// Make sure that working copy ids are unique if multiple calls to System.currentTimeMillis()
	// happen very quickly
	private static String fgLastUsedID;	
	
	// Used to identify the runtime type whose runtimes are being displayed on the block
	private IJSRuntimeType currentRuntimeType;
	
	/** 
	 * Content provider to show a list of Runtime installs
	 */ 
	class RuntimeInstallsContentProvider implements IStructuredContentProvider {		
		@Override
		public Object[] getElements(Object input) {
			return fRuntimeInstalls.toArray();
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		@Override
		public void dispose() {
		}
	}
	
	/**
	 * Label provider for installed runtimes table.
	 */
	class RuntimeInstallsLabelProvider extends LabelProvider implements ITableLabelProvider, IFontProvider, IColorProvider {

		Font bold = null;
		
		/**
		 * @see ITableLabelProvider#getColumnText(Object, int)
		 */
		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof IJSRuntimeInstall) {
				IJSRuntimeInstall runtime= (IJSRuntimeInstall)element;
				switch(columnIndex) {
					case 0:
						if (JSRuntimeManager.isContributedRuntimeInstall(runtime.getId())) {
							String contributedRuntimeLabel = 
										NLS.bind(PreferencesMessages.InstalledJSRuntimesBlock_ContributedRuntime_Label, runtime.getName());
							if (fRuntimeList.getChecked(element)) {
								contributedRuntimeLabel = 
											NLS.bind(PreferencesMessages.InstalledJSRuntimesBlock_DefaultRuntime_Label, contributedRuntimeLabel);
							}
							return contributedRuntimeLabel;
						}
						if(fRuntimeList.getChecked(element)) {
							return NLS.bind(PreferencesMessages.InstalledJSRuntimesBlock_DefaultRuntime_Label, runtime.getName());
						}
						return runtime.getName();
					case 1:
						if (runtime.getInstallLocation().exists()) {
							// Show full path
							return runtime.getInstallLocation().getAbsolutePath();
						} else {
							// Show the "system-path" label
							return PreferencesMessages.GlobalCommand_SystemPath_Label;
						}
				}
			}
			return element.toString();
		}

		/**
		 * @see ITableLabelProvider#getColumnImage(Object, int)
		 */
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return JavaScriptUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
			}
			return null;
		}

		@Override
		public Font getFont(Object element) {
			if(fRuntimeList.getChecked(element)) {
				if (bold == null) {
					Font dialogFont = JFaceResources.getDialogFont();
					FontData[] fontData = dialogFont.getFontData();
					for (int i = 0; i < fontData.length; i++) {
						FontData data = fontData[i];
						data.setStyle(SWT.BOLD);
					}
					Display display = SWTUtil.getStandardDisplay();
					bold = new Font(display, fontData);
				}
				return bold;
			}
			return null;
		}
		
		@Override
		public void dispose() {
			if(bold != null) {
				bold.dispose();
			}
			super.dispose();
		}

		@Override
		public Color getForeground(Object element) {
			if (isUnmodifiable(element)) {
				Display display = Display.getCurrent();
				return display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
			}
			return null;
		}

		@Override
		public Color getBackground(Object element) {
			if (isUnmodifiable(element)) {
				Display display = Display.getCurrent();
				return display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
			}
			return null;
		}
		
		boolean isUnmodifiable(Object element) {
			if(element instanceof IJSRuntimeInstall) {
				IJSRuntimeInstall runtime = (IJSRuntimeInstall) element;
				return JSRuntimeManager.isContributedRuntimeInstall(runtime.getId());
			}
			return false;
		}

	}	
	
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionListeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return new StructuredSelection(fRuntimeList.getCheckedElements());
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionListeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (!selection.equals(fPrevSelection)) {
				fPrevSelection = selection;
				Object runtime = ((IStructuredSelection)selection).getFirstElement();
				if (runtime == null) {
					fRuntimeList.setCheckedElements(new Object[0]);
				} else {
					fRuntimeList.setCheckedElements(new Object[]{runtime});
					fRuntimeList.reveal(runtime);
				}
				fRuntimeList.refresh(true);
				fireSelectionChanged();
			}
		}
	}

	/**
	 * Creates this block's control in the given control.
	 * 
	 * @param ancestor containing control
	 */
	public void createControl(Composite ancestor) {
		Font font = ancestor.getFont();
		Composite parent= SWTFactory.createComposite(ancestor, font, 2, 1, GridData.FILL_BOTH);
		fControl = parent;	
				
		SWTFactory.createLabel(parent, PreferencesMessages.InstalledJSRuntimesBlock_InstalledRuntimes_Label, 2);
				
		fTable= new Table(parent, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
		gd.widthHint = 350;
		fTable.setLayoutData(gd);
		fTable.setFont(font);
		fTable.setHeaderVisible(true);
		fTable.setLinesVisible(true);	

		TableColumn column = new TableColumn(fTable, SWT.NULL);
		column.setText(PreferencesMessages.InstalledJSRuntimesBlock_NameColumn_Label); 
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sortByName();
				fRuntimeList.refresh(true);
			}
		});
		int defaultwidth = 350/2 + 1;
		column.setWidth(defaultwidth);
	
		column = new TableColumn(fTable, SWT.NULL);
		column.setText(PreferencesMessages.InstalledJSRuntimesBlock_LocationColumn_Label); 
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sortByLocation();
				fRuntimeList.refresh(true);
			}
		});
		column.setWidth(defaultwidth);
			
		fRuntimeList = new CheckboxTableViewer(fTable);			
		fRuntimeList.setLabelProvider(new RuntimeInstallsLabelProvider());
		fRuntimeList.setContentProvider(new RuntimeInstallsContentProvider());
		fRuntimeList.setUseHashlookup(true);
		// by default, sort by name
		sortByName();
		
		fRuntimeList.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent evt) {
				enableButtons();
			}
		});
		
		fRuntimeList.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					setCheckedRuntimeInstall((IJSRuntimeInstall)event.getElement());
				} else {
					setCheckedRuntimeInstall(null);
				}
			}
		});
		
		fRuntimeList.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent e) {
				if (!fRuntimeList.getSelection().isEmpty()) {
					editRuntime();
				}
			}
		});
		fTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.character == SWT.DEL && event.stateMask == 0) {
					if (fRemoveButton.isEnabled()){
						removeRuntimes();
					}
				}
			}
		});	
		
		Composite buttons = SWTFactory.createComposite(parent, font, 1, 1, GridData.VERTICAL_ALIGN_BEGINNING, 0, 0);
		
		fAddButton = SWTFactory.createPushButton(buttons, PreferencesMessages.InstalledJSRuntimesBlock_AddButton_Label, null); 
		fAddButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				addRuntime();
			}
		});
		
		fEditButton= SWTFactory.createPushButton(buttons, PreferencesMessages.InstalledJSRuntimesBlock_EditButton_Label, null); 
		fEditButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				editRuntime();
			}
		});
		
		fCopyButton = SWTFactory.createPushButton(buttons, PreferencesMessages.InstalledJSRuntimesBlock_DuplicateButton_Label, null); 
		fCopyButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				copyRuntime();
			}
		});
		
		fRemoveButton= SWTFactory.createPushButton(buttons, PreferencesMessages.InstalledJSRuntimesBlock_RemoveButton_Label, null); 
		fRemoveButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				removeRuntimes();
			}
		});
		
		SWTFactory.createVerticalSpacer(parent, 1);
		
		enableButtons();
		fAddButton.setEnabled(JSRuntimeManager.getJSRuntimeTypes().size() > 0);
	}
	
	/**
	 * Adds a duplicate of the selected runtime to the block 
	 */
	protected void copyRuntime() {
        IStructuredSelection selection = (IStructuredSelection) fRuntimeList.getSelection();
        Iterator<IJSRuntimeInstall> it = selection.iterator();

        ArrayList<JSRuntimeWorkingCopy> newEntries = new ArrayList<JSRuntimeWorkingCopy>();
        while (it.hasNext()) {
            IJSRuntimeInstall selectedRuntime = it.next();
            // duplicate & add runtime
            JSRuntimeWorkingCopy standin = new JSRuntimeWorkingCopy(selectedRuntime, createUniqueId(selectedRuntime.getRuntimeType()));
            standin.setName(generateName(selectedRuntime.getName()));
			EditRuntimeInstallWizard wizard = new EditRuntimeInstallWizard(standin, currentRuntimeType, fRuntimeInstalls.toArray(new IJSRuntimeInstall[fRuntimeInstalls.size()]));
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			int dialogResult = dialog.open();
			if (dialogResult == Window.OK) {
				JSRuntimeWorkingCopy result = wizard.getResult();
				if (result != null) {
					newEntries.add(result);
				}
			} else if (dialogResult == Window.CANCEL){
				// Canceling one wizard should cancel all subsequent wizards
				break;
			}
        }
        if (newEntries.size() > 0){
        	fRuntimeInstalls.addAll(newEntries);
        	fRuntimeList.refresh();
        	fRuntimeList.setSelection(new StructuredSelection(newEntries.toArray()));
        } else {
        	fRuntimeList.setSelection(selection);
        }
        fRuntimeList.refresh(true);
    }

	/**
	 * Compares the given name against current names and adds the appropriate numerical 
	 * suffix to ensure that it is unique.
	 * @param name the name with which to ensure uniqueness 
	 * @return the unique version of the given name
	 */
	public String generateName(String name){
            if (!isDuplicateName(name)) {
                return name;
            }
            if (name.matches(".*\\(\\d*\\)")) { //$NON-NLS-1$
                int start = name.lastIndexOf('(');
                int end = name.lastIndexOf(')');
                String stringInt = name.substring(start+1, end);
                int numericValue = Integer.parseInt(stringInt);
                String newName = name.substring(0, start+1) + (numericValue+1) + ")"; //$NON-NLS-1$
                return generateName(newName);
            }
            return generateName(name + " (1)"); //$NON-NLS-1$
        }
	
	/**
	 * Fire current selection
	 */
	private void fireSelectionChanged() {
		SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		for (ISelectionChangedListener listener : fSelectionListeners) {
			listener.selectionChanged(event);
		}	
	}
	
	/**
	 * Sorts by runtime name.
	 */
	private void sortByName() {
		fRuntimeList.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if ((e1 instanceof IJSRuntimeInstall) && (e2 instanceof IJSRuntimeInstall)) {
					IJSRuntimeInstall left= (IJSRuntimeInstall)e1;
					IJSRuntimeInstall right= (IJSRuntimeInstall)e2;
					return left.getName().compareToIgnoreCase(right.getName());
				}
				return super.compare(viewer, e1, e2);
			}
			
			@Override
			public boolean isSorterProperty(Object element, String property) {
				return true;
			}
		});		
		fSortColumn = 1;		
	}
	
	/**
	 * Sorts by runtime location.
	 */
	private void sortByLocation() {
		fRuntimeList.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if ((e1 instanceof IJSRuntimeInstall) && (e2 instanceof IJSRuntimeInstall)) {
					IJSRuntimeInstall left= (IJSRuntimeInstall)e1;
					IJSRuntimeInstall right= (IJSRuntimeInstall)e2;
					return left.getInstallLocation().getAbsolutePath().compareToIgnoreCase(right.getInstallLocation().getAbsolutePath());
				}
				return super.compare(viewer, e1, e2);
			}
			
			@Override
			public boolean isSorterProperty(Object element, String property) {
				return true;
			}
		});		
		fSortColumn = 2;		
	}
		
	/**
	 * Enables the buttons based on selected items counts in the viewer
	 */
	private void enableButtons() {
		IStructuredSelection selection = (IStructuredSelection) fRuntimeList.getSelection();
		int selectionCount= selection.size();
		fEditButton.setEnabled(selectionCount == 1);
		fCopyButton.setEnabled(selectionCount > 0);
		if (selectionCount > 0 && selectionCount <= fRuntimeList.getTable().getItemCount()) {
			Iterator<IJSRuntimeInstall> iterator = selection.iterator();
			while (iterator.hasNext()) {
				IJSRuntimeInstall install = iterator.next();
				if (JSRuntimeManager.isContributedRuntimeInstall(install.getId())) {
					fRemoveButton.setEnabled(false);
					// FIXME: For now, disallow duplication of a location with
					// unexisting paths (which is the case for system global
					// commands)
					if (!install.getInstallLocation().exists()) {
						fCopyButton.setEnabled(false);
					}
					return;
				}
			}
			fRemoveButton.setEnabled(true);
		} else {
			fRemoveButton.setEnabled(false);
		}
	}	
	
	/**
	 * Returns this block's control
	 * 
	 * @return control
	 */
	public Control getControl() {
		return fControl;
	}
	
	/**
	 * Sets the runtimes to be displayed in this block
	 * 
	 * @param runtimes Runtime installs to be displayed
	 */
	protected void setRuntimeInstalls(IJSRuntimeInstall[] runtimes) {
		fRuntimeInstalls.clear();
		for (int i = 0; i < runtimes.length; i++) {
			fRuntimeInstalls.add(runtimes[i]);
		}
		fRuntimeList.setInput(fRuntimeInstalls);
		fRuntimeList.refresh();
	}
	
	/**
	 * Returns the runtimes currently being displayed in this block
	 * 
	 * @return Runtimes currently being displayed in this block
	 */
	public IJSRuntimeInstall[] getRuntimeInstalls() {
		return fRuntimeInstalls.toArray(new IJSRuntimeInstall[fRuntimeInstalls.size()]);
	}
	
	/**
	 * Bring up a wizard that lets the user create a new runtime definition.
	 */
	private void addRuntime() {
		AddRuntimeInstallWizard wizard = new AddRuntimeInstallWizard(currentRuntimeType, fRuntimeInstalls.toArray(new IJSRuntimeInstall[fRuntimeInstalls.size()]));
		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		if (dialog.open() == Window.OK) {
			JSRuntimeWorkingCopy result = wizard.getResult();
			if (result != null) {
				fRuntimeInstalls.add(result);
				//refresh from model
				fRuntimeList.refresh();
				fRuntimeList.setSelection(new StructuredSelection(result));
				//ensure labels are updated
				fRuntimeList.refresh(true);
			}
		}
	}
	
	public boolean isDuplicateName(String name) {
		for (int i= 0; i < fRuntimeInstalls.size(); i++) {
			IJSRuntimeInstall runtime = fRuntimeInstalls.get(i);
			if (runtime.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Performs the edit runtime action when the Edit... button is pressed
	 */
	private void editRuntime() {
		IStructuredSelection selection= (IStructuredSelection)fRuntimeList.getSelection();
		JSRuntimeWorkingCopy runtime = (JSRuntimeWorkingCopy)selection.getFirstElement();
		if (runtime == null) {
			return;
		}
		if (JSRuntimeManager.isContributedRuntimeInstall(runtime.getId())) {
			RuntimeDetailsDialog dialog = new RuntimeDetailsDialog(getShell(), runtime);
			dialog.open();
		} else {
			EditRuntimeInstallWizard wizard = new EditRuntimeInstallWizard(runtime, currentRuntimeType, fRuntimeInstalls.toArray(new IJSRuntimeInstall[fRuntimeInstalls.size()]));
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			if (dialog.open() == Window.OK) {
				JSRuntimeWorkingCopy result = wizard.getResult();
				if (result != null) {
					// replace with the edited runtime
					int index = fRuntimeInstalls.indexOf(runtime);
					fRuntimeInstalls.remove(index);
					fRuntimeInstalls.add(index, result);
					fRuntimeList.setSelection(new StructuredSelection(result));
					fRuntimeList.refresh(true);
				}
			}
		}
	}
	
	/**
	 * Performs the remove runtime(s) action when the Remove... button is pressed
	 */
	private void removeRuntimes() {
		IStructuredSelection selection = (IStructuredSelection)fRuntimeList.getSelection();
		IJSRuntimeInstall[] runtimes = new IJSRuntimeInstall[selection.size()];
		Iterator<IJSRuntimeInstall> iter = selection.iterator();
		int i = 0;
		while (iter.hasNext()) {
			runtimes[i] = iter.next();
			i++;
		}
		removeRuntimes(runtimes);
	}	
	
	/**
	 * Removes the given runtimes from the table.
	 * 
	 * @param runtimes
	 */
	public void removeRuntimes(IJSRuntimeInstall[] runtimes) {
		for (int i = 0; i < runtimes.length; i++) {
			fRuntimeInstalls.remove(runtimes[i]);
		}
		fRuntimeList.refresh();
		IStructuredSelection curr = (IStructuredSelection) getSelection();
		IJSRuntimeInstall[] installs = getRuntimeInstalls();
		if(installs.length < 1) {
			fPrevSelection = null;
		}
		if (curr.size() == 0 && installs.length == 1) {
			// pick a default VM automatically
			setSelection(new StructuredSelection(installs[0]));
		} else {
			fireSelectionChanged();
		}
		fRuntimeList.refresh(true);
	}
	
	protected Shell getShell() {
		return getControl().getShell();
	}

	/**
	 * Find a unique runtime id.  Check existing 'real' runtimes, as well as the last id used for
	 * a working copy instance.
	 */
	private String createUniqueId(IJSRuntimeType runtimeType) {
		String id = null;
		do {
			id = String.valueOf(System.currentTimeMillis());
		} while (JSRuntimeManager.getJSRuntimeInstall(id) != null || id.equals(fgLastUsedID));
		fgLastUsedID = id;
		return id;
	}
	
	/**
	 * Sets the checked runtimes, possible <code>null</code>
	 * 
	 * @param runtime runtime or <code>null</code>
	 */
	public void setCheckedRuntimeInstall(IJSRuntimeInstall runtime) {
		if (runtime == null) {
			setSelection(new StructuredSelection());
		} else {
			setSelection(new StructuredSelection(runtime));
		}
	}
	
	/**
	 * Returns the checked runtime or <code>null</code> if none.
	 * 
	 * @return the checked runtime or <code>null</code> if none
	 */
	public IJSRuntimeInstall getCheckedRuntimeInstall() {
		Object[] objects = fRuntimeList.getCheckedElements();
		if (objects.length == 0) {
			return null;
		}
		return (IJSRuntimeInstall)objects[0];
	}
	
	/**
	 * Persist table settings into the give dialog store, prefixed
	 * with the given key.
	 * 
	 * @param settings dialog store
	 * @param qualifier key qualifier
	 */
	public void saveColumnSettings(IDialogSettings settings, String qualifier) {
        int columnCount = fTable.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			settings.put(qualifier + ".columnWidth" + i, fTable.getColumn(i).getWidth());	 //$NON-NLS-1$
		}
		settings.put(qualifier + ".sortColumn", fSortColumn); //$NON-NLS-1$
	}
	
	/**
	 * Restore table settings from the given dialog store using the
	 * given key.
	 * 
	 * @param settings dialog settings store
	 * @param qualifier key to restore settings from
	 */
	public void restoreColumnSettings(IDialogSettings settings, String qualifier) {
		fRuntimeList.getTable().layout(true);
        restoreColumnWidths(settings, qualifier);
		try {
			fSortColumn = settings.getInt(qualifier + ".sortColumn"); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			fSortColumn = 1;
		}
		switch (fSortColumn) {
			case 1:
				sortByName();
				break;
			case 2:
				sortByLocation();
				break;
		}
	}
	
	/**
	 * Restores the column widths from dialog settings
	 * @param settings
	 * @param qualifier
	 */
	private void restoreColumnWidths(IDialogSettings settings, String qualifier) {
        int columnCount = fTable.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            int width = -1;
            try {
                width = settings.getInt(qualifier + ".columnWidth" + i); //$NON-NLS-1$
            } catch (NumberFormatException e) {}
            
            if ((width <= 0)) {
            	// Do nothing. In general this does not look really good
                // fTable.getColumn(i).pack();
            } else {
                fTable.getColumn(i).setWidth(width);
            }
        }
	}
	
	/**
	 * Populates the table with existing runtimes known on the platform
	 * for the current specified runtime type.
	 */
	protected void fillWithManagedRuntimeInstalls () {
		List<JSRuntimeWorkingCopy> standins = new ArrayList<JSRuntimeWorkingCopy>();
		IJSRuntimeInstall[] installs = JSRuntimeManager.getJSRuntimeInstallsByType(currentRuntimeType.getId());
		for (int j = 0; j < installs.length; j++) {
			IJSRuntimeInstall install = installs[j];
			standins.add(new JSRuntimeWorkingCopy(install));
		}
		setRuntimeInstalls(standins.toArray(new IJSRuntimeInstall[standins.size()]));		
	}
	
	public String getTimeStamp () {
		return getEncodedRuntimeInstalls();
	}

	private StringBuffer appendRuntimeAttributes(IJSRuntimeInstall runtimeInstall, StringBuffer buf) {
		if (runtimeInstall != null) {
			String str = runtimeInstall.getName();
			buf.append('[').append(str.length()).append(']').append(str);
			str = runtimeInstall.getRuntimeType().getName();
			buf.append('[').append(str.length()).append(']').append(str);
			if (runtimeInstall.getJSRuntimeArguments() != null && runtimeInstall.getJSRuntimeArguments().length > 0) {
				buf.append('[').append(runtimeInstall.getJSRuntimeArguments().length).append(']');
				for (int i = 0; i < runtimeInstall.getJSRuntimeArguments().length; i++) {
					str = runtimeInstall.getJSRuntimeArguments()[i];
					buf.append('[').append(str.length()).append(']').append(str);
				}
			}
			str = runtimeInstall.getInstallLocation().getAbsolutePath();
			buf.append('[').append(str.length()).append(']').append(str).append(';');
		} else {
			buf.append('[').append(']').append(';');
		}
		return buf;
	}

	private String getEncodedRuntimeInstalls() {
		StringBuffer buf = new StringBuffer();
		IJSRuntimeInstall runtimeInstall = getCheckedRuntimeInstall();

		int nElements = fRuntimeInstalls.size();
		buf.append('[').append(nElements).append(']');
		for (int i = 0; i < nElements; i++) {
			IJSRuntimeInstall elem = fRuntimeInstalls.get(i);
			if (elem == runtimeInstall)
			 {
				buf.append('[').append("defaultRuntime").append(']'); //$NON-NLS-1$
			}
			appendRuntimeAttributes(elem, buf);
		}
		return buf.toString();
	}
	
	/**
	 * @param jsRuntimeType
	 */
	public void setRuntimeType(IJSRuntimeType jsRuntimeType) {
		currentRuntimeType = jsRuntimeType;
		fillWithManagedRuntimeInstalls();
	}
	
	/**
	 * @param jsRuntimeType
	 * @param ijsRuntimeInstalls
	 */
	public void setRuntimeType(IJSRuntimeType jsRuntimeType, IJSRuntimeInstall[] installs) {
		currentRuntimeType = jsRuntimeType;
		List<JSRuntimeWorkingCopy> standins = new ArrayList<JSRuntimeWorkingCopy>();
		for (int j = 0; j < installs.length; j++) {
			IJSRuntimeInstall install = installs[j];
			standins.add(new JSRuntimeWorkingCopy(install));
		}
		setRuntimeInstalls(standins.toArray(new IJSRuntimeInstall[standins.size()]));
	}
}