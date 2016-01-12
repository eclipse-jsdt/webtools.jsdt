// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://src.chromium.org/blink/trunk/Source/devtools/protocol.json@<unknown>

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.network;

/**
 Subject of a certificate.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface CertificateSubjectValue {
  /**
   Certificate subject name.
   */
  String name();

  /**
   Subject Alternative Name (SAN) DNS names.
   */
  java.util.List<String> sanDnsNames();

  /**
   Subject Alternative Name (SAN) IP addresses.
   */
  java.util.List<String> sanIpAddresses();

}
