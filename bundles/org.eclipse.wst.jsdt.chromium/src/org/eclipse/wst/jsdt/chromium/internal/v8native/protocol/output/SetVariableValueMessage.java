// Copyright (c) 2012 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output;

import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;
import org.json.simple.JSONObject;

/**
 * Represents a "setVariableValue" request message.
 */
public class SetVariableValueMessage extends DebuggerMessage {
  public SetVariableValueMessage(ScopeMessage.Ref scopeRef, String variableName,
      EvaluateMessage.Value value) {
    super(DebuggerCommand.SETVARIABLEVALUE.value);
    JSONObject scopeObject = new JSONObject();
    scopeRef.fillJson(scopeObject);
    putArgument("scope", scopeObject);
    putArgument("name", variableName);
    putArgument("newValue", value.createJsonParameter());
  }
}
