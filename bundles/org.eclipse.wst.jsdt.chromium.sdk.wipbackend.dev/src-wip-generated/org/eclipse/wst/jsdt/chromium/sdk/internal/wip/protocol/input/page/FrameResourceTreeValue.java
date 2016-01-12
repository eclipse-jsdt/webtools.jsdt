// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page;

/**
 Information about the Frame hierarchy along with their cached resources.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface FrameResourceTreeValue {
  /**
   Frame information for this tree item.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameValue frame();

  /**
   Child frames.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameResourceTreeValue> childFrames();

  /**
   Information about frame resources.
   */
  java.util.List<Resources> resources();

  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
  public interface Resources {
    /**
     Resource URL.
     */
    String url();

    /**
     Type of this resource.
     */
    org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.ResourceTypeEnum type();

    /**
     Resource mimeType as determined by the browser.
     */
    String mimeType();

    /**
     True if the resource failed to load.
     */
    @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
    Boolean failed();

    /**
     True if the resource was canceled during loading.
     */
    @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
    Boolean canceled();

  }
}
