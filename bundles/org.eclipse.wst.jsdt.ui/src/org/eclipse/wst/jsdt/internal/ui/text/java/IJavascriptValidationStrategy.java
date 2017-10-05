/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java;

import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * A hack interface to help break jsdt / sse circular dependency
 * 
 * @author rob Stryker
 *
 */
public interface IJavascriptValidationStrategy extends IReconcilingStrategy, IReconcilingStrategyExtension {
	public void setSourceViewer(ISourceViewer viewer);
	public void aboutToBeReconciled();
}
