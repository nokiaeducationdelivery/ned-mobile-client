package org.ned.client.view;


import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import org.ned.client.NedResources;
import org.ned.client.command.AddToDownloadQueueCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.InstantDownloadCommand;
import org.ned.client.command.PlayMediaCommand;
import org.ned.client.command.ShowDetailsCommand;
import org.ned.client.command.ShowLinksCommand;


public class MediaItemContextMenu extends ContextMenu {

    public static final int DOWNLOAD = 5;//number of items on the list
    public static final int PLAY = 4;

    public MediaItemContextMenu( List aList, int aMode ) {
        super( aList, aMode );
    }

    protected void buildOptions(){
        Command[] options = null;
        if( sizeList == PLAY ) {
            options  = new Command[] { PlayMediaCommand.getInstance().getCommand(),
                                       ShowDetailsCommand.getInstance().getCommand(),
                                       ShowLinksCommand.getInstance().getCommand(),
                                       DeleteContentCommand.getInstance().getCommand(),};
        } else {
            options  = new Command[] { ShowDetailsCommand.getInstance().getCommand(),
                                       ShowLinksCommand.getInstance().getCommand(),
                                       InstantDownloadCommand.getInstance().getCommand(),
                                       AddToDownloadQueueCommand.getInstance().getCommand(),
                                       DeleteContentCommand.getInstance().getCommand(),};
        }
        buildOptions(options);
    }

    protected void buildCommands(){
        String[] commands = new String[] { NedResources.CANCEL, NedResources.SELECT };
        buildCommands(commands);
    }

    protected void action(Command cmd){
        if ( cmd == PlayMediaCommand.getInstance().getCommand() ) {
            PlayMediaCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == ShowDetailsCommand.getInstance().getCommand() ) {
            ShowDetailsCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute( mMediaItemList );
        } else if ( cmd == ShowLinksCommand.getInstance().getCommand() ) {
            ShowLinksCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
        } else if ( cmd == InstantDownloadCommand.getInstance().getCommand() ) {
            InstantDownloadCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
            new DownloadQueueScreen(mMediaItemList.getSelectedItem()).show();
        } else if ( cmd == AddToDownloadQueueCommand.getInstance().getCommand() ) {
            AddToDownloadQueueCommand.getInstance().execute( mMediaItemList.getSelectedItem() );
            new DownloadQueueScreen(mMediaItemList.getSelectedItem()).show();
        }
    }
}

