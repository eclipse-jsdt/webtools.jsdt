// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;

/**
 * Projection of {@link JsEvaluateContext} into Eclipse world.
 */
public class EvaluateContext {
  private final JsEvaluateContext jsEvaluateContext;
  private final JavascriptThread.SuspendedState threadState;

  EvaluateContext(JsEvaluateContext jsEvaluateContext,
      JavascriptThread.SuspendedState threadState) {
    this.jsEvaluateContext = jsEvaluateContext;
    this.threadState = threadState;
  }

  public JsEvaluateContext getJsEvaluateContext() {
    return jsEvaluateContext;
  }

  public JavascriptThread.SuspendedState getThreadSuspendedState() {
    return threadState;
  }
}
