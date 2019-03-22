// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native;

import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.internal.v8native.V8CommandProcessor.V8HandlerCallback;

/**
 * API to asynchronous message sender that supports callbacks.
 * @param <MESSAGE> type of message supported
 * @param <EX> exception that may be thrown synchronously.
 */
public interface V8CommandSender<MESSAGE, EX extends Exception> {
  RelayOk sendV8CommandAsync(MESSAGE message, boolean isImmediate,
      V8HandlerCallback v8HandlerCallback, SyncCallback syncCallback) throws EX;
}
