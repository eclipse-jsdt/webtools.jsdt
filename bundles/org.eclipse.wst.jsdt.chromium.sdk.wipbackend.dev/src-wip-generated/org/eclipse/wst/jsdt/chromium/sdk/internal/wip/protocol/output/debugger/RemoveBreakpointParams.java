// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.debugger;

/**
Removes JavaScript breakpoint.
 */
public class RemoveBreakpointParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  public RemoveBreakpointParams(String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.debugger.BreakpointIdTypedef*/ breakpointId) {
    this.put("breakpointId", breakpointId);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DEBUGGER + ".removeBreakpoint";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
