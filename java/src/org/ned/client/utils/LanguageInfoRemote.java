/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.utils;

public class LanguageInfoRemote extends LanguageInfo {

    private String mId;

    public LanguageInfoRemote() {
        super();
    }

    public LanguageInfoRemote( String aId, String aName, String aLocale, String aFile ) {
        super( aName, aLocale, false );
        mId = aId;
        mRemoteName = aFile;
    }

    public void setId( String aId ) {
        mId = aId;
    }

    public void setName( String aName ) {
        mLangName = aName;
    }

    public void setLocale( String aLocale ) {
        mLocale = aLocale;
    }
}
