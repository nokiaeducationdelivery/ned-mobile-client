package org.ned.client.library;

import org.ned.client.Content;


public class NedCatalog {

    public Content contents;
    public String catalogDir;

    public NedCategory currentCategory;

    public NedCatalog( Content con ) {
        contents = con;
    }
}
