// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.value;

import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.jsonObject;
import static org.eclipse.wst.jsdt.chromium.tests.internal.JsonBuilderUtil.jsonProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.SortedMap;

import org.eclipse.wst.jsdt.chromium.DebugContext;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.wst.jsdt.chromium.StandaloneVm;
import org.eclipse.wst.jsdt.chromium.internal.BrowserFactoryImplTestGate;
import org.eclipse.wst.jsdt.chromium.internal.browserfixture.FixtureChromeStub;
import org.eclipse.wst.jsdt.chromium.internal.browserfixture.StubListener;
import org.eclipse.wst.jsdt.chromium.internal.transport.ChromeStub;
import org.eclipse.wst.jsdt.chromium.internal.transport.FakeConnection;
import org.eclipse.wst.jsdt.chromium.internal.v8native.CallFrameImpl;
import org.eclipse.wst.jsdt.chromium.internal.v8native.ContextBuilder;
import org.eclipse.wst.jsdt.chromium.internal.v8native.InternalContext;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.FrameObject;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.V8ProtocolParserAccess;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ValueHandle;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Test;

/**
 * A test for the JsVariable implementor.
 */
public class JsArrayImplTest {

  private ChromeStub messageResponder;
  private CallFrameImpl callFrame;

  private ValueMirror arrayMirror;

  private final StubListener listener = new StubListener();

  @Before
  public void setUpBefore() throws Exception {
    this.messageResponder = new FixtureChromeStub();
    StandaloneVm standaloneVm = BrowserFactoryImplTestGate.createStandalone(
        new FakeConnection(messageResponder), FakeConnection.HANDSHAKER);
    standaloneVm.attach(listener);

    listener.expectSuspendedEvent();
    messageResponder.sendSuspendedEvent();
    DebugContext debugContext = listener.getDebugContext();

    String propertyRefText = "{'ref':" + FixtureChromeStub.getNumber3Ref() +
        ",'type':'number','value':3,'text':'3'}";

    InternalContext internalContext = ContextBuilder.getInternalContextForTests(debugContext);
    String valueHandleJsonText = (
            "{'protoObject':{'ref':55516,'className':'Array','type':'object'}," +
            "'text':'#<an Array>'," +
            "'handle':5559,'" +
            "constructorFunction':{'ref':55515,'inferredName':''," +
            "'name':'Array','type':'function'}," +
            "'prototypeObject':{'ref':5553,'type':'undefined'}," +
            "'className':'Array','properties':[{'name':'length'," +
            "'value':{'ref':55517,'value':2,'type':'number'}}," +
            "{'name':1,'value':" + propertyRefText + "}," +
            "{'name':3,'value':"+ propertyRefText +"}],'type':'object'}"
        ).replace('\'', '"');
    JSONObject valueHandleJson = (JSONObject) JSONValue.parse(valueHandleJsonText);
    ValueHandle valueHandle =
        V8ProtocolParserAccess.get().parseValueHandle(valueHandleJson);
    arrayMirror = internalContext.getValueLoader().addDataToMap(valueHandle);

    String proptoHandleJsonText = (
            "{'text':'#<an Object>', 'handle':55516,'className':'Object','type':'object'}"
         ).replace('\'', '"');
    JSONObject protoHandleJson = (JSONObject) JSONValue.parse(proptoHandleJsonText);
    ValueHandle protoHandle = V8ProtocolParserAccess.get().parseValueHandle(protoHandleJson);
    internalContext.getValueLoader().addDataToMap(protoHandle);



    FrameObject frameObject;
    {
      JSONObject jsonObject = jsonObject(
          jsonProperty("line", 12L),
          jsonProperty("index", 0L),
          jsonProperty("sourceLineText", ""),
          jsonProperty("script",
              jsonObject(
                  jsonProperty("ref", Long.valueOf(FixtureChromeStub.getScriptId()))
              )
          ),
          jsonProperty("func",
              jsonObject(
                  jsonProperty("name", "foofunction")
              )
          )
      );
      frameObject = V8ProtocolParserAccess.get().parseFrameObject(jsonObject);
    }

    this.callFrame = new CallFrameImpl(frameObject, internalContext);
  }

  @Test
  public void testArrayData() throws Exception {
    JsArrayImpl jsArray = new JsArrayImpl(callFrame.getInternalContext().getValueLoader(),
        arrayMirror);
    assertNotNull(jsArray.asArray());
    Collection<JsVariableBase.Property> properties = jsArray.getProperties();
    assertEquals(2 + 1, properties.size()); // 2 array element properties and one length property.
    assertEquals(4, jsArray.getLength());
    SortedMap<Long, ? extends JsVariable> sparseArray = jsArray.toSparseArray();
    assertEquals(2, sparseArray.size());
    JsVariable firstElement = sparseArray.get(1L);
    JsVariable thirdElement = sparseArray.get(3L);
    assertNull(jsArray.get(-1L));
    assertNull(jsArray.get(0L));
    assertEquals(firstElement, jsArray.get(1L));
    assertEquals("1", firstElement.getName());
    assertNull(jsArray.get(2L));
    assertEquals(thirdElement, jsArray.get(3L));
    assertEquals("3", thirdElement.getName());
    assertNull(jsArray.get(10L));
    checkElementData(firstElement);
    checkElementData(thirdElement);
  }

  private static void checkElementData(JsVariable arrayElement) {
    assertNotNull(arrayElement);
    JsValue value = arrayElement.getValue();
    assertEquals(JsValue.Type.TYPE_NUMBER, value.getType());
    assertEquals("3", value.getValueString());
  }
}
