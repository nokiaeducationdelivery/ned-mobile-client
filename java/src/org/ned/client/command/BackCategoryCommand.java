package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.CatalogScreen;


public class BackCategoryCommand extends BackNedCommand {

    private static BackCategoryCommand instance;

    private BackCategoryCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackCategoryCommand getInstance() {
        if( instance == null) {
            instance = new BackCategoryCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        new CatalogScreen((String)param).show();
    }

    protected void doLog( Object aParam ) {
        StatisticsManager.logEvent( StatType.BROWSE_CATEGORY_BACK, "Id=" + (String)aParam );
    }
}
