/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.text.java;

import org.eclipse.jface.text.contentassist.ICompletionProposal;

/**
 * A completion proposal with a relevance value.
 * The relevance value is used to sort the completion proposals. Proposals with higher relevance
 * should be listed before proposals with lower relevance.
 * <p>
 * This interface can be implemented by clients.
 * </p>
 *
 * @see org.eclipse.jface.text.contentassist.ICompletionProposal
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves. */
public interface IJavaCompletionProposal extends ICompletionProposal {

	/**
	 * Returns the relevance of this completion proposal.
	 * <p>
	 * The relevance is used to determine if this proposal is more
	 * relevant than another proposal.</p>
	 *
	 * @return the relevance of this completion proposal in the range of [0, 100]
	 */
	int getRelevance();

}
