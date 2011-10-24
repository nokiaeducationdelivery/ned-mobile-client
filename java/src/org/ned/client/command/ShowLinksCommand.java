package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.LinksScreen;


public class ShowLinksCommand extends NedCommand {
    private static ShowLinksCommand instance;

    private ShowLinksCommand() {
        command = new Command( NedResources.SHOW_LINKS );
    }

    public static ShowLinksCommand getInstance() {
        if( instance == null ) {
            instance = new ShowLinksCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        Content currentElement = (Content)aParam;
        new LinksScreen( currentElement.getId() ).show();
    }

    protected void doLog( Object aParam ) {
        Content currentElement = (Content)aParam;
        StatisticsManager.logEvent( StatType.SHOW_LINKS, "Id=" + currentElement.getId() );
    }
}