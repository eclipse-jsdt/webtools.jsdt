// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.actions;

import org.eclipse.wst.jsdt.chromium.debug.core.model.EvaluateContext;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ExpressionTracker;
import org.eclipse.wst.jsdt.chromium.debug.core.model.VProjectWorkspaceBridge;
import org.eclipse.wst.jsdt.chromium.debug.core.model.Value;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ValueBase;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IErrorReportingExpression;
import org.eclipse.debug.core.model.IValue;

/**
 * An Eclipse object for the JavaScript inspected expression.
 */
public class JsInspectExpression extends PlatformObject
    implements IErrorReportingExpression, IDebugEventSetListener {

  private final EvaluateContext evaluateContext;

  private final JsValue value;

  private final String errorMessage;

  private final String expression;

  public JsInspectExpression(EvaluateContext evaluateContext, String expression, JsValue value,
      String errorMessage) {
    this.evaluateContext = evaluateContext;
    this.expression = expression;
    this.value = value;
    this.errorMessage = errorMessage;
  }

  public String[] getErrorMessages() {
    return errorMessage == null
        ? new String[0]
        : new String[] { errorMessage };
  }

  public boolean hasErrors() {
    return errorMessage != null;
  }

  public void dispose() {
  }

  public IDebugTarget getDebugTarget() {
    IValue value = getValue();
    if (value != null) {
      return value.getDebugTarget();
    }
    return null;
  }

  public String getExpressionText() {
    return expression;
  }

  public IValue getValue() {
    return value != null
        ? Value.create(evaluateContext, value, ExpressionTracker.createExpressionNode(expression))
        : null;
  }

  public ILaunch getLaunch() {
    return getValue().getLaunch();
  }

  public String getModelIdentifier() {
    return VProjectWorkspaceBridge.DEBUG_MODEL_ID;
  }

  public void handleDebugEvents(DebugEvent[] events) {
    for (DebugEvent event : events) {
      switch (event.getKind()) {
        case DebugEvent.TERMINATE:
          if (event.getSource().equals(getDebugTarget())) {
            DebugPlugin.getDefault().getExpressionManager().removeExpression(this);
          }
          break;
        case DebugEvent.SUSPEND:
          if (event.getDetail() != DebugEvent.EVALUATION_IMPLICIT &&
              event.getSource() instanceof IDebugElement) {
            IDebugElement source = (IDebugElement) event.getSource();
            if (source.getDebugTarget().equals(getDebugTarget())) {
              DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] {
                  new DebugEvent(this, DebugEvent.CHANGE, DebugEvent.CONTENT) });
            }
          }
          break;
      }
    }
  }

}
