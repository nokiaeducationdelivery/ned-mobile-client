package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.view.CategoryScreen;


public class CategoryScreenCommand extends NedCommand{
    private static CategoryScreenCommand instance;

    private CategoryScreenCommand() {
        command = new Command( NedResources.OPEN );
    }

    public static CategoryScreenCommand getInstance() {
        if( instance == null ) {
            instance = new CategoryScreenCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
       Content contents = (Content)param;
       new CategoryScreen(contents.getId()).show();
    }
}
