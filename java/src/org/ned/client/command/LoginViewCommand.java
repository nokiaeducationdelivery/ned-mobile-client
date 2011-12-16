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
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.LoginScreen;


public class LoginViewCommand extends NedCommand {
    private static LoginViewCommand instance;

    private LoginViewCommand() {
        command = new Command( NedResources.SWITCH_USER );
    }

    public static LoginViewCommand getInstance() {
        if( instance == null ) {
            instance = new LoginViewCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.QUESTION_LOGOUT_USER ) ) {
            new LoginScreen().show();
        }
    }
}
