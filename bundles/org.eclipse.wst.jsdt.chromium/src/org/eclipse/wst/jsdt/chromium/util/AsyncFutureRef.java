// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.util;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.util.AsyncFuture;
import org.eclipse.wst.jsdt.chromium.util.AsyncFuture.Operation;

/**
 * A wrapper around {@link AtomicReference} and {@link AsyncFuture} that makes the source code
 * cleaner and shorter.
 */
public class AsyncFutureRef<T> {
  private final AtomicReference<AsyncFuture<T>> ref = new AtomicReference<AsyncFuture<T>>(null);

  public void initializeRunning(Operation<T> requester) {
    AsyncFuture.initializeReference(ref, requester);
  }

  public void reinitializeRunning(Operation<T> requester) {
    AsyncFuture.reinitializeReference(ref, requester);
  }

  public void initializeTrivial(T value) {
    AsyncFuture.initializeTrivial(ref, value);
  }

  public boolean isInitialized() {
    return ref.get() != null;
  }

  public T getSync() throws MethodIsBlockingException {
    return ref.get().getSync();
  }

  public RelayOk getAsync(AsyncFuture.Callback<T> callback, SyncCallback syncCallback) {
    return ref.get().getAsync(callback, syncCallback);
  }

  public boolean isDone() {
    return ref.get().isDone();
  }
}
