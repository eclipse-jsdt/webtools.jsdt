/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.env.ISourceField;

public class SourceField implements ISourceField {
	protected int modifiers;
	protected char[] typeName;
	protected char[] name;
	protected int declarationStart;
	protected int declarationEnd;
	protected int nameSourceStart;
	protected int nameSourceEnd; 
	protected char[] source;
public SourceField(
	int declarationStart,
	int modifiers,
	char[] typeName,
	char[] name,
	int nameSourceStart,
	int nameSourceEnd,
	char[] source) {

	this.declarationStart = declarationStart;
	this.modifiers = modifiers;
	this.typeName = typeName;
	this.name = name;
	this.nameSourceStart = nameSourceStart;
	this.nameSourceEnd = nameSourceEnd;
	this.source = source;
}
public String displayModifiers() {
	StringBuffer buffer = new StringBuffer();

	if (this.modifiers == 0)
		return null;
	if ((this.modifiers & ClassFileConstants.AccPublic) != 0)
		buffer.append("public ");
	if ((this.modifiers & ClassFileConstants.AccProtected) != 0)
		buffer.append("protected ");
	if ((this.modifiers & ClassFileConstants.AccPrivate) != 0)
		buffer.append("private ");
	if ((this.modifiers & ClassFileConstants.AccFinal) != 0)
		buffer.append("final ");
	if ((this.modifiers & ClassFileConstants.AccStatic) != 0)
		buffer.append("static ");
	if ((this.modifiers & ClassFileConstants.AccAbstract) != 0)
		buffer.append("abstract ");
	if ((this.modifiers & ClassFileConstants.AccNative) != 0)
		buffer.append("native ");
	return buffer.toString();
}
public String getActualName() {
	StringBuffer buffer = new StringBuffer();
	buffer.append(source, nameSourceStart, nameSourceEnd - nameSourceStart + 1);
	return buffer.toString();
}
public int getDeclarationSourceEnd() {
	return declarationEnd;
}
public int getDeclarationSourceStart() {
	return declarationStart;
}
public char[] getInitializationSource() {
	return null;
}
public int getModifiers() {
	return modifiers;
}
public char[] getName() {
	return name;
}
public int getNameSourceEnd() {
	return nameSourceEnd;
}
public int getNameSourceStart() {
	return nameSourceStart;
}
public char[] getTypeName() {
	return typeName;
}
protected void setDeclarationSourceEnd(int position) {
	declarationEnd = position;
}
public String tabString(int tab) {
	StringBuilder sb = new StringBuilder();
	for (int i = tab; i > 0; i--) {
		sb.append('\t');
	}
	return sb.toString();
}
public String toString() {
	return toString(0);
}
public String toString(int tab) {
	StringBuffer buffer = new StringBuffer();
	buffer.append(tabString(tab));
	String displayModifiers = displayModifiers();
	if (displayModifiers != null) {
		buffer.append(displayModifiers);
	}
	buffer.append("var ");
//	if (typeName!=null)
//	{
//		buffer.append(typeName).append(" ");	
//	}
	buffer.append(name);
	buffer.append(";");
	return buffer.toString();
}
}
