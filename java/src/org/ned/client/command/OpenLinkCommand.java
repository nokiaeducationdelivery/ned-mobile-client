package org.ned.client.command;

import com.sun.lwuit.Command;
import javax.microedition.io.ConnectionNotFoundException;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.view.GeneralAlert;


public class OpenLinkCommand extends NedCommand{
    private static OpenLinkCommand instance;

    private OpenLinkCommand() {
        command = new Command( NedResources.OPEN_LINK );
    }

    public static OpenLinkCommand getInstance() {
        if( instance == null ) {
            instance = new OpenLinkCommand();
        }
        return instance;
    }

    protected void doAction(Object aParam) {
        String link = (String)aParam;
        if (!link.startsWith( "http://" ) && !link.startsWith( "https://" ) ) {
            link = "http://".concat( link );
        }
        try {
            NedMidlet.getInstance().platformRequest( link );
        } catch ( ConnectionNotFoundException ex ) {
            GeneralAlert.show( (String)aParam + ", " + ex.getMessage(), GeneralAlert.ERROR );
        }
    }

    protected void doLog( Object aParam ) {
        String link = (String)aParam;
        StatisticsManager.logEvent( StatType.LINK_OPEN , link );
    }
}
