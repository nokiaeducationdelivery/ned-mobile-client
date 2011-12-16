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
import com.sun.lwuit.Font;
import com.sun.lwuit.List;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import org.ned.client.NedResources;
import org.ned.client.view.renderer.MenuCellRenderer;



public abstract class ContextMenu {

    protected Dialog menuDialog = null;
    protected ListModel optionsModel = null;
    protected List optionsList = null;
    protected List mMediaItemList;
    protected int sizeList;
    private boolean mIgnoreFirstRelease = false; // fix for issue when menu is activated with long press

    protected int displayWidth, displayHeight;

    public ContextMenu(List aList, int size){
        displayWidth = Display.getInstance().getDisplayWidth();
        displayHeight = Display.getInstance().getDisplayHeight();
        mMediaItemList = aList;
        sizeList = size;

        menuDialog = new Dialog();
        menuDialog.setAutoDispose(true);
        menuDialog.setScrollableY(true);
        menuDialog.setScrollableX(false);
        menuDialog.setLayout(new BorderLayout());

        optionsList = new List() {

            public void pointerPressed(int x, int y) {
                super.pointerPressed(x, y);
                mIgnoreFirstRelease = false;
            }

            public void pointerReleased(int x, int y) {
                if ( mIgnoreFirstRelease )
                    mIgnoreFirstRelease = false;
                else
                    super.pointerReleased(x, y);
            }
        };
        optionsList.getStyle().setBorder(Border.createEmpty());
        optionsList.setFixedSelection(List.FIXED_NONE_CYCLIC);

        setTransitions();
        buildMenu();

        if(com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            Style style = menuDialog.getSoftButtonStyle();
            style.setFont( Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE ) );
            style.setPadding( 10, 10, 0, 0 );
            menuDialog.setSoftButtonStyle(style);
        }
    }

    public void show( int leftMargin, int topMargin ) {
        mIgnoreFirstRelease = true;
        leftMargin += 5; // offset to avoid automatic selection of item that shows under the pointer
        int rightMargin = getHorizontalMargin() - leftMargin;
        int bottomMargin = getVerticalMargin() - topMargin;
        if ( bottomMargin < 0 ) { // check if not out off screen
            bottomMargin = 0;
            topMargin = getVerticalMargin();
        }
        if ( rightMargin < 0 ) { // check if not out off screen
            rightMargin = 0;
            leftMargin = getHorizontalMargin();
        }
        menuDialog.show( topMargin, bottomMargin,leftMargin, rightMargin, true );
    }

    public Dialog getMenuDialog(){
        return this.menuDialog;
    }

    public void setSizeList(int size) {
        this.sizeList = size;
    }

    protected void buildMenu() {
        buildOptions();
        buildCommands();
    }

    protected abstract void buildOptions();

    protected abstract void buildCommands();

    protected abstract void action(Command command);

    protected final void buildCommands(String[] commands) {
        for ( int i=0; i<commands.length; i++ ) {
            Command cmd = new Command(commands[i]);
            menuDialog.addCommand(cmd);
        }

        menuDialog.addCommandListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Command cmd = evt.getCommand();
                menuDialog.dispose();
                if ( cmd.getCommandName().equals( NedResources.CANCEL ) ) {
                    // do nothing
                } else {
                    action((Command)optionsList.getSelectedItem());
                }
            }
        });

        menuDialog.addGameKeyListener(Display.GAME_LEFT, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuDialog.dispose();
            }
        });
    }

    protected final void buildOptions(Command[] options) {
        optionsModel = new DefaultListModel(options);
        optionsList.setModel(optionsModel);
        optionsList.setListCellRenderer(new MenuCellRenderer());
        optionsList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if ( evt.getSource() instanceof List ) {
                    menuDialog.dispose();
                    List list = (List)evt.getSource();
                    Command cmd = (Command)list.getSelectedItem();
                    action(cmd);
                }
            }

        });
        menuDialog.addComponent(BorderLayout.CENTER, optionsList);
    }

    protected void setTransitions(){
        CommonTransitions ct = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 600, false);
        CommonTransitions ct2 = CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 600, false);
        menuDialog.setTransitionInAnimator(ct);
        menuDialog.setTransitionOutAnimator(ct2);
    }

    /**
     * Calculates available hotizontal space that should be distributed for top and bottom margins
     * @return  hotizontal margin
     */
    protected int getHorizontalMargin() {
         return ( displayWidth - getMenuWidth() < 0 ? 1 : displayWidth - getMenuWidth() );
    }

    /**
     * Calculates available vertical space that should be distributed for top and bottom margins
     * @return  vertical margin
     */
    protected int getVerticalMargin() {
        int val =  displayHeight - getMenuHeight() - Display.getInstance().getCurrent().getSoftButton(0).getPreferredH();
        return ( val > 0 ? val : 0 );
    }

    protected int getMenuHeight() {
        int height = menuDialog.getStyle().getMargin( Component.BOTTOM )
                + menuDialog.getStyle().getMargin( Component.TOP )
                + menuDialog.getStyle().getPadding( Component.BOTTOM )
                + menuDialog.getStyle().getPadding( Component.TOP )
                + menuDialog.getBottomGap()
                + optionsList.getStyle().getMargin( Component.BOTTOM )
                + optionsList.getStyle().getMargin( Component.TOP )
                + optionsList.getStyle().getPadding( Component.BOTTOM )
                + optionsList.getStyle().getPadding( Component.TOP )
                + optionsList.size() * (getSingleOptionHeight() + optionsList.getItemGap())
                + 2 * optionsList.getBorderGap()
                + optionsList.getBottomGap()

                + 15; // magic number, could not find all height influencing factors
        return height;
    }

    protected int getSingleOptionHeight() {
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int fontHigh =  rendererItem.getStyle().getFont().getHeight();//this font is used in cell
        int height = fontHigh
                     + rendererItem.getStyle().getPadding(Component.BOTTOM)
                     + rendererItem.getStyle().getPadding(Component.TOP)
                     + rendererItem.getStyle().getMargin(Component.BOTTOM)
                     + rendererItem.getStyle().getMargin(Component.TOP)
                     + 2 * rendererItem.getStyle().getBorder().getThickness();
        return height;
    }

    protected int getMenuWidth() {
        String longestDesc = "";
        for ( int i = 0; i< optionsList.size(); i++ ) {
            String description = ((Command)optionsList.getModel().getItemAt(i)).getCommandName();
            longestDesc = description.length() < longestDesc.length() ? longestDesc : description;
        }
        MenuCellRenderer rendererItem = (MenuCellRenderer)optionsList.getRenderer();
        int selectedWidth = rendererItem.getSelectedStyle().getFont().stringWidth( longestDesc + " " );
        int unselectedWidth = rendererItem.getUnselectedStyle().getFont().stringWidth( longestDesc + " " );
        return optionsList.getStyle().getMargin( Component.LEFT )
                               + optionsList.getStyle().getMargin( Component.RIGHT )
                               + optionsList.getStyle().getPadding( Component.LEFT )
                               + optionsList.getStyle().getPadding( Component.RIGHT )
                               + menuDialog.getStyle().getMargin( Component.LEFT )
                               + menuDialog.getStyle().getMargin( Component.RIGHT )
                               + menuDialog.getStyle().getPadding( Component.LEFT )
                               + menuDialog.getStyle().getPadding( Component.RIGHT )
                               + selectedWidth < unselectedWidth ? unselectedWidth : selectedWidth
                               + rendererItem.getStyle().getMargin( Component.LEFT )
                               + rendererItem.getStyle().getMargin( Component.RIGHT )
                               + rendererItem.getStyle().getPadding( Component.LEFT )
                               + rendererItem.getStyle().getPadding( Component.RIGHT )
                               + 2 * optionsList.getBorderGap()
                               + optionsList.getSideGap()
                               + 25; // magic number, could not find all height influencing factors
    }

    public void show() {
        show(getHorizontalMargin(), getVerticalMargin()/2);
    }
}
