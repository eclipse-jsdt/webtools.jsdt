// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@101756

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Tells whether clearing browser cookies is supported.
 */
public class CanClearBrowserCookiesParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParamsWithResponse<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.CanClearBrowserCookiesData> {
  public CanClearBrowserCookiesParams() {
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".canClearBrowserCookies";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

  @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.CanClearBrowserCookiesData parseResponse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipCommandResponse.Data data, org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
    return parser.parseNetworkCanClearBrowserCookiesData(data.getUnderlyingObject());
  }

}
