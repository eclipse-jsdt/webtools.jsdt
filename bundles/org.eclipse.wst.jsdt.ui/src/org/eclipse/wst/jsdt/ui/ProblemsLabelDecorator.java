/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.ISourceReference;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.IProblemChangedListener;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.wst.jsdt.internal.ui.viewsupport.ImageImageDescriptor;

/**
 * LabelDecorator that decorates an element's image with error and warning overlays that 
 * represent the severity of markers attached to the element's underlying resource. To see 
 * a problem decoration for a marker, the marker needs to be a subtype of <code>IMarker.PROBLEM</code>.
 * <p>
 * <b>Important</b>: Although this decorator implements ILightweightLabelDecorator, do not contribute this
 * class as a decorator to the <code>org.eclipse.ui.decorators</code> extension. Only use this class in your
 * own views and label providers. 
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 */
public class ProblemsLabelDecorator implements ILabelDecorator, ILightweightLabelDecorator {
	
	/**
	 * This is a special <code>LabelProviderChangedEvent</code> carrying additional 
	 * information whether the event origins from a maker change.
	 * <p>
	 * <code>ProblemsLabelChangedEvent</code>s are only generated by <code>
	 * ProblemsLabelDecorator</code>s.
	 * </p>
	 */
	public static class ProblemsLabelChangedEvent extends LabelProviderChangedEvent {

		private static final long serialVersionUID= 1L;
		
		private boolean fMarkerChange;

		/**
		 * Note: This constructor is for internal use only. Clients should not call this constructor.
		 * 
		 * @param eventSource the base label provider
		 * @param changedResource the changed resources
		 * @param isMarkerChange <code>true</code> if the change is a marker change; otherwise
		 *  <code>false</code> 
		 */
		public ProblemsLabelChangedEvent(IBaseLabelProvider eventSource, IResource[] changedResource, boolean isMarkerChange) {
			super(eventSource, changedResource);
			fMarkerChange= isMarkerChange;
		}
		
		/**
		 * Returns whether this event origins from marker changes. If <code>false</code> an annotation 
		 * model change is the origin. In this case viewers not displaying working copies can ignore these 
		 * events.
		 * 
		 * @return if this event origins from a marker change.
		 */
		public boolean isMarkerChange() {
			return fMarkerChange;
		}

	}

	private static final int ERRORTICK_WARNING= JavaScriptElementImageDescriptor.WARNING;
	private static final int ERRORTICK_ERROR= JavaScriptElementImageDescriptor.ERROR;	

	private ImageDescriptorRegistry fRegistry;
	private boolean fUseNewRegistry= false;
	private IProblemChangedListener fProblemChangedListener;
	
	private ListenerList fListeners;
	private ISourceRange fCachedRange;

	/**
	 * Creates a new <code>ProblemsLabelDecorator</code>.
	 */
	public ProblemsLabelDecorator() {
		this(null);
		fUseNewRegistry= true;
	}
	
	/**
	 * Note: This constructor is for internal use only. Clients should not call this constructor.
	 * 
	 * @param registry The registry to use or <code>null</code> to use the JavaScript plugin's
	 *  image registry
	 */
	public ProblemsLabelDecorator(ImageDescriptorRegistry registry) {
		fRegistry= registry;
		fProblemChangedListener= null;
	}
	
	private ImageDescriptorRegistry getRegistry() {
		if (fRegistry == null) {
			fRegistry= fUseNewRegistry ? new ImageDescriptorRegistry() : JavaScriptPlugin.getImageDescriptorRegistry();
		}
		return fRegistry;
	}
	

	/* (non-Javadoc)
	 * @see ILabelDecorator#decorateText(String, Object)
	 */
	public String decorateText(String text, Object element) {
		return text;
	}	

	/* (non-Javadoc)
	 * @see ILabelDecorator#decorateImage(Image, Object)
	 */
	public Image decorateImage(Image image, Object obj) {
		int adornmentFlags= computeAdornmentFlags(obj);
		if (adornmentFlags != 0) {
			ImageDescriptor baseImage= new ImageImageDescriptor(image);
			Rectangle bounds= image.getBounds();
			return getRegistry().get(new JavaScriptElementImageDescriptor(baseImage, adornmentFlags, new Point(bounds.width, bounds.height)));
		}
		return image;
	}

	/**
	 * Note: This method is for internal use only. Clients should not call this method.
	 * 
	 * @param obj the element to compute the flags for
	 * 
	 * @return the adornment flags
	 */
	protected int computeAdornmentFlags(Object obj) {
		try {
			if (obj instanceof IJavaScriptElement) {
				IJavaScriptElement element= (IJavaScriptElement) obj;
				int type= element.getElementType();
				switch (type) {
					case IJavaScriptElement.JAVASCRIPT_MODEL:
					case IJavaScriptElement.JAVASCRIPT_PROJECT:
					case IJavaScriptElement.PACKAGE_FRAGMENT_ROOT:
						return getErrorTicksFromMarkers(element.getResource(), IResource.DEPTH_INFINITE, null);
					case IJavaScriptElement.PACKAGE_FRAGMENT:
					case IJavaScriptElement.JAVASCRIPT_UNIT:
					case IJavaScriptElement.CLASS_FILE:
						return getErrorTicksFromMarkers(element.getResource(), IResource.DEPTH_ONE, null);
					case IJavaScriptElement.IMPORT_DECLARATION:
					case IJavaScriptElement.IMPORT_CONTAINER:
					case IJavaScriptElement.EXPORT_DECLARATION:
					case IJavaScriptElement.EXPORT_CONTAINER:
					case IJavaScriptElement.TYPE:
					case IJavaScriptElement.INITIALIZER:
					case IJavaScriptElement.METHOD:
					case IJavaScriptElement.FIELD:
					case IJavaScriptElement.LOCAL_VARIABLE:
						IJavaScriptUnit cu= (IJavaScriptUnit) element.getAncestor(IJavaScriptElement.JAVASCRIPT_UNIT);
						if (cu != null) {
							ISourceReference ref= (type == IJavaScriptElement.JAVASCRIPT_UNIT) ? null : (ISourceReference) element;
							// The assumption is that only source elements in compilation unit can have markers
							IAnnotationModel model= isInJavaAnnotationModel(cu);
							int result= 0;
							if (model != null) {
								// open in JavaScript editor: look at annotation model
								result= getErrorTicksFromAnnotationModel(model, ref);
							} else {
								result= getErrorTicksFromMarkers(cu.getResource(), IResource.DEPTH_ONE, ref);
							}
							fCachedRange= null;
							return result;
						}
						break;
					default:
				}
			} else if (obj instanceof IResource) {
				return getErrorTicksFromMarkers((IResource) obj, IResource.DEPTH_INFINITE, null);
			}
		} catch (CoreException e) {
			if (e instanceof JavaScriptModelException) {
				if (((JavaScriptModelException) e).isDoesNotExist()) {
					return 0;
				}
			}
			if (e.getStatus().getCode() == IResourceStatus.MARKER_NOT_FOUND) {
				return 0;
			}
			
			JavaScriptPlugin.log(e);
		}
		return 0;
	}

	private int getErrorTicksFromMarkers(IResource res, int depth, ISourceReference sourceElement) throws CoreException {
		if (res == null || !res.isAccessible()) {
			return 0;
		}
		int severity= 0;
		if (sourceElement == null) {
			severity= res.findMaxProblemSeverity(IMarker.PROBLEM, true, depth);
		} else {
			IMarker[] markers= res.findMarkers(IMarker.PROBLEM, true, depth);
			if (markers != null && markers.length > 0) {
				for (int i= 0; i < markers.length && (severity != IMarker.SEVERITY_ERROR); i++) {
					IMarker curr= markers[i];
					if (isMarkerInRange(curr, sourceElement)) {
						int val= curr.getAttribute(IMarker.SEVERITY, -1);
						if (val == IMarker.SEVERITY_WARNING || val == IMarker.SEVERITY_ERROR) {
							severity= val;
						}
					}			
				}
			}
		}
		if (severity == IMarker.SEVERITY_ERROR) {
			return ERRORTICK_ERROR;
		} else if (severity == IMarker.SEVERITY_WARNING) {
			return ERRORTICK_WARNING;
		}
		return 0;
	}

	private boolean isMarkerInRange(IMarker marker, ISourceReference sourceElement) throws CoreException {
		if (marker.isSubtypeOf(IMarker.TEXT)) {
			int pos= marker.getAttribute(IMarker.CHAR_START, -1);
			return isInside(pos, sourceElement);
		}
		return false;
	}
	
	private IAnnotationModel isInJavaAnnotationModel(IJavaScriptUnit original) {
		if (original.isWorkingCopy()) {
			FileEditorInput editorInput= new FileEditorInput((IFile) original.getResource());
			return JavaScriptPlugin.getDefault().getCompilationUnitDocumentProvider().getAnnotationModel(editorInput);
		}
		return null;
	}
	
	
	private int getErrorTicksFromAnnotationModel(IAnnotationModel model, ISourceReference sourceElement) throws CoreException {
		int info= 0;
		Iterator iter= model.getAnnotationIterator();
		while ((info != ERRORTICK_ERROR) && iter.hasNext()) {
			Annotation annot= (Annotation) iter.next();
			IMarker marker= isAnnotationInRange(model, annot, sourceElement);
			if (marker != null) {
				int priority= marker.getAttribute(IMarker.SEVERITY, -1);
				if (priority == IMarker.SEVERITY_WARNING) {
					info= ERRORTICK_WARNING;
				} else if (priority == IMarker.SEVERITY_ERROR) {
					info= ERRORTICK_ERROR;
				}
			}
		}
		return info;
	}
			
	private IMarker isAnnotationInRange(IAnnotationModel model, Annotation annot, ISourceReference sourceElement) throws CoreException {
		if (annot instanceof MarkerAnnotation) {
			if (sourceElement == null || isInside(model.getPosition(annot), sourceElement)) {
				IMarker marker= ((MarkerAnnotation) annot).getMarker();
				if (marker.exists() && marker.isSubtypeOf(IMarker.PROBLEM)) {
					return marker;
				}
			}
		}
		return null;
	}
	
	private boolean isInside(Position pos, ISourceReference sourceElement) throws CoreException {
		return pos != null && isInside(pos.getOffset(), sourceElement);
	}
	
	/**
	 * Tests if a position is inside the source range of an element.
	 * @param pos Position to be tested.
	 * @param sourceElement Source element (must be a IJavaScriptElement)
	 * @return boolean Return <code>true</code> if position is located inside the source element.
	 * @throws CoreException Exception thrown if element range could not be accessed.
	 * 
	 * 
	 */
	protected boolean isInside(int pos, ISourceReference sourceElement) throws CoreException {
		if (fCachedRange == null) {
			fCachedRange= sourceElement.getSourceRange();
		}
		ISourceRange range= fCachedRange;
		if (range != null) {
			int rangeOffset= range.getOffset();
			return (rangeOffset <= pos && rangeOffset + range.getLength() > pos);			
		}
		return false;
	}	
	
	/* (non-Javadoc)
	 * @see IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		if (fProblemChangedListener != null) {
			JavaScriptPlugin.getDefault().getProblemMarkerManager().removeListener(fProblemChangedListener);
			fProblemChangedListener= null;
		}
		if (fRegistry != null && fUseNewRegistry) {
			fRegistry.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see IBaseLabelProvider#isLabelProperty(Object, String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see IBaseLabelProvider#addListener(ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		if (fListeners == null) {
			fListeners= new ListenerList();
		}
		fListeners.add(listener);
		if (fProblemChangedListener == null) {
			fProblemChangedListener= new IProblemChangedListener() {
				public void problemsChanged(IResource[] changedResources, boolean isMarkerChange) {
					fireProblemsChanged(changedResources, isMarkerChange);
				}
			};
			JavaScriptPlugin.getDefault().getProblemMarkerManager().addListener(fProblemChangedListener);
		}
	}	

	/* (non-Javadoc)
	 * @see IBaseLabelProvider#removeListener(ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
		if (fListeners != null) {
			fListeners.remove(listener);
			if (fListeners.isEmpty() && fProblemChangedListener != null) {
				JavaScriptPlugin.getDefault().getProblemMarkerManager().removeListener(fProblemChangedListener);
				fProblemChangedListener= null;
			}
		}
	}
	
	private void fireProblemsChanged(IResource[] changedResources, boolean isMarkerChange) {
		if (fListeners != null && !fListeners.isEmpty()) {
			LabelProviderChangedEvent event= new ProblemsLabelChangedEvent(this, changedResources, isMarkerChange);
			Object[] listeners= fListeners.getListeners();
			for (int i= 0; i < listeners.length; i++) {
				((ILabelProviderListener) listeners[i]).labelProviderChanged(event);
			}
		}
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object, org.eclipse.jface.viewers.IDecoration)
	 */
	public void decorate(Object element, IDecoration decoration) { 
		int adornmentFlags= computeAdornmentFlags(element);
		if (adornmentFlags == ERRORTICK_ERROR) {
			decoration.addOverlay(JavaPluginImages.DESC_OVR_ERROR);
		} else if (adornmentFlags == ERRORTICK_WARNING) {
			decoration.addOverlay(JavaPluginImages.DESC_OVR_WARNING);
		}		
	}

}
