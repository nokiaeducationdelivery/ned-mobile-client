package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.SearchDialog;

public class SearchDialogCommand extends NedCommand{

    private static SearchDialogCommand exitCommand;

    private SearchDialogCommand() {
        command = new Command(NedResources.MID_SEARCH_COMMAND);
    }

    public static SearchDialogCommand getInstance() {
        if( exitCommand == null) {
           exitCommand = new SearchDialogCommand();
        }
        return exitCommand;
    }

    protected void doAction(Object param) {
        SearchDialog.show(param);
    }
}

