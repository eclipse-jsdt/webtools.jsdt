// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.util;

/**
 * An interface to destruct some object. Used from {@link DestructingGuard}.
 */
public interface Destructable {
  /**
   * Destructs object wrapped in the interface. As usual exceptions are not
   * welcome from destruct method.
   */
  void destruct();
}
