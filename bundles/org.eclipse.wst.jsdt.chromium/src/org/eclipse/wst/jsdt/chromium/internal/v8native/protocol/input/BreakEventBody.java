// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ValueHandle;
import org.json.simple.JSONObject;

@JsonType
public interface BreakEventBody extends JsonSubtype<EventNotificationBody> {

  @JsonOptionalField
  List<Long> breakpoints();

  @JsonOptionalField
  ValueHandle exception();

  @JsonOptionalField
  String sourceLineText();

  @JsonOptionalField
  Boolean uncaught();

  @JsonOptionalField
  Long sourceLine();

  @JsonOptionalField
  String invocationText();

  @JsonOptionalField
  JSONObject script();

  @JsonOptionalField
  Long sourceColumn();
}
