// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Allows overriding user agent with the given string.
 */
public class SetUserAgentOverrideParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param userAgent User agent to use.
   */
  public SetUserAgentOverrideParams(String userAgent) {
    this.put("userAgent", userAgent);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".setUserAgentOverride";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
