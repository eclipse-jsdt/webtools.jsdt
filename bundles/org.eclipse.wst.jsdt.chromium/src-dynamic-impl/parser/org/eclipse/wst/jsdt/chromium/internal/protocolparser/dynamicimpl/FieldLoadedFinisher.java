// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

/**
 * Defines object responsible for converting values saved in {@link ObjectData} to types
 * returned to user. It is necessary, because for json type fields we save {@link ObjectData}
 * rather than instance of the type itself.
 */
abstract class FieldLoadedFinisher {
  abstract Object getValueForUser(Object cachedValue);
}
