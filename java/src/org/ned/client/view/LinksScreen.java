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
import org.ned.client.NedResources;
import org.ned.client.command.BackLinksCommand;
import org.ned.client.command.OpenLinkCommand;
import org.ned.client.view.renderer.SimpleListCellRenderer;

public class LinksScreen extends NedFormBase implements ActionListener {

    private List linksList;

    public LinksScreen( String contentId ) {
        super( contentId );
        setNedTitle( mNewLibModel != null ? mNewLibModel.getName() : " " );

        addCommand( BackLinksCommand.getInstance().getCommand() );
        if ( mNewLibModel != null && mNewLibModel.getDetails() != null && mNewLibModel.getDetails().getExternalLinks() != null
             && mNewLibModel.getDetails().getExternalLinks().size() > 0 ) {
            linksList = new List( mNewLibModel.getDetails().getExternalLinks() );
            linksList.setListCellRenderer( new SimpleListCellRenderer() );
            linksList.setSelectedIndex( 0 );
            linksList.setPreferredW( Display.getInstance().getDisplayWidth() );
            linksList.addActionListener( this );
            addComponent( linksList );
            addCommand( OpenLinkCommand.getInstance().getCommand() );
        } else {
            Label noLinks = new Label( NedResources.NO_LINKS );
            noLinks.setAlignment( CENTER );
            noLinks.setPreferredW( Display.getInstance().getDisplayWidth() );
            addComponent( noLinks );
        }

        addGameKeyListener( Display.GAME_LEFT, this );
        addGameKeyListener( Display.GAME_RIGHT, this );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == BackLinksCommand.getInstance().getCommand()
             || evt.getKeyEvent() == Display.GAME_LEFT ) {
            BackLinksCommand.getInstance().execute(
                    mNewLibModel != null ? mNewLibModel.getParent().getId() : null );
        } else if ( src instanceof List
                    || src == OpenLinkCommand.getInstance().getCommand() ) {
            OpenLinkCommand.getInstance().execute( linksList.getSelectedItem() );
        }
    }
}
