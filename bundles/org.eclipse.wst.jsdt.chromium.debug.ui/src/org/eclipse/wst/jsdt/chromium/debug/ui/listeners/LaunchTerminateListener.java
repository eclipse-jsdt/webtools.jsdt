/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * 	Contributors:
 * 		 Red Hat Inc. - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.wst.jsdt.chromium.debug.ui.listeners;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * Tracking {@link ILaunch} termination and removing
 * {@link IResourceChangeListener} specified in constructor. Once {@link ILaunch}
 * is terminated, {@link LaunchTerminateListener} will be also removed from {@link DebugPlugin}
 * 
 * @author "Ilya Buziuk (ibuziuk)"
 */
public class LaunchTerminateListener implements IDebugEventSetListener {
	private IResourceChangeListener[] listeners;
	private ILaunch launch;

	public LaunchTerminateListener(ILaunch launch, IResourceChangeListener... listeners) {
		this.launch = launch;
		this.listeners = listeners;
	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (DebugEvent event : events) {
			if (event.getKind() == DebugEvent.TERMINATE) {
				Object source = event.getSource();
				if (source instanceof IProcess) {
					ILaunch l = ((IProcess) source).getLaunch();
					if (l != null && l.equals(launch)) {
						try {
							for (IResourceChangeListener listener : listeners) {
								ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
							}
						} finally {
							DebugPlugin.getDefault().removeDebugEventListener(this);
						}
					}
				}
			}
		}
	}

}
