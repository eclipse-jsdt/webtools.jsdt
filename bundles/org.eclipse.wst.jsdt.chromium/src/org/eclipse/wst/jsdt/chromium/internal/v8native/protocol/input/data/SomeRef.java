// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtype;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCondition;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;


/**
 * A reference form of object data serialization. Basically it only has one field "ref" that
 * is "handle" of an object. Using this integer value as a key, all the object data may be
 * requested (lookup'ed) from debugger. However some additional data may be available via subtype.
 * <p>Gets serialized in mirror-delay.js,
 * first part of JSONProtocolSerializer.prototype.serialize_
 */
@JsonType
public interface SomeRef extends JsonSubtype<SomeSerialized> {
  @JsonSubtypeCondition
  long ref();

  @JsonSubtypeCasting
  RefWithDisplayData asWithDisplayData();

  @JsonSubtypeCasting
  void asJustSomeRef();
}
