// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.transport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.wst.jsdt.chromium.internal.transport.Message;

public class ControlledFakeConnection extends FakeConnection {

  private final Queue<Message> messages = new ConcurrentLinkedQueue<Message>();
  private boolean isContinuousProcessing = false;

  public ControlledFakeConnection(ChromeStub responder) {
    super(responder);
  }

  @Override
  public void send(Message message) {
    if (isContinuousProcessing) {
      super.send(message);
    } else {
      messages.add(message);
    }
  }

  public void setContinuousProcessing(boolean enabled) {
    this.isContinuousProcessing = enabled;
  }

  public void processMessages(int count) {
    for (int i = 0; i < count; i++) {
      Message polled = messages.poll();
      if (polled == null) {
        break;
      }
      super.send(polled);
    }
  }
}