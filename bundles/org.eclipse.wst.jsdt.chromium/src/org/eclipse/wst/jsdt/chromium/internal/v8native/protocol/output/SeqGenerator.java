// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A singleton that keeps track of the "seq" values for them to be unique across
 * the plugin lifecycle.
 */
public class SeqGenerator {

  private static SeqGenerator INSTANCE = new SeqGenerator();

  private final AtomicInteger count = new AtomicInteger(1);

  public static SeqGenerator getInstance() {
    return INSTANCE;
  }

  public int next() {
    return count.getAndIncrement();
  }

  private SeqGenerator() {
    // not instantiable outside
  }
}
