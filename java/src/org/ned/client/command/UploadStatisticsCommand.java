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
import org.ned.client.MotdManager;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.GeneralAlert;


public class UploadStatisticsCommand extends NedCommand {

    private static UploadStatisticsCommand instance;

    public UploadStatisticsCommand() {
        command = new Command(NedResources.MID_UPLOAD_COMMAND);
    }

    public static UploadStatisticsCommand getInstance() {
        if( instance == null ) {
            instance = new UploadStatisticsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        StatisticsManager.uploadStats( false );
        MotdManager.getInstance().updateMotd();
        GeneralAlert.show( NedResources.DLM_NEWSTATS, GeneralAlert.INFO );
    }
}
