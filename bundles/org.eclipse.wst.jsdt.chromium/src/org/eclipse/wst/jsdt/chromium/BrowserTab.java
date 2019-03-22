// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;


/**
 * A lightweight abstraction of a remote Browser tab. Each browser tab
 * corresponds to a Javascript Virtual Machine and is_a {code JavascriptVm}.
 */
public interface BrowserTab extends JavascriptVm {

  /**
   * @return the "parent" Browser instance
   */
  Browser getBrowser();

  /**
   * @return a current URL of the corresponding browser tab
   */
  String getUrl();

}
