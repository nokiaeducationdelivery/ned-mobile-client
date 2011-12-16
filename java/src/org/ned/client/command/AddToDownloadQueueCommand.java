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
import org.ned.client.Content;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.transfer.UrlEncoder;


public class AddToDownloadQueueCommand extends NedCommand{
    private static AddToDownloadQueueCommand instance;

    private AddToDownloadQueueCommand() {
        command = new Command( NedResources.ADDTOQUEUE );
    }

    public static AddToDownloadQueueCommand getInstance() {
        if( instance == null ) {
            instance = new AddToDownloadQueueCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Content currentElement = (Content) param;
        String path = NedMidlet.getAccountManager().getServerUrl() + "/"
                + NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getId() + "/"
                + NedConsts.NedRemotePath.ROOTDIR + NedConsts.NedRemotePath.VIDEOSDIR + UrlEncoder.URLEncoder(currentElement.getData());
        NedMidlet.getInstance().getDownloadManager().addDownloadToQueue(currentElement.getMediaFile(), path, currentElement.getText(), false);
    }
}