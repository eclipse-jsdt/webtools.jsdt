// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

import org.json.simple.JSONArray;

/**
 * Optional base interface for JSON type interface. Underlying object becomes available
 * to user this way. The JSON type instance may be created from any supported object
 * (e.g. from {@link JSONArray}), but may take advantage of this liberty only if it has no fields.
 */
public interface AnyObjectBased {
  Object getUnderlyingObject();
}
