// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.transport;

import org.eclipse.wst.jsdt.chromium.internal.transport.Connection.NetListener;

/**
 * An implementor can provide a way to respond to a certain Message (naturally,
 * instead of Google Chrome).
 */
public interface ChromeStub {

  /**
   * Constructs responses to client requests in place of Google Chrome.
   *
   * @param requestMessage to respond to
   * @return a response message
   */
  Message respondTo(Message requestMessage);

  /**
   * Accepts the NetListener instance set for the host FakeConnection. The
   * listener can be used to report V8 debugger events without explicit requests
   * from the client.
   *
   * @param listener set for the host FakeConnection
   */
  void setNetListener(NetListener listener);

  /**
   * Creates and sends notification about VM being suspended.
   */
  void sendSuspendedEvent();
}
