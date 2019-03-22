// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal;

/**
 * Signals incorrect (or unexpected) JSON content.
 */
public class JsonException extends RuntimeException {

  JsonException() {
  }

  JsonException(String message, Throwable cause) {
    super(message, cause);
  }

  JsonException(String message) {
    super(message);
  }

  JsonException(Throwable cause) {
    super(cause);
  }

}
