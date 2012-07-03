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
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
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
        String message = NedResources.VERSION + ' ' + NedMidlet.getInstance().
                getVersion() + "\n" + NedResources.NEEDHELP + "\n"
                         + "ned(at)freelists.org";
        GeneralAlert.show( message, GeneralAlert.INFO );
    }
}
