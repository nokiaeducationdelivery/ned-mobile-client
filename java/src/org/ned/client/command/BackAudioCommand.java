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
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MediaItemsScreen;
import org.ned.client.view.HistoryScreen;


public class BackAudioCommand extends BackNedCommand {

    private static BackAudioCommand instance;

    private BackAudioCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackAudioCommand getInstance() {
        if ( instance == null) {
            instance = new BackAudioCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        NedMidlet.getInstance().getAudioPlayer().stopPlayer();
        if ( aParam == null ) {
            new HistoryScreen().show();
        }else{
            new MediaItemsScreen( (String)aParam ).show();
        }
    }

    protected void doLog( Object aParam ) {
        String id = (String)aParam;
        StatisticsManager.logEvent( StatType.PLAY_ITEM_END, "Id=" + id +";SUCESS;" );
    }
}