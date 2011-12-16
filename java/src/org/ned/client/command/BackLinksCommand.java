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
import org.ned.client.NedResources;
import org.ned.client.view.MediaItemsScreen;
import org.ned.client.view.HistoryScreen;


public class BackLinksCommand extends BackNedCommand {

    private static BackLinksCommand instance;

    private BackLinksCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackLinksCommand getInstance() {
        if( instance == null) {
            instance = new BackLinksCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        if(param == null){
            new HistoryScreen().show();
        }else{
            new MediaItemsScreen((String)param).show();
        }
    }
}