package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedResources;
import org.ned.client.contentdata.NedListModel;
import org.ned.client.view.GeneralAlert;


public class DownloadAllMediaItemsScreenCommand extends NedCommand  {

    private static DownloadAllMediaItemsScreenCommand mInstance;

    public DownloadAllMediaItemsScreenCommand() {
        command = new Command( NedResources.DOWNLOAD_ALL );
    }

    public static DownloadAllMediaItemsScreenCommand getInstance() {
        if ( mInstance == null ) {
            mInstance = new DownloadAllMediaItemsScreenCommand();
        }
        return mInstance;
    }

    protected void doAction(Object param) {
        NedListModel itemList = (NedListModel)param;
        int quantity = 0;
        for( int i = 0; i< itemList.getSize(); i++ ) {
            Content mediaItem = ((Content)itemList.getItemAt(i));
            if( !mediaItem.isDownloaded() ) {
                AddToDownloadQueueCommand.getInstance().execute(mediaItem);
                ++quantity;
            }
        }
        if( quantity > 0 ) {
            Object[] params = { String.valueOf(quantity) };
            String msg = Localization.getMessage(NedResources.ITEM_ADDED_TO_QUEUE, params);
            GeneralAlert.show( msg, GeneralAlert.INFO );
        } else {
            GeneralAlert.show( NedResources.ALL_FILES_DOWNLOADED, GeneralAlert.INFO );
        }
    }
}
