/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import java.util.Enumeration;
import java.util.Vector;
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.style.NEDStyleToolbox;

public class CategoryListCellRenderer extends ListCellRendererBase {

    private Vector fileLists = null;
    private Object[] params = new Object[3];

    public Component getListCellRendererComponent( List list, Object value,
                                                   int index, boolean isSelected ) {
        if ( value == null ) {
            return this;
        }
        LibraryElement content = (LibraryElement)value;
        mTitle.setText( content.getName() );

        if ( fileLists == null ) {
            fileLists = NedIOUtils.directoryListing( content.getDetails().
                    getMediaFilePath() );
        }

        int local = 0;
        int remote = 0;
        Enumeration en = content.getChildern().elements();
        if ( fileLists != null ) {
            while ( en.hasMoreElements() ) {
                if ( ((LibraryElement)en.nextElement()).getDetails().
                        isDownloaded( fileLists ) ) {
                    ++local;
                } else {
                    ++remote;
                }
            }
        } else {
            while ( en.hasMoreElements() ) {
                if ( ((LibraryElement)en.nextElement()).getDetails().
                        isDownloaded() ) {
                    ++local;
                } else {
                    ++remote;
                }
            }
        }

        params[0] = String.valueOf( content.getChildern().size() );
        params[1] = String.valueOf( local );
        params[2] = String.valueOf( remote );

        mQuanity.setText( Localization.getMessage( NedResources.MEDIA_ITEMS, params ) );

        if ( isSelected ) {
            setFocus( true );
            mTitle.getStyle().setFgColor( content.isNew() ? NEDStyleToolbox.BLUE
                                          : NEDStyleToolbox.WHITE );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            mTitle.getStyle().setFgColor( content.isNew() ? NEDStyleToolbox.BLUE
                                          : NEDStyleToolbox.MAIN_FONT_COLOR );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
            setFocus( false );
        }
        return this;
    }
}
