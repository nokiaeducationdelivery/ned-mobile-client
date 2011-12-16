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
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.GeneralAlert;

public class RemoveUserCommand extends NedCommand {

    private static RemoveUserCommand instance;

    public RemoveUserCommand() {
        command = new Command( NedResources.REMOVE_USER );
    }

    public static RemoveUserCommand getInstance() {
        if (instance == null) {
            instance = new RemoveUserCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        Object[] login = (Object[]) param;
        String userName = (String) login[0];
        String pass = (String) login[1];
        int result = NedMidlet.getAccountManager().login(userName, pass);
        if ( result == LoginError.SUCCESS ) {
            if (GeneralAlert.RESULT_YES == GeneralAlert.showQuestion(NedResources.QUESTION_REMOVE_USER
                    + " " + userName + "?")) {
                NedMidlet.getAccountManager().removeUser(userName);
                if (NedIOUtils.removeFile(NedIOUtils.getLocalRoot() + userName + "/")) {
                    GeneralAlert.show(NedResources.USER_DELETED, GeneralAlert.INFO);
                }
                StatisticsManager.logEvent( StatType.USER_DELETE, userName );
            }
        }
    }
}
