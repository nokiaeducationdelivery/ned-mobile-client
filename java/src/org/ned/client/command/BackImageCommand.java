package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MediaItemsScreen;
import org.ned.client.view.HistoryScreen;

public class BackImageCommand extends BackNedCommand {

    private static BackImageCommand instance;

    private BackImageCommand() {
        command = new Command( NedResources.MID_BACK_COMMAND );
    }

    public static BackImageCommand getInstance() {
        if ( instance == null ) {
            instance = new BackImageCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        if ( aParam!= null ) {
            new MediaItemsScreen( (String)aParam ).show();
        }else{
            new HistoryScreen().show();
        }
    }

    protected void doLog( Object aParam ) {
        String id = (String)aParam;
        StatisticsManager.logEvent( StatType.PLAY_ITEM_END, "Id=" + id +";SUCESS;" );
    }
}