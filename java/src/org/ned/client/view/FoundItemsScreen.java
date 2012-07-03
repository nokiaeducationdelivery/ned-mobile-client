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
package org.ned.client.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.*;
import org.ned.client.library.advanced.KeywordFilter;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.library.advanced.LibraryHelpers;
import org.ned.client.view.renderer.MediaItemsListCellRenderer;

public class FoundItemsScreen extends NedFormBase implements ActionListener, SelectionListener {

    private List mMediaList;

    public FoundItemsScreen( String keyWord, String contentId ) {
        super( contentId );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        setNedTitle( mNewLibModel != null ? mNewLibModel.getName() : " " );

        Vector mediaItems = mNewLibModel.getAllMediaItems();
        mediaItems = LibraryHelpers.filter( mediaItems, new KeywordFilter( keyWord ) );

        if ( mediaItems.size() > 0 ) {
            mMediaList = new List( mediaItems );
            mMediaList.setSelectedIndex( 0 );
            mMediaList.setRenderer( new MediaItemsListCellRenderer() );
            mMediaList.setPreferredW( Display.getInstance().getDisplayWidth() );
            mMediaList.addSelectionListener( this );
            addComponent( mMediaList );
            addGameKeyListener( Display.GAME_FIRE, this );
            mMediaList.addActionListener( this );
        } else {
            noItemsFound();
        }
        addCommand( BackGenericCommand.getInstance().getCommand() );
        addCommand( SearchDialogCommand.getInstance().getCommand() );
        addGameKeyListener( Display.GAME_LEFT, this );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == BackGenericCommand.getInstance().getCommand()
                || evt.getKeyEvent() == Display.GAME_LEFT ) {
            BackGenericCommand.getInstance().execute( mNewLibModel.getParent().
                    getId() );
        } else if ( src == ShowDetailsCommand.getInstance().getCommand() ) {
            ShowDetailsCommand.getInstance().execute( ((LibraryElement)mMediaList.
                    getSelectedItem()).getDetails() );
        } else if ( src == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mMediaList );
        } else if ( src == InstantDownloadCommand.getInstance().getCommand() ) {
            InstantDownloadCommand.getInstance().execute( ((LibraryElement)mMediaList.
                    getSelectedItem()).getDetails() );
        } else if ( src instanceof List && mMediaList.size() > 0 ) {
            LibraryElement content = ((LibraryElement)mMediaList.getSelectedItem());
            if ( content.getDetails().isDownloaded() ) {
                if ( content.isNew() ) {
                    content.setNew( false );
                    mNewModel.removeFromUpdated( content.getId() );
                }
                PlayMediaCommand.getInstance().execute( content.getDetails() );
            } else {
                AddToDownloadQueueCommand.getInstance().execute( content.
                        getDetails() );
                String state = NedResources.MID_MANUAL;
                if ( NedMidlet.getInstance().getDownloadState()
                        == NedMidlet.DOWNLOAD_AUTOMATIC ) {
                    state = NedResources.MID_AUTOMATIC;
                }
                Object[] params = {content.getDetails().getText(), state};
                String s = Localization.getMessage( NedResources.MID_ADDED_DOWNLOAD_MESSAGE, params );
                GeneralAlert.show( s, GeneralAlert.INFO );
            }
        } else if ( src == SearchDialogCommand.getInstance().getCommand() ) {
            SearchDialogCommand.getInstance().execute( mNewLibModel.getId() );
        } else if ( src == AddToDownloadQueueCommand.getInstance().getCommand() ) {
            Content content = (Content)mMediaList.getSelectedItem();
            AddToDownloadQueueCommand.getInstance().execute( content );
        } else if ( src == showFreeMem ) {
            //GeneralAlert.show(String.valueOf(Runtime.getRuntime().freeMemory()));
        }
    }

    public void selectionChanged( int oldSel, int newSel ) {
        Content content = ((LibraryElement)mMediaList.getModel().getItemAt( newSel )).
                getDetails();
        removeAllCommands();
        addCommand( BackGenericCommand.getInstance().getCommand() );

        if ( content != null && content.isDownloaded() ) {
            addCommand( ShowDetailsCommand.getInstance().getCommand() );
            addCommand( DeleteContentCommand.getInstance().getCommand() );
            addCommand( PlayMediaCommand.getInstance().getCommand() );
        } else {
            addCommand( ShowDetailsCommand.getInstance().getCommand() );
            addCommand( AddToDownloadQueueCommand.getInstance().getCommand() );
            addCommand( InstantDownloadCommand.getInstance().getCommand() );
        }
    }

    private void noItemsFound() {
        Label noItems = new Label( NedResources.NO_ITEMS_FOUND );
        noItems.setAlignment( CENTER );
        addComponent( noItems );
    }
}
