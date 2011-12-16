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

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListCellRenderer;
import org.ned.client.view.style.NEDStyleToolbox;


public class MenuCellRenderer extends Label implements ListCellRenderer {

    private static SelectedBGPainter mForegroundPainer = new SelectedBGPainter( NEDStyleToolbox.WHITE );

    public MenuCellRenderer() {
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10 ,2 ,2 );
        }
        getStyle().setFont( Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM ) );
        getSelectedStyle().setFont( Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM ) );
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if (value instanceof Command) {
            Command cmd = (Command)value;
            setText( cmd.getCommandName());
            setIcon( cmd.getIcon() );
        }

        if ( isSelected ) {
            setFocus( true );
            getStyle().setFgColor( NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mForegroundPainer );
        } else {
            setFocus( false );
            getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( null );
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        setText("");
        getStyle().setBgPainter(mForegroundPainer);
        setIcon(null);
        setFocus(true);
        return this;
    }
}
