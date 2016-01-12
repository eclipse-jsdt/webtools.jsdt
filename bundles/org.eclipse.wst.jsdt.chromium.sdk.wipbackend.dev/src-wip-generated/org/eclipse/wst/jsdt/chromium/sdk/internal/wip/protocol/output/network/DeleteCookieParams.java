// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.network;

/**
Deletes browser cookie with given name, domain and path.
 */
public class DeleteCookieParams extends org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.output.WipParams {
  /**
   @param cookieName Name of the cookie to remove.
   @param url URL to match cooke domain and path.
   */
  public DeleteCookieParams(String cookieName, String url) {
    this.put("cookieName", cookieName);
    this.put("url", url);
  }

  public static final String METHOD_NAME = org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.BasicConstants.Domain.NETWORK + ".deleteCookie";

  @Override protected String getRequestName() {
    return METHOD_NAME;
  }

}
