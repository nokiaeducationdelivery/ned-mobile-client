package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.DownloadQueueScreen;


public class DownloadsQueueViewCommand extends NedCommand{
    private static DownloadsQueueViewCommand instance;

    private DownloadsQueueViewCommand() {
        command = new Command( NedResources.DOWNLOADS );
    }

    public static DownloadsQueueViewCommand getInstance() {
        if( instance == null ) {
            instance = new DownloadsQueueViewCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new DownloadQueueScreen().show();
    }
}
