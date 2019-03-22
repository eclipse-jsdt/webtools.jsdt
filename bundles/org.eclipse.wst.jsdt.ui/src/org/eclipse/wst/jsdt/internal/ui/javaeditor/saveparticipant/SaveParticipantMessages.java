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
package org.eclipse.wst.jsdt.internal.ui.javaeditor.saveparticipant;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 *
 * 
 */
final class SaveParticipantMessages extends NLS {

	private static final String BUNDLE_NAME= SaveParticipantMessages.class.getName();

	private SaveParticipantMessages() {
		// Do not instantiate
	}

	public static String SaveParticipantRegistry_participantNotFound;

	static {
		NLS.initializeMessages(BUNDLE_NAME, SaveParticipantMessages.class);
	}
}
