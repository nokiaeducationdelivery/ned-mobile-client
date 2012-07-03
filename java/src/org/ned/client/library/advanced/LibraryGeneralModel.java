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
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.Content;
import org.ned.client.XmlManager;
import org.ned.client.utils.StringRepository;

public class LibraryGeneralModel {

    public LibraryElement mRoot;

    private LibraryGeneralModel() {
    }

    public static LibraryGeneralModel getInfo( Document aLibXml ) {
        LibraryGeneralModel model = new LibraryGeneralModel();

        Element rootElem = aLibXml.getRootElement();
        if ( rootElem != null ) {
            model.mRoot = new LibraryElement();
            model.parse( rootElem, model.mRoot );
        }

        return model;
    }

    public void getFlatList( Vector catalogsL, Vector categoriesL, Vector mediaItemsL ) {
        for ( int ctgNo = 0; ctgNo < mRoot.getChildern().size(); ctgNo++ ) {
            LibraryElement catalog = (LibraryElement) mRoot.getChildern().elementAt( ctgNo );
            catalogsL.addElement( catalog );

            for ( int ctgiesNo = 0; ctgiesNo < catalog.getChildern().size(); ctgiesNo++ ) {
                LibraryElement category = (LibraryElement) catalog.getChildern().elementAt( ctgiesNo );
                categoriesL.addElement( category );

                for ( int itemsNo = 0; itemsNo < category.getChildern().size(); itemsNo++ ) {
                    LibraryElement item = (LibraryElement) category.getChildern().elementAt( itemsNo );
                    mediaItemsL.addElement( item );
                }
            }
        }
    }

    private void parse( Element aElement, LibraryElement aLibModel ) {

        String id = aElement.getAttributeValue( "", "id" );

        if ( id != null && id.length() > 0 ) {
            Content content = XmlManager.getContentData( aElement );
            aLibModel.setId( id );
            aLibModel.setName( content.getText() );
            aLibModel.setType( content.getType() );

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
                        LibraryElement libChildInfo = new LibraryElement();
                        parse( ned_nodeElement, libChildInfo );
                        if ( libChildInfo.getId() != null && libChildInfo.getId().length() > 0 ) {
                            aLibModel.getChildern().addElement( libChildInfo );
                        }
                    }
                }
            }
        }
    }
}
