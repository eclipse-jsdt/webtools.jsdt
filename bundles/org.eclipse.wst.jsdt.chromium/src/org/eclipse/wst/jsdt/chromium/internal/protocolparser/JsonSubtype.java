// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

/**
 * A base interface for JSON subtype interface. This inheritance serves 2 purposes:
 * it declares base type (visible to human and to interface analyzer) and adds {@link #getSuper()}
 * getter that may be directly used in programs.
 */
public interface JsonSubtype<BASE> {
  BASE getSuper();
}
