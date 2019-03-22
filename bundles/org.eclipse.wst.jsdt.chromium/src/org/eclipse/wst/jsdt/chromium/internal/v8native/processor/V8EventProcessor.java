// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.processor;

import org.eclipse.wst.jsdt.chromium.internal.v8native.DebugSession;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.EventNotification;

/**
 * An abstract base implementation of DebugContextImpl-aware
 * reply handlers for certain V8 commands.
 * <p>
 * NB! The {@link #messageReceived(org.json.simple.JSONObject)} implementation
 * MUST NOT perform debugger commands in a blocking way the current thread.
 */
public abstract class V8EventProcessor {

  private final DebugSession debugSession;

  public V8EventProcessor(DebugSession debugSession) {
    this.debugSession = debugSession;
  }

  public abstract void messageReceived(EventNotification eventMessage);

  protected DebugSession getDebugSession() {
    return debugSession;
  }
}
