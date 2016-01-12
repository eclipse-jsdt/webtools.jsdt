// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page;

/**
 Information about the Frame on the page.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface FrameValue {
  /**
   Frame unique identifier.
   */
  String id();

  /**
   Parent frame identifier.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String parentId();

  /**
   Identifier of the loader associated with this frame.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.LoaderIdTypedef*/ loaderId();

  /**
   Frame's name as specified in the tag.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String name();

  /**
   Frame document's URL.
   */
  String url();

  /**
   Frame document's security origin.
   */
  String securityOrigin();

  /**
   Frame document's mimeType as determined by the browser.
   */
  String mimeType();

}
