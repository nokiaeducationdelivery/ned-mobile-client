package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.MediaItemsScreen;
import org.ned.client.view.HistoryScreen;


public class BackVideoCommand extends BackNedCommand {

    private static BackVideoCommand instance;

    private BackVideoCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackVideoCommand getInstance() {
        if( instance == null) {
            instance = new BackVideoCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
       NedMidlet.getInstance().getVideoPlayer().stopPlayer();
       if(param == null){
           new HistoryScreen().show();
       }else{
           new MediaItemsScreen((String) param).show();
       }
    }

    protected void doLog( Object aParam ) {
        String id = (String)aParam;
        StatisticsManager.logEvent( StatType.PLAY_ITEM_END, "Id=" + id +";SUCESS;" );
    }
}