// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonNullable;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;

@JsonType
public interface BreakpointBody extends JsonSubtype<CommandResponseBody> {

  long breakpoint();

  @JsonOptionalField
  @JsonNullable
  Object column();

  @JsonOptionalField
  Long line();

  @JsonOptionalField
  String script_name();

  @JsonOptionalField
  String type();
}
