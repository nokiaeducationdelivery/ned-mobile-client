package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.DownloadOptionScreen;


public class DownloadOptionsCommand extends NedCommand {
    private static DownloadOptionsCommand instance;

    private DownloadOptionsCommand() {
        command = new Command( NedResources.DOWNLOAD_OPTIONS );
    }

    public static DownloadOptionsCommand getInstance() {
        if( instance == null ) {
            instance = new DownloadOptionsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new DownloadOptionScreen().show();
    }
}
