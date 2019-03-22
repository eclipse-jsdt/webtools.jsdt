// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import org.eclipse.wst.jsdt.chromium.ConnectionLogger;

/**
 * The factory provides {@link ConnectionLogger} that can be used to output connection
 * traffic, supposedly to some UI.
 */
public interface NamedConnectionLoggerFactory {
  ConnectionLogger createLogger(String title);
}
