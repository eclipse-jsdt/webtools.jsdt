// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import static org.eclipse.wst.jsdt.chromium.util.BasicUtil.getSafe;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import org.eclipse.wst.jsdt.chromium.JsArray;
import org.eclipse.wst.jsdt.chromium.JsDeclarativeVariable;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext.EvaluateCallback;
import org.eclipse.wst.jsdt.chromium.JsFunction;
import org.eclipse.wst.jsdt.chromium.JsObject;
import org.eclipse.wst.jsdt.chromium.JsObjectProperty;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsValue.Type;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.TextStreamPosition;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.ObjectPropertyNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.PropertyNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.QualifiedNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.ValueNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipValueLoader.Getter;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipValueLoader.ObjectProperties;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.LocationValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.PropertyDescriptorValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.RemoteObjectValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.runtime.CallArgumentParam;
import org.eclipse.wst.jsdt.chromium.util.AsyncFutureRef;
import org.eclipse.wst.jsdt.chromium.util.JavaScriptExpressionBuilder;
import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * A builder for implementations of {@link JsValue} and {@link JsVariable}.
 * It works in pair with {@link WipValueLoader}.
 */
class WipValueBuilder {
  private static final Logger LOGGER = Logger.getLogger(WipValueBuilder.class.getName());

  private final WipValueLoader valueLoader;

  WipValueBuilder(WipValueLoader valueLoader) {
    this.valueLoader = valueLoader;
  }

  /**
   * Value that can serialize itself for sending back to server.
   */
  interface SerializableValue {
    CallArgumentParam createCallArgumentParam();
    /**
     * Ref id is directly used in {@link EvaluateHack} because sometimes protocol requires
     * it for CallFunctionOn.
     * @return ref id or null
     */
    String getRefId();

    class Util {
      public static SerializableValue wrapRefId(final String refId) {
        return new SerializableValue() {
          @Override public CallArgumentParam createCallArgumentParam() {
            return new CallArgumentParam(false, null, refId);
          }
          @Override public String getRefId() {
            return refId;
          }
        };
      }
    }
  }

  static abstract class JsValueBase implements JsValue, SerializableValue {
    static JsValueBase cast(JsValue value) {
      if (false == value instanceof JsValueBase) {
        throw new IllegalArgumentException("Incorrect argument type " + value.getClass());
      }
      return (JsValueBase) value;
    }
  }

  public JsObjectProperty createObjectProperty(final PropertyDescriptorValue propertyDescriptor,
      final String hostObjectRefId, ValueNameBuilder nameBuilder) {
    final QualifiedNameBuilder qualifiedNameBuilder = nameBuilder.getQualifiedNameBuilder();
    JsValue jsValue = wrap(propertyDescriptor.value(), qualifiedNameBuilder);

    final JsValue getter = wrapPropertyDescriptorFunction(propertyDescriptor.get(),
        qualifiedNameBuilder, "getter");

    final JsValue setter = wrapPropertyDescriptorFunction(propertyDescriptor.set(),
        qualifiedNameBuilder, "setter");

    return new ObjectPropertyBase(jsValue, nameBuilder) {
      @Override public JsDeclarativeVariable asDeclarativeVariable() {
        return null;
      }
      @Override public boolean isWritable() {
        return propertyDescriptor.writable() == Boolean.TRUE;
      }
      @Override public JsValue getGetter() {
        return getter;
      }
      @Override public JsValue getSetter() {
        return setter;
      }
      @Override public boolean isConfigurable() {
        return propertyDescriptor.configurable() == Boolean.TRUE;
      }
      @Override public boolean isEnumerable() {
        return propertyDescriptor.enumerable() == Boolean.TRUE;
      }

      @Override
      public JsFunction getGetterAsFunction() {
        JsObject getterObject = getter.asObject();
        if (getterObject == null) {
          return null;
        }
        return getterObject.asFunction();
      }

      @Override
      public RelayOk evaluateGet(EvaluateCallback callback, SyncCallback syncCallback) {
        WipContextBuilder.GlobalEvaluateContext evaluateContext =
            new WipContextBuilder.GlobalEvaluateContext(valueLoader);

        JsFunction getterFunction = getGetterAsFunction();
        if (getterFunction == null) {
          throw new RuntimeException("Getter is not a function");
        }

        Map<String, SerializableValue> context = new HashMap<String, SerializableValue>(2);
        context.put(GETTER_VAR_NAME, (SerializableValue) getterFunction);
        context.put(OBJECT_VAR_NAME, SerializableValue.Util.wrapRefId(hostObjectRefId));
        final QualifiedNameBuilder pseudoPropertyNameBuilder =
            createPseudoPropertyNameBuilder(qualifiedNameBuilder, "value");
        ValueNameBuilder valueNameBuilder = new ValueNameBuilder() {
          @Override
          public String getShortName() {
            return "value";
          }

          @Override
          public QualifiedNameBuilder getQualifiedNameBuilder() {
            return pseudoPropertyNameBuilder;
          }
        };

        return evaluateContext.evaluateAsyncImpl(EVALUATE_EXPRESSION, valueNameBuilder,
            context, valueLoader, callback, syncCallback);
      }
      private static final String GETTER_VAR_NAME = "gttr";
      private static final String OBJECT_VAR_NAME = "obj";
      private static final String EVALUATE_EXPRESSION =
          GETTER_VAR_NAME + ".call(" + OBJECT_VAR_NAME + ")";
    };
  }

  private static QualifiedNameBuilder createPseudoPropertyNameBuilder(
      final QualifiedNameBuilder propertyValueNameBuilder, final String symbolicName) {
    return new QualifiedNameBuilder() {
      @Override public boolean needsParentheses() {
        return false;
      }

      @Override
      public void append(StringBuilder output) {
        propertyValueNameBuilder.append(output);
        output.append("::[[").append(symbolicName).append("]]");
      }
    };
  }

  private JsValue wrapPropertyDescriptorFunction(RemoteObjectValue value,
      QualifiedNameBuilder propertyValueNameBuilder, String symbolicName) {
    if (value == null) {
      return null;
    }

    QualifiedNameBuilder qualifiedNameBuilder =
        createPseudoPropertyNameBuilder(propertyValueNameBuilder, symbolicName);

    return wrap(value, qualifiedNameBuilder);
  }

  public JsVariable createVariable(RemoteObjectValue valueData, ValueNameBuilder nameBuilder) {
    QualifiedNameBuilder qualifiedNameBuilder;
    if (nameBuilder == null) {
      qualifiedNameBuilder = null;
    } else {
      qualifiedNameBuilder = nameBuilder.getQualifiedNameBuilder();
    }
    JsValue jsValue = wrap(valueData, qualifiedNameBuilder);
    return createVariable(jsValue, nameBuilder);
  }

  public JsDeclarativeVariable createDeclarativeVariable(RemoteObjectValue valueData,
      ValueNameBuilder nameBuilder) {
    QualifiedNameBuilder qualifiedNameBuilder;
    if (nameBuilder == null) {
      qualifiedNameBuilder = null;
    } else {
      qualifiedNameBuilder = nameBuilder.getQualifiedNameBuilder();
    }
    JsValue jsValue = wrap(valueData, qualifiedNameBuilder);
    return new DeclarativeVariable(jsValue, nameBuilder);
  }

  public JsValue wrap(RemoteObjectValue valueData, QualifiedNameBuilder nameBuilder) {
    if (valueData == null) {
      return null;
    }
    return getValueType(valueData).build(valueData, valueLoader, nameBuilder);
  }

  public static JsVariable createVariable(JsValue jsValue,
      ValueNameBuilder nameBuilder) {
    return new VariableImpl(jsValue, nameBuilder);
  }

  private static ValueType getValueType(RemoteObjectValue valueData) {
    RemoteObjectValue.Type protocolType = valueData.type();
    ValueType result = getSafe(PROTOCOL_TYPE_TO_VALUE_TYPE, protocolType);

    if (result == null) {
      LOGGER.severe("Unexpected value type: " + protocolType);
      result = DEFAULT_VALUE_TYPE;
    }
    return result;
  }

  private static abstract class ValueType {
    abstract JsValue build(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder qualifiedNameBuilder);
  }

  private static abstract class PrimitiveType extends ValueType {
    private final JsValue.Type jsValueType;

    PrimitiveType(JsValue.Type jsValueType) {
      this.jsValueType = jsValueType;
    }

    protected abstract String getValueString(RemoteObjectValue valueData);

    @Override
    JsValue build(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder qualifiedNameBuilder) {
      final Object value = valueData.value();
      final String valueString = getValueString(valueData);
      return new JsValueBase() {
        @Override public Type getType() {
          return jsValueType;
        }
        @Override public String getValueString() {
          return valueString;
        }
        @Override public JsObject asObject() {
          return null;
        }
        @Override public boolean isTruncated() {
          return false;
        }
        @Override public String getRefId() {
          return null;
        }
        @Override public RelayOk reloadHeavyValue(ReloadBiggerCallback callback,
            SyncCallback syncCallback) {
          throw new UnsupportedOperationException();
        }
        @Override
        public CallArgumentParam createCallArgumentParam() {
          if (jsValueType == JsValue.Type.TYPE_NULL) {
            return new CallArgumentParam(true, null, null);
          } else if (jsValueType == JsValue.Type.TYPE_UNDEFINED) {
            return new CallArgumentParam(false, null, null);
          } else {
            return new CallArgumentParam(true, value, null);
          }
        }
      };
    }
  }

  private static class SingletonPrimitiveType extends PrimitiveType {
    private final String stringValue;

    SingletonPrimitiveType(Type jsValueType, String stringValue) {
      super(jsValueType);
      this.stringValue = stringValue;
    }

    @Override
    protected String getValueString(RemoteObjectValue valueData) {
      return stringValue;
    }
  }

  private static class PrimitiveTypeWithDescription extends PrimitiveType {
    PrimitiveTypeWithDescription(Type jsValueType) {
      super(jsValueType);
    }

    @Override
    protected String getValueString(RemoteObjectValue valueData) {
      return valueData.description();
    }
  }

  private static class PrimitiveTypeWithValue extends PrimitiveType {
    PrimitiveTypeWithValue(Type jsValueType) {
      super(jsValueType);
    }

    @Override
    protected String getValueString(RemoteObjectValue valueData) {
      return valueData.value().toString();
    }
  }


  private static abstract class ObjectTypeBase extends ValueType {
    private final JsValue.Type jsValueType;

    ObjectTypeBase(Type jsValueType) {
      this.jsValueType = jsValueType;
    }

    @Override
    JsValue build(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder qualifiedNameBuilder) {
      // TODO: Implement caching here.
      return buildNewInstance(valueData, valueLoader, qualifiedNameBuilder);
    }

    abstract JsValue buildNewInstance(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder qualifiedNameBuilder);

    abstract class JsObjectBase extends JsValueBase implements JsObject {
      private final RemoteObjectValue valueData;
      private final WipValueLoader valueLoader;
      private final QualifiedNameBuilder nameBuilder;
      private final AsyncFutureRef<Getter<ObjectProperties>> loadedPropertiesRef =
          new AsyncFutureRef<Getter<ObjectProperties>>();

      JsObjectBase(RemoteObjectValue valueData, WipValueLoader valueLoader,
          QualifiedNameBuilder nameBuilder) {
        this.valueData = valueData;
        this.valueLoader = valueLoader;
        this.nameBuilder = nameBuilder;
      }

      @Override
      public Type getType() {
        return jsValueType;
      }

      @Override
      public String getValueString() {
        return valueData.description();
      }

      @Override
      public JsObject asObject() {
        return this;
      }

      @Override
      public boolean isTruncated() {
        return false;
      }

      @Override
      public String getClassName() {
        return valueData.className();
      }

      @Override
      public RelayOk reloadHeavyValue(ReloadBiggerCallback callback,
          SyncCallback syncCallback) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Collection<? extends JsObjectProperty> getProperties()
          throws MethodIsBlockingException {
        return getLoadedProperties().properties();
      }

      @Override
      public Collection<? extends JsVariable> getInternalProperties()
          throws MethodIsBlockingException {
        return getLoadedProperties().internalProperties();
      }

      @Override
      public JsVariable getProperty(String name) throws MethodIsBlockingException {
        return getLoadedProperties().getProperty(name);
      }

      @Override
      public String getRefId() {
        return valueData.objectId();
      }

      @Override
      public WipValueLoader getRemoteValueMapping() {
        return valueLoader;
      }

      protected RemoteObjectValue getValueData() {
        return valueData;
      }

      @Override
      public CallArgumentParam createCallArgumentParam() {
        return new CallArgumentParam(false, null, valueData.objectId());
      }

      protected ObjectProperties getLoadedProperties() throws MethodIsBlockingException {
        int currentCacheState = getRemoteValueMapping().getCacheState();
        if (loadedPropertiesRef.isInitialized()) {
          ObjectProperties result = loadedPropertiesRef.getSync().get();
          if (currentCacheState == result.getCacheState()) {
            return result;
          }
          doLoadProperties(true, currentCacheState);
        } else {
          doLoadProperties(false, currentCacheState);
        }
        return loadedPropertiesRef.getSync().get();
      }

      private void doLoadProperties(boolean reload, int currentCacheState)
          throws MethodIsBlockingException {
        PropertyNameBuilder innerNameBuilder;
        if (nameBuilder == null) {
          innerNameBuilder = null;
        } else {
          innerNameBuilder = new ObjectPropertyNameBuilder(nameBuilder);
        }
        valueLoader.loadJsObjectPropertiesInFuture(valueData.objectId(), innerNameBuilder,
            reload, currentCacheState, loadedPropertiesRef);
      }
    }
  }

  private static class ObjectSubtype extends ObjectTypeBase {
    ObjectSubtype(JsValue.Type type) {
      super(type);
    }

    @Override
    JsValue buildNewInstance(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder qualifiedNameBuilder) {
      return new ObjectTypeBase.JsObjectBase(valueData, valueLoader, qualifiedNameBuilder) {
        @Override public JsArray asArray() {
          return null;
        }

        @Override public JsFunction asFunction() {
          return null;
        }
      };
    }
  }

  private static class ArrayType extends ObjectTypeBase {
    ArrayType() {
      super(JsValue.Type.TYPE_ARRAY);
    }

    @Override
    JsValue buildNewInstance(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder nameBuilder) {
      return new Array(valueData, valueLoader, nameBuilder);
    }

    private class Array extends JsObjectBase implements JsArray {
      private final AtomicReference<ArrayProperties> arrayPropertiesRef =
          new AtomicReference<ArrayProperties>(null);

      Array(RemoteObjectValue valueData, WipValueLoader valueLoader,
          QualifiedNameBuilder nameBuilder) {
        super(valueData, valueLoader, nameBuilder);
      }

      @Override
      public JsArray asArray() {
        return this;
      }

      @Override
      public JsFunction asFunction() {
        return null;
      }

      @Override
      public long getLength() throws MethodIsBlockingException {
        return getArrayProperties().getLength();
      }

      @Override
      public JsVariable get(long index) throws MethodIsBlockingException {
        return getSafe(getArrayProperties().getSparseArrayMap(), index);
      }

      @Override
      public SortedMap<Long, ? extends JsVariable> toSparseArray()
          throws MethodIsBlockingException {
        return getArrayProperties().getPublicSparseArrayMap();
      }

      private ArrayProperties getArrayProperties() throws MethodIsBlockingException {
        ArrayProperties result = arrayPropertiesRef.get();
        if (result == null) {
          ArrayProperties arrayProperties = buildArrayProperties();
          // Only set if concurrent thread hasn't set its version
          arrayPropertiesRef.compareAndSet(null, arrayProperties);
          return arrayPropertiesRef.get();
        } else {
          return result;
        }
      }

      private ArrayProperties buildArrayProperties() throws MethodIsBlockingException {
        ObjectProperties loadedProperties = getLoadedProperties();
        final TreeMap<Long, JsVariable> map = new TreeMap<Long, JsVariable>();
        JsValue lengthValue = null;
        for (JsVariable variable : loadedProperties.properties()) {
          String name = variable.getName();
          Long index = JavaScriptExpressionBuilder.parsePropertyNameAsArrayIndex(name);
          if (index != null) {
            map.put(index, variable);
          } else if ("length".equals(name)) {
            lengthValue = variable.getValue();
          }
        }
        long length;
        try {
          length = Long.parseLong(lengthValue.getValueString());
        } catch (NumberFormatException e) {
          length = -1;
        }
        return new ArrayProperties(length, map);
      }
    }

    private static class ArrayProperties {
      final long length;
      final SortedMap<Long, ? extends JsVariable> sparseArrayMap;
      final SortedMap<Long, ? extends JsVariable> publicSparseArrayMap;

      ArrayProperties(long length,
          SortedMap<Long, ? extends JsVariable> sparseArrayMap) {
        this.length = length;
        this.sparseArrayMap = sparseArrayMap;
        // We make public map synchronized, because unmodifiable map has its internal state.
        this.publicSparseArrayMap = Collections.synchronizedSortedMap(
            Collections.unmodifiableSortedMap(sparseArrayMap));
      }
      long getLength() {
        return length;
      }

      SortedMap<Long, ? extends JsVariable> getSparseArrayMap() {
        return sparseArrayMap;
      }

      SortedMap<Long, ? extends JsVariable> getPublicSparseArrayMap() {
        return publicSparseArrayMap;
      }
    }
  }

  private static class FunctionType extends ObjectTypeBase {
    FunctionType() {
      super(JsValue.Type.TYPE_FUNCTION);
    }

    @Override
    JsValue buildNewInstance(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder nameBuilder) {
      return new FunctionValueImpl(valueData, valueLoader, nameBuilder);
    }

    private class FunctionValueImpl extends ObjectTypeBase.JsObjectBase implements JsFunction {
      private final AsyncFutureRef<Getter<LocationValue>> loadedPositionRef =
          new AsyncFutureRef<Getter<LocationValue>>();

      FunctionValueImpl(RemoteObjectValue valueData,
          WipValueLoader valueLoader, QualifiedNameBuilder nameBuilder) {
        super(valueData, valueLoader, nameBuilder);
      }

      @Override public JsArray asArray() {
        return null;
      }

      @Override public JsFunction asFunction() {
        return this;
      }

      @Override
      public Script getScript() throws MethodIsBlockingException {
        LocationValue functionPosition = getLoadedPosition();
        WipScriptManager scriptManager = getRemoteValueMapping().getTabImpl().getScriptManager();
        return scriptManager.getScript(functionPosition.scriptId());
      }

      @Override
      public TextStreamPosition getOpenParenPosition()
          throws MethodIsBlockingException {
        final LocationValue functionPosition = getLoadedPosition();
        return new TextStreamPosition() {
          @Override public int getOffset() {
            return WipBrowserImpl.throwUnsupported();
          }

          @Override public int getLine() {
            return (int) functionPosition.lineNumber();
          }

          @Override
          public int getColumn() {
            Long columnObject = functionPosition.columnNumber();
            if (columnObject == null) {
              return NO_POSITION;
            }
            return columnObject.intValue();
          }
        };
      }

      private LocationValue getLoadedPosition() throws MethodIsBlockingException {
        if (!loadedPositionRef.isInitialized()) {
          getRemoteValueMapping().loadFunctionLocationInFuture(getValueData().objectId(),
              loadedPositionRef);
        }
        return loadedPositionRef.getSync().get();
      }
    }
  }

  private static abstract class VariableBase implements JsVariable {
    private final JsValue jsValue;
    private final ValueNameBuilder nameBuilder;
    private volatile String qualifiedName = null;

    VariableBase(JsValue jsValue, ValueNameBuilder nameBuilder) {
      this.jsValue = jsValue;
      this.nameBuilder = nameBuilder;
    }

    public boolean isReadable() {
      return true;
    }

    @Override
    public JsValue getValue() {
      return jsValue;
    }

    @Override
    public String getName() {
      return nameBuilder.getShortName();
    }

    public String getFullyQualifiedName() {
      String result = qualifiedName;
      if (result == null) {
        QualifiedNameBuilder qualifiedNameBuilder = nameBuilder.getQualifiedNameBuilder();
        if (qualifiedNameBuilder == null) {
          return null;
        }
        StringBuilder builder = new StringBuilder();
        qualifiedNameBuilder.append(builder);
        result = builder.toString();
        qualifiedName = result;
      }
      return result;
    }
  }

  private static class VariableImpl extends VariableBase {
    VariableImpl(JsValue jsValue, ValueNameBuilder nameBuilder) {
      super(jsValue, nameBuilder);
    }

    @Override public JsObjectProperty asObjectProperty() {
      return null;
    }
    @Override public JsDeclarativeVariable asDeclarativeVariable() {
      return null;
    }
  }

  private static class DeclarativeVariable extends VariableImpl implements JsDeclarativeVariable {
    DeclarativeVariable(JsValue jsValue, ValueNameBuilder nameBuilder) {
      super(jsValue, nameBuilder);
    }

    @Override public JsDeclarativeVariable asDeclarativeVariable() {
      return this;
    }

    @Override public boolean isMutable() {
      return false;
    }

    @Override
    public RelayOk setValue(JsValue newValue, SetValueCallback callback,
        SyncCallback syncCallback) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }
  }

  private static abstract class ObjectPropertyBase
      extends VariableBase implements JsObjectProperty {
    ObjectPropertyBase(JsValue jsValue, ValueNameBuilder nameBuilder) {
      super(jsValue, nameBuilder);
    }

    @Override public JsObjectProperty asObjectProperty() {
      return this;
    }
  }

  private static class ObjectType extends ValueType {
    @Override
    JsValue build(RemoteObjectValue valueData, WipValueLoader valueLoader,
        QualifiedNameBuilder nameBuilder) {
      ValueType secondLevelValueType =
          getSafe(PROTOCOL_SUBTYPE_TO_VALUE_TYPE, valueData.subtype());

      if (secondLevelValueType == null) {
        LOGGER.severe("Unexpected value type: " + valueData.type() + " " + valueData.subtype());
        secondLevelValueType = DEFAULT_VALUE_TYPE;
      }

      return secondLevelValueType.build(valueData, valueLoader, nameBuilder);
    }

    private static final Map<RemoteObjectValue.Subtype, ValueType> PROTOCOL_SUBTYPE_TO_VALUE_TYPE;
    static {
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE = new HashMap<RemoteObjectValue.Subtype, ValueType>();
      ObjectSubtype objectSubtype = new ObjectSubtype(JsValue.Type.TYPE_OBJECT);
      // TODO: null?
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(null, objectSubtype);
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Subtype.NULL,
          new SingletonPrimitiveType(JsValue.Type.TYPE_NULL, "null"));
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Subtype.ARRAY, new ArrayType());
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Subtype.REGEXP, objectSubtype);
      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Subtype.DATE, objectSubtype);

      PROTOCOL_SUBTYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Subtype.NODE, objectSubtype);
      // Plus 1 for null - object.
      assert PROTOCOL_SUBTYPE_TO_VALUE_TYPE.size() ==
          RemoteObjectValue.Subtype.values().length + 1;
    }
  }

  private static final Map<RemoteObjectValue.Type, ValueType> PROTOCOL_TYPE_TO_VALUE_TYPE;
  static {
    PROTOCOL_TYPE_TO_VALUE_TYPE = new HashMap<RemoteObjectValue.Type, ValueType>();
    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.STRING,
        new PrimitiveTypeWithValue(JsValue.Type.TYPE_STRING));
    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.BOOLEAN,
        new PrimitiveTypeWithValue(JsValue.Type.TYPE_BOOLEAN));
    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.NUMBER,
        new PrimitiveTypeWithDescription(JsValue.Type.TYPE_NUMBER));
    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.UNDEFINED,
        new SingletonPrimitiveType(JsValue.Type.TYPE_UNDEFINED, "undefined"));

    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.OBJECT, new ObjectType());
    PROTOCOL_TYPE_TO_VALUE_TYPE.put(RemoteObjectValue.Type.FUNCTION, new FunctionType());

    assert PROTOCOL_TYPE_TO_VALUE_TYPE.size() == RemoteObjectValue.Type.values().length;
  }

  private static final ValueType DEFAULT_VALUE_TYPE = new ObjectSubtype(JsValue.Type.TYPE_OBJECT);
}
