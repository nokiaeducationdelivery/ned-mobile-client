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
import java.util.Hashtable;
import java.util.Vector;
import org.ned.client.Localization;
import org.ned.client.NedResources;

public class LibraryChangesReport {

    private Vector mCatalogsL;
    private Vector mCategoriesL;
    private Vector mMediaItemsL;
    private Vector mCatalogsR;
    private Vector mCategoriesR;
    private Vector mediaItemsR;

    private LibraryChangesReport() {
        mCatalogsL = new Vector( 4, 4 );
        mCategoriesL = new Vector( 4, 4 );
        mMediaItemsL = new Vector( 4, 4 );
        mCatalogsR = new Vector( 4, 4 );
        mCategoriesR = new Vector( 4, 4 );
        mediaItemsR = new Vector( 4, 4 );
    }

    public static LibraryChangesReport generateReport( LibraryGeneralModel aL, LibraryGeneralModel aR ) {
        LibraryChangesReport report = new LibraryChangesReport();
        if ( aL != null ) {
            aL.getFlatList( report.mCatalogsL, report.mCategoriesL, report.mMediaItemsL );
        }
        if ( aR != null ) {
            aR.getFlatList( report.mCatalogsR, report.mCategoriesR, report.mediaItemsR );
        }

        return report;
    }

    public String shortReport() {
        StringBuffer builder = new StringBuffer();

        builder.append( Localization.getMessage( NedResources.CATALOGS_ADDED,
                                                 new Object[]{ String.valueOf( compareVector( mCatalogsL, mCatalogsR ) ) } ) );
        builder.append( "\n" );
        builder.append( Localization.getMessage( NedResources.CATALOGS_REMOVED,
                                                 new Object[]{ String.valueOf( compareVector( mCatalogsR, mCatalogsL ) ) } ) );
        builder.append( "\n" );

        builder.append( Localization.getMessage( NedResources.CATEGORIES_ADDED,
                                                 new Object[]{ String.valueOf( compareVector( mCategoriesL, mCategoriesR ) ) } ) );
        builder.append( "\n" );
        builder.append( Localization.getMessage( NedResources.CATEGORIES_REMOVED,
                                                 new Object[]{ String.valueOf( compareVector( mCategoriesR, mCategoriesL ) ) } ) );
        builder.append( "\n" );

        builder.append( Localization.getMessage( NedResources.MEDIAITEMS_ADDED,
                                                 new Object[]{ String.valueOf( compareVector( mMediaItemsL, mediaItemsR ) ) } ) );
        builder.append( "\n" );
        builder.append( Localization.getMessage( NedResources.MEDIAITEMS_REMOVED,
                                                 new Object[]{ String.valueOf( compareVector( mediaItemsR, mMediaItemsL ) ) } ) );

        return builder.toString();
    }

    private int compareVector( Vector aL, Vector aR ) {
        return getDiff( aL, aR ).size();
    }

    private Vector getDiff( Vector aL, Vector aR ) {
        Vector diff = new Vector( 4, 4 );

        Enumeration enL = aL.elements();
        while ( enL.hasMoreElements() ) {
            Object object = enL.nextElement();
            if ( !aR.contains( object ) ) {
                diff.addElement( object );
            }
        }
        return diff;
    }

    public String getFullReport() {

        Vector added = getDiff( mMediaItemsL, mediaItemsR );

        Hashtable changesInCategory = new Hashtable( 4 );

        Enumeration en = added.elements();
        while ( en.hasMoreElements() ) {
            LibraryElement mediaItem = (LibraryElement) en.nextElement();

            LibraryElement parentCategory = findParent( mCategoriesL, mediaItem );//search in new version of library firs
            if ( parentCategory == null ) {//not found in new version, must be in old version of library
                parentCategory = findParent( mCategoriesR, mediaItem );

            }
            LibraryBranchChanges categoryChnged;
            if ( changesInCategory.containsKey( parentCategory ) ) {
                categoryChnged = (LibraryBranchChanges) changesInCategory.get( parentCategory );
            } else {
                categoryChnged = new LibraryBranchChanges();
                changesInCategory.put( parentCategory, categoryChnged );
            }
            categoryChnged.getAdded().addElement( mediaItem );
        }

        en = changesInCategory.keys();

        Hashtable catalogs = new Hashtable( 4 );

        while ( en.hasMoreElements() ) {
            LibraryElement category = (LibraryElement) en.nextElement();

            LibraryElement parentCatalog = findParent( mCatalogsL, category );
            if ( parentCatalog == null ) {
                parentCatalog = findParent( mCatalogsR, category );
            }

            LibraryBranchChanges catalogsChanges;
            if ( catalogs.containsKey( parentCatalog ) ) {
                catalogsChanges = (LibraryBranchChanges) catalogs.get( parentCatalog );
            } else {
                catalogsChanges = new LibraryBranchChanges();
                catalogs.put( parentCatalog, catalogsChanges );
            }
            catalogsChanges.getAdded().addElement( category );
        }

        return serialize( catalogs, changesInCategory );
    }

    private String serialize( Hashtable catalogs, Hashtable changesInCategory ) {
        StringBuffer builder = new StringBuffer();

        Enumeration catalogsEn = catalogs.keys();
        while ( catalogsEn.hasMoreElements() ) {
            LibraryElement catalogElement = (LibraryElement) catalogsEn.nextElement();
            builder.append( NedResources.CATALOG ).append( " - " ).append( catalogElement.getName() ).append( ":\n" );

            Enumeration categoryEnumeration = ((LibraryBranchChanges) catalogs.get( catalogElement )).getAdded().elements();
            while ( categoryEnumeration.hasMoreElements() ) {
                LibraryElement category = (LibraryElement) categoryEnumeration.nextElement();
                builder.append( "   " ).append( NedResources.CATEGORY ).append( " - " ).append( category.getName() ).append( ":\n" );

                Enumeration itemsEnum = ((LibraryBranchChanges) changesInCategory.get( category )).getAdded().elements();
                while ( itemsEnum.hasMoreElements() ) {
                    LibraryElement item = (LibraryElement) itemsEnum.nextElement();
                    builder.append( "     + " ).append( item.getName() ).append( ":\n" );
                }
            }
        }
        return builder.toString();
    }

    private LibraryElement findParent( Vector categoriesR, LibraryElement mediaItem ) {

        Enumeration en = categoriesR.elements();

        while ( en.hasMoreElements() ) {
            LibraryElement category = (LibraryElement) en.nextElement();
            if ( category.getChildern().contains( mediaItem ) ) {
                return category;
            }
        }
        return null;
    }
}
