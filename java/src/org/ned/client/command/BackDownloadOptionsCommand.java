package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.SettingsScreen;

public class BackDownloadOptionsCommand extends BackNedCommand {

    private static BackDownloadOptionsCommand instance;

    private BackDownloadOptionsCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackDownloadOptionsCommand getInstance() {
        if( instance == null) {
            instance = new BackDownloadOptionsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new SettingsScreen().show();
    }
}
