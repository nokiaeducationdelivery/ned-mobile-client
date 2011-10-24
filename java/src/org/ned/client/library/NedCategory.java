package org.ned.client.library;

import org.ned.client.Content;


public class NedCategory {
    public Content content;
    public String categoryDir;

    public NedMediaItem currentMediaItem;

    public NedCategory( Content newContent ) {
        content = newContent;
    }
}
