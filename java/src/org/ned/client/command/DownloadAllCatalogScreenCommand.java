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
import com.sun.lwuit.List;
import com.sun.lwuit.list.ListModel;
import java.util.Enumeration;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.view.GeneralAlert;

public class DownloadAllCatalogScreenCommand extends NedCommand {

    private static DownloadAllCatalogScreenCommand mInstance;

    public DownloadAllCatalogScreenCommand() {
        command = new Command( NedResources.DOWNLOAD_ALL );
    }

    public static DownloadAllCatalogScreenCommand getInstance() {
        if ( mInstance == null ) {
            mInstance = new DownloadAllCatalogScreenCommand();
        }
        return mInstance;
    }

    protected void doAction( Object param ) {
        ListModel categoriesList = ((List) param).getModel();

        int quantity = 0;
        for ( int i = 0; i < categoriesList.getSize(); i++ ) {
            LibraryElement categoryItem = (LibraryElement) categoriesList.
                    getItemAt( i );

            Enumeration catEnum = categoryItem.getChildern().elements();
            while ( catEnum.hasMoreElements() ) {
                LibraryElement catalogItem = (LibraryElement) catEnum.
                        nextElement();

                Enumeration en = catalogItem.getChildern().elements();
                while ( en.hasMoreElements() ) {
                    Content mediaItem = ((LibraryElement) en.nextElement()).
                            getDetails();
                    if ( !mediaItem.isDownloaded() ) {
                        AddToDownloadQueueCommand.getInstance().execute( mediaItem );
                        ++quantity;
                    }
                }
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
