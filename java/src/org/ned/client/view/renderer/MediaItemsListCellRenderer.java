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

import com.sun.lwuit.*;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.NedMidlet;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.transfer.IMediaItemListUpdater;
import org.ned.client.utils.MediaTypeResolver;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.style.NEDStyleToolbox;

public class MediaItemsListCellRenderer extends ListCellRendererBase implements IMediaItemListUpdater {

    private static final int ICON_FIT_SIZE = 36;
    private static final int ICON_WIDTH = 32;
    private static Image local;
    private static Image remote;
    private static Image inProgress;
    private Label mFlag;
    private Label mMediaType;
    private int displayW;
    private Vector mFileLists;

    public MediaItemsListCellRenderer() {
        super();
        NedMidlet.getInstance().getDownloadManager().setMediaListUpdater( this );
        if ( com.sun.lwuit.Display.getInstance().isTouchScreenDevice() ) {
            getStyle().setPadding( 10, 10, 2, 2 );
            getSelectedStyle().setPadding( 10, 10, 2, 2 );
        }
        if ( getPreferredH() < ICON_FIT_SIZE ) {
            setPreferredH( ICON_FIT_SIZE );
        }
        displayW = Display.getInstance().getDisplayWidth();

        setLayout( new BoxLayout( BoxLayout.X_AXIS ) );
        setWidth( displayW );
        setFocusable( true );
        mMediaType = new Label( " " );//must set some text to render properly
        final Style mediaTypeStyle = mMediaType.getStyle();
        final Style mediaTypeStyleSelected = mMediaType.getSelectedStyle();
        mediaTypeStyle.setPadding( 0, 0, 0, 0 );
        mediaTypeStyle.setMargin( 0, 0, 0, 0 );
        mediaTypeStyle.setAlignment( Label.LEFT );
        mediaTypeStyle.setBgTransparency( 0 );
        mediaTypeStyleSelected.setPadding( 0, 0, 0, 0 );
        mediaTypeStyleSelected.setMargin( 0, 0, 0, 0 );
        mediaTypeStyleSelected.setAlignment( Label.LEFT );
        mMediaType.setPreferredW( ICON_WIDTH );
        mMediaType.setCellRenderer( true );

        mTitle.setPreferredW( displayW - 3 * ICON_WIDTH );
        removeComponent( mTitle );

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

        addComponent( mMediaType );
        addComponent( mTitle );
        addComponent( mFlag );

        final Image downloadStage = NedMidlet.getRes().getImage( "DownloadProgressSteps" );
        local = downloadStage.subImage( 0, 0, 32, 32, true );
        remote = downloadStage.subImage( 64, 0, 32, 32, true );
        inProgress = downloadStage.subImage( 32, 0, 32, 32, true );
    }

    public Component getListCellRendererComponent( List list, Object value, int index, boolean isSelected ) {
        if ( value == null ) {
            return this;
        }
        boolean isNew = ((LibraryElement)value).isNew();
        Content content = ((LibraryElement)value).getDetails();

        mMediaType.setText( "" );
        mMediaType.setIcon( null );
        mMediaType.setIcon( MediaTypeResolver.getTypeIcon( content.getType() ) );

        mTitle.setText( content.getText() );

        if ( content instanceof Content ) {

            if ( mFileLists == null ) {
                mFileLists = NedIOUtils.directoryListing( ((Content)content).getMediaFilePath() );
            }

            if ( mFileLists != null && ((Content)content).isDownloaded( mFileLists ) ) {
                mFlag.setIcon( local );
            } else if ( NedMidlet.getInstance().getDownloadManager().isTransferExist( content.getMediaFile() ) ) {
                mFlag.setIcon( inProgress );
            } else {
                mFlag.setIcon( remote );
            }
        }

        if ( isSelected ) {
            setFocus( true );
            mTitle.getStyle().setFgColor( isNew ? NEDStyleToolbox.BLUE : NEDStyleToolbox.WHITE );
            getStyle().setBgPainter( mSelectedPainter );
        } else {
            setFocus( false );
            mTitle.getStyle().setFgColor( isNew ? NEDStyleToolbox.BLUE : NEDStyleToolbox.MAIN_FONT_COLOR );
            getStyle().setBgPainter( mUnselectedPainter );
        }
        return this;
    }

    public void updateMediaList() {
        mFileLists = null;
    }
}
