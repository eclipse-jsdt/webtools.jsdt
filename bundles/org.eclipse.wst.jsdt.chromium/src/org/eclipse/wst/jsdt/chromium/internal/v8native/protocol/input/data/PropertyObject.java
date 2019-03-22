// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

/**
 * A type for a property object. May have 2 different forms (subtypes).
 * <p>Gets serialized in mirror-delay.js,
 * JSONProtocolSerializer.prototype.serializeProperty_
 */
@JsonType
public interface PropertyObject {
  /**
   * @return either String (normal property) or Long (array element)
   */
  Object name();

  @JsonSubtypeCasting
  PropertyWithValue asPropertyWithValue();

  @JsonSubtypeCasting
  PropertyWithRef asPropertyWithRef();
}
