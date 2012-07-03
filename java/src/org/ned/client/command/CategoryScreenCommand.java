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
import org.ned.client.NedResources;
import org.ned.client.library.advanced.LibraryElement;
import org.ned.client.view.CategoryScreen;

public class CategoryScreenCommand extends NedCommand {

    private static CategoryScreenCommand instance;

    private CategoryScreenCommand() {
        command = new Command( NedResources.OPEN );
    }

    public static CategoryScreenCommand getInstance() {
        if ( instance == null ) {
            instance = new CategoryScreenCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        LibraryElement contents = (LibraryElement)param;
        new CategoryScreen( contents.getId() ).show();
    }
}
