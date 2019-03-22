// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParseMethod;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParserRoot;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ContextData;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ValueHandle;
import org.json.simple.JSONObject;

/**
 * Interface to native V8 debug protocol parser.
 * @see http://code.google.com/p/v8/wiki/DebuggerProtocol
 */
@JsonParserRoot
public interface V8NativeProtocolParser extends V8NativeProtocolParserTestAccess {

  @JsonParseMethod
  IncomingMessage parseIncomingMessage(JSONObject json) throws JsonProtocolParseException;

  @JsonParseMethod
  SuccessCommandResponse parseSuccessCommandResponse(JSONObject json)
      throws JsonProtocolParseException;

  @JsonParseMethod
  ContextData parseContextData(JSONObject dataObject) throws JsonProtocolParseException;

  @JsonParseMethod
  ValueHandle parseValueHandle(JSONObject value) throws JsonProtocolParseException;
}
