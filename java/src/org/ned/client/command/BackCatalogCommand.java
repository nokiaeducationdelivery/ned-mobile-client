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
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MainScreen;


public class BackCatalogCommand extends BackNedCommand {

    private static BackCatalogCommand instance;

    private BackCatalogCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackCatalogCommand getInstance() {
        if( instance == null) {
            instance = new BackCatalogCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
         new MainScreen().show();
    }

    protected void doLog( Object aParam ) {
        StatisticsManager.logEvent( StatType.BROWSE_CATALOG_BACK, "Id=" + (String)aParam );
    }
}
