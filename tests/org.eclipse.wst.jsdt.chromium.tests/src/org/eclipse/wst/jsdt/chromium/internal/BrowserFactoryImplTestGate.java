package org.eclipse.wst.jsdt.chromium.internal;

import org.eclipse.wst.jsdt.chromium.internal.standalonev8.StandaloneVmImpl;
import org.eclipse.wst.jsdt.chromium.internal.transport.Connection;
import org.eclipse.wst.jsdt.chromium.internal.transport.Handshaker;

public class BrowserFactoryImplTestGate {
  public static StandaloneVmImpl createStandalone(Connection connection,
      Handshaker.StandaloneV8 handshaker) {
    return JavascriptVmFactoryImpl.INSTANCE.createStandalone(connection, handshaker);
  }
}
