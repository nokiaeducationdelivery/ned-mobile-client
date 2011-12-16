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
import org.ned.client.view.StatisticsScreen;

public class StatisticsCommand extends NedCommand{

    private static StatisticsCommand exitCommand;

    private StatisticsCommand() {
        command = new Command(NedResources.MID_STATISTICS_COMMAND);
    }

    public static StatisticsCommand getInstance() {
        if( exitCommand == null) {
           exitCommand = new StatisticsCommand();
        }
        return exitCommand;
    }

    protected void doAction(Object param) {
        new StatisticsScreen().show();
    }
}
