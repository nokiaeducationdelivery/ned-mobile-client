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
package org.ned.client;

import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedIOUtils;


public class MotdManager {

    private static MotdManager mInstance;

    private String mMOTD;

    private MotdManager() {
        init();
    }

    private void init() {
        String tmp = NedIOUtils.loadFile( NedIOUtils.getUserRootDirectory() + NedConsts.NedLocalConst.MOTDFILE );
        if ( tmp == null
          || tmp.trim().length() == 0 ) {
            mMOTD = NedResources.DEFAULT_MOTD;
        } else {
            mMOTD = tmp;
        }
    }

    public static  MotdManager getInstance() {
        if ( mInstance == null ) {
            mInstance = new MotdManager();
        }
        return mInstance;
    }

    public static String getMessage() {
        if ( mInstance == null ) {
            mInstance = new MotdManager();
        }
        return mInstance.mMOTD;
    }

    public void updateMotd() {
        try {
            String message = NedConnectionUtils.httpGet( NedMidlet.getAccountManager().getMotdServletUri() );
            NedIOUtils.saveFile( NedIOUtils.getUserRootDirectory() + NedConsts.NedLocalConst.MOTDFILE , message );
            init();
        } catch ( SecurityException ex ) {
        }
    }
}
