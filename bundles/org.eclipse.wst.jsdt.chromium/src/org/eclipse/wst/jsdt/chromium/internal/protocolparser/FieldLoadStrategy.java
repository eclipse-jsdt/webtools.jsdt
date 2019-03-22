// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

/**
 * Defines a strategy the JSON parser should use for a particular field.
 */
public enum FieldLoadStrategy {
  /**
   * Parse field immediately.
   */
  EAGER,

  /**
   * Parse field on demand.
   */
  LAZY,

  /**
   * Compiler should choose strategy itself.
   */
  AUTO;
}
