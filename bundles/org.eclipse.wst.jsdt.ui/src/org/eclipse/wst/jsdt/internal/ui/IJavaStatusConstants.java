/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui;


/**
 * Defines status codes relevant to the Java UI plug-in. When a 
 * Core exception is thrown, it contain a status object describing
 * the cause of the exception. The status objects originating from the
 * Java UI plug-in use the codes defined in this interface.
  */
public interface IJavaStatusConstants {

	// Java UI status constants start at 10000 to make sure that we don't
	// collide with resource and java model constants.
	
	public static final int INTERNAL_ERROR= 10001;
	
	/**
	 * Status constant indicating that an exception occurred on
	 * storing or loading templates.
	 */
	public static final int TEMPLATE_IO_EXCEPTION = 10002;

	/**
	 * Status constant indicating that an validateEdit call has changed the
	 * content of a file on disk.
	 */
	public static final int VALIDATE_EDIT_CHANGED_CONTENT= 10003;
	
	/**
	 * Status constant indicating that a <tt>ChangeAbortException</tt> has been
	 * caught.
 	 */
	public static final int CHANGE_ABORTED= 10004;

	/**
	 * Status constant indicating that an exception occurred while
	 * parsing template file.
	 */
	public static final int TEMPLATE_PARSE_EXCEPTION = 10005;
	
	/**
	 * Status constant indicating that a problem occurred while notifying a post
	 * save listener.
	 * 
	 * @see org.eclipse.wst.jsdt.internal.ui.javaeditor.saveparticipant.IPostSaveListener
	 * 
	 */
	public static final int EDITOR_POST_SAVE_NOTIFICATION= 10006;
	
 }
