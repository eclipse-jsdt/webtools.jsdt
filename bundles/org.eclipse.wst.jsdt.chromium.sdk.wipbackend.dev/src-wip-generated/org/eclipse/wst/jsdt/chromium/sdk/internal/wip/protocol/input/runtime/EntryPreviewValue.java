// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime;

@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface EntryPreviewValue {
  /**
   Preview of the key. Specified for map-like collection entries.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.ObjectPreviewValue key();

  /**
   Preview of the value.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.ObjectPreviewValue value();

}
