package org.ned.client.view;

import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import org.ned.client.NedResources;
import org.ned.client.command.CategoryScreenCommand;
import org.ned.client.command.DeleteContentCommand;


public class CatalogContextMenu extends ContextMenu {

    public CatalogContextMenu( List aList, int aMode ) {
        super( aList, aMode );
    }

    protected void buildOptions(){
        Command[] options  = new Command[] { CategoryScreenCommand.getInstance().getCommand(),
                                             DeleteContentCommand.getInstance().getCommand(),
                                           };

        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] { NedResources.CANCEL, NedResources.SELECT };
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if( cmd == CategoryScreenCommand.getInstance().getCommand() ) {
            CategoryScreenCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mMediaItemList );
        }
    }
}

