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
import org.ned.client.view.CategoryScreen;

public class BrowseCatalogCommand extends NedCommand {

    private static BrowseCatalogCommand instance;

    private BrowseCatalogCommand() {
        command = new Command( NedResources.BROWSE );
    }

    public static BrowseCatalogCommand getInstance() {
        if (instance == null) {
            instance = new BrowseCatalogCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        String id = (String)aParam;
        new CategoryScreen( id ).show();
    }

    protected void doLog( Object aParam ) {
        String id = (String)aParam;
        StatisticsManager.logEvent( StatType.BROWSE_CATALOG_OPEN, "Id=" + id );
    }
}
