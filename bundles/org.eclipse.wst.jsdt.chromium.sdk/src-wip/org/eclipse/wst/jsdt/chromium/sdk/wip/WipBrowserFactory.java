// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.sdk.wip;

import java.net.InetSocketAddress;

import org.eclipse.wst.jsdt.chromium.sdk.ConnectionLogger;

/**
 * A factory for connections via WebInspector protocol (WIP).
 */
public interface WipBrowserFactory {

  WipBrowserFactory INSTANCE = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.WipBrowserFactoryImpl();

  WipBrowser createBrowser(InetSocketAddress socketAddress,
      LoggerFactory connectionLoggerFactory);

  interface LoggerFactory {
    ConnectionLogger newBrowserConnectionLogger();

    ConnectionLogger newTabConnectionLogger();
  }
}
