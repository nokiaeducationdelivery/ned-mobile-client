package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.FoundItemsScreen;

public class SearchCommand extends NedCommand {

    private static SearchCommand instance;

    public SearchCommand() {
        command = new Command( NedResources.MID_SEARCH_COMMAND );
    }

    public static SearchCommand getInstance() {
        if (instance == null) {
            instance = new SearchCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        String[] params = (String[]) param;
         new FoundItemsScreen(params[0], params[1]).show();
    }

    protected void doLog( Object param ) {
        String[] params = (String[]) param;
        StatisticsManager.logEvent( StatType.SEARCH_ITEM, "SearchFor="+ params[0]
                                                        + ";atId=" + params[1] + ";" );
    }
}
