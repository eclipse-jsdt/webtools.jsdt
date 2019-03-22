// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.wip;

import java.io.IOException;
import java.util.List;

import org.eclipse.wst.jsdt.chromium.Browser;
import org.eclipse.wst.jsdt.chromium.TabDebugEventListener;

/**
 * WIP interface to browser similar to {@link Browser}.
 */
public interface WipBrowser {

  /**
   * @param backend wip implementation
   */
  List<? extends WipTabConnector> getTabs(WipBackend backend) throws IOException;

  interface WipTabConnector {
    String getTitle();

    /**
     * @return tab url that should be shown to user to let him select one tab from list
     */
    String getUrl();

    /**
     * @return true if the tab is already attached at this moment
     */
    boolean isAlreadyAttached();

    /**
     * Attaches to the related tab debugger.
     *
     * @param listener to report the debug events to
     * @return null if operation failed
     */
    WipBrowserTab attach(TabDebugEventListener listener) throws IOException;
  }
}
