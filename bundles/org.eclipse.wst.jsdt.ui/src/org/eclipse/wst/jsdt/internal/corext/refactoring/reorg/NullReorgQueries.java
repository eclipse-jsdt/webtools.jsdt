/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.reorg;

import org.eclipse.core.runtime.OperationCanceledException;

/**
 * Null implementation of reorg queries.
 * 
 * 
 */
public final class NullReorgQueries implements IReorgQueries {

	/** Null implementation of confirm query */
	private static final class NullConfirmQuery implements IConfirmQuery {

		/**
		 * {@inheritDoc}
		 */
		public boolean confirm(String question) throws OperationCanceledException {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean confirm(String question, Object[] elements) throws OperationCanceledException {
			return true;
		}
	}

	/** The null query */
	private static final IConfirmQuery NULL_QUERY= new NullConfirmQuery();

	/**
	 * {@inheritDoc}
	 */
	public IConfirmQuery createSkipQuery(String queryTitle, int queryID) {
		return NULL_QUERY;
	}

	/**
	 * {@inheritDoc}
	 */
	public IConfirmQuery createYesNoQuery(String queryTitle, boolean allowCancel, int queryID) {
		return NULL_QUERY;
	}

	/**
	 * {@inheritDoc}
	 */
	public IConfirmQuery createYesYesToAllNoNoToAllQuery(String queryTitle, boolean allowCancel, int queryID) {
		return NULL_QUERY;
	}
}
