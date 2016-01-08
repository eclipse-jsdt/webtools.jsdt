// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@135591

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom;

/**
 Fired when <code>Document</code> has been totally updated. Node ids are no longer valid.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType(allowsOtherProperties=true)
public interface DocumentUpdatedEventData extends org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonObjectBased {
  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom.DocumentUpdatedEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom.DocumentUpdatedEventData>("DOM.documentUpdated", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom.DocumentUpdatedEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.dom.DocumentUpdatedEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parseDOMDocumentUpdatedEventData(obj);
    }
  };
}
