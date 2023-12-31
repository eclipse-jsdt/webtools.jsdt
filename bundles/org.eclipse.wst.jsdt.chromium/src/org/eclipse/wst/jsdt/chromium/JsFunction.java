// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * Extends {@link JsObject} interface with the methods for function-specific properties.
 */
public interface JsFunction extends JsObject {

  /**
   * @return script the function resides in or null if script is not available
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  Script getScript() throws MethodIsBlockingException;

  /**
   * Returns position of opening parenthesis of function arguments. Position is absolute
   * within resource (not relative to script start position).
   * @return position or null if position is not available
   * @throws MethodIsBlockingException because it may need to load value from remote
   */
  TextStreamPosition getOpenParenPosition() throws MethodIsBlockingException;
}
