/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
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
import org.ned.client.transfer.DownloadManager;
import org.ned.client.transfer.UrlEncoder;
import org.ned.client.view.GeneralAlert;

public class InstantDownloadCommand extends NedCommand {

    private static InstantDownloadCommand instance;

    private InstantDownloadCommand() {
        command = new Command( NedResources.DOWNLOAD_NOW );
    }

    public static InstantDownloadCommand getInstance() {
        if ( instance == null ) {
            instance = new InstantDownloadCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Content currentElement = (Content)param;
        String path = NedMidlet.getAccountManager().getServerUrl() + "/"
                + NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getId() + "/"
                + NedConsts.NedRemotePath.ROOTDIR + NedConsts.NedRemotePath.VIDEOSDIR
                + UrlEncoder.URLEncoder( currentElement.getData() );
        if ( NedMidlet.getInstance().getDownloadManager().countActiveDownload() < DownloadManager.MAX_DOWNLOADS ) {
            NedMidlet.getInstance().getDownloadManager().addDownloadToQueue( currentElement.getMediaFile(), path, currentElement.
                    getText(), true, false );
        } else {
            if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.MAX_DOWNLOAD_REACHED_QUESTION ) ) {
                NedMidlet.getInstance().getDownloadManager().addDownloadToQueue( currentElement.getMediaFile(), path, currentElement.
                        getText(), true, true );
            } else {
                NedMidlet.getInstance().getDownloadManager().addDownloadToQueue( currentElement.getMediaFile(), path, currentElement.
                        getText(), true, false );
            }
        }
    }
}