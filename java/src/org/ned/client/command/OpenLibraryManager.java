package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.LibraryManagerScreen;


public class OpenLibraryManager extends NedCommand {

    private static OpenLibraryManager instance;


    private OpenLibraryManager() {
        command = new Command( NedResources.LIBRARYMANAGER );
    }

    public static OpenLibraryManager getInstance() {
        if( instance == null) {
            instance = new OpenLibraryManager();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new LibraryManagerScreen().show();
    }
}
