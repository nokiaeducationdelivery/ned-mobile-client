package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MainScreen;


public class GoToStartCommand extends BackNedCommand {

    private static GoToStartCommand instance;

    private GoToStartCommand() {
        command = new Command(NedResources.GO_TO_START);
    }

    public static GoToStartCommand getInstance() {
        if( instance == null) {
            instance = new GoToStartCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
         new MainScreen().show();
    }

    protected void doLog( Object aParam ) {
        StatisticsManager.logEvent( StatType.BROWSE_CATALOG_BACK, "Id=" + (String)aParam );
    }
}
