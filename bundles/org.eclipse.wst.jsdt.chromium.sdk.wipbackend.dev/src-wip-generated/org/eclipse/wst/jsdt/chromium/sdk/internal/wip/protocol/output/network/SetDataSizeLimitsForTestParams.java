// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
For testing.
 */
public class SetDataSizeLimitsForTestParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param maxTotalSize Maximum total buffer size.
   @param maxResourceSize Maximum per-resource size.
   */
  public SetDataSizeLimitsForTestParams(long maxTotalSize, long maxResourceSize) {
    this.put("maxTotalSize", maxTotalSize);
    this.put("maxResourceSize", maxResourceSize);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".setDataSizeLimitsForTest";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
