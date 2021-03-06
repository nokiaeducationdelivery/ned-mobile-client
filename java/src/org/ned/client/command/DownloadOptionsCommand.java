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
import org.ned.client.view.DownloadOptionScreen;


public class DownloadOptionsCommand extends NedCommand {
    private static DownloadOptionsCommand instance;

    private DownloadOptionsCommand() {
        command = new Command( NedResources.DOWNLOAD_OPTIONS );
    }

    public static DownloadOptionsCommand getInstance() {
        if( instance == null ) {
            instance = new DownloadOptionsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new DownloadOptionScreen().show();
    }
}
