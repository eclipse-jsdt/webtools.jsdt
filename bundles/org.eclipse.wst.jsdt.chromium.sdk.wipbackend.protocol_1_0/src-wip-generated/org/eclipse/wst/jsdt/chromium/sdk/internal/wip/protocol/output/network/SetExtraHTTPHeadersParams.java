// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@96703

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Specifies whether to always send extra HTTP headers with the requests from this page.
 */
public class SetExtraHTTPHeadersParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param headers Map with extra HTTP headers.
   */
  public SetExtraHTTPHeadersParams(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network.HeadersParam headers) {
    this.put("headers", headers);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".setExtraHTTPHeaders";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
