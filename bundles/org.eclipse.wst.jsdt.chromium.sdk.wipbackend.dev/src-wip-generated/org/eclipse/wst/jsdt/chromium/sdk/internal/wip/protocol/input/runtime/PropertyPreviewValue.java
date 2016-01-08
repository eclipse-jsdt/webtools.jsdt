// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@150309 with change #14672031

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime;

@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface PropertyPreviewValue {
  /**
   Property name.
   */
  String name();

  /**
   Object type.
   */
  Type type();

  /**
   User-friendly property value string.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  String value();

  /**
   Nested value preview.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.ObjectPreviewValue valuePreview();

  /**
   Object subtype hint. Specified for <code>object</code> type values only.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Subtype subtype();

  /**
   Object type.
   */
  public enum Type {
    OBJECT,
    FUNCTION,
    UNDEFINED,
    STRING,
    NUMBER,
    BOOLEAN,
  }
  /**
   Object subtype hint. Specified for <code>object</code> type values only.
   */
  public enum Subtype {
    ARRAY,
    NULL,
    NODE,
    REGEXP,
    DATE,
  }
}
