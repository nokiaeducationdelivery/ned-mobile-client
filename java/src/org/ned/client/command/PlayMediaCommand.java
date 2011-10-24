package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.IContent;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;


public class PlayMediaCommand extends NedCommand{
    private static PlayMediaCommand instance;

    private PlayMediaCommand() {
        command = new Command( NedResources.OPEN );
    }

    public static PlayMediaCommand getInstance() {
        if( instance == null ) {
            instance = new PlayMediaCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        if(param != null){
            IContent content = (IContent) param;
            NedMidlet.getInstance().playMedia( content );
        }
    }

    protected void doLog( Object aParam ) {
        if( aParam != null ){
            IContent content = (IContent)aParam;
            StatisticsManager.logEvent( StatType.PLAY_ITEM_START , "Id=" + content.getId()
                                                                 +";Type=" + content.getType()
                                                                 +";Title="+ content.getText() + ";" );
        }
    }
}