// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Optional base interface for JSON type interface. Underlying JSON object becomes available
 * to user this way. The JSON type instance may be created from {@link JSONObject} only
 * (not from {@link JSONArray} or whatever).
 */
public interface JsonObjectBased extends AnyObjectBased {
  JSONObject getUnderlyingObject();
}
