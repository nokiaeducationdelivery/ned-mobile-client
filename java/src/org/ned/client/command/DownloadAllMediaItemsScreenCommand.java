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
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListModel;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.view.GeneralAlert;

public class DownloadAllMediaItemsScreenCommand extends NedCommand {

    private static DownloadAllMediaItemsScreenCommand mInstance;

    public DownloadAllMediaItemsScreenCommand() {
        command = new Command( NedResources.DOWNLOAD_ALL );
    }

    public static DownloadAllMediaItemsScreenCommand getInstance() {
        if ( mInstance == null ) {
            mInstance = new DownloadAllMediaItemsScreenCommand();
        }
        return mInstance;
    }

    protected void doAction( Object param ) {
        ListModel itemList = ((List) param).getModel();

        int quantity = 0;
        for ( int i = 0; i < itemList.getSize(); i++ ) {
            Content mediaItem = ((LibraryElement) itemList.getItemAt( i )).getDetails();
            if ( !mediaItem.isDownloaded() ) {
                AddToDownloadQueueCommand.getInstance().execute( mediaItem );
                ++quantity;
            }
        }
        if ( quantity > 0 ) {
            Object[] params = { String.valueOf( quantity ) };
            String msg = Localization.getMessage( NedResources.ITEM_ADDED_TO_QUEUE, params );
            GeneralAlert.show( msg, GeneralAlert.INFO );
        } else {
            GeneralAlert.show( NedResources.ALL_FILES_DOWNLOADED, GeneralAlert.INFO );
        }
    }
}
