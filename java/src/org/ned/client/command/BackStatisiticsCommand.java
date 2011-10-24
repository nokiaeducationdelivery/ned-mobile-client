package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.MainScreen;

public class BackStatisiticsCommand extends BackNedCommand {

    private static BackStatisiticsCommand instance;

    public BackStatisiticsCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackStatisiticsCommand getInstance() {
        if( instance == null) {
            instance = new BackStatisiticsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new MainScreen().show();
    }
}
