// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

import java.net.SocketAddress;

import org.eclipse.wst.jsdt.chromium.JavascriptVmFactory;
import org.eclipse.wst.jsdt.chromium.ConnectionLogger;
import org.eclipse.wst.jsdt.chromium.StandaloneVm;
import org.eclipse.wst.jsdt.chromium.internal.standalonev8.StandaloneVmImpl;
import org.eclipse.wst.jsdt.chromium.internal.transport.Connection;
import org.eclipse.wst.jsdt.chromium.internal.transport.Handshaker;
import org.eclipse.wst.jsdt.chromium.internal.transport.SocketConnection;

/**
 * A default implementation of the BrowserFactory interface.
 * TODO: rename it somehow. It's not only a browser factory.
 */
public class JavascriptVmFactoryImpl extends JavascriptVmFactory {

  public static final JavascriptVmFactoryImpl INSTANCE = new JavascriptVmFactoryImpl();

  private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 1000;

  @Override
  public StandaloneVm createStandalone(SocketAddress socketAddress,
      ConnectionLogger connectionLogger) {
    Handshaker.StandaloneV8 handshaker = new Handshaker.StandaloneV8Impl();
    SocketConnection connection =
        new SocketConnection(socketAddress, getTimeout(), connectionLogger, handshaker);
    return createStandalone(connection, handshaker);
  }

  // Debug entry (no logger by definition)
  StandaloneVmImpl createStandalone(Connection connection, Handshaker.StandaloneV8 handshaker) {
    return new StandaloneVmImpl(connection, handshaker);
  }

  private int getTimeout() {
    String timeoutString = System.getProperty(
        "org.eclipse.wst.jsdt.chromium.client.connection.timeoutMs",
        String.valueOf(DEFAULT_CONNECTION_TIMEOUT_MS));
    int timeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;
    try {
      timeoutMs = Integer.parseInt(timeoutString);
    } catch (NumberFormatException e) {
      // fall through and use the default value
    }
    return timeoutMs;
  }

}
