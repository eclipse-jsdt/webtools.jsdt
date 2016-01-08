// Generated source.
// Generator: org.eclipse.wst.jsdt.chromium.sdk.internal.wip.tools.protocolgenerator.Generator
// Origin: http://svn.webkit.org/repository/webkit/trunk/Source/WebCore/inspector/Inspector.json@142888

package org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page;

/**
 Fired when frame schedules a potential navigation.
 */
@org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonType
public interface FrameScheduledNavigationEventData {
  /**
   Id of the frame that has scheduled a navigation.
   */
  String/*See org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.common.network.FrameIdTypedef*/ frameId();

  /**
   Delay (in seconds) until the navigation is scheduled to begin. The navigation is not guaranteed to start.
   */
  Number delay();

  public static final org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameScheduledNavigationEventData> TYPE
      = new org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipEventType<org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameScheduledNavigationEventData>("Page.frameScheduledNavigation", org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameScheduledNavigationEventData.class) {
    @Override public org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.page.FrameScheduledNavigationEventData parse(org.eclipse.wst.jsdt.chromium.sdk.internal.wip.protocol.input.WipGeneratedParserRoot parser, org.json.simple.JSONObject obj) throws org.eclipse.wst.jsdt.chromium.sdk.internal.protocolparser.JsonProtocolParseException {
      return parser.parsePageFrameScheduledNavigationEventData(obj);
    }
  };
}
