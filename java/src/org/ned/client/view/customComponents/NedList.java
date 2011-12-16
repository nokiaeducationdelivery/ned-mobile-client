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
package org.ned.client.view.customComponents;

import com.sun.lwuit.List;
import com.sun.lwuit.list.ListModel;
import org.ned.client.view.ContextMenu;


public class NedList extends List  {

    private ContextMenu mContextMenu;

    public NedList( ListModel aModel ) {
        super( aModel );
    }

    public void setContextMenu( ContextMenu aContextMenu ) {
        mContextMenu = aContextMenu;
    }

    protected void longPointerPress(int x, int y) {
        if( mContextMenu != null ) {
            mContextMenu.show( x, y );
        }
    }
}
