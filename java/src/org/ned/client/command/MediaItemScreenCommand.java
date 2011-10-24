package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.view.MediaItemsScreen;


public class MediaItemScreenCommand extends NedCommand{
    private static MediaItemScreenCommand instance;

    private MediaItemScreenCommand() {
        command = new Command( NedResources.OPEN );
    }

    public static MediaItemScreenCommand getInstance() {
        if( instance == null ) {
            instance = new MediaItemScreenCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
       Content contents = (Content)param;
       new MediaItemsScreen(contents.getId()).show();
    }
}
