package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.view.MainScreen;
import org.ned.client.view.MediaItemsScreen;


public class BackDownloadCommand extends BackNedCommand {

    private static BackDownloadCommand instance;

    private BackDownloadCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackDownloadCommand getInstance() {
        if( instance == null) {
            instance = new BackDownloadCommand();
        }
        return instance;
    }

    protected void doAction(Object previousItem) {
        if(previousItem == null)
        {
        new MainScreen().show();
        } else
        {
            Content currentContent = (Content) previousItem;
            new MediaItemsScreen(currentContent.getParentId()).show();
        }
    }
}
