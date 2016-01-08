package org.eclipse.wst.jsdt.chromium.sdk.internal;

import org.eclipse.wst.jsdt.chromium.sdk.internal.standalonev8.StandaloneVmImpl;
import org.eclipse.wst.jsdt.chromium.sdk.internal.transport.Connection;
import org.eclipse.wst.jsdt.chromium.sdk.internal.transport.Handshaker;

public class BrowserFactoryImplTestGate {
  public static StandaloneVmImpl createStandalone(Connection connection,
      Handshaker.StandaloneV8 handshaker) {
    return JavascriptVmFactoryImpl.INSTANCE.createStandalone(connection, handshaker);
  }
}
