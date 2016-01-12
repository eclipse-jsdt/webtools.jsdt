// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom;

/**
 CSS Shape Outside details.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface ShapeOutsideInfoValue {
  /**
   Shape bounds
   */
  java.util.List<Number>/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom.QuadTypedef*/ bounds();

  /**
   Shape coordinate details
   */
  java.util.List<Object> shape();

  /**
   Margin shape bounds
   */
  java.util.List<Object> marginShape();

}
