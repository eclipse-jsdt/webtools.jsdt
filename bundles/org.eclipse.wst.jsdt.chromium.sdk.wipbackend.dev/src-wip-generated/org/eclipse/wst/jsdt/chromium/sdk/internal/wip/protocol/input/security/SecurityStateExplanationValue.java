// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.security;

/**
 An explanation of an factor contributing to the security state.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface SecurityStateExplanationValue {
  /**
   Security state representing the severity of the factor being explained.
   */
  org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.security.SecurityStateEnum securityState();

  /**
   Short phrase describing the type of factor.
   */
  String summary();

  /**
   Full text explanation of the factor.
   */
  String description();

  /**
   Associated certificate id.
   */
  @org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonOptionalField
  Long/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.CertificateIdTypedef*/ certificateId();

}
