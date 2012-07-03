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

/**
 *
 * @author wojciech.luczkow
 */
public class LoginToServerCommand extends NedCommandAsync{
    
    private static LoginToServerCommand instance;

    private LoginToServerCommand() {
        command = new Command( NedResources.LOGIN );
    }

    public static LoginToServerCommand getInstance() {
        if( instance == null ) {
            instance = new LoginToServerCommand();
        }
        return instance;
    } 

    protected void doAction( Object param ) {
        Object[] login =  (Object[])param;
        String logAs = (String)login[0];
        String pass = (String)login[1];

        int result = NedMidlet.getAccountManager().loginToServer( logAs, pass );
        if ( result != LoginError.SUCCESS ) {
            throw new AsyncException(String.valueOf(result));
        }
    }
}
