/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.compiler.ValidationParticipant;
import org.eclipse.wst.jsdt.core.compiler.ReconcileContext;

public class TestvalidationParticipant extends ValidationParticipant {
	
	public static ValidationParticipant PARTICIPANT;

	public boolean isActive(IJavaScriptProject project) {
		return PARTICIPANT != null && PARTICIPANT.isActive(project);
	}
	
	public void reconcile(ReconcileContext context) {
		PARTICIPANT.reconcile(context);
	}
}
