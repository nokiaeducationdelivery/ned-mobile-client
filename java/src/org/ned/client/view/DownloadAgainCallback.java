/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.view;

import com.sun.lwuit.List;
import javax.microedition.midlet.MIDletStateChangeException;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.AsyncCompletedCallback;
import org.ned.client.utils.LanguageInfo;

public class DownloadAgainCallback implements AsyncCompletedCallback {

    private List mList;

    public DownloadAgainCallback( List aList ) {
        mList = aList;
    }

    public void onSuccess() {
        mList.repaint();
        LanguageInfo lang = (LanguageInfo) mList.getSelectedItem();
        if ( lang != null
             && lang.equals( NedMidlet.getAccountManager().getLanguage() )
             && GeneralAlert.showQuestion( NedResources.MSG_RESTART_NEEDED ) == GeneralAlert.RESULT_YES ) {
            try {
                NedMidlet.getInstance().destroyApp( true );
            } catch ( MIDletStateChangeException ex ) {
                ex.printStackTrace();
            }
        } else {
            GeneralAlert.show( NedResources.DOWNLOAD_SUCCESSFUL, GeneralAlert.INFO );
        }
    }

    public void onFailure( String error ) {
        GeneralAlert.show( error, GeneralAlert.WARNING );
    }
}