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
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.NedMidlet;
import org.ned.client.command.*;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.view.customComponents.NedList;
import org.ned.client.view.renderer.CatalogListCellRenderer;

public class CatalogScreen extends NedFormBase implements ActionListener {

    private NedList mCatalogList;

    public CatalogScreen( String id ) {
        super( id );
        setNedTitle( mNewLibModel != null ? mNewLibModel.getName() : " " );

        mCatalogList = new NedList( mNewLibModel != null ? mNewLibModel.
                getChildern() : new Vector( 0 ) );
        mCatalogList.setContextMenu( new CatalogContextMenu( mCatalogList, 2 ) );
        mCatalogList.setListCellRenderer( new CatalogListCellRenderer() );
        mCatalogList.setSelectedIndex( 0 );
        mCatalogList.addActionListener( this );

        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        setPreferredW( Display.getInstance().getDisplayWidth() );
        addComponent( mCatalogList );

        addCommand( BackCatalogCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommand( DownloadsQueueViewCommand.getInstance().getCommand() );
        addCommand( DeleteContentCommand.getInstance().getCommand() );
        addCommand( DownloadAllCatalogScreenCommand.getInstance().getCommand() );
        addCommand( SearchDialogCommand.getInstance().getCommand() );
        addCommand( UpdateLibraryCommand.getInstance().getCommand() );

        addCommandListener( this );
        addGameKeyListener( Display.GAME_LEFT, this );
        addGameKeyListener( Display.GAME_RIGHT, this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == BackCatalogCommand.getInstance().getCommand() || evt.
                getKeyEvent() == Display.GAME_LEFT ) {
            mNewModel.updateNewMediaList();
            BackCatalogCommand.getInstance().execute( mNewLibModel.getId() );
        } else if ( src == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mCatalogList );
        } else if ( src instanceof List || evt.getKeyEvent()
                                           == Display.GAME_RIGHT ) {
            LibraryElement content = (LibraryElement)mCatalogList.
                    getSelectedItem();
            if ( content != null ) {
                BrowseCatalogCommand.getInstance().execute( content.getId() );
            }
        } else if ( src == DownloadAllCatalogScreenCommand.getInstance().
                getCommand() ) {
            DownloadAllCatalogScreenCommand.getInstance().execute( mCatalogList );
        } else if ( src == SearchDialogCommand.getInstance().getCommand() ) {
            Content content = ((LibraryElement)mCatalogList.getSelectedItem()).
                    getDetails();
            if ( content != null ) {
                SearchDialogCommand.getInstance().execute( content.getId() );
            }
        } else if ( src == showFreeMem ) {
            GeneralAlert.show( String.valueOf( Runtime.getRuntime().freeMemory() ), GeneralAlert.INFO );
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            Object[] params = { this.getClass(), mNewLibModel.getId() };
            HelpCommand.getInstance().execute( params );
        } else if ( src == UpdateLibraryCommand.getInstance().getCommand() ) {
            UpdateLibraryCommand.getInstance().execute( NedMidlet.
                    getSettingsManager().getLibraryManager().getCurrentLibrary() );
        } else if ( src == DownloadsQueueViewCommand.getInstance().getCommand() ) {
            DownloadsQueueViewCommand.getInstance().execute( mNewLibModel );
        }
    }
}
