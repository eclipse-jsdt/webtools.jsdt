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

package org.eclipse.wst.jsdt.internal.ui.search;

import org.eclipse.wst.jsdt.ui.search.IQueryParticipant;

/**
 */
public class SearchParticipantRecord {
	private SearchParticipantDescriptor fDescriptor;
	private IQueryParticipant fParticipant;

	public SearchParticipantRecord(SearchParticipantDescriptor descriptor, IQueryParticipant participant) {
		super();
		fDescriptor= descriptor;
		fParticipant= participant;
	}
	/**
	 * @return Returns the descriptor.
	 */
	public SearchParticipantDescriptor getDescriptor() {
		return fDescriptor;
	}
	/**
	 * @return Returns the participant.
	 */
	public IQueryParticipant getParticipant() {
		return fParticipant;
	}
}
