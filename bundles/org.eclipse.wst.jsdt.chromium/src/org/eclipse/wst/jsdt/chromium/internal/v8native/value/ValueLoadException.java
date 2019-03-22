// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.value;

/**
 * Signals a problem in loading value from remote, most probably a parsing problem.
 */
public class ValueLoadException extends RuntimeException {

  public ValueLoadException() {
  }

  public ValueLoadException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValueLoadException(String message) {
    super(message);
  }

  public ValueLoadException(Throwable cause) {
    super(cause);
  }

}
