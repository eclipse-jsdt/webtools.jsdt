// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@142888

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Requests that backend shows debug borders on layers
 */
public class SetShowDebugBordersParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param show True for showing debug borders
   */
  public SetShowDebugBordersParams(boolean show) {
    this.put("show", show);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setShowDebugBorders";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
