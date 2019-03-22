// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import java.net.InetSocketAddress;

import org.eclipse.wst.jsdt.chromium.wip.WipBrowser;
import org.eclipse.wst.jsdt.chromium.wip.WipBrowserFactory;

public class WipBrowserFactoryImpl implements WipBrowserFactory {
  @Override
  public WipBrowser createBrowser(InetSocketAddress socketAddress,
      LoggerFactory connectionLoggerFactory) {
    return new WipBrowserImpl(socketAddress, connectionLoggerFactory);
  }
}
