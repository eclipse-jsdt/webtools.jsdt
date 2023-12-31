/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

 
#include "StdAfx.h"
#include "JSObject.h"

JSObject::JSObject(std::wstring* name, unsigned int parentHandle) {
	m_name = new std::wstring;
	m_name->assign(*name);
	m_objects = new std::map<std::wstring*, unsigned int>;
	m_parentHandle = parentHandle;
}

JSObject::~JSObject() {
	delete m_name;
	delete m_objects;
}

std::wstring JSObject::getAccessor() {
	return NULL;
}
