// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

import java.net.SocketAddress;
import java.util.logging.Logger;

import org.eclipse.wst.jsdt.chromium.internal.JavascriptVmFactoryImpl;
import org.eclipse.wst.jsdt.chromium.wip.WipBrowser;
import org.eclipse.wst.jsdt.chromium.wip.WipBrowserFactory;

/**
 * A factory for {@link JavascriptVm} instances. Note that {@link WipBrowser} instances are
 * created in a specialized class {@link WipBrowserFactory}.
 */
public abstract class JavascriptVmFactory {
  /**
   * Gets a {@link JavascriptVmFactory} instance. This method should be overridden by
   * implementations that want to construct other implementations of
   * {@link Browser}.
   *
   * @return a {@link JavascriptVmFactory} singleton instance
   */
  public static JavascriptVmFactory getInstance() {
    return JavascriptVmFactoryImpl.INSTANCE;
  }

  /**
   * Constructs StandaloneVm instance that talks to a V8 JavaScript VM via
   * DebuggerAgent opened at {@code socketAddress}.
   * @param socketAddress V8 DebuggerAgent is listening on
   * @param connectionLogger provides facility for listening to network
   *        traffic; may be null
   */
  public abstract StandaloneVm createStandalone(SocketAddress socketAddress,
      ConnectionLogger connectionLogger);

  /**
   * @return SDK root logger that can be used to add handlers or to adjust log level
   */
  public static Logger getRootLogger() {
    return LOGGER;
  }

  private static final Logger LOGGER = Logger.getLogger("org.eclipse.wst.jsdt.chromium");
}
