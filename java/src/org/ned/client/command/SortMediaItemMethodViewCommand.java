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
import org.ned.client.NedResources;
import org.ned.client.view.SortingMediaItemMethodView;

public class SortMediaItemMethodViewCommand extends NedCommand {

    private static SortMediaItemMethodViewCommand instance;

    private SortMediaItemMethodViewCommand() {
        command = new Command( NedResources.SORTING_OPTION );
    }

    public static SortMediaItemMethodViewCommand getInstance() {
        if ( instance == null ) {
            instance = new SortMediaItemMethodViewCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new SortingMediaItemMethodView().show();
    }
}
