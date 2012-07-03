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
package org.ned.client.view.customComponents;

import com.sun.lwuit.*;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import java.util.Vector;
import org.ned.client.view.renderer.MenuCellRenderer;
import org.ned.client.view.style.NEDStyleToolbox;

public class MenuBar extends com.sun.lwuit.MenuBar {

    final static int MIN_MARGIN = 30;
    final static int LEFT_MARGIN_OFFSET = 10;

    public MenuBar() {
        setMenuCellRenderer( new MenuCellRenderer() );
    }

    protected Component createCommandComponent( Vector commands ) {
        return createCommandList( commands );
    }

    protected Command showMenuDialog( Dialog menu ) {
        menu.getDialogStyle().setBgColor( NEDStyleToolbox.WHITE );

        int marginW = calculateMarginW( menu );
        marginW = marginW < MIN_MARGIN ? MIN_MARGIN : marginW;

        int marginH = calculateMarginH( menu );
        marginH = marginH < 0 ? 0 : marginH;

        return menu.show( marginH, 0, LEFT_MARGIN_OFFSET, marginW - LEFT_MARGIN_OFFSET, true );
    }

    private int calculateMarginW( Dialog menuDialog ) {
        String longestDesc = "";
        for ( int i = 0; i < getCommandCount(); i++ ) {
            String description = getCommand( i ).getCommandName();
            longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
        }
        Style style = UIManager.getInstance().getComponentStyle( "Command" );
        int commandGap = 0;
        if ( style != null ) {
            commandGap = style.getMargin( Component.LEFT )
                         + style.getMargin( Component.RIGHT )
                         + style.getPadding( Component.LEFT )
                         + style.getPadding( Component.RIGHT );
        }
        //there is no wey to get cell renderer from form
        //creating temporary one to calculete margins and offsets
        MenuCellRenderer mcr = new MenuCellRenderer();

        return Display.getInstance().getDisplayWidth() - menuDialog.getStyle().getMargin( Component.LEFT )
               - menuDialog.getStyle().getMargin( Component.RIGHT )
               - menuDialog.getStyle().getPadding( Component.LEFT )
               - menuDialog.getStyle().getPadding( Component.RIGHT )
               - mcr.getStyle().getMargin( Component.LEFT )
               - mcr.getStyle().getMargin( Component.RIGHT )
               - mcr.getStyle().getPadding( Component.LEFT )
               - mcr.getStyle().getPadding( Component.RIGHT )
               - mcr.getSelectedStyle().getFont().stringWidth( longestDesc )
               - getSideGap()
               - menuDialog.getSideGap()
               - commandGap
               - 10;
    }

    private int calculateMarginH( Dialog menuDialog ) {
        //there is no way to get cell renderer from form
        //creating temporary one to calculete margins and offsets
        MenuCellRenderer rendererItem = new MenuCellRenderer();
        int itemTopMargin = rendererItem.getStyle().getMargin( Component.TOP );
        int itemBottonMargin = rendererItem.getStyle().getMargin( Component.BOTTOM );
        int itemTopPadding = rendererItem.getStyle().getPadding( Component.TOP );
        int itemBottonPadding = rendererItem.getStyle().getPadding( Component.BOTTOM );
        int fontHigh = rendererItem.getSelectedStyle().getFont().getHeight();//this font is used in cell

        List list = new List();
        Style style = list.getStyle();
        int listGap = 0;
        if ( style != null ) {
            listGap = style.getMargin( Component.LEFT )
                      + style.getMargin( Component.RIGHT )
                      + style.getPadding( Component.LEFT )
                      + style.getPadding( Component.RIGHT )
                      + getCommandCount() * list.getItemGap()
                      + 2 * list.getBottomGap();//getBorderGap();
        }
        return Display.getInstance().getDisplayHeight() - menuDialog.getStyle().getMargin( Component.TOP )
               - menuDialog.getStyle().getMargin( Component.BOTTOM )
               - menuDialog.getStyle().getPadding( Component.BOTTOM )
               - menuDialog.getStyle().getPadding( Component.TOP )
               - Display.getInstance().getCurrent().getSoftButton( 0 ).getPreferredH()
               - getCommandCount() * (fontHigh
                                      + itemTopMargin
                                      + itemBottonMargin
                                      + itemTopPadding
                                      + itemBottonPadding)
               - listGap;
    }
}
