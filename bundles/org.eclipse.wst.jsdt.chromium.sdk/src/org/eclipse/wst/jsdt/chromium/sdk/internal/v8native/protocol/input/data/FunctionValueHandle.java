// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input.data;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.FieldLoadStrategy;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtypeCondition;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input.ScopeRef;
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
