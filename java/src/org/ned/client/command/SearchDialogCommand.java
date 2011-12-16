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
import org.ned.client.view.SearchDialog;

public class SearchDialogCommand extends NedCommand{

    private static SearchDialogCommand exitCommand;

    private SearchDialogCommand() {
        command = new Command(NedResources.MID_SEARCH_COMMAND);
    }

    public static SearchDialogCommand getInstance() {
        if( exitCommand == null) {
           exitCommand = new SearchDialogCommand();
        }
        return exitCommand;
    }

    protected void doAction(Object param) {
        SearchDialog.show(param);
    }
}

