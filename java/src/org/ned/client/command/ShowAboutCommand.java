package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;

/**
 *
 * @author damian.janicki
 */
public class ShowAboutCommand extends NedCommand{
    private static ShowAboutCommand instance;

    private ShowAboutCommand() {
        command = new Command( NedResources.ABOUT );
    }

    public static ShowAboutCommand getInstance() {
        if( instance == null ) {
            instance = new ShowAboutCommand();
        }
        return instance;
    }

    protected void doAction(Object aParam) {
        GeneralAlert.show( NedResources.VERSION + ' ' + NedMidlet.getInstance().getVersion(), GeneralAlert.INFO );
    }
}
