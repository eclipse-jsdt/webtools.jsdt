// Copyright (c) 2011-2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;

/**
 * A common base class for all stack frames. Frames may be 'real' or 'fake', e.g. frame that
 * represents throwing an exception.
 */
public abstract class StackFrameBase extends DebugElementImpl.WithEvaluate implements IStackFrame {
  public StackFrameBase(EvaluateContext evaluateContext) {
    super(evaluateContext);
  }

  public JavascriptThread getThread() {
    return getSuspendedState().getThread();
  }

  public boolean canStepInto() {
    return getThread().canStepInto();
  }

  public boolean canStepOver() {
    return getThread().canStepOver();
  }

  public boolean canStepReturn() {
    return getThread().canStepReturn();
  }

  public boolean isStepping() {
    return getThread().isStepping();
  }

  public void stepInto() throws DebugException {
    getThread().stepInto();
  }

  public void stepOver() throws DebugException {
    getThread().stepOver();
  }

  public void stepReturn() throws DebugException {
    getThread().stepReturn();
  }

  public boolean canResume() {
    return getThread().canResume();
  }

  public boolean canSuspend() {
    return getThread().canSuspend();
  }

  public boolean isSuspended() {
    return getThread().isSuspended();
  }

  public void resume() throws DebugException {
    getThread().resume();
  }

  public void suspend() throws DebugException {
    getThread().suspend();
  }

  public boolean canTerminate() {
    return getThread().canTerminate();
  }

  public boolean isTerminated() {
    return getThread().isTerminated();
  }

  public void terminate() throws DebugException {
    getThread().terminate();
  }

  public IRegisterGroup[] getRegisterGroups() throws DebugException {
    return null;
  }

  public boolean hasRegisterGroups() throws DebugException {
    return false;
  }

  abstract boolean isRegularFrame();

  abstract Object getObjectForEquals();

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof StackFrameBase == false) {
      return false;
    }
    StackFrameBase other = (StackFrameBase) obj;
    return this.getObjectForEquals().equals(other.getObjectForEquals());
  }

  @Override
  public int hashCode() {
    return getObjectForEquals().hashCode();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object getAdapter(Class adapter) {
    if (adapter == EvaluateContext.class) {
      if (getSuspendedState().isDismissed()) {
        return null;
      }
      return getEvaluateContext();
    }
    return super.getAdapter(adapter);
  }
}
