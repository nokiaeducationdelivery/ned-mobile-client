package org.ned.client.library;

import org.ned.client.Content;
import org.ned.client.MediaItem;


public class NedMediaItem {
    public Content content;

    public MediaItem item;
    
    public NedMediaItem( Content newContent ) {
        content = newContent;
    }
}
