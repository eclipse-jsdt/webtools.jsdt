// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.util;

/**
 * A utility class that is used to generate a unique key according to a proposed key and
 * taking alternative keys if that one is already taken.
 */
public class UniqueKeyGenerator {

  /**
   * A general purpose algorithm that creates a unique key in an abstract storage and returns
   * a corresponding element (of type E). It simply tries various keys that start with proposed
   * string until succeeds or gives up.
   */
  public static <E> E createUniqueKey(String proposedKey, int tryLimit,
      Factory<E> factory) {
    String nextKey = proposedKey;
    for (int i = 1; i < tryLimit; ++i) {
      E element = factory.tryCreate(nextKey);
      if (element != null) {
        return element;
      }
      nextKey = proposedKey + " (" + i + ')';  //$NON-NLS-1$
    }
    throw new RuntimeException("Failed to find a unique key");
  }

  /**
   * A factory for an abstract storage used in {@link #createElementWithUniqueName} method.
   */
  public interface Factory<E> {

    /**
     * @return created element or null if element with such name cannot be created
     */
    E tryCreate(String name);
  }
}
