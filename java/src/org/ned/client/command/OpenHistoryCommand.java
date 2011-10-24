package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.HistoryScreen;

/**
 *
 * @author damian.janicki
 */
public class OpenHistoryCommand extends NedCommand{

    private static OpenHistoryCommand instance;

    private OpenHistoryCommand() {
        command = new Command( NedResources.HISTORY );
    }

    public static OpenHistoryCommand getInstance() {
        if( instance == null) {
            instance = new OpenHistoryCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new HistoryScreen().show();
    }
}
