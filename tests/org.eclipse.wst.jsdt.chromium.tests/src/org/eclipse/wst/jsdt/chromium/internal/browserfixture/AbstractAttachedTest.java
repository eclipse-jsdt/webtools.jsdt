// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.browserfixture;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.eclipse.wst.jsdt.chromium.DebugContext;
import org.eclipse.wst.jsdt.chromium.DebugContext.ContinueCallback;
import org.eclipse.wst.jsdt.chromium.DebugContext.StepAction;
import org.eclipse.wst.jsdt.chromium.DebugEventListener;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.TabDebugEventListener;
import org.eclipse.wst.jsdt.chromium.UnsupportedVersionException;
import org.eclipse.wst.jsdt.chromium.internal.BrowserFactoryImplTestGate;
import org.eclipse.wst.jsdt.chromium.internal.standalonev8.StandaloneVmImpl;
import org.eclipse.wst.jsdt.chromium.internal.transport.Connection;
import org.eclipse.wst.jsdt.chromium.internal.transport.FakeConnection;
import org.junit.After;
import org.junit.Before;

/**
 * A base class for all tests that require an attachment to a browser tab.
 */
public abstract class AbstractAttachedTest<T extends Connection>
    implements DebugEventListener, TabDebugEventListener {

  protected FixtureChromeStub messageResponder;

  protected StandaloneVmImpl javascriptVm;

  protected DebugContext suspendContext;

  protected Script loadedScript;

  protected Runnable suspendCallback;

  protected Runnable closedCallback;

  protected Runnable navigatedCallback;

  protected Runnable scriptLoadedCallback;

  protected String newTabUrl;

  protected boolean isDisconnected = false;

  protected T connection;


  @Before
  public void setUpBefore() throws Exception {
    this.newTabUrl = "";
    this.messageResponder = new FixtureChromeStub();
    connection = createConnection();
    attachToBrowserTab();
  }

  @After
  public void tearDownAfter() {
  }

  protected void attachToBrowserTab() throws IOException, UnsupportedVersionException {
    javascriptVm = BrowserFactoryImplTestGate.createStandalone(
        new FakeConnection(messageResponder), FakeConnection.HANDSHAKER);
    javascriptVm.attach(this);
  }

  protected abstract T createConnection();

  protected T getConnection() {
    return connection;
  }

  @After
  public void tearDown() {
    suspendContext = null;
  }

  public void closed() {
    this.newTabUrl = null;
    if (closedCallback != null) {
      closedCallback.run();
    }
  }

  public void disconnected() {
    this.isDisconnected = true;
  }

  public DebugEventListener getDebugEventListener() {
    return this;
  }

  public void navigated(String newUrl) {
    this.newTabUrl = newUrl;
    if (navigatedCallback != null) {
      navigatedCallback.run();
    }
  }

  public void resumed() {
    this.suspendContext = null;
  }

  public void suspended(DebugContext context) {
    this.suspendContext = context;
    if (suspendCallback != null) {
      suspendCallback.run();
    }
  }

  public void scriptLoaded(Script newScript) {
    this.loadedScript = newScript;
    if (scriptLoadedCallback != null) {
      scriptLoadedCallback.run();
    }
  }

  public void scriptCollected(Script script) {
  }

  protected CountDownLatch expectSuspend() {
    final CountDownLatch latch = new CountDownLatch(1);
    suspendCallback = new Runnable() {
      public void run() {
        latch.countDown();
      }
    };
    return latch;
  }

  /** This should be called from a timed test. */
  protected void resume() throws Exception {
    final CountDownLatch latch = new CountDownLatch(1);
    final String[] failure = new String[1];
    suspendContext.continueVm(StepAction.CONTINUE, 0, new ContinueCallback() {
      public void failure(String errorMessage) {
        failure[0] = errorMessage == null ? "" : errorMessage;
        latch.countDown();
      }

      public void success() {
        latch.countDown();
      }
    });
    latch.await();
    assertNull("Failure on continue: " + failure[0], failure[0]);
    assertNull(suspendContext);
  }

  public VmStatusListener getVmStatusListener() {
    return null;
  }

  public void scriptContentChanged(Script newScript) {
  }
}
