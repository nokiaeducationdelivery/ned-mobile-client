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
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import org.ned.client.MotdManager;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.*;
import org.ned.client.library.NedLibrary;
import org.ned.client.library.VisibleLibraryModel;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.customComponents.NedList;
import org.ned.client.view.renderer.LibrariesListCellRenderer;
import org.ned.client.view.renderer.UnselectedBGPainter;

public class MainScreen extends NedFormBase implements ActionListener {

    private final int TICKERSPEED = 100;//delay ms
    private List librariesList = null;
    private Label motdLabel;

    public MainScreen() {
        super();
        setNedTitle( NedResources.LIBRARIES );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        motdLabel = new Label( MotdManager.getMessage() );
        motdLabel.setTickerEnabled( true );
        motdLabel.setEndsWith3Points( true );
        addComponent( motdLabel );
        motdLabel.startTicker( TICKERSPEED, true );
        addUnderlineSeparator();

        LibrariesListCellRenderer librariesRenderer = new LibrariesListCellRenderer();
        librariesList = new NedList( new VisibleLibraryModel( NedMidlet.
                getSettingsManager().getLibraryManager() ) );
        librariesList.setListCellRenderer( librariesRenderer );
        librariesList.setFixedSelection( List.FIXED_NONE );
        librariesList.getStyle().setBorder( Border.createEmpty() );
        librariesList.setSelectedIndex( 0 );

        if ( librariesList.size() > 0 ) {
            addComponent( librariesList );
        } else {
            addEmptyLabel();
        }

        addCommand( ExitCommand.getInstance().getCommand() );
        addCommand( ShowAboutCommand.getInstance().getCommand() );
        addCommand( CheckForUpdateCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommand( OpenHistoryCommand.getInstance().getCommand() );
        addCommand( StatisticsCommand.getInstance().getCommand() );
        addCommand( DownloadsQueueViewCommand.getInstance().getCommand() );
        addCommand( SettingsCommand.getInstance().getCommand() );
        addCommand( OpenLibraryManagerCommand.getInstance().getCommand() );
        addCommand( SearchDialogCommand.getInstance().getCommand() );

        addGameKeyListener( Display.GAME_RIGHT, this );

        librariesList.addActionListener( this );
        addCommandListener( this );
    }

    private void addUnderlineSeparator() {
        Label underLine = new Label( " " );
        underLine.setFocusable( false );
        underLine.setPreferredH( 3 );
        underLine.setPreferredW( Display.getInstance().getDisplayWidth() );
        underLine.getStyle().setMargin( 0, 0, 0, 0 );
        underLine.getStyle().setPadding( 0, 0, 0, 0 );
        underLine.getStyle().setBgPainter( new UnselectedBGPainter() );
        addComponent( underLine );
    }

    public void actionPerformed( ActionEvent evt ) {//add command to buttons and than remove hash map
        motdLabel.stopTicker();
        Object src = evt.getSource();
        if ( src == librariesList || evt.getKeyEvent() == Display.GAME_RIGHT ) {
            NedLibrary selectedLibrary = (NedLibrary)librariesList.
                    getSelectedItem();
            if ( selectedLibrary != null ) {
                BrowseLibraryCommand.getInstance().execute( selectedLibrary.
                        getId() );
            } else {
                GeneralAlert.show( NedResources.LIBRARY_NOT_EXISTS, GeneralAlert.INFO );
            }
        } else if ( src == ExitCommand.getInstance().getCommand() ) {
            ExitCommand.getInstance().execute( null );
        } else if ( src == StatisticsCommand.getInstance().getCommand() ) {
            StatisticsCommand.getInstance().execute( null );
        } else if ( src == SettingsCommand.getInstance().getCommand() ) {
            SettingsCommand.getInstance().execute( null );
        } else if ( src == DownloadsQueueViewCommand.getInstance().getCommand() ) {
            DownloadsQueueViewCommand.getInstance().execute( null );
        } else if ( src == OpenLibraryManagerCommand.getInstance().getCommand() ) {
            OpenLibraryManagerCommand.getInstance().execute( null );
        } else if ( src == OpenHistoryCommand.getInstance().getCommand() ) {
            OpenHistoryCommand.getInstance().execute( null );
        } else if ( src == showFreeMem ) {
            GeneralAlert.show( String.valueOf( Runtime.getRuntime().freeMemory() ), GeneralAlert.INFO );
        } else if ( src == SearchDialogCommand.getInstance().getCommand() ) {
            NedLibrary selectedLibrary = (NedLibrary)librariesList.
                    getSelectedItem();
            if ( selectedLibrary != null
                 && NedIOUtils.fileExists( selectedLibrary.getFileUri() ) ) {
                SearchDialogCommand.getInstance().execute( selectedLibrary.getId() );
            } else {
                GeneralAlert.show( NedResources.LIBRARY_NOT_EXISTS, GeneralAlert.INFO );
            }
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        } else if ( src == ShowAboutCommand.getInstance().getCommand() ) {
            ShowAboutCommand.getInstance().execute( null );
        } else if ( src == CheckForUpdateCommand.getInstance().getCommand() ) {
            CheckForUpdateCommand.getInstance().beginAsync( null, new CheckForUpdateCallback(), true );
        }
    }

    private void addEmptyLabel() {
        Label empty = new Label( NedResources.NO_LIBRARIES );
        empty.setAlignment( Label.CENTER );
        addComponent( empty );
    }


    private static class CheckForUpdateCallback implements AsyncCompletedCallback {

        public CheckForUpdateCallback() {
        }

        public void onSuccess() {
        }

        public void onFailure( String error ) {
            GeneralAlert.show( error, GeneralAlert.WARNING );
        }
    }
}
