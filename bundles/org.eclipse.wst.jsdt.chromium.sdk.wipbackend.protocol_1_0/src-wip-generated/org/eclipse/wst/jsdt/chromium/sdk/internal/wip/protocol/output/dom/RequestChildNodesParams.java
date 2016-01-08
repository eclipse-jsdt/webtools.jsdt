// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@102140

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.dom;

/**
Requests that children of the node with given id are returned to the caller in form of <code>setChildNodes</code> events.
 */
public class RequestChildNodesParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param nodeId Id of the node to get children for.
   */
  public RequestChildNodesParams(long/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.dom.NodeIdTypedef*/ nodeId) {
    this.put("nodeId", nodeId);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.DOM + ".requestChildNodes";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
