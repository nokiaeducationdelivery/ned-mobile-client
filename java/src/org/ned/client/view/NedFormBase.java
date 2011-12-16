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
package org.ned.client.view;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import org.ned.client.view.renderer.TitleBarPainter;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import org.ned.client.IContent;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.contentdata.NedListModel;
import org.ned.client.view.renderer.MenuCellRenderer;

public class NedFormBase extends Form {

    private final static int MIN_MARGIN = 30;
    private final static int LEFT_MARGIN_OFFSET = 10;

    private TitleBarPainter mTitleBar;

    protected NedListModel listModel;
    protected IContent currentElement;
    protected Command showFreeMem;


    public NedFormBase(String contentId) {
        this();
        listModel = new NedListModel();
        listModel.loadNode(contentId);
        currentElement = XmlManager.getContentData(contentId);
    }

    public NedFormBase() {
        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            Style style = getSoftButtonStyle();
            style.setFont( Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE ) );
            style.setPadding( 10, 10, 0, 0 );
            setSoftButtonStyle( style );
        }
        setMenuCellRenderer(new MenuCellRenderer());
    }

    public void show() {
        if (WaitingScreen.isShowed()) {
            WaitingScreen.dispose();
        }
        super.show();
//        System.gc();
//        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    protected void setNedTitle(String title) {
        setTitle(" ");
        mTitleBar =  new TitleBarPainter( NedResources.MID_TITLE, title );
        getTitleStyle().setBgPainter( mTitleBar );
        getTitleComponent().setPreferredH(  mTitleBar.getPrefferedH() );
    }

     protected Command showMenuDialog( Dialog menu ) {
        int marginW = calculateMarginW(menu);
        marginW = marginW < MIN_MARGIN ? MIN_MARGIN : marginW;

        int marginH = calculateMarginH( menu );
        marginH = marginH < 0 ? 0 :marginH;

        return menu.show( marginH,0, LEFT_MARGIN_OFFSET, marginW - 2*LEFT_MARGIN_OFFSET , true );
    }

    private int calculateMarginW( Dialog menuDialog ) {
         String longestDesc = "";
         for ( int i = 0; i< getCommandCount(); i++ ) {
             String description = getCommand(i).getCommandName();
             longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
         }
         Style style = UIManager.getInstance().getComponentStyle("Command");
         int commandGap = 0;
         if ( style != null ) {
             commandGap = style.getMargin(Component.LEFT)
                        + style.getMargin(Component.RIGHT)
                        + style.getPadding(Component.LEFT)
                        + style.getPadding(Component.RIGHT);
         }
         //there is no wey to get cell renderer from form
         //creating temporary one to calculete margins and offsets
         MenuCellRenderer mcr = new MenuCellRenderer();
         int f1W = mcr.getStyle().getFont().stringWidth( longestDesc );
         int f2W = mcr.getSelectedStyle().getFont().stringWidth( longestDesc );

         return Display.getInstance().getDisplayWidth() - menuDialog.getStyle().getMargin(Component.LEFT)
                                                        - menuDialog.getStyle().getMargin(Component.RIGHT)
                                                        - menuDialog.getStyle().getPadding(Component.LEFT)
                                                        - menuDialog.getStyle().getPadding(Component.RIGHT)
                                                        - mcr.getStyle().getMargin(Component.LEFT)
                                                        - mcr.getStyle().getMargin(Component.RIGHT)
                                                        - mcr.getStyle().getPadding(Component.LEFT)
                                                        - mcr.getStyle().getPadding(Component.RIGHT)
                                                        - ( f1W < f2W ? f2W : f1W )
                                                        - getSideGap()
                                                        - menuDialog.getSideGap()
                                                        - commandGap
                                                        - 15;
    }

    private int calculateMarginH( Dialog menuDialog ) {
         //there is no way to get cell renderer from form
         //creating temporary one to calculete margins and offsets
        MenuCellRenderer rendererItem = new MenuCellRenderer();
        int itemTopMargin = rendererItem.getStyle().getMargin(Component.TOP);
        int itemBottonMargin = rendererItem.getStyle().getMargin(Component.BOTTOM);
        int itemTopPadding = rendererItem.getStyle().getPadding(Component.TOP);
        int itemBottonPadding = rendererItem.getStyle().getPadding(Component.BOTTOM);
        int fontHigh = rendererItem.getStyle().getFont().getHeight();//this font is used in cell

        List list = new List();
        Style style = list.getStyle();
        int listGap = 0;
        if ( style != null ) {
            listGap = style.getMargin(Component.LEFT)
                    + style.getMargin(Component.RIGHT)
                    + style.getPadding(Component.LEFT)
                    + style.getPadding(Component.RIGHT)
                    + getCommandCount() * list.getItemGap()
                    + 2 * list.getBorderGap();
        }
        return  Display.getInstance().getDisplayHeight() - menuDialog.getStyle().getMargin( Component.TOP )
                                                         - menuDialog.getStyle().getMargin( Component.BOTTOM )
                                                         - menuDialog.getStyle().getPadding( Component.BOTTOM )
                                                         - menuDialog.getStyle().getPadding( Component.TOP )
                                                         - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH()
                                                         - getCommandCount() * ( fontHigh
                                                                                + itemTopMargin
                                                                                + itemBottonMargin
                                                                                + itemTopPadding
                                                                                + itemBottonPadding)
                                                         - listGap;
    }
}
