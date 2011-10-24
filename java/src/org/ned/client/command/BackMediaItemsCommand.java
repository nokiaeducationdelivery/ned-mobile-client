package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.CategoryScreen;


public class BackMediaItemsCommand extends BackNedCommand {

    private static BackMediaItemsCommand instance;

    private BackMediaItemsCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackMediaItemsCommand getInstance() {
        if( instance == null) {
            instance = new BackMediaItemsCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        NedMidlet.getInstance().getDownloadManager().setMediaListUpdater(null);
        new CategoryScreen( (String)aParam ).show();
    }

    protected void doLog( Object aParam ) {
        StatisticsManager.logEvent( StatType.BROWSE_MEDIAITEM_BACK, "Id=" + (String)aParam );
    }
}