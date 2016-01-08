// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@136521

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

/**
Emulates the given media for CSS media queries.
 */
public class SetEmulatedMediaParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param media Media type to emulate. Empty string disables the override.
   */
  public SetEmulatedMediaParams(String media) {
    this.put("media", media);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".setEmulatedMedia";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
