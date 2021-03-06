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
package org.ned.client.view.customComponents;

import org.ned.client.view.renderer.DownloadTaskCellRenderer;
import com.sun.lwuit.List;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;
import org.ned.client.transfer.DownloadManager;
import org.ned.client.transfer.IDownloadListUpdater;
import org.ned.client.transfer.DownloadTask;

public class DownloadList extends List implements IDownloadListUpdater {

    public static DownloadList getDownloadList( DownloadManager downloadManager ) {
        DownloadList list = new DownloadList();
        list.setRenderer( new DownloadTaskCellRenderer() );
        Vector downloadsQueue = downloadManager.getMainDownloadQueue();
        for ( int i = 0; i < downloadsQueue.size(); i++ ) {
            list.addItem( downloadsQueue.elementAt( i ) );
        }

        return list;
    }

    public void dataChanged( DownloadTask aSource ) {
        for ( int i = 0; i < getModel().getSize(); i++ ) {
            if ( aSource == getModel().getItemAt( i ) ) {
                ((DefaultListModel)getModel()).setItem( i, aSource );
                return;
            }
        }
    }

    public void sourceDestroyed( DownloadTask aSource ) {
        for ( int i = 0; i < getModel().getSize(); i++ ) {
            if ( aSource == getModel().getItemAt( i ) ) {
                getModel().removeItem( i );
                return;
            }
        }
    }
}
