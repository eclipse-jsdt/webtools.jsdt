// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.browserfixture;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.wst.jsdt.chromium.DebugContext;
import org.eclipse.wst.jsdt.chromium.DebugEventListener;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.TabDebugEventListener;

// TODO(peter.rybin): get rid of semaphore once we are single-threaded
public class StubListener implements DebugEventListener, TabDebugEventListener {
  private DebugContext debugContext = null;
  private Semaphore semaphore;

  public void closed() {
  }

  public void disconnected() {
  }

  public DebugEventListener getDebugEventListener() {
    return this;
  }

  public void navigated(String newUrl) {
  }

  public void resumed() {
    debugContext = null;
  }

  public void suspended(DebugContext context) {
    debugContext = context;
    if (semaphore != null) {
      semaphore.release();
    }
  }

  public void scriptLoaded(Script newScript) {
  }

  public void scriptCollected(Script script) {
  }

  public void expectSuspendedEvent() {
    if (semaphore != null) {
      throw new IllegalStateException();
    }
    semaphore = new Semaphore(0);
  }

  public DebugContext getDebugContext() {
    boolean res;
    try {
      res = semaphore.tryAcquire(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    if (!res) {
      throw new RuntimeException();
    }
    semaphore = null;
    if (debugContext == null) {
      throw new IllegalStateException();
    }
    return debugContext;
  }

  public VmStatusListener getVmStatusListener() {
    return null;
  }

  public void scriptContentChanged(Script newScript) {
  }
}