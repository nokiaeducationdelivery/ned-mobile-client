package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.MainScreen;


public class BackToMainScreenCommand extends BackNedCommand {

    private static BackToMainScreenCommand instance;

    private BackToMainScreenCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackToMainScreenCommand getInstance() {
        if( instance == null) {
            instance = new BackToMainScreenCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new MainScreen().show();
    }
}