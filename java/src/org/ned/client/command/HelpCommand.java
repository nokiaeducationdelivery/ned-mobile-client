package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.HelpView;


public class HelpCommand extends NedCommand{
    private static HelpCommand instance;

    private HelpCommand() {
        command = new Command( NedResources.HELP );
    }

    public static HelpCommand getInstance() {
        if( instance == null ) {
            instance = new HelpCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        if(param != null){
            try {
                new HelpView(param).show();
            } catch ( Exception ex ) {
                GeneralAlert.show( NedResources.DOC_NOT_SUPPORTED, GeneralAlert.WARNING );
            }
        }
    }

    protected void doLog( Object aParam ) {
        if( aParam != null ){
//            IContent content = (IContent)aParam;
//            StatisticsManager.logEvent( StatType. , "Id=" + content.getId()
//                                                                 +";Type=" + content.getType()
//                                                                 +";Title="+ content.getText() + ";" );
        }
    }
}