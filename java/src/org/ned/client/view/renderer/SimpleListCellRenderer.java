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
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import org.ned.client.view.style.NEDStyleToolbox;

public class SimpleListCellRenderer extends ListCellRendererBase {

    public SimpleListCellRenderer() {
        super();
        if ( com.sun.lwuit.Display.getInstance().isTouchScreenDevice() ) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10, 2, 2 );
        }
        mTitle.setPreferredW( Display.getInstance().getDisplayWidth() );
        mTitle.getStyle().setAlignment( CENTER );
        mTitle.getSelectedStyle().setAlignment( CENTER );
    }

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        mTitle.setText( value.toString() );

        if ( isSelected ) {
            setFocus( true );
            mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
            setFocus( false );
        }
        return this;
    }
}
