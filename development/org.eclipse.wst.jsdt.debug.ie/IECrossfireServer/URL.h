/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
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

class URL {

public:
	URL();
	URL(wchar_t* value);
	~URL();
	wchar_t* getString();
	bool isEqual(wchar_t* urlString);
	bool isEqual(URL* url);
	bool isValid();
	bool setString(wchar_t* value);

private:
	bool standardize(std::wstring* url);

	wchar_t* m_value;
};

