// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * The implementation of {@link InvocationHandler} for JSON types. It dispatches calls to method
 * handlers from the map.
 */
class JsonInvocationHandler implements InvocationHandler {
  private final ObjectData objectData;
  private final Map<Method, MethodHandler> methodHandlerMap;

  JsonInvocationHandler(ObjectData objectData, Map<Method, MethodHandler> methodHandlerMap) {
    this.objectData = objectData;
    this.methodHandlerMap = methodHandlerMap;
  }

  ObjectData getObjectData() {
    return objectData;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodHandler methodHandler = methodHandlerMap.get(method);
    if (methodHandler == null) {
      throw new RuntimeException("No method handler for " + method);
    }
    return methodHandler.handle(objectData, args);
  }
}
