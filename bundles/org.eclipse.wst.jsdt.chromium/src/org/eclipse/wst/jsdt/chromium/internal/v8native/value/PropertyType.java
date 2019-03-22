// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.value;

import java.util.HashMap;
import java.util.Map;

/**
 * Known V8 VM property types. The default is NORMAL.
 */
public enum PropertyType {
  NORMAL(0),
  FIELD(1),
  CONSTANT_FUNCTION(2),
  CALLBACKS(3),
  INTERCEPTOR(4),
  MAP_TRANSITION(5),
  CONSTANT_TRANSITION(6),
  NULL_DESCRIPTOR(7),
  ;

  public final int value;

  private PropertyType(int value) {
    this.value = value;
  }

  private static Map<Integer, PropertyType> valueToTypeMap = new HashMap<Integer, PropertyType>();

  static {
    for (PropertyType type : values()) {
      valueToTypeMap.put(type.value, type);
    }
  }

  public static PropertyType forValue(Integer value) {
    if (value == null) {
      return null;
    }
    return valueToTypeMap.get(value);
  }

}
