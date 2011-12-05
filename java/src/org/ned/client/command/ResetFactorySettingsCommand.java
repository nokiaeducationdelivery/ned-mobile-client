package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedConsts.LoginError;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.GeneralAlert;

public class ResetFactorySettingsCommand extends NedCommand {

    private static ResetFactorySettingsCommand instance;

    public ResetFactorySettingsCommand() {
        command = new Command( NedResources.FACTORY_SETTINGS );
    }

    public static ResetFactorySettingsCommand getInstance() {
        if (instance == null) {
            instance = new ResetFactorySettingsCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        Object[] login = (Object[]) param;
        String userName = (String) login[0];
        String pass = (String) login[1];
        int retval = NedMidlet.getAccountManager().login(userName, pass);
        if ( retval == LoginError.SUCCESS ) {
            if (GeneralAlert.RESULT_YES == GeneralAlert.showQuestion(NedResources.QUESTION_FACTORY)) {
                if (GeneralAlert.RESULT_YES == GeneralAlert.showQuestion(NedResources.QUESTION_FACTORY2)) {

                    if (NedIOUtils.removeFile(NedIOUtils.getLocalRoot())) {
                        NedMidlet.getInstance().resetApp();
                    }
                }
            }
        }
    }
}
