/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
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
import com.sun.lwuit.List;
import org.ned.client.NedResources;
import org.ned.client.command.CategoryScreenCommand;
import org.ned.client.command.DeleteContentCommand;


public class CatalogContextMenu extends ContextMenu {

    public CatalogContextMenu( List aList, int aMode ) {
        super( aList, aMode );
    }

    protected void buildOptions(){
        Command[] options  = new Command[] { CategoryScreenCommand.getInstance().getCommand(),
                                             DeleteContentCommand.getInstance().getCommand(),
                                           };

        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] { NedResources.CANCEL, NedResources.SELECT };
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if( cmd == CategoryScreenCommand.getInstance().getCommand() ) {
            CategoryScreenCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mMediaItemList );
        }
    }
}

