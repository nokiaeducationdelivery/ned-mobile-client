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
package org.ned.client.view;

import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.NedResources;
import org.ned.client.command.OpenLibraryManagerCommand;
import org.ned.client.library.advanced.LibraryChangesReport;

public class ChangesDetailsScreen extends NedFormBase implements ActionListener {

    public ChangesDetailsScreen( LibraryChangesReport aReport ) {
        super();
        setNedTitle( NedResources.LATEST_CHANGES );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );

        TextArea item = new TextArea( 5, 20 );
        item.setSelectedStyle( item.getUnselectedStyle() );
        item.setEditable( false );
        item.setFocusable( true );
        item.setText( aReport.getFullReport().trim() );
        item.setRows( item.getLines() - 1 );
        addComponent( item );
        addCommand( OpenLibraryManagerCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();
        if ( src == OpenLibraryManagerCommand.getInstance().getCommand() ) {
            OpenLibraryManagerCommand.getInstance().execute( null );
        }
    }
}
