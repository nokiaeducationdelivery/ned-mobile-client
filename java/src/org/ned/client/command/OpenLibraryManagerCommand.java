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
import org.ned.client.view.LibraryManagerScreen;


public class OpenLibraryManagerCommand extends NedCommand {

    private static OpenLibraryManagerCommand instance;


    private OpenLibraryManagerCommand() {
        command = new Command( NedResources.LIBRARYMANAGER );
    }

    public static OpenLibraryManagerCommand getInstance() {
        if( instance == null) {
            instance = new OpenLibraryManagerCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new LibraryManagerScreen().show();
    }
}
