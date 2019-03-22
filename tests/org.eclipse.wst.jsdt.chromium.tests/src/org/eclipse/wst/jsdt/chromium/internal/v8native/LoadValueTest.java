// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native;

import static org.junit.Assert.*;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import org.eclipse.wst.jsdt.chromium.CallbackSemaphore;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext.ResultOrException;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.internal.browserfixture.AbstractAttachedTest;
import org.eclipse.wst.jsdt.chromium.internal.transport.FakeConnection;
import org.junit.Test;

/**
 * A test for the DebugContextImpl class.
 */
public class LoadValueTest extends AbstractAttachedTest<FakeConnection>{

  @Test
  public void testLoadFullValue() throws Exception {
    {
      CountDownLatch latch = expectSuspend();
      messageResponder.hitBreakpoints(Collections.<Long>emptyList());
      latch.await();
    }

    final JsValue [] expressionResult = { null };
    JsEvaluateContext.EvaluateCallback evaluateCallback = new JsEvaluateContext.EvaluateCallback() {
      @Override
      public void success(ResultOrException result) {
        result.accept(new ResultOrException.Visitor<Void>() {
          @Override
          public Void visitResult(JsValue value) {
            expressionResult[0] = value;
            return null;
          }

          @Override public Void visitException(JsValue exception) {
            return null;
          }
        });
      }
      @Override
      public void failure(Exception cause) {
      }
    };

    suspendContext.getGlobalEvaluateContext().evaluateSync("#long_value", null, evaluateCallback);
    assertNotNull(expressionResult[0]);

    JsValue value = expressionResult[0];
    assertTrue(value.isTruncated());
    String shortValue = value.getValueString();

    final boolean[] reloadResult = { false } ;
    JsValue.ReloadBiggerCallback callback = new JsValue.ReloadBiggerCallback() {
      public void done() {
        reloadResult[0] = true;
      }
    };
    CallbackSemaphore semaphore = new CallbackSemaphore();
    RelayOk relayOk = value.reloadHeavyValue(callback, semaphore);
    semaphore.acquireDefault(relayOk);
    assertTrue(reloadResult[0]);

    String reloadedValue = value.getValueString();

    assertTrue(shortValue.length() < reloadedValue.length());
  }

  @Override
  protected FakeConnection createConnection() {
    return new FakeConnection(messageResponder);
  }
}
