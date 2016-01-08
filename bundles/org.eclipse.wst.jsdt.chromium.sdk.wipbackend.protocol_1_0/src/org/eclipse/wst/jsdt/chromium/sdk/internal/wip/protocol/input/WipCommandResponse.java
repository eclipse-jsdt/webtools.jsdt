// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input;

import java.util.List;

import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonObjectBased;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOverrideField;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonSubtypeCondition;
import org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants;

@JsonType
public interface WipCommandResponse extends JsonObjectBased {
  @JsonField(jsonLiteralName = BasicConstants.Property.ID)
  Long id();

  @JsonSubtypeCasting Success asSuccess();
  @JsonSubtypeCasting Error asError();

  @JsonType
  interface Success extends JsonSubtype<WipCommandResponse> {
    @JsonSubtypeCondition(fieldIsAbsent=true)
    @JsonOptionalField
    Void error();

    @JsonField(jsonLiteralName="result")
    @JsonSubtypeCondition
    Data data();
  }

  @JsonType
  interface Error extends JsonSubtype<WipCommandResponse> {
    @JsonOverrideField
    @JsonSubtypeCondition()
    ErrorInfo error();

    @JsonSubtypeCondition(fieldIsAbsent=true)
    @JsonField(jsonLiteralName="result")
    @JsonOptionalField
    Data data();

    @JsonType
    interface ErrorInfo {
      String message();

      @JsonOptionalField
      List<String> data();
      long code();
    }
  }

  @JsonType(subtypesChosenManually=true, allowsOtherProperties=true)
  interface Data extends JsonObjectBased {
  }
}
