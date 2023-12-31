// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * An object that represents a browser JavaScript VM call frame.
 */
public interface CallFrame {

  /**
   * @return the scopes known in this frame; ordered, innermost first, global scope last
   */
  List<? extends JsScope> getVariableScopes();

  /**
   * @return the receiver variable known in this frame
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  JsVariable getReceiverVariable() throws MethodIsBlockingException;

  /**
   * @return the source script this call frame is associated with; {@code null}
   *         if no script is associated with the call frame (e.g. an exception
   *         could have been thrown in a native script)
   */
  Script getScript();

  /**
   * @return the start position (absolute) of the current statement in the Script corresponding
   *     to this frame or null if position in not available
   */
  TextStreamPosition getStatementStartPosition();

  /**
   * @return the name of the current function of this frame
   */
  String getFunctionName();

  /**
   * @return context for evaluating expressions in scope of this frame
   */
  JsEvaluateContext getEvaluateContext();
}
