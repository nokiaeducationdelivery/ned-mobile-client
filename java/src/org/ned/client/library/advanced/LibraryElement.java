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

import java.util.Enumeration;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.NedConsts;

public class LibraryElement {

    private LibraryElement mParent;
    private Vector/*LibraryElement*/ mChildern;
    private Content mDetails;
    private boolean mIsNew;

    public LibraryElement() {
        mChildern = new Vector( 4, 4 );

    }

    public LibraryElement( LibraryElement aParent ) {
        this();
        mParent = aParent;
    }

    public LibraryElement getElement( String contentId ) {
        if ( getId().equals( contentId ) ) {
            return this;
        } else {
            Enumeration en = mChildern.elements();
            while ( en.hasMoreElements() ) {
                LibraryElement object = (LibraryElement)en.nextElement();
                LibraryElement obj = object.getElement( contentId );
                if ( obj != null ) {
                    return obj;
                }
            }
        }
        return null;
    }

    public Vector getAllMediaItems() {
        Vector mediaList = new Vector( 4, 4 );
        getMediaItem( mediaList );
        return mediaList;
    }

    private void getMediaItem( Vector aMediaList ) {
        if ( isMediaType() ) {
            aMediaList.addElement( this );
        } else {
            Enumeration en = mChildern.elements();
            while ( en.hasMoreElements() ) {
                LibraryElement object = (LibraryElement)en.nextElement();
                object.getMediaItem( aMediaList );
            }
        }
    }

    public boolean isMediaType() {
        return getType().equals( NedConsts.NedContentType.VIDEO )
               || getType().equals( NedConsts.NedContentType.AUDIO )
               || getType().equals( NedConsts.NedContentType.TEXT )
               || getType().equals( NedConsts.NedContentType.IMAGE )
               || getType().equals( NedConsts.NedContentType.UNDEFINED );
    }

    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    public boolean equals( Object aR ) {
        return aR != null && aR instanceof LibraryElement
               && ((LibraryElement)aR).getId().equals( getId() );
    }

    public Content getDetails() {
        return mDetails;
    }

    public void setDetails( Content aDetails ) {
        mDetails = aDetails;
    }

    /**
     * @return the mId
     */
    public String getId() {
        return mDetails.getId();
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mDetails.getText();
    }

    /**
     * @return the mType
     */
    public String getType() {
        return mDetails.getType();
    }

    /**
     * @return the mParentId
     */
    public LibraryElement getParent() {
        return mParent;
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

    public boolean isNew() {
        return mIsNew;
    }

    public void setNew( boolean aNew ) {
        mIsNew = aNew;
    }

    void setNew( boolean aNew, boolean aModifyParent ) {
        mIsNew = aNew;
        if ( getParent() != null && aModifyParent ) {
            getParent().setNew( aNew, aModifyParent );
        }
    }
}
