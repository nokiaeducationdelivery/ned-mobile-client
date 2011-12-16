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
import javax.microedition.midlet.MIDletStateChangeException;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;

public class ExitCommand extends NedCommand {

    private static ExitCommand exitCommand;

    private ExitCommand() {
        command = new Command(NedResources.MID_EXIT_COMMAND);
    }

    public static ExitCommand getInstance() {
        if( exitCommand == null) {
           exitCommand = new ExitCommand();
        }
        return exitCommand;
    }

    protected void doAction( Object param ) {
        if ( NedMidlet.getInstance().getDownloadManager() != null
          && NedMidlet.getInstance().getDownloadManager().countActiveDownload() > 0 ) {
            //message to cancel downloads first
            GeneralAlert.show( NedResources.MID_EXIT_QUEUE_MESSAGE, GeneralAlert.WARNING );
        } else {
            if( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.EXIT_CONFIRM_MESSAGE  ) ) {
                try {
                    NedMidlet.getInstance().destroyApp(false);
                } catch ( MIDletStateChangeException e ) {
                }
            }
        }
    }
}
