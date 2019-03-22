// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.value;


/**
 * A named property reference.
 */
public class PropertyReference {
  private final Object name;

  private final DataWithRef smthWithRef;

  /**
   * @param propertyName the name of the property
   * @param valueObject a JSON descriptor of the property
   */
  public PropertyReference(Object propertyName, DataWithRef smthWithRef) {
    this.name = propertyName;
    this.smthWithRef = smthWithRef;
  }

  public long getRef() {
    return smthWithRef.ref();
  }

  public Object getName() {
    return name;
  }

  public DataWithRef getValueObject() {
    return smthWithRef;
  }
}