// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;

/**
 * A base type for all incoming message from debugger. There are 2 kinds of messages: response
 * to a command and event notification. All messages must have unique sequence id.
 */
@JsonType
public interface IncomingMessage {
  long seq();

  MessageType type();

  @JsonSubtypeCasting
  CommandResponse asCommandResponse();

  @JsonSubtypeCasting
  EventNotification asEventNotification();
}
