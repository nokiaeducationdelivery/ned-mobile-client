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

public class PauseAudioCommand extends NedCommand {

    private static PauseAudioCommand instance;

    private PauseAudioCommand() {
        command = new Command(NedResources.AC_PAUSE);
    }

    public static PauseAudioCommand getInstance() {
        if (instance == null) {
            instance = new PauseAudioCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        if (NedMidlet.getInstance().getAudioPlayer() != null) {
            NedMidlet.getInstance().getAudioPlayer().pause();
        }
    }
}
