// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.liveeditprotocol;

import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParseMethod;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParserRoot;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.json.simple.JSONObject;

/**
 * Interface to LiveEdit protocol parser.
 */
@JsonParserRoot
public interface LiveEditProtocolParser {

  @JsonParseMethod
  LiveEditResult parseLiveEditResult(JSONObject underlyingObject) throws JsonProtocolParseException;

}
