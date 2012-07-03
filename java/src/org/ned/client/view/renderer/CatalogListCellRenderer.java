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
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.view.style.NEDStyleToolbox;

public class CatalogListCellRenderer extends ListCellRendererBase {

    private Object[] params = new Object[1];

    public Component getListCellRendererComponent( List list, Object value,
                                                   int index, boolean isSelected ) {
        if ( value == null ) {
            return this;
        }
        LibraryElement content = (LibraryElement)value;
        mTitle.setText( content.getName() );

        params[0] = String.valueOf( content.getChildern().size() );
        mQuanity.setText( Localization.getMessage( NedResources.CATEGORIES, params ) );

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
