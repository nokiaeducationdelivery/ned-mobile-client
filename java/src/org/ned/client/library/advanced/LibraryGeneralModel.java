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
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.Content;
import org.ned.client.NedMidlet;
import org.ned.client.XmlManager;
import org.ned.client.utils.StringRepository;

public class LibraryGeneralModel {

    private static final String DELTAFILE = ".delta";
    private static LibraryGeneralModel mInstance;
    public LibraryElement mRoot;
    private Document mCurrentDoc;
    private LibraryChanges mLatestChanges;

    private LibraryGeneralModel() {
    }

    public static LibraryGeneralModel getInfo( Document aLibXml ) {
        if ( mInstance == null || mInstance.mCurrentDoc != aLibXml ) {
            mInstance = new LibraryGeneralModel();
            mInstance.mCurrentDoc = aLibXml;
            Element rootElem = aLibXml.getRootElement();
            if ( rootElem != null ) {
                mInstance.mRoot = new LibraryElement();
                mInstance.parse( rootElem, mInstance.mRoot );
            }
            mInstance.loadLatestChangesInfo();
        }

        return mInstance;
    }

    public void getFlatList( Vector catalogsL, Vector categoriesL, Vector mediaItemsL ) {
        for ( int ctgNo = 0; ctgNo < mRoot.getChildern().size(); ctgNo++ ) {
            LibraryElement catalog = (LibraryElement)mRoot.getChildern().
                    elementAt( ctgNo );
            catalogsL.addElement( catalog );

            for ( int ctgiesNo = 0; ctgiesNo < catalog.getChildern().size(); ctgiesNo++ ) {
                LibraryElement category = (LibraryElement)catalog.getChildern().
                        elementAt( ctgiesNo );
                categoriesL.addElement( category );

                for ( int itemsNo = 0; itemsNo < category.getChildern().size(); itemsNo++ ) {
                    LibraryElement item = (LibraryElement)category.getChildern().
                            elementAt( itemsNo );
                    mediaItemsL.addElement( item );
                }
            }
        }
    }

    private void loadLatestChangesInfo() {
        mLatestChanges = new LibraryChanges( NedMidlet.getSettingsManager().
                getLibraryManager().getCurrentLibrary().
                getFileUri() + DELTAFILE );
        mLatestChanges.load();
        markNewElements();
    }

    public void markNewElements() {
        Vector mediaItems = mRoot.getAllMediaItems();
        resetModifiedFlag( mediaItems );
        Vector updates = mLatestChanges.getNewItemIds();

        Enumeration updatedEn = updates.elements();
        while ( updatedEn.hasMoreElements() ) {
            String updatedId = (String)updatedEn.nextElement();

            Enumeration mediaEn = mediaItems.elements();
            while ( mediaEn.hasMoreElements() ) {
                LibraryElement item = (LibraryElement)mediaEn.nextElement();
                if ( item.getId().equals( updatedId ) ) {
                    item.setNew( true, true );
                    break;
                }
            }
        }
    }

    private void parse( Element aElement, LibraryElement aLibModel ) {

        //String id = aElement.getAttributeValue( "", "id" );

        //if ( id != null && id.length() > 0 ) {
        Content content = XmlManager.getContentData( aElement );
        aLibModel.setDetails( content );

        for ( int i = 0; i < aElement.getChildCount(); i++ ) {

            if ( aElement.getType( i ) != Node.ELEMENT ) {
                continue;
            }

            Element element = aElement.getElement( i );

            if ( element.getName().equals( StringRepository.TAG_CHILDS ) ) {

                for ( int j = 0; j < element.getChildCount(); j++ ) {
                    if ( element.getType( j ) != Node.ELEMENT ) {
                        continue;
                    }

                    Element ned_nodeElement = element.getElement( j );
                    LibraryElement libChildInfo = new LibraryElement( aLibModel );
                    parse( ned_nodeElement, libChildInfo );
                    if ( libChildInfo.getId() != null && libChildInfo.getId().
                            length() > 0 ) {
                        aLibModel.getChildern().addElement( libChildInfo );
                    }
                }
            }
        }
        //}
    }

    public LibraryElement getElement( String contentId ) {
        return mRoot.getElement( contentId );
    }

    public void updateNewMediaList() {
        LibraryChanges.persistChangesInfo( mLatestChanges.getNewItemIds(), NedMidlet.
                getSettingsManager().
                getLibraryManager().getCurrentLibrary().
                getFileUri() + DELTAFILE, true );
    }

    public void removeFromUpdated( String id ) {
        if ( mLatestChanges.removeItem( id ) ) {
            markNewElements();
        }
    }

    private void resetModifiedFlag( Vector mediaItems ) {
        Enumeration en = mediaItems.elements();
        while ( en.hasMoreElements() ) {
            LibraryElement item = (LibraryElement)en.nextElement();
            item.setNew( false, true );

        }
    }
}
