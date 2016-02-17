package org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input;

import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.WipParserAccess;
import org.junit.Test;

public class WipProtocolTest {
  @Test
  public void buildParser() {
    // Just make sure parser is built.
    WipParserAccess.get().hashCode();
  }
}
