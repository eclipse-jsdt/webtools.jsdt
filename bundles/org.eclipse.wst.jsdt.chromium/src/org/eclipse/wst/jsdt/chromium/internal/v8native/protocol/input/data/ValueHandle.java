// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOptionalField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonOverrideField;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;


/**
 * A serialization of a JavaScript value. May be cast to {@link ObjectValueHandle} if value is
 * an object.
 * <p>Gets serialized in mirror-delay.js,
 * JSONProtocolSerializer.prototype.serialize_, main part
 */
@JsonType
public interface ValueHandle extends JsonSubtype<SomeHandle>  {
  @JsonOverrideField
  long handle();

  String text();

  @JsonOptionalField
  Object value();

  @JsonOverrideField
  String type();

  // for string type (the true length, value field may be truncated)
  @JsonOptionalField
  Long length();
  @JsonOptionalField
  Long fromIndex();
  @JsonOptionalField
  Long toIndex();

  @JsonOptionalField
  String className();

  @JsonSubtypeCasting
  ObjectValueHandle asObject();

  @JsonSubtypeCasting
  void asNotObject();
}

