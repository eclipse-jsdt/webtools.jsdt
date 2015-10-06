/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Mickael Istria (Red Hat Inc.) - Outline relies on CNF
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.javaeditor;


import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.WorkbenchAdapter;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wst.jsdt.core.ElementChangedEvent;
import org.eclipse.wst.jsdt.core.IElementChangedListener;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.ISourceReference;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.IProductConstants;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.ProductProperties;
import org.eclipse.wst.jsdt.internal.ui.actions.AbstractToggleLinkingAction;
import org.eclipse.wst.jsdt.internal.ui.actions.CategoryFilterActionGroup;
import org.eclipse.wst.jsdt.internal.ui.actions.CompositeActionGroup;
import org.eclipse.wst.jsdt.internal.ui.packageview.PackagesMessages;
import org.eclipse.wst.jsdt.internal.ui.preferences.MembersOrderPreferenceCache;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.AppearanceAwareLabelProvider;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.ColoredViewersManager;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.SourcePositionComparator;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.StatusBarUpdater;
import org.eclipse.wst.jsdt.ui.JavaScriptElementComparator;
import org.eclipse.wst.jsdt.ui.JavaScriptElementLabels;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;
import org.eclipse.wst.jsdt.ui.PreferenceConstants;
import org.eclipse.wst.jsdt.ui.actions.CCPActionGroup;
import org.eclipse.wst.jsdt.ui.actions.CustomFiltersActionGroup;
import org.eclipse.wst.jsdt.ui.actions.GenerateActionGroup;
import org.eclipse.wst.jsdt.ui.actions.JavaSearchActionGroup;
import org.eclipse.wst.jsdt.ui.actions.MemberFilterActionGroup;
import org.eclipse.wst.jsdt.ui.actions.OpenViewActionGroup;
import org.eclipse.wst.jsdt.ui.actions.RefactorActionGroup;


/**
 * The content outline page of the JavaScript editor.
 * The viewer implements the Common Navigator Framework via {@link CommonViewer}. Common navigator
 * contributions that are bound to navigator id <code>JavaScriptPlugin.getDefault().getPluginId() + ".outline"</code>
 * are visible in the outline.
 * Publishes its context menu under <code>JavaScriptPlugin.getDefault().getPluginId() + ".outline"</code>.
 */
public class JavaOutlinePage extends Page implements IContentOutlinePage, IAdaptable , IPostSelectionProvider {
	
	private static final String OUTLINE_COMMON_NAVIGATOR_ID = JavaScriptPlugin.getPluginId() + ".outline"; //$NON-NLS-1$

			static Object[] NO_CHILDREN= new Object[0];

			/**
			 * The element change listener of the java outline viewer.
			 * @see IElementChangedListener
			 */
			protected class ElementChangedListener implements IElementChangedListener {

				public void elementChanged(final ElementChangedEvent e) {

					if (getControl() == null)
						return;

					Display d= getControl().getDisplay();
					if (d != null) {
						d.asyncExec(new Runnable() {
							public void run() {
								IJavaScriptUnit cu= (IJavaScriptUnit) fInput;
								IJavaScriptElement base= cu;
								if (fTopLevelTypeOnly) {
									base= cu.findPrimaryType();
									if (base == null) {
										if (fOutlineViewer != null)
											fOutlineViewer.refresh(true);
										return;
									}
								}
								IJavaScriptElementDelta delta= findElement(base, e.getDelta());
								if (delta != null && fOutlineViewer != null) {
									fOutlineViewer.refresh(delta.getElement());
								}
							}
						});
					}
				}

				private boolean isPossibleStructuralChange(IJavaScriptElementDelta cuDelta) {
					if (cuDelta.getKind() != IJavaScriptElementDelta.CHANGED) {
						return true; // add or remove
					}
					int flags= cuDelta.getFlags();
					if ((flags & IJavaScriptElementDelta.F_CHILDREN) != 0) {
						return true;
					}
					return (flags & (IJavaScriptElementDelta.F_CONTENT | IJavaScriptElementDelta.F_FINE_GRAINED)) == IJavaScriptElementDelta.F_CONTENT;
				}

				protected IJavaScriptElementDelta findElement(IJavaScriptElement unit, IJavaScriptElementDelta delta) {

					if (delta == null || unit == null)
						return null;

					IJavaScriptElement element= delta.getElement();

					if (unit.equals(element)) {
						if (isPossibleStructuralChange(delta)) {
							return delta;
						}
						return null;
					}


					if (element.getElementType() > IJavaScriptElement.CLASS_FILE)
						return null;

					IJavaScriptElementDelta[] children= delta.getAffectedChildren();
					if (children == null || children.length == 0)
						return null;

					for (int i= 0; i < children.length; i++) {
						IJavaScriptElementDelta d= findElement(unit, children[i]);
						if (d != null)
							return d;
					}

					return null;
				}
			}

			static class NoClassElement extends WorkbenchAdapter implements IAdaptable {
				/*
				 * @see java.lang.Object#toString()
				 */
				public String toString() {
					return JavaEditorMessages.JavaOutlinePage_error_NoTopLevelType;
				}

				/*
				 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
				 */
				public Object getAdapter(Class clas) {
					if (clas == IWorkbenchAdapter.class)
						return this;
					return null;
				}
			}

			class LexicalSortingAction extends Action {

				private JavaScriptElementComparator fComparator= new JavaScriptElementComparator();
				private SourcePositionComparator fSourcePositonComparator= new SourcePositionComparator();

				public LexicalSortingAction() {
					super();
					PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.LEXICAL_SORTING_OUTLINE_ACTION);
					setText(JavaEditorMessages.JavaOutlinePage_Sort_label);
					JavaPluginImages.setLocalImageDescriptors(this, "alphab_sort_co.gif"); //$NON-NLS-1$
					setToolTipText(JavaEditorMessages.JavaOutlinePage_Sort_tooltip);
					setDescription(JavaEditorMessages.JavaOutlinePage_Sort_description);

					boolean checked= JavaScriptPlugin.getDefault().getPreferenceStore().getBoolean("LexicalSortingAction.isChecked"); //$NON-NLS-1$
					valueChanged(checked, false);
				}

				public void run() {
					valueChanged(isChecked(), true);
				}

				private void valueChanged(final boolean on, boolean store) {
					setChecked(on);
					BusyIndicator.showWhile(fOutlineViewer.getControl().getDisplay(), new Runnable() {
						public void run() {
							if (on)
								fOutlineViewer.setComparator(fComparator);
							else
								fOutlineViewer.setComparator(fSourcePositonComparator);
						}
					});

					if (store)
						JavaScriptPlugin.getDefault().getPreferenceStore().setValue("LexicalSortingAction.isChecked", on); //$NON-NLS-1$
				}
			}

		class ClassOnlyAction extends Action {

			public ClassOnlyAction() {
				super();
				PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.GO_INTO_TOP_LEVEL_TYPE_ACTION);
				setText(JavaEditorMessages.JavaOutlinePage_GoIntoTopLevelType_label);
				setToolTipText(JavaEditorMessages.JavaOutlinePage_GoIntoTopLevelType_tooltip);
				setDescription(JavaEditorMessages.JavaOutlinePage_GoIntoTopLevelType_description);
				JavaPluginImages.setLocalImageDescriptors(this, "gointo_toplevel_type.gif"); //$NON-NLS-1$

				IPreferenceStore preferenceStore= JavaScriptPlugin.getDefault().getPreferenceStore();
				boolean showclass= preferenceStore.getBoolean("GoIntoTopLevelTypeAction.isChecked"); //$NON-NLS-1$
				setTopLevelTypeOnly(showclass);
			}

			/*
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				setTopLevelTypeOnly(!fTopLevelTypeOnly);
			}

			private void setTopLevelTypeOnly(boolean show) {
				fTopLevelTypeOnly= show;
				setChecked(show);
				fOutlineViewer.refresh(false);

				IPreferenceStore preferenceStore= JavaScriptPlugin.getDefault().getPreferenceStore();
				preferenceStore.setValue("GoIntoTopLevelTypeAction.isChecked", show); //$NON-NLS-1$
			}
		}

		private class CollapseAllAction extends Action {
			TreeViewer viewer;
			CollapseAllAction(TreeViewer viewer) {
				super(PackagesMessages.CollapseAllAction_label); 
				setDescription(PackagesMessages.CollapseAllAction_description); 
				setToolTipText(PackagesMessages.CollapseAllAction_tooltip); 
				JavaPluginImages.setLocalImageDescriptors(this, "collapseall.gif"); //$NON-NLS-1$
				
				this.viewer = viewer;
				PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IJavaHelpContextIds.COLLAPSE_ALL_ACTION);
			}
		 
			public void run() { 
				this.viewer.collapseAll();
			}
		}


		/**
		 * This action toggles whether this Java Outline page links
		 * its selection to the active editor.
		 *
		 * 
		 */
		public class ToggleLinkingAction extends AbstractToggleLinkingAction {

			JavaOutlinePage fJavaOutlinePage;

			/**
			 * Constructs a new action.
			 *
			 * @param outlinePage the Java outline page
			 */
			public ToggleLinkingAction(JavaOutlinePage outlinePage) {
				boolean isLinkingEnabled= PreferenceConstants.getPreferenceStore().getBoolean(PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE);
				setChecked(isLinkingEnabled);
				fJavaOutlinePage= outlinePage;
			}

			/**
			 * Runs the action.
			 */
			public void run() {
				PreferenceConstants.getPreferenceStore().setValue(PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, isChecked());
				if (isChecked() && fEditor != null)
					fEditor.synchronizeOutlinePage(fEditor.computeHighlightRangeSourceReference(), false);
			}

		}

		/**
		 * Empty selection provider.
		 * 
		 * 
		 */
		private static final class EmptySelectionProvider implements ISelectionProvider {
			public void addSelectionChangedListener(ISelectionChangedListener listener) {
			}
			public ISelection getSelection() {
				return StructuredSelection.EMPTY;
			}
			public void removeSelectionChangedListener(ISelectionChangedListener listener) {
			}
			public void setSelection(ISelection selection) {
			}
		}


	/** A flag to show contents of top level type only */
	private boolean fTopLevelTypeOnly;

	private IJavaScriptElement fInput;
	private String fContextMenuID;
	private Menu fMenu;
	private CommonViewer fOutlineViewer;
	private JavaEditor fEditor;

	private MemberFilterActionGroup fMemberFilterActionGroup;

	private ListenerList fSelectionChangedListeners= new ListenerList(ListenerList.IDENTITY);
	private ListenerList fPostSelectionChangedListeners= new ListenerList(ListenerList.IDENTITY);
	private Hashtable fActions= new Hashtable();

	private TogglePresentationAction fTogglePresentation;

	private ToggleLinkingAction fToggleLinkingAction;

	private CompositeActionGroup fActionGroups;

	private IPropertyChangeListener fPropertyChangeListener;
	/**
	 * Custom filter action group.
	 * 
	 */
	private CustomFiltersActionGroup fCustomFiltersActionGroup;
	/**
	 * Category filter action group.
	 * 
	 */
	private CategoryFilterActionGroup fCategoryFilterActionGroup;

	public JavaOutlinePage(String contextMenuID, JavaEditor editor) {
		super();

		Assert.isNotNull(editor);

		fContextMenuID= contextMenuID;
		fEditor= editor;

		fTogglePresentation= new TogglePresentationAction();
		fTogglePresentation.setEditor(editor);

		fPropertyChangeListener= new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				doPropertyChange(event);
			}
		};
		JavaScriptPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(fPropertyChangeListener);
	}

	/* (non-Javadoc)
	 * Method declared on Page
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
	}

	private void doPropertyChange(PropertyChangeEvent event) {
		if (fOutlineViewer != null) {
			if (MembersOrderPreferenceCache.isMemberOrderProperty(event.getProperty())) {
				fOutlineViewer.refresh(false);
			}
		}
	}

	/*
	 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (fOutlineViewer != null)
			fOutlineViewer.addSelectionChangedListener(listener);
		else
			fSelectionChangedListeners.add(listener);
	}

	/*
	 * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (fOutlineViewer != null)
			fOutlineViewer.removeSelectionChangedListener(listener);
		else
			fSelectionChangedListeners.remove(listener);
	}

	/*
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	public void setSelection(ISelection selection) {
		if (fOutlineViewer != null)
			fOutlineViewer.setSelection(selection);
	}

	/*
	 * @see ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		if (fOutlineViewer == null)
			return StructuredSelection.EMPTY;
		return fOutlineViewer.getSelection();
	}

	/*
	 * @see org.eclipse.jface.text.IPostSelectionProvider#addPostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		if (fOutlineViewer != null)
			fOutlineViewer.addPostSelectionChangedListener(listener);
		else
			fPostSelectionChangedListeners.add(listener);
	}

	/*
	 * @see org.eclipse.jface.text.IPostSelectionProvider#removePostSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		if (fOutlineViewer != null)
			fOutlineViewer.removePostSelectionChangedListener(listener);
		else
			fPostSelectionChangedListeners.remove(listener);
	}

	private void registerToolbarActions(IActionBars actionBars) {
		IToolBarManager toolBarManager= actionBars.getToolBarManager();
		toolBarManager.add(new LexicalSortingAction());

		fMemberFilterActionGroup= new MemberFilterActionGroup(fOutlineViewer, "org.eclipse.wst.jsdt.ui.JavaOutlinePage"); //$NON-NLS-1$
		fMemberFilterActionGroup.contributeToToolBar(toolBarManager);

		fCustomFiltersActionGroup.fillActionBars(actionBars);

		IMenuManager viewMenuManager= actionBars.getMenuManager();
		viewMenuManager.add(new Separator("EndFilterGroup")); //$NON-NLS-1$

		fToggleLinkingAction= new ToggleLinkingAction(this);
	//	viewMenuManager.add(new ClassOnlyAction());
		viewMenuManager.add(fToggleLinkingAction);
		toolBarManager.add(new CollapseAllAction(this.fOutlineViewer));

		fCategoryFilterActionGroup= new CategoryFilterActionGroup(fOutlineViewer, "org.eclipse.wst.jsdt.ui.JavaOutlinePage", new IJavaScriptElement[] {fInput}); //$NON-NLS-1$
		fCategoryFilterActionGroup.contributeToViewMenu(viewMenuManager);
	}

	/*
	 * @see IPage#createControl
	 */
	public void createControl(Composite parent) {

		AppearanceAwareLabelProvider lprovider= new AppearanceAwareLabelProvider(
			AppearanceAwareLabelProvider.DEFAULT_TEXTFLAGS |  JavaScriptElementLabels.F_APP_TYPE_SIGNATURE | JavaScriptElementLabels.ALL_CATEGORY,
			AppearanceAwareLabelProvider.DEFAULT_IMAGEFLAGS
		);

		this.fOutlineViewer= new CommonViewer(OUTLINE_COMMON_NAVIGATOR_ID, parent, SWT.MULTI);
		// seems like common filters need to be explicitly added
		for (ViewerFilter filter : fOutlineViewer.getNavigatorContentService().getFilterService().getVisibleFilters(true)) {
			this.fOutlineViewer.addFilter(filter);
		}
		ColoredViewersManager.install(fOutlineViewer);

		Object[] listeners= fSelectionChangedListeners.getListeners();
		for (int i= 0; i < listeners.length; i++) {
			fSelectionChangedListeners.remove(listeners[i]);
			fOutlineViewer.addSelectionChangedListener((ISelectionChangedListener) listeners[i]);
		}

		listeners= fPostSelectionChangedListeners.getListeners();
		for (int i= 0; i < listeners.length; i++) {
			fPostSelectionChangedListeners.remove(listeners[i]);
			fOutlineViewer.addPostSelectionChangedListener((ISelectionChangedListener) listeners[i]);
		}

		MenuManager manager= new MenuManager(fContextMenuID, fContextMenuID);
		manager.setRemoveAllWhenShown(true);
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				contextMenuAboutToShow(m);
			}
		});
		fMenu= manager.createContextMenu(this.fOutlineViewer.getTree());
		this.fOutlineViewer.getTree().setMenu(fMenu);

		IPageSite site= getSite();
		site.registerContextMenu(JavaScriptPlugin.getPluginId() + ".outline", manager, fOutlineViewer); //$NON-NLS-1$
		
		updateSelectionProvider(site);
		
		// we must create the groups after we have set the selection provider to the site
		fActionGroups= new CompositeActionGroup(new ActionGroup[] {
				new OpenViewActionGroup(this),
				new CCPActionGroup(this),
				new GenerateActionGroup(this),
				new RefactorActionGroup(this),
				new JavaSearchActionGroup(this)});

		// register global actions
		IActionBars actionBars= site.getActionBars();
		actionBars.setGlobalActionHandler(ITextEditorActionConstants.UNDO, fEditor.getAction(ITextEditorActionConstants.UNDO));
		actionBars.setGlobalActionHandler(ITextEditorActionConstants.REDO, fEditor.getAction(ITextEditorActionConstants.REDO));

		IAction action= fEditor.getAction(ITextEditorActionConstants.NEXT);
		actionBars.setGlobalActionHandler(ITextEditorActionDefinitionIds.GOTO_NEXT_ANNOTATION, action);
		actionBars.setGlobalActionHandler(ITextEditorActionConstants.NEXT, action);
		action= fEditor.getAction(ITextEditorActionConstants.PREVIOUS);
		actionBars.setGlobalActionHandler(ITextEditorActionDefinitionIds.GOTO_PREVIOUS_ANNOTATION, action);
		actionBars.setGlobalActionHandler(ITextEditorActionConstants.PREVIOUS, action);
		
		actionBars.setGlobalActionHandler(ITextEditorActionDefinitionIds.TOGGLE_SHOW_SELECTED_ELEMENT_ONLY, fTogglePresentation);

		fActionGroups.fillActionBars(actionBars);

		IStatusLineManager statusLineManager= actionBars.getStatusLineManager();
		if (statusLineManager != null) {
			StatusBarUpdater updater= new StatusBarUpdater(statusLineManager);
			fOutlineViewer.addPostSelectionChangedListener(updater);
		}
		// Custom filter group
		fCustomFiltersActionGroup= new CustomFiltersActionGroup("org.eclipse.wst.jsdt.ui.JavaOutlinePage", fOutlineViewer); //$NON-NLS-1$

		registerToolbarActions(actionBars);

		fOutlineViewer.setInput(fInput);
	}

	/*
	 * 
	 */
	private void updateSelectionProvider(IPageSite site) {
		ISelectionProvider provider= fOutlineViewer;
		if (fInput != null) {
			IJavaScriptUnit cu= (IJavaScriptUnit)fInput.getAncestor(IJavaScriptElement.JAVASCRIPT_UNIT);
			if (cu != null && !JavaModelUtil.isPrimary(cu))
				provider= new EmptySelectionProvider();
		}
		site.setSelectionProvider(provider);
	}

	public void dispose() {

		if (fEditor == null)
			return;

		if (fMemberFilterActionGroup != null) {
			fMemberFilterActionGroup.dispose();
			fMemberFilterActionGroup= null;
		}
		
		if (fCategoryFilterActionGroup != null) {
			fCategoryFilterActionGroup.dispose();
			fCategoryFilterActionGroup= null;
		}

		if (fCustomFiltersActionGroup != null) {
			fCustomFiltersActionGroup.dispose();
			fCustomFiltersActionGroup= null;
		}


		fEditor.outlinePageClosed();
		fEditor= null;

		fSelectionChangedListeners.clear();
		fSelectionChangedListeners= null;

		fPostSelectionChangedListeners.clear();
		fPostSelectionChangedListeners= null;

		if (fPropertyChangeListener != null) {
			JavaScriptPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(fPropertyChangeListener);
			fPropertyChangeListener= null;
		}

		if (fMenu != null && !fMenu.isDisposed()) {
			fMenu.dispose();
			fMenu= null;
		}

		if (fActionGroups != null)
			fActionGroups.dispose();

		fTogglePresentation.setEditor(null);

		fOutlineViewer= null;

		super.dispose();
	}

	public Control getControl() {
		if (fOutlineViewer != null)
			return fOutlineViewer.getControl();
		return null;
	}

	public void setInput(IJavaScriptElement inputElement) {
		fInput= inputElement;
		if (fOutlineViewer != null) {
			fOutlineViewer.setInput(fInput);
			updateSelectionProvider(getSite());
		}
		if (fCategoryFilterActionGroup != null) 
			fCategoryFilterActionGroup.setInput(new IJavaScriptElement[] {fInput});
	}

	public void select(ISourceReference reference) {
		if (fOutlineViewer != null) {

			ISelection s= fOutlineViewer.getSelection();
			if (s instanceof IStructuredSelection) {
				IStructuredSelection ss= (IStructuredSelection) s;
				List elements= ss.toList();
				if (!elements.contains(reference)) {
					s= (reference == null ? StructuredSelection.EMPTY : new StructuredSelection(reference));
					fOutlineViewer.setSelection(s, true);
				}
			}
		}
	}

	public void setAction(String actionID, IAction action) {
		Assert.isNotNull(actionID);
		if (action == null)
			fActions.remove(actionID);
		else
			fActions.put(actionID, action);
	}

	public IAction getAction(String actionID) {
		Assert.isNotNull(actionID);
		return (IAction) fActions.get(actionID);
	}

	/*
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IShowInSource.class) {
			return getShowInSource();
		}
		if (key == IShowInTargetList.class) {
			return new IShowInTargetList() {
				public String[] getShowInTargetIds() {
					String explorerViewID = ProductProperties.getProperty(IProductConstants.PERSPECTIVE_EXPLORER_VIEW);
					// make sure the specified view ID is known
					if (PlatformUI.getWorkbench().getViewRegistry().find(explorerViewID) == null)
						explorerViewID = IPageLayout.ID_PROJECT_EXPLORER;
					return new String[] {explorerViewID, JavaScriptUI.ID_PACKAGES };

				}

			};
		}
		if (key == IShowInTarget.class) {
			return getShowInTarget();
		}

		return null;
	}

	/**
	 * Convenience method to add the action installed under the given actionID to the
	 * specified group of the menu.
	 *
	 * @param menu		the menu manager
	 * @param group		the group to which to add the action
	 * @param actionID	the ID of the new action
	 */
	protected void addAction(IMenuManager menu, String group, String actionID) {
		IAction action= getAction(actionID);
		if (action != null) {
			if (action instanceof IUpdate)
				((IUpdate) action).update();

			if (action.isEnabled()) {
		 		IMenuManager subMenu= menu.findMenuUsingPath(group);
		 		if (subMenu != null)
		 			subMenu.add(action);
		 		else
		 			menu.appendToGroup(group, action);
			}
		}
	}

	protected void contextMenuAboutToShow(IMenuManager menu) {

		JavaScriptPlugin.createStandardGroups(menu);

		IStructuredSelection selection= (IStructuredSelection)getSelection();
		fActionGroups.setContext(new ActionContext(selection));
		fActionGroups.fillContextMenu(menu);
	}

	/*
	 * @see Page#setFocus()
	 */
	public void setFocus() {
		if (fOutlineViewer != null)
			fOutlineViewer.getControl().setFocus();
	}

	/**
	 * Checks whether a given Java element is an inner type.
	 *
	 * @param element the java element
	 * @return <code>true</code> iff the given element is an inner type
	 */
	private boolean isInnerType(IJavaScriptElement element) {

		if (element != null && element.getElementType() == IJavaScriptElement.TYPE) {
			IType type= (IType)element;
			try {
				return type.isMember();
			} catch (JavaScriptModelException e) {
				IJavaScriptElement parent= type.getParent();
				if (parent != null) {
					int parentElementType= parent.getElementType();
					return (parentElementType != IJavaScriptElement.JAVASCRIPT_UNIT && parentElementType != IJavaScriptElement.CLASS_FILE);
				}
			}
		}

		return false;
	}

	/**
	 * Returns the <code>IShowInSource</code> for this view.
	 *
	 * @return the {@link IShowInSource}
	 */
	protected IShowInSource getShowInSource() {
		return new IShowInSource() {
			public ShowInContext getShowInContext() {
				return new ShowInContext(
					null,
					getSite().getSelectionProvider().getSelection());
			}
		};
	}

	/**
	 * Returns the <code>IShowInTarget</code> for this view.
	 *
	 * @return the {@link IShowInTarget}
	 */
	protected IShowInTarget getShowInTarget() {
		return new IShowInTarget() {
			public boolean show(ShowInContext context) {
				ISelection sel= context.getSelection();
				if (sel instanceof ITextSelection) {
					ITextSelection tsel= (ITextSelection) sel;
					int offset= tsel.getOffset();
					IJavaScriptElement element= fEditor.getElementAt(offset);
					if (element != null) {
						setSelection(new StructuredSelection(element));
						return true;
					}
				} else if (sel instanceof IStructuredSelection) {
					setSelection(sel);
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * Returns whether only the contents of the top level type is to be shown.
	 * 
	 * @return <code>true</code> if only the contents of the top level type is to be shown.
	 * 
	 */
	protected final boolean isTopLevelTypeOnly() {
		return fTopLevelTypeOnly;
	}
	
}
