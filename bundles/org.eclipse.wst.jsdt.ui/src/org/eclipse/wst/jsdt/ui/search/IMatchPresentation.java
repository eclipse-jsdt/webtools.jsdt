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
package org.eclipse.wst.jsdt.ui.search;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.PartInitException;

/**
 * This interface serves to display elements that a search participant has contributed to a search
 * result.
 * <p>
 * Each {@link IMatchPresentation} is associated with a particular {@link IQueryParticipant}. The {@link IMatchPresentation}
 * will only be asked to handle elements and matches which its {@link IQueryParticipant} contributed to the 
 * search result. If two search participants report matches against the same element, one of them will
 * be chosen to handle the element.
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves. */
public interface IMatchPresentation {
	/**
	 * Creates a new instance of a label provider for elements that have been contributed
	 * to a search result by the corresponding query participant. The search view
	 * will call this method when it needs to render elements and will dispose the 
	 * label providers when it is done with them. This method may therefore be called
	 * multiple times.
	 * @return A label provider for elements found by the corresponding query participant.
	 */
	ILabelProvider createLabelProvider();
	/**
	 * Opens an editor on the given element and selects the given range of text.
	 * The location of matches are automatically updated when a file is edited
	 * through the file buffer infrastructure (see {@link org.eclipse.core.filebuffers.ITextFileBufferManager}). 
	 * When a file buffer is saved, the current positions are written back to the 
	 * match.
	 * If the <code>activate</code> parameter is <code>true</code> the opened editor
	 * should have be activated. Otherwise the focus should not be changed.
	 * 
	 * @param match
	 *            The match to show.
	 * @param currentOffset
	 *            The current start offset of the match.
	 * @param currentLength
	 *            The current length of the selection.
	 * @param activate
	 * 			  Whether to activate the editor the match is shown in.
	 * @throws PartInitException
	 *             If an editor can't be opened.
	 */
	void showMatch(Match match, int currentOffset, int currentLength, boolean activate) throws PartInitException;
}
