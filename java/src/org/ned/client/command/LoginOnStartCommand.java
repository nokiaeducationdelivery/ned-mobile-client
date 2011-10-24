package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;



public class LoginOnStartCommand extends NedCommand{
    private static LoginOnStartCommand instance;

    private LoginOnStartCommand() {
        command = new Command( NedResources.LOGIN );
    }

    public static LoginOnStartCommand getInstance() {
        if( instance == null ) {
            instance = new LoginOnStartCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Object[] login =  (Object[])param;
        String logAs = (String)login[0];
        String pass = (String)login[1];
        Boolean remember = (Boolean)login[2];

        boolean result = NedMidlet.getAccountManager().login( logAs, pass );
        if ( result ) {
            NedMidlet.getAccountManager().savePassword(logAs, remember.booleanValue());
            NedMidlet.getInstance().continueApploading();
        }
    }
}
