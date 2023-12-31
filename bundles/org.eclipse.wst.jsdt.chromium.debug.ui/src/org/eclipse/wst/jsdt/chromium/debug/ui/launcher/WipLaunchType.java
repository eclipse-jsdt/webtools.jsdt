// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.launcher;

import org.eclipse.wst.jsdt.chromium.debug.core.model.JavascriptVmEmbedder.ConnectionToRemote;
import org.eclipse.wst.jsdt.chromium.debug.core.model.BreakpointSynchronizer;
import org.eclipse.wst.jsdt.chromium.debug.core.model.JavascriptVmEmbedderFactory;
import org.eclipse.wst.jsdt.chromium.debug.core.model.LaunchParams;
import org.eclipse.wst.jsdt.chromium.debug.core.model.NamedConnectionLoggerFactory;
import org.eclipse.wst.jsdt.chromium.debug.ui.DialogBasedTabSelector;
import org.eclipse.wst.jsdt.chromium.ConnectionLogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;

import org.eclipse.wst.jsdt.chromium.wip.WipBackend;
import org.eclipse.wst.jsdt.chromium.wip.eclipse.BackendRegistry;

public class WipLaunchType extends LaunchTypeBase {
  @Override
  protected ConnectionToRemote createConnectionToRemote(String host, int port,
      final ILaunch launch, boolean addConsoleLogger) throws CoreException {

    ILaunchConfiguration config = launch.getLaunchConfiguration();
    String wipBackendId = config.getAttribute(LaunchParams.WIP_BACKEND_ID, (String) null);

    if (wipBackendId == null) {
      throw new RuntimeException("Missing 'wip backend' parameter in launch config");
    }

    WipBackend backend;
    findWipBackend: {
      for (WipBackend nextBackend : BackendRegistry.INSTANCE.getBackends()) {
        if (nextBackend.getId().equals(wipBackendId)) {
          backend = nextBackend;
          break findWipBackend;
        }
      }
      // Nothing found.
      throw new RuntimeException("Cannot find required wip backend in Eclipse: " + wipBackendId);
    }

    NamedConnectionLoggerFactory consoleFactory;
    if (addConsoleLogger) {
      consoleFactory = new NamedConnectionLoggerFactory() {
        public ConnectionLogger createLogger(String title) {
          return LaunchTypeBase.createConsoleAndLogger(launch, false, title);
        }
      };
    } else {
      consoleFactory = NO_CONNECTION_LOGGER_FACTORY;
    }

    return JavascriptVmEmbedderFactory.connectToWipBrowser(host, port, backend, consoleFactory,
        consoleFactory, DialogBasedTabSelector.WIP_INSTANCE);
  }

  @Override
  protected BreakpointSynchronizer.Direction getPresetSyncDirection() {
    return BreakpointSynchronizer.Direction.RESET_REMOTE;
  }
}
