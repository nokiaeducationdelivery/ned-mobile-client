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
package org.ned.client.command;

import com.sun.lwuit.Command;
import javax.microedition.midlet.MIDletStateChangeException;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.LanguageInfo;
import org.ned.client.view.GeneralAlert;

public class ConfirmLanguageSelectCommand extends NedCommandAsync {

    private static ConfirmLanguageSelectCommand instance;

    public ConfirmLanguageSelectCommand() {
        command = new Command( NedResources.MID_OK_COMMAND );
    }

    public static ConfirmLanguageSelectCommand getInstance() {
        if ( instance == null ) {
            instance = new ConfirmLanguageSelectCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        if ( (!aParam.equals( NedMidlet.getAccountManager().getLanguage() )) ) {
            NedMidlet.getAccountManager().setLanguage( (LanguageInfo) aParam );
            NedMidlet.getAccountManager().saveSetup();

            if ( GeneralAlert.showQuestion( NedResources.MSG_RESTART_NEEDED ) == GeneralAlert.RESULT_YES ) {
                try {
                    NedMidlet.getInstance().destroyApp( true );
                } catch ( MIDletStateChangeException ex ) {
                    ex.printStackTrace();
                }
            } else {
                GeneralAlert.show( NedResources.MSG_NEW_LANGUAGE, GeneralAlert.INFO );
            }
        }
    }
}
