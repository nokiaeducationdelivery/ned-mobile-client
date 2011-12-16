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
import org.ned.client.view.StatisticsOptionScreen;


public class StatisticsOptionsCommand extends NedCommand {
    private static StatisticsOptionsCommand instance;

    private StatisticsOptionsCommand() {
        command = new Command( NedResources.STATISTICS_OPTIONS );
    }

    public static StatisticsOptionsCommand getInstance() {
        if( instance == null ) {
            instance = new StatisticsOptionsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new StatisticsOptionScreen().show();
    }
}
