// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core;

import org.eclipse.osgi.util.NLS;

/**
 * NLS messages for the package.
 */
public class Messages extends NLS {
  private static final String BUNDLE_NAME =
      "org.eclipse.wst.jsdt.chromium.debug.core.messages"; //$NON-NLS-1$

  public static String ChromiumDebugPlugin_InternalError;

  public static String ChromiumSourceDirector_WARNING_TEXT_PATTERN;

  public static String ChromiumSourceDirector_WARNING_TITLE;

  public static String SourceNameMapperContainer_NAME;

  public static String VProjectSourceContainer_DEFAULT_TYPE_NAME;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
