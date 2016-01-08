// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.console;

/**
Toggles monitoring of XMLHttpRequest. If <code>true</code>, console will receive messages upon each XHR issued.
 */
public class SetMonitoringXHREnabledParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param enabled Monitoring enabled state.
   */
  public SetMonitoringXHREnabledParams(boolean enabled) {
    this.put("enabled", enabled);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.CONSOLE + ".setMonitoringXHREnabled";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
