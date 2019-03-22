// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * An extension to {@link JsFunction} API that returns function (closure) hidden scope.
 * @see JavascriptVm#getFunctionScopeExtension()
 */
public interface FunctionScopeExtension {
  /**
   * @return list of hidden function scopes (possibly empty)
   * @throws MethodIsBlockingException because function may need to load scope data
   *     on demand
   */
  List<? extends JsScope> getScopes(JsFunction function) throws MethodIsBlockingException;
}
