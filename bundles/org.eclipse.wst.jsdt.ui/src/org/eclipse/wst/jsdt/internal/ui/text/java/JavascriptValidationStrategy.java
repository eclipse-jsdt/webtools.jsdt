/*******************************************************************************
 * Copyright (c) 2016 RedHat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *  RedHat, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.text.java;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;

/**
 * @author V.V. Rubezhny
 *
 */
public class JavascriptValidationStrategy implements IJavascriptValidationStrategy {
	IJavascriptValidationStrategy delegate = null;
	public JavascriptValidationStrategy(ISourceViewer viewer) {
		createDelegate(viewer);
	}
	
	private void createDelegate(ISourceViewer viewer) {
		delegate = null;
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor("org.eclipse.wst.jsdt.ui", "javascriptValidationHelper");
		for (int i = 0; delegate == null && i < cf.length; i++) {
			try {
				IJavascriptValidationStrategy p = (IJavascriptValidationStrategy) cf[i].createExecutableExtension("class");
				if( p != null ) 
					delegate = p;
			} catch (CoreException e) {
				JavaScriptPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, JavaScriptPlugin.getPluginId(),
							"Loading IJavascriptValidationStrategy extension has failed.", e));
			}

		}
		setSourceViewer(viewer);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setProgressMonitor(IProgressMonitor monitor) {
		if( delegate != null )
			delegate.setProgressMonitor(monitor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#initialReconcile()
	 */
	public void initialReconcile() {
		if( delegate != null )
			delegate.initialReconcile();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
	 */
	public void setDocument(IDocument document) {
		if( delegate != null )
			delegate.setDocument(document);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion, org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		if( delegate != null )
			delegate.reconcile(dirtyRegion, subRegion);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(IRegion partition) {
		if( delegate != null )
			delegate.reconcile(partition);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.IJavascriptValidationStrategy#setSourceViewer(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public void setSourceViewer(ISourceViewer viewer) {
		if( delegate != null ) {
			delegate.setSourceViewer(viewer);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.IJavascriptValidationStrategy#aboutToBeReconciled()
	 */
	public void aboutToBeReconciled() {
		if( delegate != null ) {
			delegate.aboutToBeReconciled();
		}
	}

}
