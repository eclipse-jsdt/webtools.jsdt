// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.page;

public class RemoveScriptToEvaluateOnLoadParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  public RemoveScriptToEvaluateOnLoadParams(String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.page.ScriptIdentifierTypedef*/ identifier) {
    this.put("identifier", identifier);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.PAGE + ".removeScriptToEvaluateOnLoad";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
