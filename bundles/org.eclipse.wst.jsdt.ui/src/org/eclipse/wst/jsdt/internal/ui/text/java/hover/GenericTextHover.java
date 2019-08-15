/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.java.hover;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.internal.genericeditor.GenericEditorPlugin;
import org.eclipse.ui.internal.genericeditor.hover.TextHoverRegistry;

public class GenericTextHover extends AbstractJavaEditorTextHover implements ITextHoverExtension, IInformationProviderExtension2 {

	private IInformationControlCreator fPresenterControlCreator;
	
	private List<ITextHover> getGenericHovers(ISourceViewer viewer) {
		TextHoverRegistry registry = GenericEditorPlugin.getDefault().getHoverRegistry();
		IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		Set<IContentType> types = new HashSet<>();
		IContentType contentType = contentTypeManager.getContentType("org.eclipse.wst.jsdt.core.jsSource");
		types.add(contentType);
		contentType = contentTypeManager.getContentType("org.eclipse.core.runtime.text");
		types.add(contentType);
		List<ITextHover> hovers = registry.getAvailableHovers(viewer, null, types);
		return hovers;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.information.IInformationProviderExtension2#
	 * getInformationPresenterControlCreator()
	 */
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (fPresenterControlCreator == null) {
			List<ITextHover> hovers = getGenericHovers(null);
			if (hovers.isEmpty()) {
				fPresenterControlCreator = new JavadocHover().getHoverControlCreator();
			}
			else {
				fPresenterControlCreator = hovers.stream().filter(p -> p instanceof IInformationProviderExtension2).map(hover -> {
					return ((IInformationProviderExtension2) hover).getInformationPresenterControlCreator();
				}).findFirst().get();
				if (fPresenterControlCreator == null) {
					fPresenterControlCreator = hovers.stream().filter(p -> p instanceof ITextHoverExtension).map(hover -> {
						return ((ITextHoverExtension) hover).getHoverControlCreator();
					}).findFirst().get();
				}
			}
		}
		return fPresenterControlCreator;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.internal.ui.text.java.hover.
	 * AbstractJavaEditorTextHover#getHoverInfo(org.eclipse.jface.text.
	 * ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (textViewer instanceof ISourceViewer) {
			final ISourceViewer viewer = (ISourceViewer) textViewer;
			List<ITextHover> hovers = getGenericHovers(viewer);
			if (hovers != null) {
				return hovers.stream().map(hover -> hover.getHoverInfo(viewer, hoverRegion)).filter(s -> s != null).collect(Collectors.joining("\n"));
			}
		}
		return null;
	}

	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		if (textViewer instanceof ISourceViewer) {
			final ISourceViewer viewer = (ISourceViewer) textViewer;
			List<ITextHover> hovers = getGenericHovers(viewer);
			if (hovers != null) {
				for (ITextHover hover : hovers) {
					if (hover instanceof ITextHoverExtension2) {
						Object hoverInfo2 = ((ITextHoverExtension2) hover).getHoverInfo2(viewer, hoverRegion);
						if (hoverInfo2 != null) {
							return hoverInfo2;
						}

					}
				}
			}
		}
		return super.getHoverInfo2(textViewer, hoverRegion);
	}
}
