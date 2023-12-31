// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.core.sourcemap;

import org.eclipse.wst.jsdt.chromium.debug.core.model.VmResourceId;

/**
 * Defines a position within a whole source base. All numbers are 0-based.
 */
public class SourcePosition {
  private final VmResourceId id;

  /**
   * 0-based number.
   */
  private final int line;

  /**
   * 0-based number.
   */
  private final int column;

  public SourcePosition(VmResourceId id, int line, int column) {
    this.id = id;
    this.line = line;
    this.column = column;
  }

  public VmResourceId getId() {
    return id;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof SourcePosition == false) {
      return false;
    }
    SourcePosition other = (SourcePosition) obj;
    return this.id.equals(other.id) && this.line == other.line && this.column == other.column;
  }

  @Override
  public int hashCode() {
    return id.hashCode() + 17 * line + 31 * column;
  }

  @Override
  public String toString() {
    return id + ":" + line + ":" + column;
  }
}