// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@130398

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Toggles mouse event-based touch event emulation.
 */
public class SetTouchEmulationEnabledParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param enabled Whether the touch event emulation should be enabled.
   */
  public SetTouchEmulationEnabledParams(boolean enabled) {
    this.put("enabled", enabled);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setTouchEmulationEnabled";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
