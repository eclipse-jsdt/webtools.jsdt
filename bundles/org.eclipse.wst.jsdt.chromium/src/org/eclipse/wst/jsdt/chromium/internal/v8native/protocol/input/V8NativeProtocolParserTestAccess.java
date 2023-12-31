// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParseMethod;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParserRoot;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ScriptHandle;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.SomeRef;
import org.json.simple.JSONObject;

/**
 * A part of {@link V8NativeProtocolParser} interface that is introduced specially for
 * test disposals.
 */
@JsonParserRoot
public interface V8NativeProtocolParserTestAccess {

  @JsonParseMethod
  FrameObject parseFrameObject(JSONObject jsonObject) throws JsonProtocolParseException;

  @JsonParseMethod
  SomeRef parseSomeRef(JSONObject valueObject) throws JsonProtocolParseException;

  @JsonParseMethod
  ScriptHandle parseScriptHandle(JSONObject body) throws JsonProtocolParseException;
}