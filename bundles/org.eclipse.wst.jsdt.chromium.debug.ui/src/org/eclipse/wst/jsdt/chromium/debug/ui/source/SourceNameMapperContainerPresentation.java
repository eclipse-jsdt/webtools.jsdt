// Copyright (c) 2010 The Chromium Authors. All rights reserved.
// This program and the accompanying materials are made available
// under the terms of the Eclipse Public License v2.0 which accompanies
// this distribution, and is available at
// https://www.eclipse.org/legal/epl-2.0/

package org.eclipse.wst.jsdt.chromium.debug.ui.source;

import org.eclipse.wst.jsdt.chromium.debug.core.SourceNameMapperContainer;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.debug.ui.sourcelookup.ISourceContainerBrowser;
import org.eclipse.swt.widgets.Shell;

/**
 * A presentation for JavaScript Source Name Mapper container that supports adding and editing.
 */
public class SourceNameMapperContainerPresentation implements ISourceContainerBrowser {

  public ISourceContainer[] addSourceContainers(Shell shell, ISourceLookupDirector director) {
    return openDialog(shell, director, null);
  }

  public boolean canAddSourceContainers(ISourceLookupDirector director) {
    return true;
  }

  public boolean canEditSourceContainers(ISourceLookupDirector director,
      ISourceContainer[] containers) {
    return containers.length == 1;
  }

  public ISourceContainer[] editSourceContainers(Shell shell, ISourceLookupDirector director,
      ISourceContainer[] containers) {
    final SourceNameMapperContainer originalContainer = (SourceNameMapperContainer) containers[0];
    SourceNameMapperContainerDialog.PresetFieldValues params =
        new SourceNameMapperContainerDialog.PresetFieldValues() {
      public ISourceContainer getContainer() {
        return originalContainer.getTargetContainer();
      }
      public String getPrefix() {
        return originalContainer.getPrefix();
      }
    };

    return openDialog(shell, director, params);
  }

  private ISourceContainer[] openDialog(Shell shell, ISourceLookupDirector director,
      SourceNameMapperContainerDialog.PresetFieldValues params) {
    SourceNameMapperContainerDialog dialog =
        new SourceNameMapperContainerDialog(shell, director, params);
    dialog.open();
    SourceNameMapperContainerDialog.Result dialogResult = dialog.getResult();
    if (dialogResult == null) {
      return new ISourceContainer[0];
    }
    ISourceContainer result = new SourceNameMapperContainer(dialogResult.getResultPrefix(),
        dialogResult.getResultContainer());
    return new ISourceContainer[] { result };
  }
}
