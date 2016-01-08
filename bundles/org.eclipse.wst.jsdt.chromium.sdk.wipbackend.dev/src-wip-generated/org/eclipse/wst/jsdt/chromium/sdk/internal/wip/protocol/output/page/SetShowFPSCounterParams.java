// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@135591

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Requests that backend shows the FPS counter
 */
public class SetShowFPSCounterParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param show True for showing the FPS counter
   */
  public SetShowFPSCounterParams(boolean show) {
    this.put("show", show);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setShowFPSCounter";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
