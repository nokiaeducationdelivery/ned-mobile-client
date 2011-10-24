package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.Content;
import org.ned.client.NedConsts.NedContentType;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;


public class BackGenericCommand extends BackNedCommand {

    private static BackGenericCommand instance;

    private BackGenericCommand() {
        command = new Command(NedResources.MID_BACK_COMMAND);
    }

    public static BackGenericCommand getInstance() {
        if( instance == null) {
            instance = new BackGenericCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        String contentId = (String) param;
        if (contentId == null) {
            BackCatalogCommand.getInstance().execute(null);
        } else {
            Content content = XmlManager.getContentData(contentId);
            if (content.getType().equals(NedContentType.LIBRARY)) {
                BackCategoryCommand.getInstance().execute(contentId);
            } else if (content.getType().equals(NedContentType.CATALOG)) {
                BackMediaItemsCommand.getInstance().execute(contentId);
            } else if (content.getType().equals(NedContentType.CATEGORY)) {
                BackMediaItemsCommand.getInstance().execute(contentId);
            }
        }
    }
}