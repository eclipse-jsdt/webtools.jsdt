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


#pragma once

#include <string>
#include <map>

class JSObject {

public:
	JSObject(std::wstring* name, unsigned int parentHandle);
	~JSObject();
	std::wstring getAccessor();

private:
	std::wstring* m_name;
	std::map<std::wstring*, unsigned int>* m_objects;
	unsigned int m_parentHandle;
};
