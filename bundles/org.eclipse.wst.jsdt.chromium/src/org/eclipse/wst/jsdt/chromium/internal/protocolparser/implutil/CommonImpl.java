// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser.implutil;


/**
 * Methods and classes commonly used by dynamic and generated implementations of
 * {@link JsonProtocolParser}.
 */
public class CommonImpl {

  public static class ParseRuntimeException extends RuntimeException {
    public ParseRuntimeException() {
    }
    public ParseRuntimeException(String message, Throwable cause) {
      super(message, cause);
    }
    public ParseRuntimeException(String message) {
      super(message);
    }
    public ParseRuntimeException(Throwable cause) {
      super(cause);
    }
  }

}
