// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCondition;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

@JsonType
public interface ObjectValueHandle extends JsonSubtype<ValueHandle> {
  @JsonSubtypeCondition
  List<PropertyObject> properties();

  @JsonOptionalField
  List<PropertyObject> internalProperties();

  SomeRef protoObject();
  SomeRef constructorFunction();

  @JsonOptionalField
  SomeRef primitiveValue();

  @JsonOptionalField
  SomeRef prototypeObject();

  @JsonSubtypeCasting
  FunctionValueHandle asFunction();

  @JsonSubtypeCasting
  void notFunction();
}
