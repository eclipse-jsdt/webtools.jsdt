// Copyright (c) 2009 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.internal.v8native.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.wst.jsdt.chromium.JsDeclarativeVariable;
import org.eclipse.wst.jsdt.chromium.JsObject;
import org.eclipse.wst.jsdt.chromium.JsScope;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.internal.v8native.CallFrameImpl;
import org.eclipse.wst.jsdt.chromium.internal.v8native.InternalContext;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.V8ProtocolUtil;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.ScopeRef;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.input.data.ObjectValueHandle;
import org.eclipse.wst.jsdt.chromium.internal.v8native.protocol.output.ScopeMessage;
import org.eclipse.wst.jsdt.chromium.util.AsyncFuture;
import org.eclipse.wst.jsdt.chromium.util.AsyncFuture.SyncOperation;
import org.eclipse.wst.jsdt.chromium.util.MethodIsBlockingException;

/**
 * A generic implementation of the JsScope interface.
 */
public abstract class JsScopeImpl<D extends JsScopeImpl.DataBase> implements JsScope {

  /**
   * An abstraction over object that hosts the scope. It could be either call frame or function.
   */
  public static abstract class Host {
    public static Host create(final CallFrameImpl callFrameImpl) {
      return new Host() {
        @Override InternalContext getInternalContext() {
          return callFrameImpl.getInternalContext();
        }
        @Override ScopeMessage.Host getProtocolParameter() {
          return ScopeMessage.Host.createFrame(callFrameImpl.getIdentifier());
        }
      };
    }

    public static Host create(final JsFunctionImpl jsFunctionImpl) {
      return new Host() {
        @Override InternalContext getInternalContext() {
          return jsFunctionImpl.getInternalContext();
        }
        @Override ScopeMessage.Host getProtocolParameter() {
          return ScopeMessage.Host.createFunction(jsFunctionImpl.getRef());
        }
      };
    }

    abstract InternalContext getInternalContext();

    abstract ScopeMessage.Host getProtocolParameter();
  }

  private final Host host;
  private final int scopeIndex;
  private final Type type;
  private final AtomicReference<AsyncFuture<D>> deferredDataRef =
      new AtomicReference<AsyncFuture<D>>(null);

  public static JsScopeImpl<?> create(Host host, ScopeRef scopeRef) {
    Type type = convertType((int) scopeRef.type());
    if (type == Type.WITH || type == Type.GLOBAL) {
      return new ObjectBasedImpl(host, type, (int) scopeRef.index());
    } else {
      return new DeclarativeImpl(host, type, (int) scopeRef.index());
    }
  }

  protected JsScopeImpl(Host host, Type type, int scopeIndex) {
    this.host = host;
    this.type = type;
    this.scopeIndex = scopeIndex;
  }

  @Override
  public Type getType() {
    return type;
  }

  protected int getScopeIndex() {
    return scopeIndex;
  }

  protected Host getScopeHost() {
    return host;
  }

  protected D getDeferredData() throws MethodIsBlockingException {
    AsyncFuture<D> future = deferredDataRef.get();
    ValueLoaderImpl valueLoader = host.getInternalContext().getValueLoader();
    int cacheState = valueLoader.getCurrentCacheState();
    boolean restartOperation;
    if (future == null) {
      // Do not restart operation if other thread has already started it.
      restartOperation = false;
    } else {
      D result = future.getSync();
      if (!result.isCacheObsolete(cacheState)) {
        return result;
      }
      restartOperation = true;
    }
    SyncOperation<D> loadOperation = createLoadDataOperation(valueLoader, cacheState);
    // Create future, so that other threads didn't start operations of their own.
    AsyncFuture.initializeReference(deferredDataRef, loadOperation.asAsyncOperation(),
        restartOperation);
    loadOperation.execute();
    return deferredDataRef.get().getSync();
  }

  protected abstract SyncOperation<D> createLoadDataOperation(ValueLoaderImpl valueLoader,
      int cacheState);

  protected ObjectValueHandle loadScopeObject(ValueLoaderImpl valueLoader)
      throws MethodIsBlockingException {
    ScopeMessage.Ref ref = new ScopeMessage.Ref(scopeIndex, host.getProtocolParameter());
    return valueLoader.loadScopeFields(ref);
  }

  public static Type convertType(int typeCode) {
    Type type = CODE_TO_TYPE.get(typeCode);
    if (type == null) {
      type = Type.UNKNOWN;
    }
    return type;
  }

  private static class DeclarativeImpl extends JsScopeImpl<DeclarativeImpl.DeferredData>
      implements JsScope.Declarative {
    DeclarativeImpl(Host host, Type type, int scopeIndex) {
      super(host, type, scopeIndex);
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
      return getDeferredData().variables;
    }

    @Override
    protected SyncOperation<DeferredData> createLoadDataOperation(
        final ValueLoaderImpl valueLoader, final int cacheState) {
      return new SyncOperation<DeferredData>() {
        @Override
        protected DeferredData runSync() throws MethodIsBlockingException {
          List<JsDeclarativeVariable> list = load(valueLoader);
          return new DeferredData(list, cacheState);
        }
      };
    }

    private List<JsDeclarativeVariable> load(ValueLoaderImpl valueLoader)
        throws MethodIsBlockingException {
      ObjectValueHandle scopeObject = loadScopeObject(valueLoader);
      if (scopeObject == null) {
        return Collections.emptyList();
      }
      List<? extends PropertyReference> propertyRefs =
          V8ProtocolUtil.extractObjectProperties(scopeObject);

      List<ValueMirror> propertyMirrors = valueLoader.getOrLoadValueFromRefs(propertyRefs);

      ScopeMessage.Ref scopeRef =
          new ScopeMessage.Ref(getScopeIndex(), getScopeHost().getProtocolParameter());
      JsVariableBase.VariableChanger variableHost =
          new JsVariableBase.VariableChanger(getScopeHost().getInternalContext(), scopeRef);

      List<JsDeclarativeVariable> properties =
          new ArrayList<JsDeclarativeVariable>(propertyMirrors.size());
      for (int i = 0; i < propertyMirrors.size(); i++) {
        // This name should be string. We are making it string as a fall-back strategy.
        String varNameStr = propertyRefs.get(i).getName().toString();
        properties.add(new JsVariableBase.Declarative(valueLoader, propertyMirrors.get(i),
            varNameStr, variableHost));
      }
      return properties;
    }

    static class DeferredData extends DataBase {
      final List<? extends JsDeclarativeVariable> variables;
      private final int cacheState;

      DeferredData(List<? extends JsDeclarativeVariable> variables, int cacheState) {
        this.variables = variables;
        this.cacheState = cacheState;
      }
      @Override boolean isCacheObsolete(int newCacheState) {
        return cacheState != newCacheState;
      }
    }
  }

  private static class ObjectBasedImpl extends JsScopeImpl<ObjectBasedImpl.DeferredData>
      implements JsScope.ObjectBased {
    ObjectBasedImpl(Host host, Type type, int scopeIndex) {
      super(host, type, scopeIndex);
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
    protected SyncOperation<DeferredData> createLoadDataOperation(
        final ValueLoaderImpl valueLoader, final int cacheState) {
      return new SyncOperation<DeferredData>() {
        @Override
        protected DeferredData runSync() throws MethodIsBlockingException {
          return load(valueLoader, cacheState);
        }
      };
    }

    private DeferredData load(ValueLoaderImpl valueLoader, int cacheState)
        throws MethodIsBlockingException {
      ObjectValueHandle scopeObject = loadScopeObject(valueLoader);
      ValueMirror mirror = valueLoader.addDataToMap(scopeObject.getSuper());
      JsValue jsValue = JsVariableBase.createValue(valueLoader, mirror);
      return new DeferredData(jsValue);
    }

    @Override
    public JsObject getScopeObject() throws MethodIsBlockingException {
      JsObject asObject = getDeferredData().jsValue.asObject();
      if (asObject == null) {
        throw new RuntimeException("Received scope object value is not an object");
      }
      return asObject;
    }

    static class DeferredData extends DataBase {
      final JsValue jsValue;
      DeferredData(JsValue jsValue) {
        this.jsValue = jsValue;
      }

      @Override boolean isCacheObsolete(int newCacheState) {
        return false;
      }
    }
  }

  static abstract class DataBase {
    abstract boolean isCacheObsolete(int newCacheState);
  }

  private static final Map<Integer, Type> CODE_TO_TYPE;
  static {
    CODE_TO_TYPE = new HashMap<Integer, Type>();
    CODE_TO_TYPE.put(0, Type.GLOBAL);
    CODE_TO_TYPE.put(1, Type.LOCAL);
    CODE_TO_TYPE.put(2, Type.WITH);
    CODE_TO_TYPE.put(3, Type.CLOSURE);
    CODE_TO_TYPE.put(4, Type.CATCH);
  }
}
