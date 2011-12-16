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
import org.ned.client.view.SettingsScreen;

public class BackStatisticsOptionsCommand extends BackNedCommand {

    private static BackStatisticsOptionsCommand instance;

    private BackStatisticsOptionsCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackStatisticsOptionsCommand getInstance() {
        if( instance == null) {
            instance = new BackStatisticsOptionsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new SettingsScreen().show();
    }
}
