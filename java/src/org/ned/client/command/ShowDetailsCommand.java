package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.GeneralAlert;


public class ShowDetailsCommand extends NedCommand {
    private static ShowDetailsCommand instance;

    private ShowDetailsCommand() {
        command = new Command( NedResources.SHOW_DETAILS );
    }

    public static ShowDetailsCommand getInstance() {
        if( instance == null ) {
            instance = new ShowDetailsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Content currentElement = (Content)param;
        String description = currentElement.getDescription();
        if( description == null || description.length() == 0 ) {
            description = NedResources.NO_DETAILS;
        }
        GeneralAlert.show( description, GeneralAlert.TEXT );
    }

    protected void doLog( Object aParam ) {
        Content currentElement = (Content)aParam;
        StatisticsManager.logEvent( StatType.SHOW_DETAILS, "Id=" + currentElement.getId() );
    }
}