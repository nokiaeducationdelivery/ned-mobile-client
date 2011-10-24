package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;


public class ChangeUserCommand extends NedCommand{
    private static ChangeUserCommand instance;

    private ChangeUserCommand() {
        command = new Command( "ChangeUser" );
    }

    public static ChangeUserCommand getInstance() {
        if( instance == null ) {
            instance = new ChangeUserCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        NedMidlet.getInstance().getSettingsManager().setUser((String)param);
        NedMidlet.getInstance().getSettingsManager().saveSettings();
    }
}