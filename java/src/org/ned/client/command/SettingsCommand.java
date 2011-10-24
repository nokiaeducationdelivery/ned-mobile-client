package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.SettingsScreen;
//import ned.view.SettingsScreen;


public class SettingsCommand extends NedCommand {

    private static SettingsCommand instance;


    private SettingsCommand() {
        command = new Command(NedResources.MID_OPTIONS_COMMAND);
    }

    public static SettingsCommand getInstance() {
        if( instance == null) {
            instance = new SettingsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new SettingsScreen().show();
    }
}
