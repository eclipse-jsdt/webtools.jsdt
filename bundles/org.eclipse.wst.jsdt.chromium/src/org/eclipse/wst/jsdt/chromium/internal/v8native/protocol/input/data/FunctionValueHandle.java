// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.FieldLoadStrategy;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCondition;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.ScopeRef;
import org.json.simple.JSONObject;

@JsonType
public interface FunctionValueHandle extends JsonSubtype<ObjectValueHandle> {
  @JsonOptionalField
  Long position();

  @JsonOptionalField
  Long line();

  @JsonOptionalField
  JSONObject script();

  @JsonSubtypeCondition
  boolean resolved();

  @JsonOptionalField
  String source();

  @JsonOptionalField
  String inferredName();

  @JsonOptionalField
  String name();

  @JsonOptionalField
  Long column();

  @JsonOptionalField
  Long scriptId();

  @JsonField(loadStrategy=FieldLoadStrategy.LAZY)
  List<ScopeRef> scopes();
}
