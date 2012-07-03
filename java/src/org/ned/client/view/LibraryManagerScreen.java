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

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListModel;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.*;
import org.ned.client.library.NedLibrary;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.customComponents.ClearTextField;
import org.ned.client.view.renderer.LibrariesListCheckBoxCellRenderer;
import org.ned.client.view.renderer.UnselectedBGPainter;

public class LibraryManagerScreen extends NedFormBase implements ActionListener, FocusListener, SelectionListener {

    private ClearTextField mServerUrlTextArea;
    private ClickableList mLibrariesList;
    private final String mDefaultInput = "";

    public LibraryManagerScreen() {
        super();
        setNedTitle( NedResources.LIBRARY_MANAGER );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );

        initForm();

        addCommandListener( this );
        addFocusListener( this );
    }

    private void initForm() {
        addDemoLabels();
        addUrlLabel();
        addUrlTextArea();
        addLibrariesList();
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

    private void addLibrariesList() {
        Label showLibrary = new Label( NedResources.SHOW_LIBRARY );
        showLibrary.setFocusable( false );
        addComponent( showLibrary );

        addUnderlineSeparator();

        mLibrariesList = new ClickableList( NedMidlet.getSettingsManager().
                getLibraryManager() );
        mLibrariesList.setRenderer( new LibrariesListCheckBoxCellRenderer() );
        mLibrariesList.setFixedSelection( List.FIXED_NONE );
        mLibrariesList.addActionListener( this );
        mLibrariesList.addSelectionListener( this );
        addComponent( mLibrariesList );
    }

    private void addUrlTextArea() {
        mServerUrlTextArea = new ClearTextField();
        mServerUrlTextArea.setText( mDefaultInput );
        mServerUrlTextArea.addFocusListener( this );
        addComponent( mServerUrlTextArea );
    }

    private void addUrlLabel() {
        Label serverUrlLabel = new Label( NedResources.LIBRARY_ID );
        serverUrlLabel.setAlignment( Label.LEFT );
        serverUrlLabel.setFocusable( false );
        addComponent( serverUrlLabel );
    }

    public void actionPerformed( ActionEvent evt ) {

        Object src = evt.getSource();
        if ( src == BackSettingsCommand.getInstance().getCommand() ) {
            NedMidlet.getSettingsManager().saveSettings();
            BackSettingsCommand.getInstance().execute( null );
        } else if ( src == RemoveLibraryCommand.getInstance().getCommand() ) {
            RemoveLibraryCommand.getInstance().execute( mLibrariesList.
                    getSelectedItem() );
        } else if ( src == mLibrariesList ) {
            int maxPressedWidth = (int)(0.2 * mLibrariesList.getWidth());
            if ( maxPressedWidth > 50 ) {
                maxPressedWidth = 50;
            }
            if ( mLibrariesList.getLastPointerPressedCoordinateX()
                    < maxPressedWidth
                    || mLibrariesList.getSelectedIndex()
                    == mLibrariesList.getPreviusSelection() ) {
                NedLibrary library = (NedLibrary)mLibrariesList.getSelectedItem();
                if ( library != null ) {
                    library.setVisible( !library.getVisible() );
                }
            }
        } else if ( src == AddLibraryCommand.getInstance().getCommand() ) {
            AddLibraryCommand.getInstance().beginAsync( mServerUrlTextArea.
                    getText(), null, false );
        } else if ( src == UpdateLibraryCommand.getInstance().getCommand() ) {
            UpdateLibraryCommand.getInstance().execute( mLibrariesList.
                    getSelectedItem() );
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }

    public void focusGained( Component cmpnt ) {
        super.focusGained();
        removeAllCommands();
        addCommand( BackSettingsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        if ( cmpnt == mLibrariesList && mLibrariesList.size() > 0 ) {
            addCommand( RemoveLibraryCommand.getInstance().getCommand() );
            addCommand( UpdateLibraryCommand.getInstance().getCommand() );
        } else if ( cmpnt == mServerUrlTextArea ) {
            addCommand( AddLibraryCommand.getInstance().getCommand() );
        }
    }

    public void focusLost( Component cmpnt ) {
        super.focusLost();
    }

    public void selectionChanged( int oldSek, int newSel ) {
        if ( getFocused() == mLibrariesList ) {
            removeAllCommands();
            addCommand( BackSettingsCommand.getInstance().getCommand() );

            if ( mLibrariesList.size() > 0 ) {
                addCommand( RemoveLibraryCommand.getInstance().getCommand() );
                addCommand( UpdateLibraryCommand.getInstance().getCommand() );
                NedLibrary lib = (NedLibrary)mLibrariesList.getModel().getItemAt( newSel );
                if ( NedIOUtils.fileExists( lib.getFileUri() ) ) {
                    addCommand( UpdateLibraryCommand.getInstance().getCommand() );
                }
            }
        }
    }

    private void addDemoLabels() {
        if ( NedMidlet.getAccountManager().getServerUrl().equalsIgnoreCase( NedConsts.NedDemo.DEMOURL )
                && NedMidlet.getAccountManager().getCurrentUser().login.equals( NedConsts.NedDemo.DEMOUSERNAME )
                && NedMidlet.getAccountManager().getCurrentUser().password.
                equals( NedConsts.NedDemo.DEMOPASSWORD )
                && NedMidlet.getSettingsManager().getLibraryManager().getSize()
                == 0 ) {
            Label username = new Label( NedResources.DEMOLIBID );
            username.setAlignment( Label.LEFT );
            addComponent( username );
        }
    }
}

/**
 * List class extension that remembers last pointer press X coordinate It may be
 * used to perform different actions based on which part of List item was
 * clicked
 */
class ClickableList extends List implements SelectionListener, FocusListener {

    private int m_x = 0;
    private int previusSelection = -1;
    private boolean focus = false;

    public ClickableList( ListModel model ) {
        super( model );
        addSelectionListener( this );
        addFocusListener( this );
    }

    public void pointerPressed( int x, int y ) {
        m_x = x;
        super.pointerPressed( x, y );
    }

    public int getLastPointerPressedCoordinateX() {
        return m_x;
    }

    public int getPreviusSelection() {
        return previusSelection;
    }

    public void selectionChanged( int i, int i1 ) {
        if ( !hasFocus() ) {
            return;
        }

        if ( focus ) {
            previusSelection = i;
        } else {
            focus = true;
        }
    }

    public void focusGained( Component cmpnt ) {
        super.focusGained();
        focus = false;
    }

    public void focusLost( Component cmpnt ) {
        super.focusLost();
        setSelectedIndex( 0 );
        previusSelection = -1;
    }
};
