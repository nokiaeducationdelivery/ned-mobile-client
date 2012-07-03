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
import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.library.NedLibrary;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.ContentNotExistException;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.UnauthorizedLibraryUsageException;
import org.ned.client.view.*;

public class BrowseLibraryCommand extends NedCommand {

    private static BrowseLibraryCommand instance;

    private BrowseLibraryCommand() {
        command = new Command( NedResources.BROWSE );
    }

    public static BrowseLibraryCommand getInstance() {
        if ( instance == null ) {
            instance = new BrowseLibraryCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        String id = (String) param;

        NedLibrary selected = NedMidlet.getSettingsManager().getLibraryManager().
                findLibrary( id );
        if ( NedIOUtils.fileExists( selected.getFileUri() ) ) {
            NedIOUtils.createDirectory( selected.getDirUri() + "/"
                                        + NedLocalConst.VIDEOSDIR );
            new CatalogScreen( id ).show();
        } else {
            WaitingScreen.show( NedResources.GLOBAL_CONNECTING );
            LoadLibraryRunnable vrr = new LoadLibraryRunnable( selected );
            Thread t = new Thread( vrr );  //create new thread to compensate for waitingform
            t.setPriority( Thread.MIN_PRIORITY );
            t.start();
        }
    }

    protected void doLog( Object aParam ) {
        String id = (String) aParam;
        StatisticsManager.logEvent( StatType.BROWSE_LIBRARY_OPEN, "Id=" + id );
    }

    public class LoadLibraryRunnable implements Runnable {

        private NedLibrary library;

        private LoadLibraryRunnable( NedLibrary selected ) {
            this.library = selected;
        }

        public void run() {
            try {
                Thread.sleep( 200 );
            } catch ( Exception e ) {
            }
            boolean success = false;
            try {
                success = NedMidlet.getInstance().getDownloadManager().
                        getViaServlet(
                        NedMidlet.getAccountManager().getContentServletUri(), library );
            } catch ( SecurityException ex ) {
            } catch ( UnauthorizedLibraryUsageException ex ) {
                WaitingScreen.dispose();//to get main view not a "Connecting..." dialog by Diaplay.getCurrent
                if ( GeneralAlert.showQuestion( NedResources.LOGIN_AGAIN )
                     == GeneralAlert.RESULT_YES ) {
                    new LoginOnLineScreen( MainScreen.class ).show();
                }
                return;
            } catch ( ContentNotExistException ex ) {
                WaitingScreen.dispose();
                GeneralAlert.show( NedResources.LIB_NOT_EXIST_ANY_MORE, GeneralAlert.WARNING );
                return;
            }
            if ( success ) {
                library.setCatalogCount();
                new CatalogScreen( library.getId() ).show();
            } else {
                Display.getInstance().callSerially( new Runnable() {

                    public void run() {
                        GeneralAlert.show( NedResources.DLM_CONNECTION_FAILED, GeneralAlert.WARNING );
                    }
                });
            }
            if ( NedMidlet.getSettingsManager().getAutoStatSend() ) {
                StatisticsManager.uploadStats( true );
            }
            MotdManager.getInstance().updateMotd();
        }
    }
}
