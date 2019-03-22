// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonObjectBased;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

@JsonType
public interface WipEvent {
  String method();

  @JsonField(jsonLiteralName = "params")
  @JsonOptionalField
  Data data();

  @JsonType(subtypesChosenManually=true, allowsOtherProperties=true)
  interface Data extends JsonObjectBased {
  }
}
