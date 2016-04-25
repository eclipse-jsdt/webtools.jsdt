// Copyright (c) 2009 -2016 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v1.0 which accompanies
// this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IDropToFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.wst.jsdt.chromium.CallFrame;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext;
import org.eclipse.wst.jsdt.chromium.JsEvaluateContext.ResultOrException;
import org.eclipse.wst.jsdt.chromium.JsScope;
import org.eclipse.wst.jsdt.chromium.JsScope.Declarative;
import org.eclipse.wst.jsdt.chromium.JsScope.ObjectBased;
import org.eclipse.wst.jsdt.chromium.JsValue;
import org.eclipse.wst.jsdt.chromium.JsValue.Type;
import org.eclipse.wst.jsdt.chromium.JsVariable;
import org.eclipse.wst.jsdt.chromium.RestartFrameExtension;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.TextStreamPosition;
import org.eclipse.wst.jsdt.chromium.debug.core.ChromiumDebugPlugin;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.SourcePosition;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.SourcePositionMap;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.SourcePositionMap.TranslateDirection;
import org.eclipse.wst.jsdt.chromium.util.GenericCallback;
import org.eclipse.wst.jsdt.chromium.util.JavaScriptExpressionBuilder;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue;

/**
 * An IStackFrame implementation over a JsStackFrame instance.
 */
public class StackFrame extends StackFrameBase implements IDropToFrame, IJavaScriptStackFrame {

  private final CallFrame stackFrame;

  private IVariable[] variables;

  private volatile CachedUserPosition userCachedSourcePosition = null;

  /**
   * Constructs a stack frame for the given handler using the FrameMirror data
   * from the remote V8 VM.
   *
   * @param debugTarget the global parent
   * @param thread for which the stack frame is created
   * @param stackFrame an underlying SDK stack frame
   */
  public StackFrame(JavascriptThread.SuspendedState threadState, CallFrame stackFrame) {
    super(new EvaluateContext(stackFrame.getEvaluateContext(), threadState));
    this.stackFrame = stackFrame;
  }

  public CallFrame getCallFrame() {
    return stackFrame;
  }

  public IVariable[] getVariables() throws DebugException {
    if (variables == null) {
      try {
        variables = wrapScopes(getEvaluateContext(), stackFrame.getVariableScopes(),
            stackFrame.getReceiverVariable(), ExpressionTracker.STACK_FRAME_FACTORY);
      } catch (RuntimeException e) {
        // We shouldn't throw RuntimeException from here, because calling
        // ElementContentProvider#update will forget to call update.done().
        throw new DebugException(new Status(IStatus.ERROR, ChromiumDebugPlugin.PLUGIN_ID,
            "Failed to read variables", e)); //$NON-NLS-1$
      }
    }
    return variables;
  }

  static IVariable[] wrapVariables(
      EvaluateContext evaluateContext, Collection<? extends JsVariable> jsVars,
      Set<? extends String> propertyNameBlackList,
      Collection <? extends JsVariable> jsInternalProperties,
      Collection<? extends Variable> additional, ExpressionTracker.Node expressionNode) {
    List<Variable> vars = new ArrayList<Variable>(jsVars.size());
    for (JsVariable jsVar : jsVars) {
      if (propertyNameBlackList.contains(jsVar.getName())) {
        continue;
      }
      ExpressionTracker.Node expressionTrackerNode =
          expressionNode.createVariableNode(jsVar, false);
      vars.add(Variable.forRealValue(evaluateContext, jsVar, false, expressionTrackerNode));
    }
    // Sort all regular properties by name.
    Collections.sort(vars, VARIABLE_COMPARATOR);
    // Always put internal properties in the end.
    if (jsInternalProperties != null) {
      for (JsVariable jsMetaVar : jsInternalProperties) {
        ExpressionTracker.Node expressionTrackerNode =
            expressionNode.createVariableNode(jsMetaVar, true);
        vars.add(Variable.forRealValue(evaluateContext, jsMetaVar, true, expressionTrackerNode));
      }
    }
    if (additional != null) {
      vars.addAll(additional);
    }
    return vars.toArray(new IVariable[vars.size()]);
  }

  static IVariable[] wrapScopes(final EvaluateContext evaluateContext,
      List<? extends JsScope> jsScopes, JsVariable receiverVariable,
      final ExpressionTracker.ScopeAndVariableFactory trackerNodeFactory) {
    final List<Variable> vars = new ArrayList<Variable>();

    JsScope.Visitor<Void> scopeVisitor = new JsScope.Visitor<Void>() {
      @Override
      public Void visitDeclarative(Declarative declarativeScope) {
        int startPos = vars.size();
        for (JsVariable var : declarativeScope.getVariables()) {
          vars.add(Variable.forRealValue(evaluateContext, var, false,
              trackerNodeFactory.createVariableNode(var,  false)));
        }
        // TODO: consider not sorting them once V8 native protocol returns locals ordered.
        final boolean sortVariables = true;
        if (sortVariables) {
          int endPos = vars.size();
          List<Variable> sublist = vars.subList(startPos, endPos);
          Collections.sort(sublist, VARIABLE_COMPARATOR);
        }
        return null;
      }

      @Override
      public Void visitObject(ObjectBased objectScope) {
        vars.add(Variable.forObjectScope(evaluateContext, objectScope,
            trackerNodeFactory.createScopeNode()));
        return null;
      }
    };

    for (JsScope scope : jsScopes) {
      if (scope.getType() == JsScope.Type.GLOBAL) {
        if (receiverVariable != null) {
          ExpressionTracker.Node expressionTrackerNode =
              trackerNodeFactory.createVariableNode(receiverVariable, false);
          vars.add(Variable.forRealValue(evaluateContext, receiverVariable, false,
              expressionTrackerNode));
          receiverVariable = null;
        }
      }
      scope.accept(scopeVisitor);
    }
    if (receiverVariable != null) {
      vars.add(Variable.forRealValue(evaluateContext, receiverVariable, false,
          trackerNodeFactory.createVariableNode(receiverVariable, false)));
    }

    IVariable[] result = new IVariable[vars.size()];
    // Return in reverse order.
    for (int i = 0; i < result.length; i++) {
      result[result.length - i - 1] = vars.get(i);
    }
    return result;
  }

  public boolean hasVariables() throws DebugException {
    return stackFrame.getReceiverVariable() != null || stackFrame.getVariableScopes().size() > 0;
  }

  @Override
  public boolean canDropToFrame() {
    RestartFrameExtension resetFrameExtension = getSdkResetFrameExtension();

    return resetFrameExtension != null && resetFrameExtension.canRestartFrame(stackFrame);
  }

  @Override
  public void dropToFrame() throws DebugException {
    GenericCallback<Boolean> callback = new GenericCallback<Boolean>() {
      @Override
      public void success(Boolean stackResumed) {
        if (stackResumed) {
          // We could report it to Eclipse debug framework, but we simply ignore it, because
          // resumed will arrive real soon and Eclipse is okey about double suspended event.
        } else {
          // TODO: update stack frames in Eclipse debug framework. However this branch
          // is never reachable with current V8 implementation.
        }
      }

      @Override
      public void failure(Exception exception) {
        ChromiumDebugPlugin.log(new Exception("Failed to 'drop to frame' action", exception)); //$NON-NLS-1$
      }
    };

    SyncCallback syncCallback = new SyncCallback() {
      @Override
      public void callbackDone(RuntimeException e) {
        if (e != null) {
          ChromiumDebugPlugin.log(e);
        }
      }
    };

    getSdkResetFrameExtension().restartFrame(stackFrame, callback, syncCallback);
  }

  private RestartFrameExtension getSdkResetFrameExtension() {
    return getConnectedData().getJavascriptVm().getRestartFrameExtension();
  }

  public int getLineNumber() throws DebugException {
    return getUserPosition().getLine() + 1;
  }

  public int getCharStart() throws DebugException {
    return -1;
  }

  public int getCharEnd() throws DebugException {
    // There's no default return value for this method when getCharStart() return
    // non-default value. Let's return the same number, it's the best we have.
    return getCharStart();
  }

  public String getName() throws DebugException {
    return getDebugTarget().getLabelProvider().getStackFrameLabel(this);
  }

  @Override
  Object getObjectForEquals() {
    return stackFrame;
  }

  @Override
  boolean isRegularFrame() {
    return true;
  }

  private final static Comparator<Variable> VARIABLE_COMPARATOR = new Comparator<Variable>() {
    public int compare(Variable var1, Variable var2) {
      return compareNameObjects(getNameObject(var1), getNameObject(var2));
    }
    // Get property name as String or Long.
    private Object getNameObject(Variable var) {
      String name = var.getName();
      Long num = JavaScriptExpressionBuilder.parsePropertyNameAsArrayIndex(name);
      if (num != null) {
        return num;
      }
      return name;
    }
    // Compare property name (either string or long).
    private int compareNameObjects(Object nameObj1, Object nameObj2) {
      if (nameObj1 instanceof Long) {
        Long n1 = (Long) nameObj1;
        if (nameObj2 instanceof Long) {
          Long n2 = (Long) nameObj2;
          return n1.compareTo(n2);
        } else {
          return COMPARE_LONG_WITH_STRING;
        }
      } else {
        String s1 = (String) nameObj1;
        if (nameObj2 instanceof String) {
          String s2 = (String) nameObj2;
          return s1.compareTo(s2);
        } else {
          return -COMPARE_LONG_WITH_STRING;
        }
      }
    }
    // Strings go before numbers.
    private static final int COMPARE_LONG_WITH_STRING = 1;
  };

  private SourcePosition getUserPosition() {
    CachedUserPosition currentCachedPosition = userCachedSourcePosition;
    if (currentCachedPosition == null || currentCachedPosition.token.isUpdated()) {
      VmResourceId id;
      Script script = stackFrame.getScript();
      if (script == null) {
        id = null;
      } else {
        id = VmResourceId.forScript(script);
      }
      TextStreamPosition vmPosition = stackFrame.getStatementStartPosition();
      SourcePositionMap sourceTransformationMap = getConnectedData().getSourcePositionMap();
      SourcePositionMap.Token token = sourceTransformationMap.getCurrentToken();
      SourcePosition originalPosition;
      if (id == null) {
        originalPosition = new SourcePosition(id, vmPosition.getLine(), vmPosition.getColumn());
      } else {
        originalPosition = sourceTransformationMap.translatePosition(id,
            vmPosition.getLine(), vmPosition.getColumn(),
            TranslateDirection.VM_TO_USER);
      }
      currentCachedPosition = new CachedUserPosition(originalPosition, token);
      userCachedSourcePosition = currentCachedPosition;
    }
    return currentCachedPosition.position;
  }

  /**
   * @return nullable
   */
  public VmResourceId getVmResourceId() {
    return getUserPosition().getId();
  }

  private final class CachedUserPosition {
    final SourcePosition position;
    final SourcePositionMap.Token token;

    CachedUserPosition(SourcePosition position, SourcePositionMap.Token token) {
      this.position = position;
      this.token = token;
    }
  }

	@Override
	public IJavaScriptValue evaluate(String expression) {
		final JsValue[] result = new JsValue[1];
		getEvaluateContext().getJsEvaluateContext().evaluateSync(expression, Collections.<String, JsValue>emptyMap(),
				new JsEvaluateContext.EvaluateCallback() {
					@Override
					public void success(ResultOrException valueOrException) {
						result[0] = valueOrException.accept(new ResultOrException.Visitor<JsValue>() {
			                  @Override public JsValue visitResult(JsValue value) {
			                    return value;
			                  }
			                  @Override public JsValue visitException(JsValue exception) {
			                    return null;
			                  }
			                });
						
					}
					
					@Override
					public void failure(Exception cause) {
						// TODO Auto-generated method stub
						
					}
				});
		if(result[0] != null){
			return new IJavaScriptValue() {
				
				@Override
				public <T> T getAdapter(Class<T> adapter) {
					return null;
				}
				
				@Override
				public String getModelIdentifier() {
					return ChromiumDebugPlugin.PLUGIN_ID;
				}
				
				@Override
				public ILaunch getLaunch() {
					return getConnectedData().getDebugTarget().getLaunch();
				}
				
				@Override
				public IDebugTarget getDebugTarget() {
					return getConnectedData().getDebugTarget();
				}
				
				@Override
				public boolean isAllocated() throws DebugException {
					return true;
				}
				
				@Override
				public boolean hasVariables() throws DebugException {
					return false;
				}
				
				@Override
				public IVariable[] getVariables() throws DebugException {
					return null;
				}
				
				@Override
				public String getValueString() throws DebugException {
					return result[0].getValueString();
				}
				
				@Override
				public String getReferenceTypeName() throws DebugException {
					return result[0].getType().toString();
				}
				
				@Override
				public boolean isNull() {
					return result[0].getType() == Type.TYPE_NULL;
				}
				
				@Override
				public String getDetailString() {
					return result[0].getValueString();
				}
			};
		}
		return null;
	}

	@Override
	public String getSourceName() {
		if (this.stackFrame.getScript() == null)
			return null;
		return this.stackFrame.getScript().getName();
	}

	@Override
	public String getSourcePath() {
		if (this.stackFrame.getScript() == null)
			return null;
		return this.stackFrame.getScript().getName();
	}

	@Override
	public String getSource() {
		if (this.stackFrame.getScript() == null)
			return null;
		return this.stackFrame.getScript().getSource();
	}

	@Override
	public IVariable getThisObject() {
		//No special treatment to this seems to be necessary as this is part of the 
		//variable list already.
		return null;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == IJavaScriptStackFrame.class)
			return this;
		return super.getAdapter(adapter);
	}
}
