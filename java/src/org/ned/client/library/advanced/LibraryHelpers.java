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
import org.ned.client.NedConsts;
import org.ned.client.utils.Sort;

public class LibraryHelpers {

    public static Vector sortBy( Vector aMediaList, int aSortBy ) {
        switch ( aSortBy ) {
            case NedConsts.SortOrder.BY_TYPE:
                return sortByType( aMediaList, false );
            case NedConsts.SortOrder.BY_TYPE_AND_NAME:
                return sortByType( aMediaList, true );
            case NedConsts.SortOrder.BY_NAME:
                return sortByName( aMediaList );
            case NedConsts.SortOrder.NONE:
            default:
                return aMediaList;
        }
    }

    public static Vector sortByType( Vector aMediaList, boolean aSortByName ) {
        Vector video = new Vector( aMediaList.size(), 4 );
        Vector audio = new Vector( 4, 4 );
        Vector picture = new Vector( 4, 4 );
        Vector text = new Vector( 4, 4 );
        Vector rest = new Vector( 4, 4 );

        LibraryElement content;
        String type;
        Enumeration en = aMediaList.elements();
        while ( en.hasMoreElements() ) {
            content = (LibraryElement)en.nextElement();
            type = content.getDetails().getType();
            if ( type.equals( NedConsts.NedContentType.VIDEO ) ) {
                video.addElement( content );
            } else if ( type.equals( NedConsts.NedContentType.AUDIO ) ) {
                audio.addElement( content );
            } else if ( type.equals( NedConsts.NedContentType.IMAGE ) ) {
                picture.addElement( content );
            } else if ( type.equals( NedConsts.NedContentType.TEXT ) ) {
                text.addElement( content );
            } else {
                rest.addElement( content );
            }
        }

        if ( aSortByName ) {
            Sort.sort( video );
            Sort.sort( audio );
            Sort.sort( picture );
            Sort.sort( rest );
        }

        addVectors( video, audio );
        addVectors( video, picture );
        addVectors( video, text );
        return addVectors( video, rest );
    }

    public static Vector addVectors( Vector aL, Vector aR ) {
        Enumeration en = aR.elements();
        while ( en.hasMoreElements() ) {
            aL.addElement( en.nextElement() );
        }
        return aL;
    }

    public static Vector filter( Vector aInput, IFilter aFilter ) {
        Vector output = new Vector( aInput.size(), 1 );

        Enumeration en = aInput.elements();
        while ( en.hasMoreElements() ) {
            LibraryElement object = (LibraryElement)en.nextElement();
            if ( aFilter.isMatch( object ) ) {
                output.addElement( object );
            }
        }
        return output;
    }

    private static Vector sortByName( Vector aMediaList ) {
        Vector toBeSorted = new Vector( aMediaList.size() );
        final Enumeration en = aMediaList.elements();
        while ( en.hasMoreElements() ) {
            toBeSorted.addElement( en.nextElement() );
        }
        Sort.sort( toBeSorted );

        return toBeSorted;
    }
}
