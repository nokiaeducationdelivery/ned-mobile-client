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
import org.ned.client.Localization;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.Utils;
import org.ned.client.utils.Version;
import org.ned.client.view.GeneralAlert;

/**
 *
 * @author damian.janicki
 */
public class ShowAboutCommand extends NedCommand {

    private static ShowAboutCommand instance;

    private ShowAboutCommand() {
        command = new Command( NedResources.ABOUT );
    }

    public static ShowAboutCommand getInstance() {
        if ( instance == null ) {
            instance = new ShowAboutCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        Object[] params = new Object[3];
        Version version = Utils.versionParser( NedMidlet.getInstance().getVersion() );
        params[0] = String.valueOf( version.Major );
        params[1] = String.valueOf( version.Minor );
        params[2] = String.valueOf( version.Build );

        String message = Localization.getMessage( NedResources.VERSION , params )
                + "\n" + "ned(at)freelists.org" + "\n" + NedResources.NEEDHELP;
        GeneralAlert.show( message, GeneralAlert.INFO );
    }
}
