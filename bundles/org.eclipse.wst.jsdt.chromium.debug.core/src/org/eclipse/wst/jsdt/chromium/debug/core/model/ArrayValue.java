// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.wst.jsdt.chromium.JsArray;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IIndexedValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * An IIndexedValue implementation for an array element range using a JsArray
 * instance.
 */
public class ArrayValue extends Value implements IIndexedValue {

  private final AtomicReference<IVariable[]> elementsRef = new AtomicReference<IVariable[]>(null);

  public ArrayValue(EvaluateContext evaluateContext, JsArray array,
      ExpressionTracker.Node expressionTrackerNode) {
    super(evaluateContext, array, expressionTrackerNode);
  }

  private IVariable[] createElements() {
    JsArray jsArray = (JsArray) getJsValue();
    return StackFrame.wrapVariables(getEvaluateContext(), jsArray.getProperties(),
        ARRAY_HIDDEN_PROPERTY_NAMES,
        // Do not show internal properties for arrays (this may be an option).
        null, null, getExpressionTrackerNode());
  }

  private IVariable[] getElements() {
    IVariable[] result = elementsRef.get();
    if (result == null) {
      result = createElements();
      elementsRef.compareAndSet(null, result);
      return elementsRef.get();
    } else {
      return result;
    }
  }

  public int getInitialOffset() {
    return 0;
  }

  public int getSize() throws DebugException {
    return getElements().length;
  }

  public IVariable getVariable(int offset) throws DebugException {
    return getElements()[offset];
  }

  public IVariable[] getVariables(int offset, int length) throws DebugException {
    IVariable[] result = new IVariable[length];
    System.arraycopy(getElements(), offset, result, 0, length);
    return result;
  }

  @Override
  public IVariable[] getVariables() throws DebugException {
    return getElements();
  }

  @Override
  public boolean hasVariables() throws DebugException {
    // Being optimistic. Too complicated to check accurately.
    return true;
  }

  private static final Set<String> ARRAY_HIDDEN_PROPERTY_NAMES = Collections.singleton("length");
}
