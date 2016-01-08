// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: Local file Inspector-1.0.json.r107603.manual_fix

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 Information about the cached resource.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface CachedResourceValue {
  /**
   Resource URL.
   */
  String url();

  /**
   Type of this resource.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.ResourceTypeEnum type();

  /**
   Cached response data.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network.ResponseValue response();

  /**
   Cached response body size.
   */
  Number bodySize();

}
