package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;


public class ChangePasswordCommand extends NedCommand{
    private static ChangePasswordCommand instance;

    private ChangePasswordCommand() {
        command = new Command( "ChangePassword" );
    }

    public static ChangePasswordCommand getInstance() {
        if( instance == null ) {
            instance = new ChangePasswordCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        NedMidlet.getInstance().getAccountManager().changePassword((String)param);
    }
}
