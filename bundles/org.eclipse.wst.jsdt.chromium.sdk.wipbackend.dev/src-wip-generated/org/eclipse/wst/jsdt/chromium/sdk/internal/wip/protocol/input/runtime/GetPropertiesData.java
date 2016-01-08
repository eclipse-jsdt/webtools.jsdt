// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@130398

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime;

/**
 Returns properties of a given object. Object group of the result is inherited from the target object.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface GetPropertiesData {
  /**
   Object properties.
   */
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.PropertyDescriptorValue> result();

  /**
   Internal object properties.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  java.util.List<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.runtime.InternalPropertyDescriptorValue> internalProperties();

}
