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
import com.sun.lwuit.Display;
import org.ned.client.MotdManager;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.library.NedLibrary;
import org.ned.client.library.advanced.LibraryChangesReport;
import org.ned.client.library.advanced.LibraryGeneralModel;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.ContentNotExistException;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedXmlUtils;
import org.ned.client.utils.UnauthorizedLibraryUsageException;
import org.ned.client.view.*;

/**
 *
 * @author damian.janicki
 */
public class UpdateLibraryCommand extends NedCommand {

    private static UpdateLibraryCommand instance;

    public static UpdateLibraryCommand getInstance() {
        if ( instance == null ) {
            instance = new UpdateLibraryCommand();
        }
        return instance;
    }

    public UpdateLibraryCommand() {
        command = new Command( NedResources.CHECK_FOR_UPDATE );
    }

    protected void doAction( Object param ) {
        NedLibrary lib = (NedLibrary) param;
        UpdateLibraryRunnable ulr = new UpdateLibraryRunnable( lib );

        Thread t = new Thread( ulr );
        t.start();
    }

    public class UpdateLibraryRunnable implements Runnable {

        private NedLibrary library;

        private UpdateLibraryRunnable( NedLibrary selected ) {
            this.library = selected;
        }

        public void run() {

            try {
                WaitingScreen.show( NedResources.CONNECTING );
                NedLibrary newLibraryInfo = NedConnectionUtils.getLibraryInfo( library.getId() );
                WaitingScreen.dispose();

                if ( newLibraryInfo != null ) {
                    if ( NedMidlet.getSettingsManager().getAutoStatSend() ) {
                        StatisticsManager.uploadStats( true );
                    }
                    MotdManager.getInstance().updateMotd();
                    if ( library.getVersionInt() < newLibraryInfo.getVersionInt() ) {
                        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.DOWNLOAD_NEW_LIBRARY ) ) {
                            downloadLibrary();
                        }

                    } else {
                        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.LIBRARY_UPTODATE ) ) {//TODO change text
                            downloadLibrary();
                        }
                    }
                }
            } catch ( UnauthorizedLibraryUsageException ex ) {
                WaitingScreen.dispose();//to get main view not a "Connecting..." dialog by Diaplay.getCurrent
                if ( GeneralAlert.showQuestion( NedResources.LOGIN_AGAIN ) == GeneralAlert.RESULT_YES ) {
                    new LoginOnLineScreen( LibraryManagerScreen.class ).show();
                }
            } catch ( ContentNotExistException ex ) {
                WaitingScreen.dispose();
                GeneralAlert.show( NedResources.LIB_NOT_EXIST_ANY_MORE, GeneralAlert.WARNING, true );
            }
        }

        private void downloadLibrary() throws SecurityException, UnauthorizedLibraryUsageException, ContentNotExistException {
            WaitingScreen.show( NedResources.GLOBAL_CONNECTING );
            //basic informatin about changes
            LibraryGeneralModel libModel = LibraryGeneralModel.getInfo(
                    NedXmlUtils.getDocFile(
                    NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getFileUri() ) );


            if ( !NedMidlet.getInstance().getDownloadManager().getViaServlet(
                    NedMidlet.getAccountManager().getContentServletUri(), library ) ) {
                GeneralAlert.show( NedResources.DLM_CONNECTION_FAILED, GeneralAlert.WARNING, true );
            } else {
                NedXmlUtils.cleanDocCache();
                LibraryGeneralModel newLibModel = LibraryGeneralModel.getInfo(
                        NedXmlUtils.getDocFile(
                        NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getFileUri() ) );

                LibraryChangesReport changes = null;
                if ( newLibModel != null && libModel != null ) {
                    changes = LibraryChangesReport.generateReport( newLibModel, libModel );
                }
                library.setCatalogCount();
                NedMidlet.getSettingsManager().resetServer();
                WaitingScreen.dispose();
                try {
                    Thread.sleep( 250 );
                } catch ( InterruptedException ex ) {
                }
                String report = changes.getFullReport();
                GeneralAlert.show( report.length() == 0 ? NedResources.NO_CHANGES : report.trim(), GeneralAlert.INFO, true );
                changes.persistChangesInfo();

                if ( Display.getInstance().getCurrent() instanceof CatalogScreen ) {//this is workaround for UpdateLibrary form CatalogScreen
                    new CatalogScreen( library.getId() ).show();
                }
            }
        }
    }
}
