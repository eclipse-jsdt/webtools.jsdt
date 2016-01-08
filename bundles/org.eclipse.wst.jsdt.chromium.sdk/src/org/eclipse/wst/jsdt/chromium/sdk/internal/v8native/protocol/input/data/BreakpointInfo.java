// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.sdk.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonNullable;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;

@JsonType
public interface BreakpointInfo {
  Type type();

  @JsonOptionalField
  String script_name();

  @JsonOptionalField
  Long script_id();

  @JsonOptionalField
  String script_regexp();

  long number();

  long line();

  Long column();

  Long groupId();

  long hit_count();

  boolean active();

  @JsonNullable
  String condition();

  long ignoreCount();

  enum Type {
    SCRIPTNAME,
    SCRIPTID,
    SCRIPTREGEXP,
    FUNCTION
  }
}
