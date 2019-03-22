// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

/**
 * A condition for property value. Implementation may provide any logic here.
 * @param <T> type of value
 * @see JsonSubtypeConditionCustom
 */
public interface JsonValueCondition<T> {
  /**
   * @param value parsed data from JSON property
   * @return true if value satisfies condition
   */
  boolean conforms(T value);
}
