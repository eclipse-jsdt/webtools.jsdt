// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.util;

/**
 * A generic callback used in asynchronous operations that either fail with exception
 * or return a result.
 */
public interface GenericCallback<T> {
  void success(T value);
  void failure(Exception exception);
}