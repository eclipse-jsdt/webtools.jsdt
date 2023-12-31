/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.core.refactoring.descriptors;

import org.eclipse.osgi.util.NLS;

public class DescriptorMessages extends NLS {

	private static final String BUNDLE_NAME= "org.eclipse.wst.jsdt.internal.core.refactoring.descriptors.DescriptorMessages"; //$NON-NLS-1$

	public static String JavaRefactoringDescriptor_no_description;

	public static String JavaRefactoringDescriptor_no_resulting_descriptor;

	public static String JavaRefactoringDescriptor_not_available;

	public static String MoveDescriptor_no_destination_set;

	public static String MoveDescriptor_no_elements_set;

	public static String MoveStaticMembersDescriptor_invalid_members;

	public static String MoveStaticMembersDescriptor_no_members;

	public static String MoveStaticMembersDescriptor_no_type;

	public static String RenameJavaElementDescriptor_accessor_constraint;

	public static String RenameJavaElementDescriptor_delegate_constraint;

	public static String RenameJavaElementDescriptor_deprecation_constraint;

	public static String RenameJavaElementDescriptor_hierarchical_constraint;

	public static String RenameJavaElementDescriptor_no_java_element;

	public static String RenameJavaElementDescriptor_patterns_constraint;

	public static String RenameJavaElementDescriptor_patterns_qualified_constraint;

	public static String RenameJavaElementDescriptor_project_constraint;

	public static String RenameJavaElementDescriptor_qualified_constraint;

	public static String RenameJavaElementDescriptor_reference_constraint;

	public static String RenameJavaElementDescriptor_similar_constraint;

	public static String RenameJavaElementDescriptor_textual_constraint;

	public static String RenameLocalVariableDescriptor_no_compilation_unit;

	public static String RenameLocalVariableDescriptor_no_selection;

	public static String RenameResourceDescriptor_no_new_name;

	public static String RenameResourceDescriptor_no_resource;

	public static String RenameResourceDescriptor_project_constraint;

	public static String UseSupertypeDescriptor_no_subtype;

	public static String UseSupertypeDescriptor_no_supertype;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, DescriptorMessages.class);
	}

	private DescriptorMessages() {
	}
}