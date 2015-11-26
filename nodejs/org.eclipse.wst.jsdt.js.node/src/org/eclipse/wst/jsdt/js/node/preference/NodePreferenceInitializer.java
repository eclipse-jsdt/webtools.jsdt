/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.js.node.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.js.node.NodePlugin;
import org.eclipse.wst.jsdt.js.node.util.NodeDetector;

/**
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class NodePreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NodePlugin.getDefault().getPreferenceStore();
		String nodeLocation = NodeDetector.detectNode();
		store.setDefault(NodePreferenceHolder.PREF_NODE_LOCATION, ((nodeLocation != null) ? nodeLocation : "")); //$NON-NLS-1$
	}

}
