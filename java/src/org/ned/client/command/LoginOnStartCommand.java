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
import org.ned.client.NedConsts.LoginError;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;


public class LoginOnStartCommand extends NedCommand{
    private static LoginOnStartCommand instance;

    private LoginOnStartCommand() {
        command = new Command( NedResources.LOGIN );
    }

    public static LoginOnStartCommand getInstance() {
        if( instance == null ) {
            instance = new LoginOnStartCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Object[] login =  (Object[])param;
        String logAs = (String)login[0];
        String pass = (String)login[1];
        Boolean remember = (Boolean)login[2];

        int result = NedMidlet.getAccountManager().login( logAs, pass );
        if ( result == LoginError.SUCCESS ) {
            NedMidlet.getAccountManager().savePassword(logAs, remember.booleanValue());
            NedMidlet.getInstance().continueApploading();
        }
    }
}
