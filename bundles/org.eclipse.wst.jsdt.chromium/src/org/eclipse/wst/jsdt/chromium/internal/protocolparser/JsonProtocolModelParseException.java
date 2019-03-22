// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.protocolparser;

/**
 * Signals that JSON model has some problem in it.
 */
public class JsonProtocolModelParseException extends Exception {
  public JsonProtocolModelParseException() {
    super();
  }
  public JsonProtocolModelParseException(String message, Throwable cause) {
    super(message, cause);
  }
  public JsonProtocolModelParseException(String message) {
    super(message);
  }
  public JsonProtocolModelParseException(Throwable cause) {
    super(cause);
  }
}
