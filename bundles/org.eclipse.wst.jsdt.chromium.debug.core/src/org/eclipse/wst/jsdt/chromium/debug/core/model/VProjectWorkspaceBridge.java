// Copyright (c) 2009, 2017 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/
//
// Contributors:
//      Ilya Buziuk <ilyabuziuk@gmail.com> - https://bugs.eclipse.org/bugs/show_bug.cgi?id=486061

package org.eclipse.wst.jsdt.chromium.debug.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.chromium.Breakpoint;
import org.eclipse.wst.jsdt.chromium.CallFrame;
import org.eclipse.wst.jsdt.chromium.ExceptionData;
import org.eclipse.wst.jsdt.chromium.JavascriptVm;
import org.eclipse.wst.jsdt.chromium.JavascriptVm.ScriptsCallback;
import org.eclipse.wst.jsdt.chromium.RelayOk;
import org.eclipse.wst.jsdt.chromium.Script;
import org.eclipse.wst.jsdt.chromium.SyncCallback;
import org.eclipse.wst.jsdt.chromium.debug.core.ChromiumDebugPlugin;
import org.eclipse.wst.jsdt.chromium.debug.core.ChromiumSourceDirector;
import org.eclipse.wst.jsdt.chromium.debug.core.ScriptNameManipulator.ScriptNamePattern;
import org.eclipse.wst.jsdt.chromium.debug.core.internal.sourcemap.SourceMap;
import org.eclipse.wst.jsdt.chromium.debug.core.model.BreakpointSynchronizer.Callback;
import org.eclipse.wst.jsdt.chromium.debug.core.model.ChromiumLineBreakpoint.MutableProperty;
import org.eclipse.wst.jsdt.chromium.debug.core.model.VmResource.Metadata;
import org.eclipse.wst.jsdt.chromium.debug.core.model.VmResourceRef.Visitor;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.SourcePositionMapBuilder;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.TextSectionMapping;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapLanguageSupport;
import org.eclipse.wst.jsdt.chromium.debug.core.sourcemap.extension.ISourceMapManager;
import org.eclipse.wst.jsdt.chromium.debug.core.util.ChromiumDebugPluginUtil;
import org.eclipse.wst.jsdt.chromium.debug.core.util.JavaScriptRegExpSupport;

/**
 * Virtual project-supporting implementation of {@link WorkspaceBridge}.
 */
public class VProjectWorkspaceBridge implements WorkspaceBridge {
  /** The debug model ID. */
  public static final String DEBUG_MODEL_ID = "org.eclipse.wst.jsdt.chromium.debug"; //$NON-NLS-1$

  public static class FactoryImpl implements Factory {
    private final String projectNameBase;

    public FactoryImpl(String projectNameBase) {
      this.projectNameBase = projectNameBase;
    }

    public WorkspaceBridge attachedToVm(ConnectedTargetData connectedTargetData,
        JavascriptVm javascriptVm) {
      // We might want to add URL or something to project name.
      return new VProjectWorkspaceBridge(projectNameBase, connectedTargetData, javascriptVm);
    }

    public String getDebugModelIdentifier() {
      return DEBUG_MODEL_ID;
    }

    public JsLabelProvider getLabelProvider() {
      return LABEL_PROVIDER;
    }
  }

  private final IProject debugProject;
  private final JavascriptVm javascriptVm;
  private final ResourceManager resourceManager;
  private final ConnectedTargetData connectedTargetData;
  private final ChromiumSourceDirector sourceDirector;
  private final UpdateMappingVisitor updateMappingVisitor;
  
  public VProjectWorkspaceBridge(String projectName, ConnectedTargetData connectedTargetData,
      JavascriptVm javascriptVm) {
    this.connectedTargetData = connectedTargetData;
    this.javascriptVm = javascriptVm;
    this.debugProject = ChromiumDebugPluginUtil.createEmptyProject(projectName);
    this.resourceManager = new ResourceManager(debugProject);
    this.updateMappingVisitor = new UpdateMappingVisitor();
    
    ILaunch launch = connectedTargetData.getDebugTarget().getLaunch();

    sourceDirector = (ChromiumSourceDirector) launch.getSourceLocator();
    sourceDirector.initializeVProjectContainers(debugProject, resourceManager,
        connectedTargetData.getJavascriptEmbedder());
  }

  public BreakpointSynchronizer getBreakpointSynchronizer() {
    return new BreakpointSynchronizer(javascriptVm, sourceDirector, breakpointHandler,
        DEBUG_MODEL_ID);
  }

  public void synchronizeBreakpoints(BreakpointSynchronizer.Direction direction,
      Callback callback) {
    getBreakpointSynchronizer().syncBreakpoints(direction, callback);
  }

  public void startInitialization() {
    LaunchInitializationProcedure.startAsync(this,
        connectedTargetData.getDebugTarget().getPresetSyncDirection());
  }

  public void launchRemoved() {
    if (debugProject != null) {
      ChromiumDebugPluginUtil.deleteVirtualProjectAsync(debugProject);
    }
  }

  public void beforeDetach() {
  }

  public void handleVmResetEvent() {
    resourceManager.clear();
  }

  public void scriptLoaded(Script newScript) {
    resourceManager.addScript(newScript);
  }

  public void scriptCollected(Script script) {
    resourceManager.scriptCollected(script);
  }

  public void reloadScriptsAtStart() {
    javascriptVm.getScripts(new ScriptsCallback() {
      public void failure(String errorMessage) {
        ChromiumDebugPlugin.logError(errorMessage);
      }

      public void success(Collection<Script> scripts) {
        if (!javascriptVm.isAttached()) {
          return;
        }
        for (Script script : scripts) {
          resourceManager.addScript(script);
        }
      }
    });
  }

  public Collection<? extends VmResource> findVmResourcesFromWorkspaceFile(IFile resource)
      throws CoreException {
    VmResourceRef vmResourceRef = findVmResourceRefFromWorkspaceFile(resource);
    if (vmResourceRef == null) {
      return null;
    }
    return vmResourceRef.accept(RESOLVE_RESOURCE_VISITOR);
  }

  private final VmResourceRef.Visitor<Collection<? extends VmResource>> RESOLVE_RESOURCE_VISITOR =
      new VmResourceRef.Visitor<Collection<? extends VmResource>>() {
    @Override
    public java.util.Collection<? extends VmResource> visitRegExpBased(
        ScriptNamePattern scriptNamePattern) {
      Pattern pattern = JavaScriptRegExpSupport.convertToJavaPattern(scriptNamePattern);
      return resourceManager.findVmResources(pattern);
    }

    @Override
    public Collection<? extends VmResource> visitResourceId(VmResourceId resourceId) {
      VmResource vmResource = resourceManager.getVmResource(resourceId);
      if (vmResource == null) {
        return Collections.emptyList();
      } else {
        return Collections.singletonList(vmResource);
      }
    }
  };

  public VmResource getVProjectVmResource(IFile file) {
    VmResourceId resourceId = resourceManager.getResourceId(file);
    if (resourceId == null) {
      return null;
    }
    return resourceManager.getVmResource(resourceId);
  }

  public VmResource createTemporaryFile(Metadata metadata,
      String proposedFileName) {
    return resourceManager.createTemporaryFile(metadata, proposedFileName);
  }

  private VmResourceRef findVmResourceRefFromWorkspaceFile(IFile resource) throws CoreException {
    return sourceDirector.findVmResourceRef(resource);
  }

  public void reloadScript(Script script) {
    resourceManager.reloadScript(script);
  }

  public BreakpointHandler getBreakpointHandler() {
    return breakpointHandler;
  }

  private final BreakpointHandlerImpl breakpointHandler = new BreakpointHandlerImpl();

  private class BreakpointHandlerImpl implements BreakpointHandler,
      BreakpointSynchronizer.BreakpointHelper {

    private volatile JavascriptVm.ExceptionCatchMode breakExceptionMode = null;

    private final LineBreakpointHandler lineBreakpointHandler = new LineBreakpointHandler();
    private final ExceptionBreakpointHandler exceptionBreakpointHandler =
        new ExceptionBreakpointHandler();
    private final List<BreakpointMapperBase<?, ?>> allHandlers;
    {
      allHandlers = new ArrayList<BreakpointMapperBase<?,?>>(2);
      allHandlers.add(lineBreakpointHandler);
      allHandlers.add(exceptionBreakpointHandler);
    }

    public void initBreakpointManagerListenerState(IBreakpointManager breakpointManager) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        handler.initEnablement(breakpointManager);
      }
    }

    public boolean supportsBreakpoint(IBreakpoint breakpoint) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        Object res = handler.tryCastBreakpoint(breakpoint);
        if (res != null) {
          return true;
        }
      }
      return false;
    }

    @Override
    public Breakpoint getSdkBreakpoint(ChromiumLineBreakpoint chromiumLineBreakpoint) {
      return lineBreakpointHandler.getSdkBreakpoint(chromiumLineBreakpoint);
    }

    public void breakpointAdded(IBreakpoint breakpoint) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        boolean res = handler.breakpointAdded(breakpoint);
        if (res) {
          return;
        }
      }
    }

    public RelayOk createBreakpointOnRemote(final ChromiumLineBreakpoint lineBreakpoint,
        final VmResourceRef vmResourceRef,
        final CreateCallback createCallback, SyncCallback syncCallback) throws CoreException {
      vmResourceRef.accept(updateMappingVisitor);	
      return lineBreakpointHandler.createBreakpointOnRemote(lineBreakpoint, vmResourceRef,
          createCallback, syncCallback);
    }

    @Override
    public BreakpointInTargetMap<Breakpoint, ChromiumLineBreakpoint> getLineBreakpointMap() {
      return lineBreakpointHandler.getMap();
    }

    @Override
    public void registerExceptionBreakpoint(
        Collection<ChromiumExceptionBreakpoint> collection) {
      exceptionBreakpointHandler.registerLocalBreakpoints(collection);
    }

    public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        boolean res = handler.breakpointChanged(breakpoint, delta);
        if (res) {
          return;
        }
      }
    }

    public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        boolean res = handler.breakpointRemoved(breakpoint, delta);
        if (res) {
          return;
        }
      }
    }

    public synchronized void breakpointManagerEnablementChanged(boolean enabled) {
      for (BreakpointMapperBase<?, ?> handler : allHandlers) {
        handler.breakpointManagerEnablementChanged(enabled);
      }
    }

    public Collection<? extends IBreakpoint> breakpointsHit(
        Collection<? extends Breakpoint> breakpointsHit) {
      return lineBreakpointHandler.handleBreakpointsHit(breakpointsHit);
    }

    @Override
    public Collection<? extends IBreakpoint> exceptionBreakpointHit(
        boolean isUncaught) {
      return exceptionBreakpointHandler.getHitBreakpoints(isUncaught);
    }

    private String getTargetNameSafe() {
      try {
        return connectedTargetData.getDebugTarget().getLaunch().getLaunchConfiguration().getName();
      } catch (RuntimeException e) {
        return "<unknown>"; //$NON-NLS-1$
      }
    }

    private class LineBreakpointHandler
        extends BreakpointMapperBase<Breakpoint, ChromiumLineBreakpoint> {
      private final EnablementMonitor enablementMonitor = new EnablementMonitor();

      private class EnablementMonitor {
        synchronized void init(IBreakpointManager breakpointManager) {
          sendRequest(breakpointManager.isEnabled());
        }
        synchronized void setState(boolean enabled) {
          sendRequest(enabled);
        }
        private void sendRequest(boolean enabled) {
          javascriptVm.enableBreakpoints(enabled, null, null);
        }
      }

      void initEnablement(IBreakpointManager breakpointManager) {
        enablementMonitor.init(breakpointManager);
      }

      @Override
      void breakpointAdded(ChromiumLineBreakpoint lineBreakpoint) {
        if (ChromiumLineBreakpoint.getIgnoreList().contains(lineBreakpoint)) {
          return;
        }
        IFile file = (IFile) lineBreakpoint.getMarker().getResource();
        VmResourceRef vmResourceRef;
        try {
          vmResourceRef = findVmResourceRefFromWorkspaceFile(file);
        } catch (CoreException e) {
          ChromiumDebugPlugin.log(
              new Exception("Failed to resolve script for the file " + file, e)); //$NON-NLS-1$
          return;
        }
        if (vmResourceRef == null) {
          // Might be a script from a different debug target
          return;
        }

        try {
          createBreakpointOnRemote(lineBreakpoint, vmResourceRef, null, null);
        } catch (CoreException e) {
          ChromiumDebugPlugin.log(new Exception("Failed to create breakpoint in " + //$NON-NLS-1$
              getTargetNameSafe(), e));
          throw new RuntimeException(e);
        }
      }

      public Collection<? extends IBreakpoint> handleBreakpointsHit(
          Collection<? extends Breakpoint> sdkBreakpoints) {
        if (sdkBreakpoints.isEmpty()) {
          return Collections.emptyList();
        }

        Collection<IBreakpoint> uiBreakpoints = new ArrayList<IBreakpoint>(sdkBreakpoints.size());

        for (Breakpoint sdkBreakpoint : sdkBreakpoints) {
          ChromiumLineBreakpoint uiBreakpoint = getMap().getUiBreakpoint(sdkBreakpoint);
          if (uiBreakpoint != null) {
            try {
              uiBreakpoint.silentlyResetIgnoreCount(); // reset ignore count as we've hit it
            } catch (CoreException e) {
              ChromiumDebugPlugin.log(new Exception("Failed to reset breakpoint ignore count", e));
            }

            uiBreakpoints.add(uiBreakpoint);
          }
        }
        return uiBreakpoints;
      }

      public Breakpoint getSdkBreakpoint(ChromiumLineBreakpoint chromiumLineBreakpoint) {
        return getMap().getSdkBreakpoint(chromiumLineBreakpoint);
      }

      public RelayOk createBreakpointOnRemote(final ChromiumLineBreakpoint lineBreakpoint,
          final VmResourceRef vmResourceRef,
          final CreateCallback createCallback, SyncCallback syncCallback) throws CoreException {
        ChromiumLineBreakpoint.Helper.CreateOnRemoveCallback callback =
            new ChromiumLineBreakpoint.Helper.CreateOnRemoveCallback() {
          public void success(Breakpoint breakpoint) {
            getMap().add(breakpoint, lineBreakpoint);
            if (createCallback != null) {
              createCallback.success();
            }
          }
          public void failure(String errorMessage) {
            if (createCallback == null) {
              ChromiumDebugPlugin.logError(errorMessage);
            } else {
              createCallback.failure(new Exception(errorMessage));
            }
          }
        };
        return ChromiumLineBreakpoint.Helper.createOnRemote(lineBreakpoint, vmResourceRef,
            connectedTargetData, callback, syncCallback);
      }

      @Override
      void breakpointChanged(ChromiumLineBreakpoint lineBreakpoint,
          IMarkerDelta delta) {
        if (ChromiumLineBreakpoint.getIgnoreList().contains(lineBreakpoint)) {
          return;
        }
        Breakpoint sdkBreakpoint = getMap().getSdkBreakpoint(lineBreakpoint);
        if (sdkBreakpoint == null) {
          return;
        }

        Set<MutableProperty> propertyDelta = lineBreakpoint.getChangedProperty(delta);

        if (propertyDelta.isEmpty()) {
          return;
        }

        try {
          ChromiumLineBreakpoint.Helper.updateOnRemote(sdkBreakpoint, lineBreakpoint,
              propertyDelta);
        } catch (RuntimeException e) {
          ChromiumDebugPlugin.log(new Exception("Failed to change breakpoint in " + //$NON-NLS-1$
              getTargetNameSafe(), e));
        } catch (CoreException e) {
          ChromiumDebugPlugin.log(new Exception("Failed to change breakpoint in " + //$NON-NLS-1$
              getTargetNameSafe(), e));
        }
      }

      @Override
      void breakpointRemoved(ChromiumLineBreakpoint lineBreakpoint,
          IMarkerDelta delta) {
        if (ChromiumLineBreakpoint.getIgnoreList().contains(lineBreakpoint)) {
          return;
        }

        Breakpoint sdkBreakpoint = getMap().getSdkBreakpoint(lineBreakpoint);
        if (sdkBreakpoint == null) {
          return;
        }

        if (!lineBreakpoint.isEnabled()) {
          return;
        }
        JavascriptVm.BreakpointCallback callback = new JavascriptVm.BreakpointCallback() {
          public void failure(String errorMessage) {
            ChromiumDebugPlugin.log(new Exception("Failed to remove breakpoint in " + //$NON-NLS-1$
                getTargetNameSafe() + ": " + errorMessage)); //$NON-NLS-1$
          }
          public void success(Breakpoint breakpoint) {
          }
        };
        try {
          sdkBreakpoint.clear(callback, null);
        } catch (RuntimeException e) {
          ChromiumDebugPlugin.log(new Exception("Failed to remove breakpoint in " + //$NON-NLS-1$
              getTargetNameSafe(), e));
        }
        getMap().remove(lineBreakpoint);
      }

      @Override
      void breakpointManagerEnablementChanged(boolean enabled) {
        enablementMonitor.setState(enabled);
      }

      @Override
      ChromiumLineBreakpoint tryCastBreakpoint(IBreakpoint breakpoint) {
        if (connectedTargetData.getDebugTarget().isDisconnected()) {
          return null;
        }
        return ChromiumBreakpointAdapter.tryCastBreakpoint(breakpoint);
      }

      
		@Override
		ChromiumLineBreakpoint tryCastBreakpointOnAddition(IBreakpoint breakpoint) {
			if (connectedTargetData.getDebugTarget().isDisconnected()) {
				return null;
			}
			return ChromiumBreakpointAdapter.tryCastBreakpointOnAddition(breakpoint);
		}

		@Override
		ChromiumLineBreakpoint tryCastBreakpointOnRemoval(IBreakpoint breakpoint) {
			if (connectedTargetData.getDebugTarget().isDisconnected()) {
				return null;
			}
			return ChromiumBreakpointAdapter.tryCastBreakpointOnRemoval(breakpoint);
		}
	}

    private class ExceptionBreakpointHandler extends
        BreakpointMapperBase<ExceptionBreakpointHandler.FakeSdkBreakpoint,
        ChromiumExceptionBreakpoint> {
      private final List<FakeSdkBreakpoint> breakpoints = new ArrayList<FakeSdkBreakpoint>(2);
      private JavascriptVm.ExceptionCatchMode currentRemoteFlag = null;
      private boolean breakpointsEnabled;

      /**
       * Represents an SDK breakpoint object similar to {@link IBreakpoint} for line breakpoints.
       * There is no real SDK exception breakpoints, we use {@link JavascriptVm#enableBreakpoints}
       * directly.
       */
      class FakeSdkBreakpoint {
        boolean includeCaught;
        boolean enabled;

        void initProperties(ChromiumExceptionBreakpoint uiBreakpoint) {
          includeCaught = uiBreakpoint.getIncludeCaught();
          try {
            enabled = uiBreakpoint.isEnabled();
          } catch (CoreException e) {
            ChromiumDebugPlugin.log(e);
          }
        }
      }

      @Override
      synchronized void breakpointManagerEnablementChanged(boolean enabled) {
        breakpointsEnabled = enabled;
        updateRemoteState();
      }

      public Collection<? extends IBreakpoint> getHitBreakpoints(boolean isUncaught) {
        List<ChromiumExceptionBreakpoint> result = new ArrayList<ChromiumExceptionBreakpoint>(2);
        synchronized (this) {
          for (FakeSdkBreakpoint sdkBreakpoint : breakpoints) {
            if (!isUncaught && !sdkBreakpoint.includeCaught) {
              continue;
            }
            ChromiumExceptionBreakpoint uiBreakpoint = getMap().getUiBreakpoint(sdkBreakpoint);
            if (uiBreakpoint == null) {
              continue;
            }
            result.add(uiBreakpoint);
          }
        }
        return result;
      }

      @Override
      synchronized void initEnablement(IBreakpointManager breakpointManager) {
        breakpointsEnabled = breakpointManager.isEnabled();
      }

      public void registerLocalBreakpoints(
          Collection<ChromiumExceptionBreakpoint> collection) {
        synchronized (this) {
          for (ChromiumExceptionBreakpoint uiBreakpoint : collection) {
            FakeSdkBreakpoint sdkBreakpoint = new FakeSdkBreakpoint();
            sdkBreakpoint.initProperties(uiBreakpoint);
            breakpoints.add(sdkBreakpoint);
            getMap().add(sdkBreakpoint, uiBreakpoint);
          }
        }
        updateRemoteState();
      }

      @Override
      void breakpointAdded(ChromiumExceptionBreakpoint uiBreakpoint) {
        FakeSdkBreakpoint sdkBreakpoint = new FakeSdkBreakpoint();
        sdkBreakpoint.initProperties(uiBreakpoint);
        synchronized (this) {
          breakpoints.add(sdkBreakpoint);
          getMap().add(sdkBreakpoint, uiBreakpoint);
        }
        updateRemoteState();
      }

      @Override
      void breakpointChanged(ChromiumExceptionBreakpoint uiBreakpoint,
          IMarkerDelta delta) {
        FakeSdkBreakpoint sdkBreakpoint = getMap().getSdkBreakpoint(uiBreakpoint);
        if (sdkBreakpoint == null) {
          return;
        }
        boolean includeCaught = uiBreakpoint.getIncludeCaught();
        boolean enabled;
        try {
          enabled = uiBreakpoint.isEnabled();
        } catch (CoreException e) {
          throw new RuntimeException(e);
        }
        boolean changed = false;
        synchronized (this) {
          if (includeCaught != sdkBreakpoint.includeCaught) {
            changed = true;
            sdkBreakpoint.includeCaught = includeCaught;
          }
          if (enabled != sdkBreakpoint.enabled) {
            changed = true;
            sdkBreakpoint.enabled = enabled;
          }
        }
        if (changed) {
          updateRemoteState();
        }
      }

      @Override
      void breakpointRemoved(ChromiumExceptionBreakpoint uiBreakpoint,
          IMarkerDelta delta) {
        FakeSdkBreakpoint sdkBreakpoint = getMap().getSdkBreakpoint(uiBreakpoint);
        if (sdkBreakpoint == null) {
          return;
        }
        synchronized (this) {
          breakpoints.remove(sdkBreakpoint);
          getMap().remove(uiBreakpoint);
        }
        updateRemoteState();
      }

      void updateRemoteState() {
        JavascriptVm.ExceptionCatchMode newRemoteFlag = JavascriptVm.ExceptionCatchMode.NONE;

        synchronized (this) {
          if (breakpointsEnabled) {
            for (FakeSdkBreakpoint sdkBreakpoint : breakpoints) {
              if (!sdkBreakpoint.enabled) {
                continue;
              }
              if (sdkBreakpoint.includeCaught) {
                newRemoteFlag = JavascriptVm.ExceptionCatchMode.ALL;
                break;
              }
              newRemoteFlag = JavascriptVm.ExceptionCatchMode.UNCAUGHT;
            }
          }
          if (newRemoteFlag != currentRemoteFlag) {
            javascriptVm.setBreakOnException(newRemoteFlag, null, null);
            currentRemoteFlag = newRemoteFlag;
          }
        }
      }

      @Override
      ChromiumExceptionBreakpoint tryCastBreakpoint(IBreakpoint breakpoint) {
        if (connectedTargetData.getDebugTarget().isDisconnected()) {
          return null;
        }
        return ChromiumExceptionBreakpoint.tryCastBreakpoint(breakpoint);
      }

	@Override
	ChromiumExceptionBreakpoint tryCastBreakpointOnAddition(IBreakpoint breakpoint) {
		return tryCastBreakpoint(breakpoint);

	}

	@Override
	ChromiumExceptionBreakpoint tryCastBreakpointOnRemoval(IBreakpoint breakpoint) {
		return tryCastBreakpoint(breakpoint);
	}
    }

    private abstract class BreakpointMapperBase<SDK, UI> {
      boolean breakpointAdded(IBreakpoint breakpoint) {
        UI castBreakpoint = tryCastBreakpointOnAddition(breakpoint);
        if (castBreakpoint == null) {
          return false;
        }
        breakpointAdded(castBreakpoint);
        return true;
      }

      boolean breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
        UI castBreakpoint = tryCastBreakpoint(breakpoint);
        if (castBreakpoint == null) {
          return false;
        }
        breakpointChanged(castBreakpoint, delta);
        return true;
      }

      boolean breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
        UI castBreakpoint = tryCastBreakpointOnRemoval(breakpoint);
        if (castBreakpoint == null) {
          return false;
        }
        breakpointRemoved(castBreakpoint, delta);
        return true;
      }

      abstract void initEnablement(IBreakpointManager breakpointManager);

      abstract void breakpointManagerEnablementChanged(boolean enabled);

      abstract void breakpointAdded(UI breakpoint);
      abstract void breakpointChanged(UI breakpoint, IMarkerDelta delta);
      abstract void breakpointRemoved(UI breakpoint, IMarkerDelta delta);

      abstract UI tryCastBreakpoint(IBreakpoint breakpoint);
      abstract UI tryCastBreakpointOnAddition(IBreakpoint breakpoint);
      abstract UI tryCastBreakpointOnRemoval(IBreakpoint breakpoint);

      protected BreakpointInTargetMap<SDK, UI> getMap() {
        return map;
      }

      private final BreakpointInTargetMap<SDK, UI> map = new BreakpointInTargetMap<SDK, UI>();
    }
  }

  private final static JsLabelProvider LABEL_PROVIDER = new JsLabelProvider() {
    public String getTargetLabel(DebugTargetImpl debugTarget) {
      String name = debugTarget.getName();
      String status = debugTarget.getVmStatus();
      if (status == null) {
        return name;
      } else {
        return NLS.bind(Messages.DebugTargetImpl_TARGET_NAME_PATTERN, name, status);
      }
    }

    public String getThreadLabel(JavascriptThread thread) {
      String url = thread.getConnectedData().getJavascriptEmbedder().getThreadName();
      return NLS.bind(Messages.JsThread_ThreadLabelFormat,
          getThreadStateLabel(thread),
          (url.length() > 0 ? (" : " + url) : "")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private String getThreadStateLabel(JavascriptThread thread) {
      return thread.describeState(THREAD_DESCRIBE_VISITOR);
    }

    private final JavascriptThread.StateVisitor<String> THREAD_DESCRIBE_VISITOR =
        new JavascriptThread.StateVisitor<String>() {
          @Override
          public String visitResumed(JavascriptThread.ResumeReason resumeReason) {
            return Messages.JsThread_ThreadLabelRunning;
          }

          @Override
          public String visitSuspended(IBreakpoint[] breakpoints,
              ExceptionData exceptionData) {
            if (exceptionData != null) {
              return NLS.bind(Messages.JsThread_ThreadLabelSuspendedExceptionFormat,
                  exceptionData.getExceptionMessage());
            } else {
              return Messages.JsThread_ThreadLabelSuspended;
            }
          }
        };

    public String getStackFrameLabel(StackFrame stackFrame) throws DebugException {
      CallFrame callFrame = stackFrame.getCallFrame();
      String name = callFrame.getFunctionName();
      Script script = callFrame.getScript();
      String scriptName;
      if (script == null) {
        scriptName = Messages.StackFrame_UnknownScriptName;
      } else {
        scriptName = VmResourceId.forScript(script).getVisibleName();
      }
      int line = stackFrame.getLineNumber();
      if (line != -1) {
        name = NLS.bind(Messages.StackFrame_NameFormat,
            new Object[] {name, scriptName, line});
      }
      return name;
    }
  };

  public ConnectedTargetData getConnectedTargetData() {
    return connectedTargetData;
  }
  
  
	class UpdateMappingVisitor implements Visitor<Object> {

		@Override
		public Object visitRegExpBased(ScriptNamePattern scriptNamePattern) {
			return null;
		}

		@Override
		public Object visitResourceId(final VmResourceId tsResourceId) {
			try {
				String fileName = tsResourceId.getName();
				int index = fileName.lastIndexOf('.');
				if (index == -1) {
					return null;
				}
				ISourceMapManager manager = ChromiumDebugPlugin.getSourceMapManager();
				String fileExtension = fileName.substring(index + 1, fileName.length());
				// check if the given resource support source map
				ISourceMapLanguageSupport support = manager.getSourceMapLanguageSupport(fileExtension);
				if (support == null) {
					return null;
				}

				IPath tsFilePath = new Path(fileName);
				final IFile tsFile = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(tsFilePath)[0];
				
				IPath jsFilePath = support.getJsFile(tsFilePath);
				final IFile jsFile = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(jsFilePath)[0];
				
				IPath sourceMapFilePath = support.getSourceMapFile(tsFilePath);
				final IFile sourceMapFile = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(sourceMapFilePath)[0];
				final SourceMap sourceMap = SourceMap.load(sourceMapFile.getContents());
				
				
				VmResourceRef jsRef = sourceDirector.findVmResourceRef(jsFile);
				
				jsRef.accept(new Visitor<Object>() {
					@Override
					public Object visitRegExpBased(ScriptNamePattern scriptNamePattern) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object visitResourceId(VmResourceId jsResourceId) {
						SourcePositionMapBuilder.ResourceSection vmResourceSection = create(jsFile, jsResourceId);
						SourcePositionMapBuilder.ResourceSection originalResourceSection = create(tsFile, tsResourceId);

						try {
							TextSectionMapping mapTable = sourceMap.getMapping(tsFile.getName());
							connectedTargetData.getSourcePositionMapBuilder().addMapping(originalResourceSection,
									vmResourceSection, mapTable);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
				});
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

	}
	
	private static SourcePositionMapBuilder.ResourceSection create(IFile file, VmResourceId resourceId) {
		IDocument document = getDocument(file);
		int endLine = document.getNumberOfLines();
		if (endLine > 0) {
			endLine--;
		}
		int endColumn = 0;
		try {
			endColumn = document.getLineLength(endLine);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return new SourcePositionMapBuilder.ResourceSection(resourceId, 0, 0, endLine, endColumn);
	}

	private static IDocument getDocument(IFile file) {
		ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
		IPath location = file.getLocation();
		boolean connected = false;
		try {
			ITextFileBuffer buffer = manager.getTextFileBuffer(location, LocationKind.NORMALIZE);
			if (buffer == null) {
				// no existing file buffer..create one
				manager.connect(location, LocationKind.NORMALIZE, new NullProgressMonitor());
				connected = true;
				buffer = manager.getTextFileBuffer(location, LocationKind.NORMALIZE);
				if (buffer == null) {
					return null;
				}
			}

			return buffer.getDocument();
		} catch (CoreException ce) {
			ChromiumDebugPlugin.log(ce);
			return null;
		} finally {
			if (connected) {
				try {
					manager.disconnect(location, LocationKind.NORMALIZE, new NullProgressMonitor());
				} catch (CoreException e) {
					ChromiumDebugPlugin.log(e);
				}
			}
		}
	}
}
