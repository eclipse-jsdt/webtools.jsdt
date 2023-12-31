// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.browserfixture;

import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.convertToRealJson;
import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.jsonArray;
import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.jsonObject;
import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.jsonProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.wst.jsdt.chromium.Breakpoint;
import org.eclipse.wst.jsdt.chromium.JavascriptVm.BreakpointCallback;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.internal.FixtureParserAccess;
import org.eclipse.wst.jsdt.chromium.internal.JsonUtil;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParseMethod;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonParserRoot;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonSubtypeCasting;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonType;
import org.eclipse.wst.jsdt.chromium.internal.transport.ChromeStub;
import org.eclipse.wst.jsdt.chromium.internal.transport.Connection.NetListener;
import org.eclipse.wst.jsdt.chromium.internal.transport.Message;
import org.eclipse.wst.jsdt.chromium.internal.v8native.BreakpointImpl;
import org.eclipse.wst.jsdt.chromium.internal.v8native.BreakpointManager;
import org.eclipse.wst.jsdt.chromium.internal.v8native.DebuggerCommand;
import org.eclipse.wst.jsdt.chromium.internal.v8native.ScriptImplTestGate;
import org.eclipse.wst.jsdt.chromium.internal.v8native.ScriptManager;
import org.eclipse.wst.jsdt.chromium.internal.v8native.V8ContextFilter;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.V8Protocol;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.V8ProtocolParserAccess;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ContextHandle;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ScriptHandle;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.SomeHandle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * A ChromeStub implementation with a certain fixture applied.
 */
@SuppressWarnings("unchecked")
public class FixtureChromeStub implements ChromeStub {

  private static final Map<Long, Integer> scriptIdToScriptRefMap = new HashMap<Long, Integer>();

  private static final V8ContextFilter contextFilter = new V8ContextFilter() {
    @Override
    public boolean isContextOurs(ContextHandle contextHandle) {
      return true;
    }
  };

  private static final Map<Long, String> refToObjectMap;
  private static final Map<Long, String> refToFullVersionMap;
  static {
    refToObjectMap = new HashMap<Long, String>();
    refToFullVersionMap = new HashMap<Long, String>();

    // MouseEvent
    refToObjectMap.put(Long.valueOf(getMouseEventRef()),
        "{\"handle\":" + getMouseEventRef() + "," +
        "\"type\":\"object\",\"className\":\"MouseEvent\"," +
        "\"constructorFunction\":{\"ref\":19},\"protoObject\":{\"ref\":73}," +
        "\"prototypeObject\":{\"ref\":2},\"properties\":[" +
        "{\"name\":\"x\",\"propertyType\":3,\"ref\":" + getNumber3Ref() + "}," +
        "{\"name\":\"y\",\"propertyType\":3,\"ref\":" + getNumber3Ref() + "}]," +
        "\"text\":\"#<an Object>\"}");

    // Fake constructor function handle.
    refToObjectMap.put(19L, convertToRealJson("{'handle':19,'type':'null','text':'null'}"));
    // Fake proto object handle.
    refToObjectMap.put(73L, convertToRealJson("{'handle':73,'type':'null','text':'null'}"));

    // Script
    refToObjectMap.put(Long.valueOf(getScriptRef()),
        jsonObject(
            jsonProperty("handle", getScriptRef()),
            jsonProperty("type", "script"),
            jsonProperty("name", "file:///C:/1.js"),
            jsonProperty("id", getScriptId()),
            jsonProperty("lineOffset", 0),
            jsonProperty("columnOffset", 0),
            jsonProperty("lineCount", 32),
            jsonProperty("source",
                "SomeObject = function() {\r\n  this.fieldOne = \"One\";\r\n};\r\n\r\n"
                + "SomeObject.prototype.methodTwo = function() {\r\n  alert(this.fieldOne);"
                + "\r\n}\r\n\r\n\r\nfunction clicked() {\r\n  var obj = {\r\n"
                + "    objField : { internalObj : { intField : 1}, simpleString : \"foo\" },\r\n"
                + "    someNumber : 3\r\n  };\r\n  var a = 1;\r\n\r\n  var arr;\r\n"
                + "  arr = [\"foo\", 3, new Date(), obj, arr];\r\n"
                + "  for (var i = 5; i < 240; i += 2) {\r\n    arr[i] = \"bar\";\r\n  }\r\n"
                + "  arr[100] = 0.99999887;\r\n//  var b = a + 1\r\n//  console.log('Foo');\r\n"
                + "  anotherScript();\r\n}\r\n\r\nfunction anotherScript() {\r\n  var i = 0;\r\n"
                + "  i += 2;\r\n  var someObj = new SomeObject();\r\n}\r\n"
            ),
            jsonProperty("sourceLength", 595),
            jsonProperty("type", "script"),
            jsonProperty("type", "script"),
            jsonProperty("scriptType", 2),
            jsonProperty("context", jsonObject(jsonProperty("ref", 0))),
            jsonProperty("text", "file:///C:/1.js (lines: 32)"),
            jsonProperty("compilationType", 17) // a random value
            ).toJSONString()
        );
    // Compiled Script
    refToObjectMap.put(Long.valueOf(getCompiledScriptRef()),
        jsonObject(
            jsonProperty("handle", getCompiledScriptRef()),
            jsonProperty("type", "script"),
            jsonProperty("name", "file:///C:/2.js"),
            jsonProperty("id", getCompiledScriptId()),
            jsonProperty("lineOffset", 0),
            jsonProperty("columnOffset", 0),
            jsonProperty("lineCount", 32),
            jsonProperty("source",
                "SomeObject = function() {\r\n  this.fieldOne = \"One\";\r\n};" +
                "\r\n\r\nSomeObject.prototype.methodTwo = function() {\r\n  alert(this.fieldOne);" +
                "\r\n}\r\n\r\n\r\nfunction compiled() {\r\n  var obj = {\r\n" +
                "    compiled : { internalObj : { intField : 1}, simpleString : \"foo\" },\r\n" +
                "    someNumber : 3\r\n  };\r\n  var a = 1;\r\n\r\n  var arr;\r\n" +
                "  arr = [\"foo\", 3, new Date(), obj, arr];\r\n" +
                "  for (var i = 5; i < 240; i += 2) {\r\n    arr[i] = \"bar\";\r\n  }\r\n" +
                "  arr[100] = 0.99999887;\r\n//  var b = a + 1\r\n//  console.log('Foo');\r\n" +
                "  anotherScript();\r\n}\r\n\r\nfunction anotherScript() {\r\n  var i = 0;\r\n" +
                "  i += 2;\r\n  var someObj = new SomeObject();\r\n}\r\n"
            ),
            jsonProperty("sourceLength", 595),
            jsonProperty("scriptType", 2),
            jsonProperty("context", jsonObject(jsonProperty("ref", 0))),
            jsonProperty("text", "file:///C:/2.js (lines: 32)"),
            jsonProperty("compilationType", 17) // a random value
        ).toJSONString()
        );
    // Function
    refToObjectMap.put(Long.valueOf(getFunctionRef()),
        "{\"handle\":" + getFunctionRef() + ",\"type\":\"function\",\"className\":\"Function\"," +
        "\"constructorFunction\":{\"ref\":31},\"protoObject\":{\"ref\":32}," +
        "\"prototypeObject\":{\"ref\":33},\"name\":\"clicked\",\"inferredName\":\"\"," +
        "\"resolved\":true,\"source\":\"function clicked() {\\r\\n  var obj = {\\r\\n" +
        "    objField : { internalObj : { intField : 1}, simpleString : \\\"foo\\\" },\\r\\n" +
        "    someNumber : 3\\r\\n  };\\r\\n  var a = 1;\\r\\n\\r\\n  var arr;\\r\\n" +
        "  arr = [\\\"foo\\\", 3, new Date(), obj, arr];\\r\\n" +
        "  for (var i = 5; i < 240; i += 2) {\\r\\n    arr[i] = \\\"bar\\\";\\r\\n  }\\r\\n" +
        "  arr[100] = 0.99999887;\\r\\n//  var b = a + 1\\r\\n//  console.log('Foo');\\r\\n" +
        "  anotherScript();\\r\\n}\",\"script\":{\"ref\":" + getScriptRef() +
        "},\"properties\":[{\"name\":\"name\"," +
        "\"attributes\":7,\"propertyType\":3,\"ref\":34},{\"name\":\"caller\",\"attributes\":7," +
        "\"propertyType\":3,\"ref\":11},{\"name\":\"length\",\"attributes\":7,\"propertyType\":3," +
        "\"ref\":35},{\"name\":\"arguments\",\"attributes\":7,\"propertyType\":3,\"ref\":327}," +
        "{\"name\":\"prototype\",\"attributes\":4,\"propertyType\":3,\"ref\":33}]," +
        "\"text\":\"function clicked() {\\r\\n  var obj = {\\r\\n    objField : { internalObj :" +
        " { intField : 1}, simpleString : \\\"foo\\\" },\\r\\n    someNumber : 3\\r\\n  };\\r\\n" +
        "  var a = 1;\\r\\n\\r\\n  var arr;\\r\\n  arr = [\\\"foo\\\", 3, new Date(), obj, arr];" +
        "\\r\\n  for (var i = 5; i < 240; i += 2) {\\r\\n    arr[i] = \\\"bar\\\";\\r\\n  }\\r\\n" +
        "  arr[100] = 0.99999887;\\r\\n//  var b = a + 1\\r\\n//  console.log('Foo');\\r\\n" +
        "  anotherScript();\\r\\n}\"}");
    // Number "3"
    refToObjectMap.put(Long.valueOf(getNumber3Ref()),
        "{\"handle\":" + getNumber3Ref() + ",\"type\":\"number\",\"value\":3,\"text\":\"3\"}");


    refToObjectMap.put(Long.valueOf(getLongValueRef()),
        LongValue.createHandle(true).toJSONString());
    refToFullVersionMap.put(Long.valueOf(getLongValueRef()),
        LongValue.createHandle(false).toJSONString());

    scriptIdToScriptRefMap.put(Long.valueOf(getScriptId()), getScriptRef());
    scriptIdToScriptRefMap.put(Long.valueOf(getCompiledScriptId()), getCompiledScriptRef());
  }

  private final ScriptManager scriptManager = ScriptImplTestGate.create(contextFilter);

  public FixtureChromeStub() {
    JSONObject body = getJsonObjectByRef(getScriptRef());
    ScriptHandle scriptsNormalBody;
    try {
      scriptsNormalBody = V8ProtocolParserAccess.get().parseScriptHandle(body);
    } catch (JsonProtocolParseException e) {
      throw new RuntimeException(e);
    }
    ScriptImplTestGate.addScript(scriptManager, scriptsNormalBody, constructScriptRefsTyped());
  }

  public static int getNumber3Ref() {
    return 65;
  }

  public static int getLongValueRef() {
    return 66;
  }

  private static int getCompiledScriptRef() {
    return 6;
  }

  public static int getFunctionRef() {
    return 0;
  }

  public static int getScriptRef() {
    return 5;
  }

  public static Long getScriptId() {
    return Long.valueOf(566);
  }

  public static Long getCompiledScriptId() {
    return Long.valueOf(567);
  }

  public static int getMouseEventRef() {
    return 1;
  }

  private final Map<Long, BreakpointImpl> breakpoints = new HashMap<Long, BreakpointImpl>();
  private boolean isRunning = true;
  private NetListener listener;
  private static long breakpointCounter = 1;
  private static long seqCounter = 1;

  private static final BreakpointManager NULL_BREAKPOINT_MANAGER =
    new BreakpointManager(null) {

      @Override
      public RelayOk changeBreakpoint(BreakpointImpl breakpointImpl, BreakpointCallback callback,
          SyncCallback syncCallback) {
        throw new UnsupportedOperationException();
      }

      @Override
      public RelayOk clearBreakpoint(BreakpointImpl breakpointImpl, BreakpointCallback callback,
          SyncCallback syncCallback, long originalId) {
        throw new UnsupportedOperationException();
      }

      @Override
      public RelayOk setBreakpoint(Breakpoint.Target target, int line, int position,
          boolean enabled, String condition, BreakpointCallback callback,
          SyncCallback syncCallback) {
        throw new UnsupportedOperationException();
      }
    };

  private static long nextBreakpointId() {
    return breakpointCounter++;
  }

  private static long nextSeq() {
    return seqCounter++;
  }

  @Override
  public Message respondTo(Message requestMessage) {
    // needs request_seq and command at the end
    JSONObject data;
    try {
      data = JsonUtil.jsonObjectFromJson(requestMessage.getContent());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    String response = null;
    boolean success = true;
    // TODO: remove the block, that was introduced to preserve indentation.
    {
      JSONObject responseMessage = new JSONObject();
      Map<String, Object> nameToJsonValue = new HashMap<String, Object>();
      Long seq = JsonUtil.getAsLong(data, V8Protocol.KEY_SEQ);
      String debuggerCommandString = JsonUtil.getAsString(data, V8Protocol.KEY_COMMAND);
      DebuggerCommand debuggerCommand = DebuggerCommand.forString(debuggerCommandString);
      JSONObject args = JsonUtil.getAsJSON(data, "arguments");
      switch (debuggerCommand) {
        case LOOKUP:
          {
            JSONArray handles = JsonUtil.getAsJSONArray(args, "handles");
            JSONObject jsonBody = putJsonValue("body", new JSONObject(), nameToJsonValue);
            for (int i = 0; i < handles.size(); i++) {
              Long ref = (Long) handles.get(i);
              String objectData = refToObjectMap.get(ref);
              if (args.get("maxStringLength") != null ) {
                String fullVersion = refToFullVersionMap.get(ref);
                if (fullVersion != null) {
                  objectData = fullVersion;
                }
              }
              if (objectData != null) {
                try {
                  jsonBody.put(String.valueOf(ref), JsonUtil.jsonObjectFromJson(objectData));
                } catch (ParseException e) {
                  throw new RuntimeException(e);
                }
              } else {
                success = false;
              }
            }
            JSONArray jsonRefs = putJsonValue("refs", new JSONArray(), nameToJsonValue);
            jsonRefs.add(getJsonObjectByRef(getNumber3Ref()));
          }
          break;
        case EVALUATE:
          success = handleEvaluate(args, nameToJsonValue);
          break;
        case SETBREAKPOINT:
        case CHANGEBREAKPOINT:
        {
          long id = (debuggerCommand == DebuggerCommand.SETBREAKPOINT)
              ? nextBreakpointId()
              : JsonUtil.getAsLong(args, "breakpoint");
          Breakpoint.Target target = new Breakpoint.Target.ScriptName("abcde.js");
          breakpoints.put(id, new FakeBreakpoint(id, target,
              JsonUtil.getAsBoolean(args, "enabled"),
              JsonUtil.getAsString(args, "condition")));
          JSONObject jsonBody = putJsonValue("body", new JSONObject(), nameToJsonValue);
          jsonBody.put("type", "script");
          jsonBody.put("breakpoint", id);
          break;
        }
        case CLEARBREAKPOINT: {
          long id = JsonUtil.getAsLong(args, "breakpoint");
          breakpoints.remove(id);
          JSONObject jsonBody = putJsonValue("body", new JSONObject(), nameToJsonValue);
          jsonBody.put("type", "script");
          jsonBody.put("breakpoint", id);
          break;
        }
        case CONTINUE:
          isRunning = true;
          break;
        case BACKTRACE:
          JSONObject jsonBody = putJsonValue("body", new JSONObject(), nameToJsonValue);
          constructBacktrace(responseMessage, jsonBody);
          break;
        case SCRIPTS:
          nameToJsonValue.put("body", constructScripts(JsonUtil.getAsJSONArray(args, "ids")));
          nameToJsonValue.put("refs", constructScriptRefsJson());
          break;
        case SOURCE:
          //constructSource();
          break;
        case SCOPE:
          constructScopeResponse(nameToJsonValue);
          break;
        default:
          success = false;
      }
      responseMessage.put("seq", nextSeq());
      responseMessage.put("request_seq", seq);
      responseMessage.put("command", debuggerCommandString);
      responseMessage.put("type", "response");
      if (success) {
        responseMessage.put("success", true);
        responseMessage.put("running", isRunning);
        for (Entry<String, Object> entry : nameToJsonValue.entrySet()) {
          responseMessage.put(entry.getKey(), entry.getValue());
        }
      } else {
        responseMessage.put("success", false);
        responseMessage.put("message", "An error occurred");
      }
      response = responseMessage.toJSONString();
    }
    return new Message(Collections.<String, String>emptyMap(), response);
  }

  @Override
  public void sendSuspendedEvent() {
    String response = "{\"seq\":" + nextSeq() + ",\"type\":\"event\",\"event\":\"break\","
        + "\"body\":{\"invocationText\":\"wasteCpu();\",\"sourceLine\":25,\"sourceColumn\":4,"
        + "\"sourceLineText\":\"    debugger;\",\"script\":{\"id\":11,\"name\":\"samples/test.js\","
        + "\"lineOffset\":0,\"columnOffset\":0,\"lineCount\":36}}}";
    Message message = new Message(Collections.<String, String>emptyMap(), response);
    listener.messageReceived(message);
  }

  private <T> T putJsonValue(String name, T value, Map<String, Object> targetMap) {
    targetMap.put(name, value);
    return value;
  }

  private JSONArray constructScriptRefsJson() {
    return jsonArray(jsonObject(
        jsonProperty("text", "#<a ContextMirror>"),
        jsonProperty("handle", 0L), // must match the context ref in the script object
        jsonProperty("type", "context"),
        jsonProperty("data", jsonObject(
            jsonProperty("value", 1L),
            jsonProperty("type", "page")
            ))
        ));
  }

  private List<SomeHandle> constructScriptRefsTyped() {
    JSONArray refs = constructScriptRefsJson();
    try {
      return FixtureParserAccess.get().parseRefs(refs).asHandles();
    } catch (JsonProtocolParseException e) {
      throw new RuntimeException(e);
    }
  }

  private JSONArray constructScripts(JSONArray ids) {
    JSONArray scripts = new JSONArray();
    for (Script script : scriptManager.allScripts()) {
      Object id = script.getId();
      if (ids == null || ids.contains(id)) {
        scripts.add(getJsonObjectByRef(scriptIdToScriptRefMap.get(script.getId())));
      }
    }
    return scripts;
  }

  private void constructBacktrace(JSONObject response, JSONObject responseBody) {
    response.put("refs", getRefs(getScriptRef(), getFunctionRef()));
    responseBody.put("fromFrame", 0);
    responseBody.put("toFrame", 1);
    responseBody.put("totalFrames", 1);
    JSONArray frames = new JSONArray();
    JSONObject frame = new JSONObject();
    frame.put("type", "frame");
    frame.put("index", 0);
    frame.put("receiver", getReceiver());
    frame.put("func", getFunc());
    frame.put("script", getScript());
    frame.put("constructCall", false);
    frame.put("debuggerFrame", false);
    frame.put("arguments", new JSONArray());
    frame.put("locals", getLocalsArray());
    frame.put("scopes", getScopesArray());
    frame.put("position", 305);
    frame.put("line", 18);
    frame.put("column", 3);
    frame.put("sourceLineText", "   foo = bar;");
    frame.put("text", "#00 clicked() file:///C:/1.js line 18 column 3 (position 305)");
    frames.add(frame);
    responseBody.put("frames", frames);
  }

  private void constructScopeResponse(Map<String, Object> nameToJsonValue) {
    nameToJsonValue.put("body", jsonObject(
        jsonProperty("type", 1),
        jsonProperty("index", 0),
        jsonProperty("frameIndex", 0),
        jsonProperty("object",
            jsonObject(
                jsonProperty("handle", -1),
                jsonProperty("type", "object"),
                jsonProperty("className", "Object"),
                jsonProperty("constructorFunction", jsonObject(
                    "'ref':20,'type':'function','name':'Object','inferredName':''")),
                jsonProperty("protoObject", jsonObject(
                  "'ref':21,'type':'object','className':'Object'")),
                jsonProperty("prototypeObject", jsonObject("'ref':2,'type':'undefined'")),
                jsonProperty("properties",
                    jsonArray(
                        jsonObject("'name':'x','value':{'ref':" + getMouseEventRef() +
                            ",'type':'object','className':'MouseEvent'}"),
                        jsonObject("'name':'y','value':{'ref':" + getNumber3Ref() +
                            ",'type':'number','value':3}")
                    )
                ),
                jsonProperty("text", "#<an Object>")
            )
        ),
        jsonProperty("text", "#<a ScopeMirror>")
    ));
    nameToJsonValue.put("refs", getRefs(getNumber3Ref(), getMouseEventRef()));
  }

  private boolean handleEvaluate(JSONObject args, Map<String, Object> nameToJsonValue) {
    String expression = JsonUtil.getAsString(args, "expression");
    if (!"#long_value".equals(expression)) {
      return false;
    }
    nameToJsonValue.put("body", LongValue.createHandle(true));
    return true;
  }

  private static class LongValue {
    static final String FULL_VALUE = "A very very very very very very very long value";
    static final int LENGTH_LIMIT = 10;

    static JSONObject createHandle(boolean truncated) {
      if (truncated) {
        String truncatedValue = LongValue.FULL_VALUE.substring(0, LongValue.LENGTH_LIMIT) + " ...";
        return jsonObject(
            jsonProperty("handle", getLongValueRef()),
            jsonProperty("type", "string"),
            jsonProperty("value", truncatedValue),
            jsonProperty("text", truncatedValue),
            jsonProperty("fromIndex", 0),
            jsonProperty("toIndex", LongValue.LENGTH_LIMIT),
            jsonProperty("length", LongValue.FULL_VALUE.length())
        );
      } else {
        return jsonObject(
            jsonProperty("handle", getLongValueRef()),
            jsonProperty("type", "string"),
            jsonProperty("value", LongValue.FULL_VALUE),
            jsonProperty("text", LongValue.FULL_VALUE)
        );
      }
    }
  }


  private JSONArray getRefs(int ... refParams) {
    JSONObject [] refHandleObjects = new JSONObject [refParams.length];
    for (int i = 0; i < refParams.length; i++) {
      refHandleObjects[i] = getJsonObjectByRef(refParams[i]);
    }
    return jsonArray(refHandleObjects);
  }

  private JSONObject getJsonObjectByRef(int ref) {
    String jsonText = refToObjectMap.get(Long.valueOf(ref));
    try {
      return JsonUtil.jsonObjectFromJson(jsonText);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private JSONArray getLocalsArray() {
    return jsonArray(
        jsonObject(
            jsonProperty("name", "a"),
            jsonProperty("value",
                jsonObject(
                    jsonProperty("ref", getNumber3Ref()),
                    jsonProperty("type", "number"),
                    jsonProperty("value", 1)
                )
            )
        )
    );
  }

  private JSONArray getScopesArray() {
    return jsonArray(
        jsonObject(
            jsonProperty("type", 0),
            jsonProperty("index", 0)
        )
    );
  }

  private JSONObject getScript() {
    JSONObject script = new JSONObject();
    script.put("ref", getScriptRef());
    return script;
  }

  private JSONObject getFunc() {
    JSONObject func = new JSONObject();
    func.put("ref", getFunctionRef());
    func.put("type", "function");
    func.put("name", "clicked");
    func.put("scriptId", getScriptId());
    return func;
  }

  private JSONObject getReceiver() {
    JSONObject receiver = new JSONObject();
    receiver.put("ref", getMouseEventRef());
    receiver.put("type", "object");
    receiver.put("className", "global");
    return receiver;
  }

  public void sendEvent(Message eventMessage) {
    listener.messageReceived(eventMessage);
  }

  public void hitBreakpoints(Collection<Long> breakpointIds) {
    isRunning = false;
    JSONObject eventObject = new JSONObject();
    eventObject.put("seq", nextSeq());
    eventObject.put("type", "event");
    eventObject.put("event", "break");
    JSONObject body = new JSONObject();
    JSONArray bps = new JSONArray();
    bps.addAll(breakpointIds);
    body.put("breakpoints", bps);
    // TODO: add other data if needed
    eventObject.put("body", body);
    sendEvent(createMessage(eventObject.toJSONString()));
  }

  public void sendAfterCompile() {
    JSONObject scriptsObject = getJsonObjectByRef(getCompiledScriptRef());
    ScriptHandle scriptsNormalBody;
    try {
      scriptsNormalBody = V8ProtocolParserAccess.get().parseScriptHandle(scriptsObject);
    } catch (JsonProtocolParseException e) {
      throw new RuntimeException(e);
    }
    ScriptImplTestGate.addScript(scriptManager, scriptsNormalBody, constructScriptRefsTyped());

    JSONObject scriptObject = getJsonObjectByRef(getCompiledScriptRef());
    scriptObject.remove("source");

    JSONObject afterCompileObject = jsonObject(
        jsonProperty("seq", nextSeq()),
        jsonProperty("type", "event"),
        jsonProperty("event", "afterCompile"),
        jsonProperty("success", true),
        jsonProperty("body", jsonObject(jsonProperty("script", scriptObject))),
        jsonProperty("refs", constructScriptRefsJson())
        );

    sendEvent(createMessage(afterCompileObject.toJSONString()));
  }

  private Message createMessage(String content) {
    return new Message(Collections.<String, String>emptyMap(), content);
  }

  private static class FakeBreakpoint extends BreakpointImpl {
    public FakeBreakpoint(long id, Target target, boolean enabled, String condition) {
      super(id, target, 15, enabled, condition, NULL_BREAKPOINT_MANAGER);
    }
  }

  @Override
  public void setNetListener(NetListener listener) {
    this.listener = listener;
  }

  @JsonType(subtypesChosenManually=true)
  public interface Refs {
    @JsonSubtypeCasting
    List<SomeHandle> asHandles() throws JsonProtocolParseException;
    @JsonSubtypeCasting
    List<? extends SomeHandle> asHandles2() throws JsonProtocolParseException;
  }

  @JsonParserRoot
  public interface FixtureParser {

    @JsonParseMethod
    Refs parseRefs(Object refs) throws JsonProtocolParseException;

  }
}
