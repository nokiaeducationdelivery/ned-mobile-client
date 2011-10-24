package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;


public class LoginCommand extends NedCommand{
    private static LoginCommand instance;

    private LoginCommand() {
        command = new Command( NedResources.LOGIN );
    }

    public static LoginCommand getInstance() {
        if( instance == null ) {
            instance = new LoginCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Object[] login =  (Object[])param;
        String logAs = (String)login[0];
        String pass = (String)login[1];
        NedMidlet.getAccountManager().login( logAs, pass );
    }
}
