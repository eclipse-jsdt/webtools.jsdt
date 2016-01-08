// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@101756

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Tells whether clearing browser cache is supported.
 */
public class CanClearBrowserCacheParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.CanClearBrowserCacheData> {
  public CanClearBrowserCacheParams() {
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".canClearBrowserCache";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.CanClearBrowserCacheData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseNetworkCanClearBrowserCacheData(data.getUnderlyingObject());
  }

}
