package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.SettingsScreen;

public class BackStatisticsOptionsCommand extends BackNedCommand {

    private static BackStatisticsOptionsCommand instance;

    private BackStatisticsOptionsCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackStatisticsOptionsCommand getInstance() {
        if( instance == null) {
            instance = new BackStatisticsOptionsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new SettingsScreen().show();
    }
}
