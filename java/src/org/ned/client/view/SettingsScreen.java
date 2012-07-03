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

import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import org.ned.client.NedResources;
import org.ned.client.command.*;
import org.ned.client.view.renderer.SimpleListCellRenderer;


public class SettingsScreen extends NedFormBase implements ActionListener{

    private List mCommands;

    public SettingsScreen() {
        super();
        setNedTitle( NedResources.MID_SETTINGS_TITLE );
        mCommands = new List( new Object[]{ StatisticsOptionsCommand.getInstance(),
                                            DownloadOptionsCommand.getInstance(),
                                            LoginViewCommand.getInstance(),
                                            LanguagesViewCommand.getInstance()
                                            } );
        mCommands.setListCellRenderer( new SimpleListCellRenderer() );
        mCommands.addActionListener( this );
        addComponent( mCommands );
        addCommand( BackSettingsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();
        if ( src == BackSettingsCommand.getInstance().getCommand() ) {
            BackSettingsCommand.getInstance().execute( null );
        } else if ( src == mCommands && mCommands.getSelectedIndex() >= 0 ) {
            ((NedCommand)mCommands.getSelectedItem()).execute( null );
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }
}
