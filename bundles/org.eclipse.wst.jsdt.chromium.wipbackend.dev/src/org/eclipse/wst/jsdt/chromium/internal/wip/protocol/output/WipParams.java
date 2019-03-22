// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output;

import org.json.simple.JSONObject;

/**
 * A base class for all method parameter classes.
 * It also allows to get the method name it corresponds to.
 */
public abstract class WipParams extends JSONObject {
  protected abstract String getRequestName();
}
