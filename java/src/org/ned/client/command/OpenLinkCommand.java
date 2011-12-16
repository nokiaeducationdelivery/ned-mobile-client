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
import javax.microedition.io.ConnectionNotFoundException;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.GeneralAlert;


public class OpenLinkCommand extends NedCommand{
    private static OpenLinkCommand instance;

    private OpenLinkCommand() {
        command = new Command( NedResources.OPEN_LINK );
    }

    public static OpenLinkCommand getInstance() {
        if( instance == null ) {
            instance = new OpenLinkCommand();
        }
        return instance;
    }

    protected void doAction(Object aParam) {
        String link = (String)aParam;
        if (!link.startsWith( "http://" ) && !link.startsWith( "https://" ) ) {
            link = "http://".concat( link );
        }
        try {
            NedMidlet.getInstance().platformRequest( link );
        } catch ( ConnectionNotFoundException ex ) {
            GeneralAlert.show( (String)aParam + ", " + ex.getMessage(), GeneralAlert.ERROR );
        }
    }

    protected void doLog( Object aParam ) {
        String link = (String)aParam;
        StatisticsManager.logEvent( StatType.LINK_OPEN , link );
    }
}
