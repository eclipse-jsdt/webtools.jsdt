// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@98328

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.console;

/**
Enables console domain, sends the messages collected so far to the client by means of the <code>messageAdded</code> notification.
 */
public class EnableParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  public EnableParams() {
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.CONSOLE + ".enable";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
