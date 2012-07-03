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
package org.ned.client.library.advanced;

import java.util.Vector;

public class LibraryElement {

    private String mId;
    private String mName;
    private String mType;
    private Vector/*LibraryElement*/ mChildern;

    public LibraryElement() {
        mChildern = new Vector( 4, 4 );
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    public boolean equals( Object aR ) {
        return aR != null && aR instanceof LibraryElement && ((LibraryElement) aR).getId().equals( getId() );
    }

    /**
     * @return the mId
     */
    public String getId() {
        return mId;
    }

    /**
     * @param aId the aId to set
     */
    public void setId( String aId ) {
        mId = aId;
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param aName the aName to set
     */
    public void setName( String aName ) {
        mName = aName;
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mType;
    }

    /**
     * @param aType the aType to set
     */
    public void setType( String aType ) {
        mType = aType;
    }

    /**
     * @return the mChildern
     */
    public Vector getChildern() {
        return mChildern;
    }

    /**
     * @param aChildern the aChildern to set
     */
    public void setChildern( Vector aChildern ) {
        mChildern = aChildern;
    }
}
