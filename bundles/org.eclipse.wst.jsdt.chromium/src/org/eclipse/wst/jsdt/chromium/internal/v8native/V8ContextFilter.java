// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native;

import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ContextHandle;

/**
 * Embedder-specific filter for V8 VM contexts.
 */
public interface V8ContextFilter {
  /**
   * Given a context handler, it should check whether it is our context or not.
   * The field {@link ContextHandle#data()} of embedder-specific type should be used.
   * @return whether the context is ours
   */
  boolean isContextOurs(ContextHandle contextHandle);
}
