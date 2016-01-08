// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@108993

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.console;

public class AddInspectedHeapObjectParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  public AddInspectedHeapObjectParams(long heapObjectId) {
    this.put("heapObjectId", heapObjectId);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.CONSOLE + ".addInspectedHeapObject";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
