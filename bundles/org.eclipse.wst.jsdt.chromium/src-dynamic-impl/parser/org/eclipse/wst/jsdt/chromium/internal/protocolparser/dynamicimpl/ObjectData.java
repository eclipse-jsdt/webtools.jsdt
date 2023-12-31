// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

/**
 * Stores all data for instance of json type.
 * Each implementation of json type interface is a java dynamic proxy, that holds reference
 * to {@link JsonInvocationHandler} which holds reference to this structure. ObjectData points
 * back to dynamic proxy instance in {@link #proxy}.
 */
class ObjectData {

  /**
   * Stores type-specific set of pre-parsed fields.
   */
  private final Object[] fieldArray;
  private final AtomicReferenceArray<Object> atomicReferenceArray;

  /**
   * May be JSONObject (in most cases) or any
   * object (for {@link JsonType#subtypesChosenManually()}=true).
   */
  private final Object underlyingObject;
  private final TypeHandler<?> typeHandler;

  /**
   * Holds reference to base type object data (or null).
   */
  private final ObjectData superObjectData;
  private Object proxy = null;

  ObjectData(TypeHandler<?> typeHandler, Object inputObject, int fieldArraySize,
      int volatileArraySize, ObjectData superObjectData) {
    this.superObjectData = superObjectData;
    this.typeHandler = typeHandler;
    this.underlyingObject = inputObject;

    if (fieldArraySize == 0) {
      fieldArray = null;
    } else {
      fieldArray = new Object[fieldArraySize];
    }
    if (volatileArraySize == 0) {
      atomicReferenceArray = null;
    } else {
      atomicReferenceArray = new AtomicReferenceArray<Object>(volatileArraySize);
    }
  }

  void initProxy(Object proxy) {
    this.proxy = proxy;
  }

  Object[] getFieldArray() {
    return fieldArray;
  }

  AtomicReferenceArray<Object> getAtomicReferenceArray() {
    return atomicReferenceArray;
  }

  Object getUnderlyingObject() {
    return underlyingObject;
  }

  TypeHandler<?> getTypeHandler() {
    return typeHandler;
  }

  ObjectData getSuperObjectData() {
    return superObjectData;
  }

  Object getProxy() {
    return proxy;
  }

  @Override
  public String toString() {
    return typeHandler.getShortName() + "/" + underlyingObject;
  }
}
