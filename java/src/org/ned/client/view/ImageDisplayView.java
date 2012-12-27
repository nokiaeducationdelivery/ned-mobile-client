/*******************************************************************************
 * Copyright (c) 2011-2021 Nokia Corporation
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
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.IContent;
import org.ned.client.NedResources;
import org.ned.client.command.BackImageCommand;

/**
 * @author community
 */
public class ImageDisplayView extends NedFormBase implements ActionListener {

    private static final Command mFitCommand = new Command( NedResources.FIT_TO_SCREEN );
    private static final Command mZoomIn = new Command( NedResources.ZOOM_IN );
    private static final Command mZoomOut = new Command( NedResources.ZOOM_OUT );
    private static final int ZOOMSTEPS = 4;
    private Image mImg = null;
    private Label mImage;
    private IContent mContent;
    private int mCurrentFactorIndex;

    public ImageDisplayView( IContent content ) {
        mContent = content;
        mCurrentFactorIndex = 0;
        String pictureFile = content.getMediaFile();
        setLayout( new BorderLayout() );
        setNedTitle( mContent.getText() );
        setScrollable( true );

        FileConnection fc = null;
        InputStream is = null;
        try {
            fc = (FileConnection)Connector.open( pictureFile, Connector.READ );
            if ( fc.exists() ) {
                is = fc.openInputStream();
                mImg = Image.createImage( is );
                mImage = new Label( mImg.scaledWidth( Display.getInstance().getDisplayWidth() ) );
                mImage.setFocusable( false );
                mImage.getStyle().setMargin( 0, 0, 0, 0 );
                mImage.getStyle().setPadding( 0, 0, 0, 0 );
                mImage.getStyle().setAlignment( Label.CENTER );
                mImage.getSelectedStyle().setAlignment( Label.CENTER );
                addComponent( BorderLayout.CENTER, mImage );
            }
        } catch ( IOException ex ) {
            ex.printStackTrace(); // TODO default pic?
        } finally {
            try {
                is.close();
            } catch ( Exception ex ) {
            }
            try {
                fc.close();
            } catch ( Exception ex ) {
            }
        }

        addCommand( BackImageCommand.getInstance().getCommand() );
        addCommand( mZoomIn );
        addCommandListener( this );
        addGameKeyListener( Display.GAME_FIRE, this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();
        if ( src == BackImageCommand.getInstance().getCommand() ) {
            BackImageCommand.getInstance().execute( mContent.getParentId() );
        } else if ( src == mFitCommand ) {
            mImage.setIcon( mImg.scaledWidth( Display.getInstance().getDisplayWidth() ) );
            removeAllCommands();
            addCommand( BackImageCommand.getInstance().getCommand() );
            addCommand( mZoomIn );
            mCurrentFactorIndex = 0;
        } else if ( src == mZoomIn ) {
            removeAllCommands();
            addCommand( BackImageCommand.getInstance().getCommand() );

            if ( ++mCurrentFactorIndex < ZOOMSTEPS ) {
                try {
                    doZoom();
                    addCommand( mZoomIn );
                } catch ( OutOfMemoryError ex ) {//OOM possible when rescaling
                    --mCurrentFactorIndex;
                    GeneralAlert.show( NedResources.MAX_ZOOM, GeneralAlert.INFO );
                }
                addCommand( mZoomOut );
                addCommand( mFitCommand );
            } else {
                --mCurrentFactorIndex;
                addCommand( mZoomOut );
                addCommand( mFitCommand );
                try {
                    doZoom();
                } catch ( OutOfMemoryError ex ) {//OOM possible when rescaling
                    removeCommand( mZoomIn );
                    GeneralAlert.show( NedResources.MAX_ZOOM, GeneralAlert.INFO );
                }
            }
        } else if ( src == mZoomOut ) {
            removeAllCommands();
            addCommand( BackImageCommand.getInstance().getCommand() );
            addCommand( mZoomIn );
            if ( --mCurrentFactorIndex > 0 ) {
                doZoom();
                addCommand( mZoomOut );
            } else {
                mImage.setIcon( mImg.scaledWidth( Display.getInstance().getDisplayWidth() ) );
                mCurrentFactorIndex = 0;
            }
            addCommand( mFitCommand );
        }
    }

    private void doZoom() {
        mImage.setIcon( mImg.scaledWidth( Display.getInstance().getDisplayWidth() << mCurrentFactorIndex ) );
    }
}
