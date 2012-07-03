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
import org.ned.client.NedConsts.LoginError;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.GeneralAlert;

public class ResetFactorySettingsCommand extends NedCommand {

    private static ResetFactorySettingsCommand instance;

    public ResetFactorySettingsCommand() {
        command = new Command( NedResources.FACTORY_SETTINGS );
    }

    public static ResetFactorySettingsCommand getInstance() {
        if ( instance == null ) {
            instance = new ResetFactorySettingsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Object[] login = (Object[])param;
        String userName = (String)login[0];
        String pass = (String)login[1];
        if ( NedMidlet.getAccountManager().getUsersCount() == 0 ) {
            reset();
        } else if ( userName != null
                && !userName.equals( "" ) ) {
            int retval = NedMidlet.getAccountManager().login( userName, pass );
            if ( retval == LoginError.SUCCESS ) {
                if ( GeneralAlert.RESULT_YES
                        == GeneralAlert.showQuestion( NedResources.QUESTION_FACTORY ) ) {
                    if ( GeneralAlert.RESULT_YES
                            == GeneralAlert.showQuestion( NedResources.QUESTION_FACTORY2 ) ) {
                        reset();
                    }
                }
            }
        }
    }

    private void reset() {
        if ( NedIOUtils.removeFile( NedIOUtils.getLocalRoot() ) ) {
            NedMidlet.getInstance().resetApp();
        }
    }
}
