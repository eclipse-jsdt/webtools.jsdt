// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Overrides the Device Orientation.
 */
public class SetDeviceOrientationOverrideParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param alpha Mock alpha
   @param beta Mock beta
   @param gamma Mock gamma
   */
  public SetDeviceOrientationOverrideParams(Number alpha, Number beta, Number gamma) {
    this.put("alpha", alpha);
    this.put("beta", beta);
    this.put("gamma", gamma);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setDeviceOrientationOverride";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
