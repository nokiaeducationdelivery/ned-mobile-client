/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
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
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.view.style.NEDStyleToolbox;

public class CatalogListCellRenderer extends ListCellRendererBase {

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {

        Content content = (Content)value;
        mTitle.setText( content.getText() );
        mQuanity.setText( XmlManager.getContentChilds( content.getId(), null ).size() + " " + NedResources.CATEGORIES );

        if ( isSelected ) {
            setFocus( true );
            mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            mQuanity.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
            setFocus( false );
        }
        return this;
    }
}
