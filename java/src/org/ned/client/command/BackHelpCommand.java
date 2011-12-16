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
import org.ned.client.view.CatalogScreen;
import org.ned.client.view.CategoryScreen;
import org.ned.client.view.MediaItemsScreen;
import org.ned.client.view.NedFormBase;

public class BackHelpCommand extends BackNedCommand {

    private static BackHelpCommand instance;

    private BackHelpCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackHelpCommand getInstance() {
        if ( instance == null ) {
            instance = new BackHelpCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {

        try {
            if( aParam instanceof Class) {
                Class caller = (Class) aParam;
                ((NedFormBase) caller.newInstance()).show();
            } else {
                Object[] params = (Object[])aParam;
                Class caller = (Class) params[0];
                String id = (String) params[1];
                if (caller.getName().equals(CatalogScreen.class.getName())) {
                    new CatalogScreen(id).show();
                } else if (caller.getName().equals(MediaItemsScreen.class.getName())) {
                    new MediaItemsScreen(id).show();
                } else if (caller.getName().equals(CategoryScreen.class.getName())) {
                    new CategoryScreen(id).show();
                }
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    protected void doLog( Object aParam ) {
//        String id = (String)aParam;
//        StatisticsManager.logEvent( StatType.PLAY_ITEM_END, "Id=" + id +";SUCESS;" );
    }
}