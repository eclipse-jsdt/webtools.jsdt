// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.wip;

import static org.eclipse.wst.jsdt.chromium.util.BasicUtil.getSafe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.wst.jsdt.chromium.Breakpoint;
import org.eclipse.wst.jsdt.chromium.CallFrame;
import org.eclipse.wst.jsdt.chromium.DebugContext;
import org.eclipse.wst.jsdt.chromium.DebugContext.StepAction;
import org.eclipse.wst.jsdt.chromium.JsDeclarativeVariable;
import org.eclipse.wst.jsdt.chromium.ExceptionData;
import org.eclipse.wst.jsdt.chromium.JavascriptVm;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsObject;
import org.eclipse.wst.jsdt.chromium.JsScope;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.RemoteValueMapping;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.TextStreamPosition;
import org.eclipse.wst.jsdt.chromium.internal.protocolparser.JsonProtocolParseException;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipExpressionBuilder.ValueNameBuilder;
import org.eclipse.wst.jsdt.chromium.internal.wip.WipValueLoader.Getter;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.WipParserAccess;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.CallFrameValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.EvaluateOnCallFrameData;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.PausedEventData;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.ResumedEventData;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.debugger.ScopeValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.EvaluateData;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.PropertyDescriptorValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.input.runtime.RemoteObjectValue;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.WipParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.WipParamsWithResponse;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.debugger.EvaluateOnCallFrameParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.debugger.ResumeParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.debugger.StepIntoParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.debugger.StepOutParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.debugger.StepOverParams;
import org.eclipse.wst.jsdt.chromium.internal.wip.protocol.output.runtime.EvaluateParams;
import org.eclipse.wst.jsdt.chromium.util.AsyncFutureRef;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;
import org.eclipse.wst.jsdt.chromium.util.LazyConstructable;
import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;
import org.eclipse.wst.jsdt.chromium.util.RelaySyncCallback;
import org.json.simple.JSONObject;

/**
 * Builder for {@link DebugContext} that works with Wip protocol.
 */
class WipContextBuilder {
  private static final Logger LOGGER = Logger.getLogger(WipContextBuilder.class.getName());

  private final WipTabImpl tabImpl;
  private final EvaluateHack evaluateHack;
  private WipDebugContextImpl currentContext = null;

  WipContextBuilder(WipTabImpl tabImpl) {
    this.tabImpl = tabImpl;
    this.evaluateHack = new EvaluateHack(tabImpl);
  }

  // Called from Dispatch Thread.
  RelayOk updateStackTrace(List<CallFrameValue> callFrames,
      GenericCallback<Void> callback, final SyncCallback syncCallback) {
    if (currentContext == null) {
      if (callback != null) {
        callback.success(null);
      }
      return RelaySyncCallback.finish(syncCallback);
    } else {
      return currentContext.setFrames(callFrames, callback, syncCallback);
    }
  }

  void createContext(PausedEventData data) {
    if (currentContext != null) {
      LOGGER.severe("Context is already created");
      currentContext = null;
    }

    final WipDebugContextImpl context = new WipDebugContextImpl(data);
    currentContext = context;

    GenericCallback<Void> callback = new GenericCallback<Void>() {
      @Override
      public void success(Void value) {
        tabImpl.getDebugListener().getDebugEventListener().suspended(context);
      }

      @Override
      public void failure(Exception exception) {
        throw new RuntimeException(exception);
      }
    };

    context.setFrames(data.callFrames(), callback, null);
  }

  EvaluateHack getEvaluateHack() {
    return evaluateHack;
  }

  void onResumeReportedFromRemote(ResumedEventData event) {
    if (currentContext == null) {
      throw new IllegalStateException();
    }
    WipDebugContextImpl context = currentContext;
    currentContext = null;
    this.tabImpl.getDebugListener().getDebugEventListener().resumed();
    context.reportClosed();
  }

  class WipDebugContextImpl implements DebugContext {
    private volatile List<CallFrameImpl> frames = null;
    private final ExceptionData exceptionData;
    private final AtomicReference<CloseRequest> closeRequest =
        new AtomicReference<CloseRequest>(null);
    private final JsEvaluateContext globalContext;

    public WipDebugContextImpl(PausedEventData data) {
      PausedEventData.Data additionalData = data.data();
      if (data.reason() == PausedEventData.Reason.EXCEPTION && additionalData != null) {
        RemoteObjectValue exceptionRemoteObject;
        try {
          JSONObject additionalDataObject = additionalData.getUnderlyingObject();
          exceptionRemoteObject =
              WipParserAccess.get().parseRemoteObjectValue(additionalDataObject);
        } catch (JsonProtocolParseException e) {
          throw new RuntimeException("Failed to parse exception data", e);
        }
        JsValue exceptionValue =
            valueLoader.getValueBuilder().wrap(exceptionRemoteObject, null);
        exceptionData = new ExceptionDataImpl(exceptionValue);
      } else {
        exceptionData = null;
      }
      globalContext = new GlobalEvaluateContext(getValueLoader());
    }

    RelayOk setFrames(List<CallFrameValue> frameDataList,
        final GenericCallback<Void> callback, final SyncCallback syncCallback) {
      frames = new ArrayList<CallFrameImpl>(frameDataList.size());
      for (CallFrameValue frameData : frameDataList) {
        frames.add(new CallFrameImpl(frameData));
      }

      return tabImpl.getScriptManager().loadScriptSourcesAsync(getScriptIds(),
          new WipScriptManager.ScriptSourceLoadCallback() {
            @Override
            public void done(Map<String, WipScriptImpl> loadedScripts) {
              setScripts(loadedScripts);

              if (callback != null) {
                callback.success(null);
              }
            }
          },
          syncCallback);
    }

    WipValueLoader getValueLoader() {
      return valueLoader;
    }

    void reportClosed() {
      CloseRequest request = this.closeRequest.get();
      if (request != null && request.callback != null) {
        request.callback.success();
      }
    }

    Set<String> getScriptIds() {
      Set<String> scriptIds = new HashSet<String>();
      for (CallFrameImpl frame : frames) {
        String sourceId = frame.getSourceId();
        if (sourceId != null) {
          scriptIds.add(sourceId);
        }
      }
      return scriptIds;
    }

    void setScripts(Map<String, WipScriptImpl> loadedScripts) {
      for (CallFrameImpl frame : frames) {
        String sourceId = frame.getSourceId();
        if (sourceId != null) {
          WipScriptImpl script = getSafe(loadedScripts, sourceId);
          // Script can be null.
          frame.setScript(script);
        }
      }
    }

    @Override
    public State getState() {
      if (exceptionData == null) {
        return State.NORMAL;
      } else {
        return State.EXCEPTION;
      }
    }

    @Override
    public ExceptionData getExceptionData() {
      return exceptionData;
    }

    @Override
    public Collection<? extends Breakpoint> getBreakpointsHit() {
      if (frames.isEmpty()) {
        return Collections.emptyList();
      }
      CallFrameImpl topFrame = frames.get(0);
      return tabImpl.getBreakpointManager().findRelatedBreakpoints(topFrame);
    }

    @Override
    public List<? extends CallFrame> getCallFrames() {
      return frames;
    }

    @Override
    public void continueVm(StepAction stepAction, int stepCount,
        ContinueCallback callback) {
      continueVm(stepAction, stepCount, callback, null);
    }

    @Override
    public RelayOk continueVm(StepAction stepAction, int stepCount,
        ContinueCallback callback, SyncCallback syncCallback) {
      {
        boolean updated = closeRequest.compareAndSet(null, new CloseRequest(callback));
        if (!updated) {
          throw new IllegalStateException("Continue already requested");
        }
      }

      WipParams params = sdkStepToProtocolStep(stepAction);
      return tabImpl.getCommandProcessor().send(params, null, syncCallback);
    }

    @Override
    public JsEvaluateContext getGlobalEvaluateContext() {
      return globalContext;
    }

    @Override
    public RemoteValueMapping getDefaultRemoteValueMapping() {
      return valueLoader;
    }

    public WipTabImpl getTab() {
      return tabImpl;
    }

    public WipCommandProcessor getCommandProcessor() {
      return tabImpl.getCommandProcessor();
    }

    private class CloseRequest {
      final ContinueCallback callback;

      CloseRequest(ContinueCallback callback) {
        this.callback = callback;
      }
    }

    class CallFrameImpl implements CallFrame {
      private final String functionName;
      private final String id;
      private final LazyConstructable<List<JsScope>> scopeData;
      private final JsVariable thisObject;
      private final TextStreamPosition streamPosition;
      private final String sourceId;
      private WipScriptImpl scriptImpl;

      public CallFrameImpl(CallFrameValue frameData) {
        functionName = frameData.functionName();
        id = frameData.callFrameId();
        sourceId = frameData.location().scriptId();
        final List<ScopeValue> scopeDataList = frameData.scopeChain();

        scopeData = LazyConstructable.create(new LazyConstructable.Factory<List<JsScope>>() {
          @Override
          public List<JsScope> construct() {
            final List<JsScope> scopes = new ArrayList<JsScope>(scopeDataList.size());

            for (int i = 0; i < scopeDataList.size(); i++) {
              ScopeValue scopeData = scopeDataList.get(i);
              JsScope.Type type = WIP_TO_SDK_SCOPE_TYPE.get(scopeData.type());
              if (type == null) {
                type = JsScope.Type.UNKNOWN;
              }
              scopes.add(createScope(scopeData, type));
            }
            return scopes;
          }
        });

        RemoteObjectValue thisObjectData = frameData.getThis();
        if (thisObjectData == null) {
          LOGGER.log(Level.SEVERE, "Missing local scope", new Exception());
          thisObject = null;
        } else {
          thisObject = createSimpleNameVariable("this", thisObjectData);
        }

        // 0-based.
        final int line = (int) frameData.location().lineNumber();

        // 0-based.
        // TODO: check documentation, whether it's 0-based
        Long columnObject = frameData.location().columnNumber();
        final int column;
        if (columnObject == null) {
          column = 0;
        } else {
          column = columnObject.intValue();
        }
        streamPosition = new TextStreamPosition() {
          @Override public int getOffset() {
            return WipBrowserImpl.throwUnsupported();
          }
          @Override public int getLine() {
            return line;
          }

          @Override public int getColumn() {
            return column;
          }
        };
      }

      String getSourceId() {
        return sourceId;
      }

      void setScript(WipScriptImpl scriptImpl) {
        this.scriptImpl = scriptImpl;
      }

      @Override
      public List<? extends JsScope> getVariableScopes() {
        return scopeData.get();
      }

      @Override
      public JsVariable getReceiverVariable() {
        return thisObject;
      }

      @Override
      public WipScriptImpl getScript() {
        return scriptImpl;
      }

      @Override
      public TextStreamPosition getStatementStartPosition() {
        return streamPosition;
      }

      @Override
      public String getFunctionName() {
        return functionName;
      }

      @Override
      public JsEvaluateContext getEvaluateContext() {
        return evaluateContext;
      }

      private JsVariable createSimpleNameVariable(final String name,
          RemoteObjectValue thisObjectData) {
        ValueNameBuilder valueNameBuidler = WipExpressionBuilder.createRootName(name, false);
        return valueLoader.getValueBuilder().createVariable(thisObjectData, valueNameBuidler);
      }

      private final WipEvaluateContextBase<?> evaluateContext =
          new WipEvaluateContextBase<EvaluateOnCallFrameData>(getValueLoader()) {
        @Override
        protected WipParamsWithResponse<EvaluateOnCallFrameData> createRequestParams(
            String expression, WipValueLoader destinationValueLoader) {
          return new EvaluateOnCallFrameParams(id, expression,
              destinationValueLoader.getObjectGroupId(), false, false);
        }

        @Override protected RemoteObjectValue getRemoteObjectValue(EvaluateOnCallFrameData data) {
          return data.result();
        }

        @Override protected Boolean getWasThrown(EvaluateOnCallFrameData data) {
          return data.wasThrown();
        }
      };
    }

    private class ExceptionDataImpl implements ExceptionData {
      private final JsValue exceptionValue;

      ExceptionDataImpl(JsValue exceptionValue) {
        this.exceptionValue = exceptionValue;
      }

      @Override
      public JsValue getExceptionValue() {
        return exceptionValue;
      }

      @Override
      public boolean isUncaught() {
        // TODO: implement.
        return false;
      }

      @Override
      public String getSourceText() {
        // Not supported.
        return null;
      }

      @Override
      public String getExceptionMessage() {
        // TODO: implement.
        return exceptionValue.getValueString();
      }
    }

    @Override
    public JavascriptVm getJavascriptVm() {
      return tabImpl;
    }

    JsScope createScope(ScopeValue scopeData, JsScope.Type type) {
      if (type == JsScope.Type.WITH || type == JsScope.Type.GLOBAL) {
        return new ObjectScopeImpl(scopeData, type);
      } else {
        return new DeclarativeScopeImpl(scopeData, type);
      }
    }

    private class DeclarativeScopeImpl implements JsScope.Declarative {
      private final AsyncFutureRef<Getter<ScopeVariables>> propertiesRef =
          new AsyncFutureRef<Getter<ScopeVariables>>();
      private final String objectId;
      private final Type type;

      public DeclarativeScopeImpl(ScopeValue scopeData, Type type) {
        this.type = type;
        this.objectId = scopeData.object().objectId();
      }

      @Override public Type getType() {
        return type;
      }

      @Override public Declarative asDeclarativeScope() {
        return this;
      }

      @Override public ObjectBased asObjectBased() {
        return null;
      }

      @Override public <R> R accept(Visitor<R> visitor) {
        return visitor.visitDeclarative(this);
      }

      @Override
      public List<? extends JsDeclarativeVariable> getVariables() throws MethodIsBlockingException {
        int currentCacheState = valueLoader.getCacheState();
        if (propertiesRef.isInitialized()) {
          ScopeVariables result = propertiesRef.getSync().get();
          if (result.cacheState == currentCacheState) {
            return result.variables;
          }
          startLoadOperation(true, currentCacheState);
        } else {
          startLoadOperation(false, currentCacheState);
        }

        // This is blocking.
        return propertiesRef.getSync().get().variables;
      }

      /**
       * Starts load operation that works synchronously, i.e. it may block the calling method.
       * This is done because some thread must take post-processing anyway and it shouldn't
       * be the Dispatch thread.
       * The method may not be blocking, if another thread is already doing the same operation.
       */
      private void startLoadOperation(boolean reload, int currentCacheState)
          throws MethodIsBlockingException {
        WipValueLoader.LoadPostprocessor<Getter<ScopeVariables>> processor =
            new WipValueLoader.LoadPostprocessor<Getter<ScopeVariables>>() {
          @Override
          public Getter<ScopeVariables> process(
              List<? extends PropertyDescriptorValue> propertyList, int currentCacheState) {
            final List<JsDeclarativeVariable> properties =
                new ArrayList<JsDeclarativeVariable>(propertyList.size());

            WipValueBuilder valueBuilder = valueLoader.getValueBuilder();
            for (PropertyDescriptorValue property : propertyList) {
              final String name = property.name();

              ValueNameBuilder valueNameBuilder =
                  WipExpressionBuilder.createRootName(name, false);
              JsDeclarativeVariable variable =
                  valueBuilder.createDeclarativeVariable(property.value(), valueNameBuilder);
              properties.add(variable);
            }
            final ScopeVariables scopeVariables =
                new ScopeVariables(properties, currentCacheState);
            return new Getter<ScopeVariables>() {
              @Override
              ScopeVariables get() {
                return scopeVariables;
              }
            };
          }

          @Override
          public Getter<ScopeVariables> getEmptyResult() {
            return EMPTY_SCOPE_VARIABLES_OPTIONAL;
          }

          @Override
          public Getter<ScopeVariables> forException(Exception exception) {
            return WipValueLoader.Getter.newFailure(exception);
          }
        };
        // This is blocking.
        valueLoader.loadPropertiesInFuture(objectId, processor, reload, currentCacheState,
            propertiesRef);
      }
    }

    private class ObjectScopeImpl implements JsScope.ObjectBased {
      private final JsValue jsValue;
      private final JsScope.Type type;

      ObjectScopeImpl(ScopeValue scopeData, JsScope.Type type) {
        jsValue = valueLoader.getValueBuilder().wrap(scopeData.object(), null);
        this.type = type;
      }

      @Override public Type getType() {
        return type;
      }

      @Override public Declarative asDeclarativeScope() {
        return null;
      }

      @Override public ObjectBased asObjectBased() {
        return this;
      }

      @Override public <R> R accept(Visitor<R> visitor) {
        return visitor.visitObject(this);
      }

      @Override
      public JsObject getScopeObject() throws MethodIsBlockingException {
        JsObject jsObject = jsValue.asObject();
        if (jsObject == null) {
          throw new RuntimeException("Received scope object value is not an object");
        }
        return jsObject;
      }
    }

    private final WipValueLoader valueLoader = new WipValueLoader(tabImpl) {
      @Override
      String getObjectGroupId() {
        return null;
      }
    };
  }

  static final class GlobalEvaluateContext extends WipEvaluateContextBase<EvaluateData> {

    GlobalEvaluateContext(WipValueLoader valueLoader) {
      super(valueLoader);
    }

    @Override protected WipParamsWithResponse<EvaluateData> createRequestParams(String expression,
        WipValueLoader destinationValueLoader) {
      boolean doNotPauseOnExceptions = true;
      return new EvaluateParams(expression, destinationValueLoader.getObjectGroupId(),
          false, doNotPauseOnExceptions, null, false);
    }

    @Override protected RemoteObjectValue getRemoteObjectValue(EvaluateData data) {
      return data.result();
    }

    @Override protected Boolean getWasThrown(EvaluateData data) {
      return data.wasThrown();
    }
  }

  private static final Map<ScopeValue.Type, JsScope.Type> WIP_TO_SDK_SCOPE_TYPE;
  static {
    WIP_TO_SDK_SCOPE_TYPE = new HashMap<ScopeValue.Type, JsScope.Type>();

    WIP_TO_SDK_SCOPE_TYPE.put(ScopeValue.Type.GLOBAL, JsScope.Type.GLOBAL);
    WIP_TO_SDK_SCOPE_TYPE.put(ScopeValue.Type.LOCAL, JsScope.Type.LOCAL);
    WIP_TO_SDK_SCOPE_TYPE.put(ScopeValue.Type.WITH, JsScope.Type.WITH);
    WIP_TO_SDK_SCOPE_TYPE.put(ScopeValue.Type.CLOSURE, JsScope.Type.CLOSURE);
    WIP_TO_SDK_SCOPE_TYPE.put(ScopeValue.Type.CATCH, JsScope.Type.CATCH);

    assert WIP_TO_SDK_SCOPE_TYPE.size() == ScopeValue.Type.values().length;
  }

  private static class ScopeVariables {
    final List<JsDeclarativeVariable> variables;
    final int cacheState;

    ScopeVariables(List<JsDeclarativeVariable> variables, int cacheState) {
      this.variables = variables;
      this.cacheState = cacheState;
    }
  }

  private final Getter<ScopeVariables> EMPTY_SCOPE_VARIABLES_OPTIONAL =
      new Getter<ScopeVariables>() {
        private final ScopeVariables value =
          new ScopeVariables(Collections.<JsDeclarativeVariable>emptyList(), Integer.MAX_VALUE);

        @Override ScopeVariables get() {
          return value;
        }
      };


  private WipParams sdkStepToProtocolStep(StepAction stepAction) {
    switch (stepAction) {
    case CONTINUE:
      return RESUME_PARAMS;
    case IN:
      return STEP_INTO_PARAMS;
    case OUT:
      return STEP_OUT_PARAMS;
    case OVER:
      return STEP_OVER_PARAMS;
    default:
      throw new RuntimeException();
    }
  }

  private static final ResumeParams RESUME_PARAMS = new ResumeParams();
  private static final StepIntoParams STEP_INTO_PARAMS = new StepIntoParams();
  private static final StepOutParams STEP_OUT_PARAMS = new StepOutParams();
  private static final StepOverParams STEP_OVER_PARAMS = new StepOverParams();
}