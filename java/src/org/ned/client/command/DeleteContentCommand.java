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
import java.util.Vector;
import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.library.NedLibrary;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.transfer.DownloadTask;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.GeneralAlert;

/**
 *
 * @author damian.janicki
 */
public class DeleteContentCommand extends NedCommand {

    private static DeleteContentCommand instance;

    private DeleteContentCommand() {
        command = new Command( NedResources.DELETE );
    }

    public static DeleteContentCommand getInstance() {
        if ( instance == null ) {
            instance = new DeleteContentCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {

        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.QUESTION_REMOVE_CONTENT ) ) {
            if ( param instanceof List ) {
                List uiList = (List) param;

                if ( uiList.getSelectedItem() instanceof LibraryElement ) {
                    LibraryElement content = (LibraryElement) uiList.getSelectedItem();
                    Vector fileList = XmlManager.getContentAllFiles( content.getId() );
                    XmlManager.removeContentChild( content.getId() );
                    int index = uiList.getSelectedIndex();
                    uiList.getModel().removeItem( index );
                    if ( (index == uiList.size()) && index > 0 ) {
                        uiList.setSelectedIndex( --index );
                    }
                    uiList.repaint();
                    removeFiles( fileList );
                    NedLibrary selected = NedMidlet.getSettingsManager().getLibraryManager().findLibrary( content.getParent().getId() );
                    if ( selected != null ) {
                        selected.setCatalogCount();
                    }

                    StatisticsManager.logEvent( StatType.DELETE_ITEM, "Id=" + content.getId() );
                }
            }
        }
    }

    private void removeFiles( Vector fileList ) {//TODO meybe other thread??
        for ( int idx = 0; idx < fileList.size(); idx++ ) {
            String fileName = (String) fileList.elementAt( idx );

            if ( !NedIOUtils.removeFile( fileName ) ) {
                DownloadTask tr = NedMidlet.getInstance().getDownloadManager().getTransfer( fileName );
                if ( tr != null ) {
                    tr.stopDownload();
                    NedMidlet.getInstance().getDownloadManager().removeFromQueue( tr );
                    NedIOUtils.removeFile( fileName + NedLocalConst.TMP );
                }
            }
        }
    }
}
