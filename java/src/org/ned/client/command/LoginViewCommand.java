package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.LoginScreen;


public class LoginViewCommand extends NedCommand {
    private static LoginViewCommand instance;

    private LoginViewCommand() {
        command = new Command( NedResources.SWITCH_USER );
    }

    public static LoginViewCommand getInstance() {
        if( instance == null ) {
            instance = new LoginViewCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.QUESTION_LOGOUT_USER ) ) {
            new LoginScreen().show();
        }
    }
}
