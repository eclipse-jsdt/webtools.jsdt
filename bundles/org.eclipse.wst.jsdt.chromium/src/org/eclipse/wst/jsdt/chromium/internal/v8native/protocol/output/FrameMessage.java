// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output;

import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;

/**
 * Represents a "frame" V8 request message.
 */
public class FrameMessage extends DebuggerMessage {

  /**
   * @param frame number (top is 0)
   */
  public FrameMessage(Integer frame) {
    super(DebuggerCommand.FRAME.value);
    putArgument("number", frame);
  }
}
