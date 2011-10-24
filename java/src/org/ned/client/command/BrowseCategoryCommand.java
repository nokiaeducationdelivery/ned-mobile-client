package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MediaItemsScreen;

public class BrowseCategoryCommand extends NedCommand {

    private static BrowseCategoryCommand instance;

    private BrowseCategoryCommand() {
        command = new Command( NedResources.BROWSE );
    }

    public static BrowseCategoryCommand getInstance() {
        if (instance == null) {
            instance = new BrowseCategoryCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        String id = (String)aParam;
        new MediaItemsScreen( id ).show();
    }

    protected void doLog( Object aParam ) {
        String id = (String)aParam;
        StatisticsManager.logEvent( StatType.BROWSE_CATEGORY_OPEN, "Id=" + id );
    }
}
