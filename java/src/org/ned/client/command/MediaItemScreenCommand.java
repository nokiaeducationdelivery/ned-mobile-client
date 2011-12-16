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
package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.view.MediaItemsScreen;


public class MediaItemScreenCommand extends NedCommand{
    private static MediaItemScreenCommand instance;

    private MediaItemScreenCommand() {
        command = new Command( NedResources.OPEN );
    }

    public static MediaItemScreenCommand getInstance() {
        if( instance == null ) {
            instance = new MediaItemScreenCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
       Content contents = (Content)param;
       new MediaItemsScreen(contents.getId()).show();
    }
}
