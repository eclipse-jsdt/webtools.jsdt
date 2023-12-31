// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.chromium.CallbackSemaphore;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsObject;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.RemoteValueMapping;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.internal.JsEvaluateContextBase;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.ValueNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipValueBuilder.JsValueBase;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipValueBuilder.SerializableValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.RemoteObjectValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.WipParamsWithResponse;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.runtime.CallArgumentParam;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;
import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;
import org.eclipse.wst.jsdt.chromium.wip.EvaluateToMappingExtension;
import org.json.simple.JSONValue;

/**
 * Basic implementation of the abstract {@link JsEvaluateContextBase}. Class leaves unimplemented
 * parts that deal with a particular context details (callframe or global) and particular protocol
 * message.
 * @param <DATA> type of protocol message response
 */
abstract class WipEvaluateContextBase<DATA> extends JsEvaluateContextBase {

  private final WipValueLoader valueLoader;

  WipEvaluateContextBase(WipValueLoader valueLoader) {
    this.valueLoader = valueLoader;
  }

  @Override
  public RelayOk evaluateAsync(final String expression,
      Map<String, ? extends JsValue> additionalContext, EvaluateCallback callback,
      SyncCallback syncCallback) {

    WipExpressionBuilder.ValueNameBuilder valueNameBuilder =
        WipExpressionBuilder.createRootName(expression, true);

    return evaluateAsync(expression, valueNameBuilder, additionalContext, valueLoader,
        callback, syncCallback);
  }

  @Override
  public PrimitiveValueFactory getValueFactory() {
    return PRIMITIVE_VALUE_FACTORY;
  }

  RelayOk evaluateAsync(String expression, ValueNameBuilder valueNameBuidler,
      Map<String, ? extends JsValue> additionalContext, EvaluateCallback callback,
      SyncCallback syncCallback) {
    return evaluateAsync(expression, valueNameBuidler, additionalContext, valueLoader,
        callback, syncCallback);
  }

  private RelayOk evaluateAsync(String expression, final ValueNameBuilder valueNameBuidler,
      Map<String, ? extends JsValue> additionalContext, WipValueLoader destinationValueLoaderParam,
      final EvaluateCallback callback, SyncCallback syncCallback) {
    Map<String, JsValueBase> internalAdditionalContext;
    if (additionalContext == null) {
      internalAdditionalContext = null;
    } else {
      internalAdditionalContext = new LinkedHashMap<String, JsValueBase>(additionalContext.size());
      for (Map.Entry<String, ? extends JsValue> en : additionalContext.entrySet()) {
        JsValueBase jsValueBase = JsValueBase.cast(en.getValue());
        internalAdditionalContext.put(en.getKey(), jsValueBase);
      }
    }
    return evaluateAsyncImpl(expression, valueNameBuidler, internalAdditionalContext,
        destinationValueLoaderParam, callback, syncCallback);
  }

  RelayOk evaluateAsyncImpl(String expression, final ValueNameBuilder valueNameBuidler,
      Map<String, ? extends SerializableValue> additionalContext,
      WipValueLoader destinationValueLoaderParam,
      final EvaluateCallback callback, SyncCallback syncCallback) {
    if (destinationValueLoaderParam == null) {
      destinationValueLoaderParam = valueLoader;
    }
    final WipValueLoader destinationValueLoader = destinationValueLoaderParam;
    if (additionalContext != null && !additionalContext.isEmpty()) {
      WipContextBuilder contextBuilder = valueLoader.getTabImpl().getContextBuilder();
      EvaluateHack evaluateHack = contextBuilder.getEvaluateHack();
      return evaluateHack.evaluateAsync(expression, valueNameBuidler, additionalContext,
          destinationValueLoader, evaluateHackHelper, callback, syncCallback);
    }

    WipParamsWithResponse<DATA> params = createRequestParams(expression, destinationValueLoader);

    GenericCallback<DATA> commandCallback;
    if (callback == null) {
      commandCallback = null;
    } else {
      commandCallback = new GenericCallback<DATA>() {
        @Override
        public void success(DATA data) {
          ResultOrException resultOrException =
              processResponse(data, destinationValueLoader, valueNameBuidler);
          callback.success(resultOrException);
        }
        @Override
        public void failure(Exception exception) {
          callback.failure(exception);
        }
      };
    }
    WipCommandProcessor commandProcessor = valueLoader.getTabImpl().getCommandProcessor();
    return commandProcessor.send(params, commandCallback, syncCallback);
  }

  private ResultOrException processResponse(DATA data, WipValueLoader destinationValueLoader,
      ValueNameBuilder valueNameBuidler) {
    RemoteObjectValue valueData = getRemoteObjectValue(data);

    WipValueBuilder valueBuilder = destinationValueLoader.getValueBuilder();

    final JsValue value = valueBuilder.wrap(valueData, valueNameBuidler.getQualifiedNameBuilder());

    if (getWasThrown(data) == Boolean.TRUE) {
      return new ResultOrException() {
            @Override public JsValue getResult() {
              return null;
            }
            @Override public JsValue getException() {
              return value;
            }
            @Override public <R> R accept(Visitor<R> visitor) {
              return visitor.visitException(value);
            }
          };
    } else {
      return new ResultOrException() {
            @Override public JsValue getResult() {
              return value;
            }
            @Override public JsValue getException() {
              return null;
            }
            @Override public <R> R accept(Visitor<R> visitor) {
              return visitor.visitResult(value);
            }
          };
    }
  }

  private final EvaluateHack.EvaluateCommandHandler<DATA> evaluateHackHelper =
      new EvaluateHack.EvaluateCommandHandler<DATA>() {
    @Override
    public WipParamsWithResponse<DATA> createRequest(
        String patchedUserExpression, WipValueLoader destinationValueLoader) {
      return createRequestParams(patchedUserExpression, destinationValueLoader);
    }

    @Override
    public ResultOrException processResult(DATA response, WipValueLoader destinationValueLoader,
        ValueNameBuilder valueNameBuidler) {
      return processResponse(response, destinationValueLoader, valueNameBuidler);
    }

    @Override
    public Exception processFailure(Exception cause) {
      return cause;
    }
  };

  protected abstract WipParamsWithResponse<DATA> createRequestParams(String expression,
      WipValueLoader destinationValueLoader);

  protected abstract RemoteObjectValue getRemoteObjectValue(DATA data);

  protected abstract Boolean getWasThrown(DATA data);

  static WipEvaluateContextBase<?> castArgument(JsEvaluateContext context) {
    try {
      return (WipEvaluateContextBase<?>) context;
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Incorrect evaluate context argument", e);
    }
  }

  static final EvaluateToMappingExtension EVALUATE_TO_MAPPING_EXTENSION =
      new EvaluateToMappingExtension() {
    @Override
    public void evaluateSync(JsEvaluateContext evaluateContext,
        String expression, Map<String, ? extends JsValue> additionalContext,
        RemoteValueMapping targetMapping, EvaluateCallback evaluateCallback)
        throws MethodIsBlockingException {
      CallbackSemaphore callbackSemaphore = new CallbackSemaphore();
      RelayOk relayOk = evaluateAsync(evaluateContext, expression, additionalContext,
          targetMapping, evaluateCallback, callbackSemaphore);
      callbackSemaphore.acquireDefault(relayOk);
    }

    @Override
    public RelayOk evaluateAsync(JsEvaluateContext evaluateContext,
        String expression, Map<String, ? extends JsValue> additionalContext,
        RemoteValueMapping targetMapping, EvaluateCallback evaluateCallback,
        SyncCallback syncCallback) {
      WipEvaluateContextBase<?> contextImpl =
          WipEvaluateContextBase.castArgument(evaluateContext);
      return contextImpl.evaluateAsync(expression, additionalContext,
          evaluateCallback, syncCallback);
    }
  };

  private static final PrimitiveValueFactory PRIMITIVE_VALUE_FACTORY =
      new PrimitiveValueFactory() {
    @Override
    public JsValueBase getUndefined() {
      return new JsValueBaseImpl(JsValue.Type.TYPE_UNDEFINED) {
        @Override public String getValueString() {
          return "undefined";
        }
        @Override public CallArgumentParam createCallArgumentParam() {
          return new CallArgumentParam(false, null, null);
        }
      };
    }

    @Override
    public JsValueBase getNull() {
      return new JsValueBaseImpl(JsValue.Type.TYPE_NULL) {
        @Override public String getValueString() {
          return "null";
        }
        @Override public CallArgumentParam createCallArgumentParam() {
          return new CallArgumentParam(true, null, null);
        }
      };
    }

    @Override
    public JsValueBase createString(String value) {
      return new SimpleValue(JsValue.Type.TYPE_STRING, value);
    }

    @Override
    public JsValueBase createNumber(double value) {
      return new SimpleValue(JsValue.Type.TYPE_NUMBER, value);
    }

    @Override
    public JsValueBase createNumber(long value) {
      return new SimpleValue(JsValue.Type.TYPE_NUMBER, value);
    }

    @Override
    public JsValueBase createNumber(final String stringRepresentation) {
      return new JsValueBaseImpl(JsValue.Type.TYPE_STRING) {
        @Override public String getValueString() {
          return stringRepresentation;
        }
        @Override public CallArgumentParam createCallArgumentParam() {
          return new CallArgumentParam(true, JSONValue.parse(stringRepresentation), null);
        }
      };
    }

    @Override
    public JsValueBase createBoolean(boolean value) {
      return new SimpleValue(JsValue.Type.TYPE_BOOLEAN, value);
    }

    abstract class JsValueBaseImpl extends JsValueBase {
      private final Type type;
      JsValueBaseImpl(Type type) {
        this.type = type;
      }
      @Override public Type getType() {
        return type;
      }
      @Override public JsObject asObject() {
        return null;
      }
      @Override public boolean isTruncated() {
        return false;
      }
      @Override public RelayOk reloadHeavyValue(ReloadBiggerCallback callback,
          SyncCallback syncCallback) {
        throw new UnsupportedOperationException();
      }
      @Override public String getRefId() {
        return null;
      }
    }

    class SimpleValue extends JsValueBaseImpl {
      private final Object value;
      SimpleValue(Type type, Object value) {
        super(type);
        this.value = value;
      }
      @Override public String getValueString() {
        return value.toString();
      }
      @Override public CallArgumentParam createCallArgumentParam() {
        return new CallArgumentParam(true, value, null);
      }
    }
  };
}