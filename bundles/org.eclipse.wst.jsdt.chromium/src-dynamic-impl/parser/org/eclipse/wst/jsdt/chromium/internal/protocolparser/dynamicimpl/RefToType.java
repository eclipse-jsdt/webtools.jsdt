// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.dynamicimpl;

/**
 * Late-resolvable reference to {@link TypeHandler}, for building {@link JsonTypeParser}.
 */
abstract class RefToType<T> {
  /**
   * Returns json type.
   */
  abstract Class<?> getTypeClass();

  /**
   * Returns {@link TypeHandler} corresponding to {@link #getTypeClass()}. The method becomes
   * available only after cross-reference resolving has been finished in depths of
   * {@link DynamicParserImpl} constructor.
   */
  abstract TypeHandler<T> get();
}
