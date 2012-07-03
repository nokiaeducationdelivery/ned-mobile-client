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

public class LanguageInfo {

    protected String mLangName;
    protected String mLocale;
    protected boolean mIsLocal;
    protected String mRemoteName;

    protected LanguageInfo() {
    }

    public LanguageInfo( String langName, String locale, boolean aIsLocal ) {
        mLangName = langName;
        mLocale = locale;
        mIsLocal = aIsLocal;
    }

    public LanguageInfo( String langName, String locale, boolean aIsLocal, String aRemoteName ) {
        mLangName = langName;
        mLocale = locale;
        mIsLocal = aIsLocal;
        mRemoteName = aRemoteName;
    }

    public String getLangName() {
        return mLangName;
    }

    public String getLocale() {
        return mLocale;
    }

    public boolean isLocal() {
        return mIsLocal;
    }

    public String getFile() {
        return mRemoteName;
    }

    public void setFile( String aFile ) {
        mRemoteName = aFile;
    }

    public boolean equals( Object obj ) {
        if ( obj == this ) {
            return true;

        }
        if ( obj == null || this.getClass() != obj.getClass() ) {
            return false;
        }

        return mLangName.equals( ((LanguageInfo) obj).mLangName ) && mLocale.equals( ((LanguageInfo) obj).mLocale );
    }

    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.mLangName != null ? this.mLangName.hashCode() : 0);
        hash = 83 * hash + (this.mLocale != null ? this.mLocale.hashCode() : 0);
        return hash;
    }

    public void setLocal() {
        mIsLocal = true;
    }
}
