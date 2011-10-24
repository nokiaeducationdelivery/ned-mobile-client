package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.MainScreen;

public class BackSettingsCommand extends BackNedCommand {

    private static BackSettingsCommand instance;

    private BackSettingsCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackSettingsCommand getInstance() {
        if( instance == null) {
            instance = new BackSettingsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new MainScreen().show();
    }
}
