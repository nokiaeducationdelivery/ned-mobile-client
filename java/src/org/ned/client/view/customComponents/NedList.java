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
