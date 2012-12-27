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
package org.ned.client.view.renderer;

import com.sun.lwuit.*;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;
import org.ned.client.NedMidlet;
import org.ned.client.utils.LanguageInfo;
import org.ned.client.view.style.NEDStyleToolbox;

public class RadioButtonCellRenderer extends ListCellRendererBase {

    private static final int ICON_FIT_SIZE = 36;
    private static final int ICON_WIDTH = 32;
    private static Image mLocal;
    private static Image mRemote;
    private static String mCurrentLang;
    private RadioButton mRadioBtn;
    private Label mFlag;

    public RadioButtonCellRenderer( String aCurrentLang ) {
        super();
        mCurrentLang = aCurrentLang;
        if ( com.sun.lwuit.Display.getInstance().isTouchScreenDevice() ) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10, 2, 2 );
        }
        if ( getPreferredH() < ICON_FIT_SIZE ) {
            setPreferredH( ICON_FIT_SIZE );
        }
        removeAll();
        Container cont = new Container( new BoxLayout( BoxLayout.X_AXIS ) );
        mRadioBtn = new RadioButton();
        mRadioBtn.getStyle().setBgTransparency( NEDStyleToolbox.TRANSPARENT );
        mRadioBtn.setCellRenderer( true );
        mTitle.setFocusable( false );
        mTitle.setPreferredW( Display.getInstance().getDisplayWidth() - 3 * ICON_WIDTH );
        setFocusable( true );
        mRadioBtn.setWidth( Display.getInstance().getDisplayWidth() );
        mRadioBtn.getStyle().setPadding( 0, 0, 10, 10 );
        mFlag = new Label();
        final Style flagStyle = mFlag.getStyle();
        final Style flagSelectedStyle = mFlag.getSelectedStyle();
        flagStyle.setPadding( 0, 0, 0, 0 );
        flagStyle.setMargin( 0, 0, 0, 0 );
        flagStyle.setAlignment( Label.RIGHT );
        flagStyle.setBgTransparency( 0 );

        flagSelectedStyle.setPadding( 0, 0, 0, 0 );
        flagSelectedStyle.setMargin( 0, 0, 0, 0 );

        mFlag.setPreferredW( ICON_WIDTH );
        mFlag.setCellRenderer( true );

        cont.addComponent( mRadioBtn );
        cont.addComponent( mTitle );
        cont.addComponent( mFlag );
        Label spacer = new Label( " " );
        spacer.setCellRenderer( true );
        spacer.setPreferredH( 1 );
        spacer.setFocusable( false );
        addComponent( cont );
        addComponent( spacer );

        final Image downloadStage = NedMidlet.getRes().getImage( "DownloadProgressSteps" );
        if ( mLocal == null ) {
            mLocal = downloadStage.subImage( 0, 0, 32, 32, true );
        }
        if ( mRemote == null ) {
            mRemote = downloadStage.subImage( 64, 0, 32, 32, true );
        }
    }

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        LanguageInfo langInfo = (LanguageInfo)value;
        if ( langInfo != null ) {
            mTitle.setText( langInfo.getLangName() );
            mRadioBtn.setSelected( (langInfo.getLocale() == null ? mCurrentLang == null
                                    : langInfo.getLocale().equals( mCurrentLang )) );
            mFlag.setIcon( langInfo.isLocal() ? mLocal : mRemote );

            if ( isSelected && list.hasFocus() ) {
                mTitle.getStyle().setFgColor( NEDStyleToolbox.WHITE );
                getStyle().setBgPainter( mSelectedPainter );
            } else {
                mTitle.getStyle().setFgColor( NEDStyleToolbox.MAIN_FONT_COLOR );
                getStyle().setBgPainter( mUnselectedPainter );
            }
        }
        return this;
    }
}
