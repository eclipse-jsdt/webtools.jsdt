/*******************************************************************************
 * Copyright (c) 2010, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptFunctionBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.ui.DebugWCManager;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.ASTProvider;

/**
 * JavaScript adapter for toggling breakpoints in the JSDT editor
 *
 * @since 1.0
 */
public class ToggleBreakpointAdapter implements IToggleBreakpointsTargetExtension {

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension#canToggleBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}

	/**
	 * Toggles a line breakpoint
	 * @param part
	 * @param selection
	 * @param element
	 */
	void toggleLineBreakpoint(final IWorkbenchPart part, final ITextSelection selection, final IJavaScriptElement element, final int linenumber) {
		Job job = new Job("Toggle Line Breakpoints") { //$NON-NLS-1$
            protected IStatus run(IProgressMonitor monitor) {
            	try {
            		ITextEditor editor = getTextEditor(part);
					if(editor != null && part instanceof IEditorPart) {
						if(element == null) {
							reportToStatusLine(part, Messages.failed_line_bp_no_element);
							return Status.CANCEL_STATUS;
						}
						IResource resource = element.getResource();
						if(resource == null) {
							reportToStatusLine(part, NLS.bind(Messages.failed_line_bp_no_resource, element.getElementName()));
							return Status.CANCEL_STATUS;
						}
						IBreakpoint bp = lineBreakpointExists(resource, linenumber);
						if(bp != null) {
							DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(bp, true);
							return Status.OK_STATUS;
						}
						IDocumentProvider documentProvider = editor.getDocumentProvider();
						IDocument document = documentProvider.getDocument(editor.getEditorInput());
						int charstart = -1, charend = -1;
						try {
							IRegion line = document.getLineInformation(linenumber - 1);
							charstart = line.getOffset();
							charend = charstart + line.getLength();
						}
						catch (BadLocationException ble) {}
						HashMap attributes = new HashMap();
						attributes.put(IJavaScriptBreakpoint.TYPE_NAME, getTypeName(element));
						attributes.put(IJavaScriptBreakpoint.SCRIPT_PATH, getScriptPath(element));
						attributes.put(IJavaScriptBreakpoint.ELEMENT_HANDLE, element.getHandleIdentifier());
						JavaScriptDebugModel.createLineBreakpoint(resource, linenumber, charstart, charend, attributes, true);
						return Status.OK_STATUS;
					}
					reportToStatusLine(part, Messages.failed_to_create_line_bp);
					return Status.CANCEL_STATUS;
	            }
	        	catch(CoreException ce) {
	        		return ce.getStatus();
        		}
        	}
		};
		job.setPriority(Job.INTERACTIVE);
        job.setSystem(true);
        job.schedule();
	}

	/**
	 * Callback from {@link JavaScriptHtmlBreakpointProvider}
	 * @param resource
	 * @param document
	 * @param linenumber
	 * @throws CoreException
	 */
	void addBreakpoint(IResource resource, IDocument document, int linenumber) throws CoreException {
		IBreakpoint bp = lineBreakpointExists(resource, linenumber);
		if(bp != null) {
			DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(bp, true);
		}
		int charstart = -1, charend = -1;
		try {
			IRegion line = document.getLineInformation(linenumber - 1);
			charstart = line.getOffset();
			charend = charstart + line.getLength();
		}
		catch (BadLocationException ble) {}
		HashMap attributes = new HashMap();
		attributes.put(IJavaScriptBreakpoint.TYPE_NAME, null);
		attributes.put(IJavaScriptBreakpoint.SCRIPT_PATH, resource.getFullPath().makeAbsolute().toString());
		attributes.put(IJavaScriptBreakpoint.ELEMENT_HANDLE, null);
		JavaScriptDebugModel.createLineBreakpoint(resource, linenumber, charstart, charend, attributes, true);
	}

	/**
	 * Returns the path to the script in the workspace or the name of the script in the event it is
	 * and external or virtual script
	 * @param element
	 * @return the path to the script
	 */
	String getScriptPath(IJavaScriptElement element) {
		IPath path = element.getPath();
		return path.makeAbsolute().toString();
	}

	/**
	 * Resolves the type name from the given element
	 * @param element
	 * @return
	 */
	String getTypeName(IJavaScriptElement element) {
		switch(element.getElementType()) {
			case IJavaScriptElement.TYPE: {
				return ((IType)element).getFullyQualifiedName();
			}
			case IJavaScriptElement.METHOD:
			case IJavaScriptElement.FIELD: {
				IMember member = (IMember) element;
				IType type = member.getDeclaringType();
				if(type != null) {
					return type.getFullyQualifiedName();
				}
			}
			//$FALL-THROUGH$
			default: {
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleLineBreakpoints(final IWorkbenchPart part, final ISelection selection) throws CoreException {
		if (!(part instanceof IEditorPart) || !(selection instanceof ITextSelection)) {
			return;
		}

		ITextEditor textEditor = part.getAdapter(ITextEditor.class);
		if (textEditor != null) {
			IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
			if (document != null) {
				int lineNumber;
				try {
					lineNumber = document.getLineOfOffset(((ITextSelection) selection).getOffset());
					addBreakpoint(getResource((IEditorPart) part), document, lineNumber + 1);
				}
				catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Resolves the declaring type for the member
	 * @param member
	 * @return the declaring type for the member
	 */
	IType resolveType(IJavaScriptElement element) {
		switch(element.getElementType()) {
			case IJavaScriptElement.TYPE: {
				return (IType) element;
			}
			case IJavaScriptElement.METHOD:
			case IJavaScriptElement.FIELD: {
				IMember member = (IMember) element;
				IType type = member.getDeclaringType();
				if(type == null) {
					ITypeRoot root = (ITypeRoot) member.getParent();
					type = root.findPrimaryType();
				}
				return type;
			}
			default: {
				//already a type root
				if(element instanceof ITypeRoot) {
					return ((ITypeRoot)element).findPrimaryType();
				}
			}
		}
		return null;
	}

	/**
	 * Resolves the region from the given document and tries to parse the text of the region. Returns the {@link JavaScriptUnit}
	 * representing the region or <code>null</code> if it could not be computed
	 *
	 * @param doc
	 * @param offset
	 * @return the {@link JavaScriptUnit} for the region from the document or <code>null</code>
	 */
	JavaScriptUnit parse(IDocument doc, int offset) {
		try {
			ITypedRegion region = doc.getPartition(offset);
			if(region != null) {
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				//parser.setKind(ASTParser.K_STATEMENTS);
				parser.setSource(doc.get(offset, region.getLength()).toCharArray());
				return (JavaScriptUnit)parser.createAST(null);
			}
		}
		catch(BadLocationException ble) {
			//do nothing, return null
		}
		return null;
	}

	/**
	 * Returns the resource on which a breakpoint marker should
	 * be created for the given member. The resource returned is the
	 * associated file, or workspace root in the case of a binary in
	 * an external archive.
	 *
	 * @param element member in which a breakpoint is being created
	 * @return resource the resource on which a breakpoint marker
	 *  should be created
	 */
	IResource getBreakpointResource(IJavaScriptElement element) {
		IResource res = element.getResource();
		if (res == null) {
			res = ResourcesPlugin.getWorkspace().getRoot();
		}
		else if(!res.getProject().exists()) {
			res = ResourcesPlugin.getWorkspace().getRoot();
		}
		return res;
	}

	/**
	 * Returns the {@link ITypeRoot} for the given editor input
	 * @param input
	 * @return the {@link ITypeRoot} for the editor input or <code>null</code>
	 */
	ITypeRoot getTypeRoot(IEditorInput input) {
		ITypeRoot root = input.getAdapter(IClassFile.class);
		if(root == null) {
			root = DebugWCManager.getCompilationUnit(input, false);
		}
		return root;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		return selection instanceof ITextSelection;
	}

	/**
	 * Delegate for toggling a method breakpoint
	 * @param part
	 * @param element
	 * @param line
	 */
	void toggleMethodBreakpoint(final IWorkbenchPart part, final IJavaScriptElement element, final int line) {
		Job job = new Job("Toggle Function Breakpoints") { //$NON-NLS-1$
            protected IStatus run(IProgressMonitor monitor) {
            	try {
					if(element == null) {
						reportToStatusLine(part, Messages.failed_function_bp_no_element);
						return Status.CANCEL_STATUS;
					}
					if(element.getElementType() == IJavaScriptElement.METHOD) {
						IFunction method = (IFunction) element;
						IResource resource = element.getResource();
						if(resource == null) {
							reportToStatusLine(part, NLS.bind(Messages.failed_function_bp_no_resource, element.getElementName()));
							return Status.CANCEL_STATUS;
						}
						IBreakpoint bp = methodBreakpointExists(resource, method.getElementName(), method.getSignature());
						if(bp != null) {
							DebugPlugin.getDefault().getBreakpointManager().removeBreakpoint(bp, true);
							return Status.OK_STATUS;
						}
						ISourceRange range = method.getNameRange();
						int start = -1, end = -1;
						if(range != null) {
							start = range.getOffset();
							end = start + range.getLength();
						}
						HashMap attributes = new HashMap();
						//hack if the type is null the member is declared in a top-level type
						//nothing else we can do
						attributes.put(IJavaScriptBreakpoint.TYPE_NAME, getTypeName(element));
						attributes.put(IJavaScriptBreakpoint.SCRIPT_PATH, getScriptPath(element));
						attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(line));
						IJavaScriptFunctionBreakpoint breakpoint = JavaScriptDebugModel.createFunctionBreakpoint(resource, method.getElementName(), method.getSignature(), start, end, attributes, true);
						breakpoint.setJavaScriptElementHandle(element.getHandleIdentifier());
						return Status.OK_STATUS;
					}
					reportToStatusLine(part, Messages.failed_to_create_function_bp);
				}
	        	catch(CoreException ce) {
	        		return ce.getStatus();
        		}
	        	return Status.CANCEL_STATUS;
        	}
		};
		job.setPriority(Job.INTERACTIVE);
        job.setSystem(true);
        job.schedule();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleMethodBreakpoints(final IWorkbenchPart part, final ISelection selection) throws CoreException {
		//do nothing
	}

	/**
	 * Sends a friendly message to the status line about why we could not set a breakpoint
	 * @param part
	 * @param message
	 */
	void reportToStatusLine(final IWorkbenchPart part, final String message) {
		getStandardDisplay().asyncExec(new Runnable() {
            public void run() {
				IEditorStatusLine statusLine = part.getAdapter(IEditorStatusLine.class);
		        if (statusLine != null) {
		            if (message != null) {
		                statusLine.setMessage(true, message, null);
		            } else {
		                statusLine.setMessage(true, null, null);
		            }
		        }
            }
		});
	}

	/**
	 * Returns the standard display to be used.
	 */
	Display getStandardDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTargetExtension#toggleBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		if(selection instanceof ITextSelection) {
			ITextEditor textEditor = getTextEditor(part);
			if(textEditor == null) {
				reportToStatusLine(part, Messages.no_editor_could_be_found);
				return;
			}
			ITypeRoot root = getTypeRoot(textEditor.getEditorInput());
            if(root instanceof IJavaScriptUnit) {
            	IJavaScriptUnit unit = (IJavaScriptUnit) root;
                synchronized (unit) {
                    unit.reconcile(IJavaScriptUnit.NO_AST , false, null, null);
                }
            }
            if(root == null) {
            	reportToStatusLine(part, Messages.type_root_could_not_be_computed);
            	return;
            }
			JavaScriptUnit jsunit = JavaScriptPlugin.getDefault().getASTProvider().getAST(root, ASTProvider.WAIT_YES, null);
			BreakpointLocationFinder finder = new BreakpointLocationFinder(jsunit, ((TextSelection)selection).getStartLine()+1, false);
			jsunit.accept(finder);
			switch(finder.getLocation()) {
				case BreakpointLocationFinder.UNKNOWN : {
					reportToStatusLine(part, Messages.no_valid_location);
					return;
				}
				case BreakpointLocationFinder.LINE: {
					IJavaScriptElement element = root.getElementAt(finder.getOffset());
					if(element == null) {
						element = root;
					}
					toggleLineBreakpoint(part, (ITextSelection) selection, element, finder.getLineNumber());
					return;
				}
				case BreakpointLocationFinder.FUNCTION: {
					toggleMethodBreakpoint(part, root.getElementAt(finder.getOffset()), finder.getLineNumber());
					return;
				}
			}
		}
		else {
			IStructuredSelection ss = (IStructuredSelection) selection;
        	Object o = ss.getFirstElement();
        	if(o instanceof IMember) {
        		IMember member = (IMember) o;
        		if(member.getElementType() == IJavaScriptElement.METHOD) {
    				toggleMethodBreakpoint(part, member, -1);
    			}
        	}
		}
	}

	/**
	 * Returns if a line breakpoint exists on the given line in the given resource
	 * @param resource
	 * @param linenumber
	 * @return true if a line breakpoint exists on the given line in the given resource, false otherwise
	 */
	IBreakpoint lineBreakpointExists(IResource resource, int linenumber) {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
		IJavaScriptLineBreakpoint breakpoint = null;
		for (int i = 0; i < breakpoints.length; i++) {
			if(breakpoints[i] instanceof IJavaScriptLineBreakpoint) {
				breakpoint = (IJavaScriptLineBreakpoint) breakpoints[i];
				try {
					if(IJavaScriptLineBreakpoint.MARKER_ID.equals(breakpoint.getMarker().getType()) &&
						resource.equals(breakpoint.getMarker().getResource()) &&
						linenumber == breakpoint.getLineNumber()) {
						return breakpoint;
					}
				} catch (CoreException e) {}
			}
		}
		return null;
	}

	/**
	 * Returns if a method breakpoint exists with the given name and signature in the given resource
	 * @param resource
	 * @param name
	 * @param signature
	 * @return true if a method breakpoint exists with the given name and signature in the given resource, false otherwise
	 */
	IBreakpoint methodBreakpointExists(IResource resource, String name, String signature) {
		IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
		IJavaScriptBreakpoint breakpoint = null;
		for (int i = 0; i < breakpoints.length; i++) {
			breakpoint = (IJavaScriptBreakpoint) breakpoints[i];
			try {
				if(IJavaScriptFunctionBreakpoint.MARKER_ID.equals(breakpoint.getMarker().getType()) &&
					resource.equals(breakpoint.getMarker().getResource()) &&
					name.equals(breakpoint.getMarker().getAttribute(IJavaScriptFunctionBreakpoint.FUNCTION_NAME)) &&
					signature.equals(breakpoint.getMarker().getAttribute(IJavaScriptFunctionBreakpoint.FUNCTION_SIGNAURE))) {
					return breakpoint;
				}
			} catch (CoreException e) {}
		}
		return null;
	}

	/**
     * Returns the resource associated with the specified editor part
     * @param editor the currently active editor part
     * @return the corresponding <code>IResource</code> from the editor part
     */
    IResource getResource(IEditorPart editor) {
        IEditorInput editorInput = editor.getEditorInput();
        IResource resource = editorInput.getAdapter(IFile.class);
        if (resource == null) {
            resource = ResourcesPlugin.getWorkspace().getRoot();
        }
        return resource;
    }

    /**
     * Returns the text editor associated with the given part or <code>null</code>
     * if none. In case of a multi-page editor, this method should be used to retrieve
     * the correct editor to perform the breakpoint operation on.
     *
     * @param part workbench part
     * @return text editor part or <code>null</code>
     */
    ITextEditor getTextEditor(IWorkbenchPart part) {
    	if (part instanceof ITextEditor) {
    		return (ITextEditor) part;
    	}
    	return part.getAdapter(ITextEditor.class);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {}
}
