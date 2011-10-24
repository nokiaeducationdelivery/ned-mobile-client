package org.ned.client.view;


import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import org.ned.client.NedResources;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.PlayMediaCommand;


public class OldMediaItemContextMenu extends ContextMenu {


    public OldMediaItemContextMenu( List aList ) {
        super( aList, 3 );
    }

    protected void buildOptions(){
        Command[] options = null;
        options = new Command[]{PlayMediaCommand.getInstance().getCommand(),
                                DeleteContentCommand.getInstance().getCommand(),};
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] { NedResources.CANCEL, NedResources.SELECT };
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if ( cmd == PlayMediaCommand.getInstance().getCommand() ) {
            PlayMediaCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mMediaItemList );
        }
    }
}

