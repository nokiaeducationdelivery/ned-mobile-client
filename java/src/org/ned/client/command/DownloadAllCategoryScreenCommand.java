package org.ned.client.command;

import com.sun.lwuit.Command;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.contentdata.NedListModel;
import org.ned.client.view.GeneralAlert;


public class DownloadAllCategoryScreenCommand extends NedCommand {

    private static DownloadAllCategoryScreenCommand mInstance;

    public DownloadAllCategoryScreenCommand() {
        command = new Command( NedResources.DOWNLOAD_ALL );
    }

    public static DownloadAllCategoryScreenCommand getInstance() {
        if ( mInstance == null ) {
            mInstance = new DownloadAllCategoryScreenCommand();
        }
        return mInstance;
    }

    protected void doAction(Object param) {
        NedListModel catalogList = (NedListModel)param;

        int quantity = 0;
        for( int i = 0; i < catalogList.getSize(); i++) {
            Content catalogItem = (Content)catalogList.getItemAt( i );
            Vector mediaItems = XmlManager.getContentChilds( catalogItem.getId() , null );
            for( int j = 0; j< mediaItems.size(); j++ ) {
                Content mediaItem = (Content)mediaItems.elementAt( j );
                if( !mediaItem.isDownloaded() ) {
                    AddToDownloadQueueCommand.getInstance().execute( mediaItem );
                    ++quantity;
                }
            }
        }
        if( quantity > 0 ) {
            Object[] params = { String.valueOf( quantity ) };
            String msg = Localization.getMessage( NedResources.ITEM_ADDED_TO_QUEUE, params);
            GeneralAlert.show( msg, GeneralAlert.INFO );
        } else {
            GeneralAlert.show( NedResources.ALL_FILES_DOWNLOADED, GeneralAlert.INFO );
        }
    }
}
