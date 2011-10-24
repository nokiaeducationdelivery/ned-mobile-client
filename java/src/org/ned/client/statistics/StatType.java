package org.ned.client.statistics;


public class StatType {
    public static final int BROWSE_LIBRARY_OPEN = 0x1;
    public static final int BROWSE_LIBRARY_BACK = 0x2;
    public static final int BROWSE_CATALOG_OPEN = 0x3;
    public static final int BROWSE_CATALOG_BACK = 0x4;
    public static final int BROWSE_CATEGORY_OPEN = 0x5;
    public static final int BROWSE_CATEGORY_BACK = 0x6;
    public static final int BROWSE_MEDIAITEM_BACK = 0x7;

    public static final int DOWNLOAD_ADD = 0x10;
    public static final int DOWNLOAD_REMOVE = 0x20;
    public static final int DOWNLOAD_START = 0x30;
    public static final int DOWNLOAD_END = 0x40;
    public static final int DOWNLOAD_COMPLETED = 0x50;
    public static final int LIBRARY_ADD = 0x60;
    public static final int LIBRARY_REMOVED = 0x70;

    public static final int PLAY_ITEM_START = 0x100;
    public static final int PLAY_ITEM_END = 0x200;
    public static final int LINK_OPEN = 0x300;
    public static final int SHOW_DETAILS = 0x400;
    public static final int DELETE_ITEM = 0x500;
    public static final int SEARCH_ITEM = 0x600;
    public static final int SHOW_LINKS = 0x700;

    public static final int DELETE_HISTORY_ITEM = 0x1000;
    public static final int PLAY_HISTORY_ITEM = 0x2000;

    public static final int USER_LOGGED = 0x10000;
    public static final int USER_DELETE = 0x20000;
    public static final int APP_EXIT = 0x100000;

    public static String type2String( int aType ) {
        switch( aType ) {
            case BROWSE_LIBRARY_OPEN:
                return "BROWSE_LIBRARY_OPEN";
            case BROWSE_LIBRARY_BACK:
                return "BROWSE_LIBRARY_BACK";
            case BROWSE_CATALOG_OPEN:
                return "BROWSE_CATALOG_OPEN";
            case BROWSE_CATALOG_BACK:
                return "BROWSE_CATALOG_BACK";
            case BROWSE_CATEGORY_OPEN:
                return "BROWSE_CATEGORY_OPEN";
            case BROWSE_CATEGORY_BACK:
                return "BROWSE_CATEGORY_BACK";
            case BROWSE_MEDIAITEM_BACK:
                return "BROWSE_MEDIAITEM_BACK";
            case DOWNLOAD_ADD:
                return "DOWNLOAD_ADD";
            case DOWNLOAD_REMOVE:
                return "DOWNLOAD_REMOVE";
            case DOWNLOAD_START:
                return "DOWNLOAD_START";
            case DOWNLOAD_END:
                return "DOWNLOAD_END";
            case DOWNLOAD_COMPLETED:
                return "DOWNLOAD_COMPLETED";
            case LIBRARY_ADD:
                return "LIBRARY_ADD";
            case LIBRARY_REMOVED:
                return "LIBRARY_REMOVED";
            case PLAY_ITEM_START:
                return "PLAY_ITEM_START";
            case PLAY_ITEM_END:
                 return "PLAY_ITEM_END";
            case LINK_OPEN:
               return "LINK_OPEN";
            case SHOW_DETAILS:
                 return "DETAILS_SHOW";
            case SHOW_LINKS:
                return "SHOW_LINKS";
            case DELETE_ITEM:
               return "DELETE_ITEM";
            case SEARCH_ITEM:
                 return "SEARCH_ITEM";
            case DELETE_HISTORY_ITEM:
                 return "DELETE_HISTORY_ITEM";
            case PLAY_HISTORY_ITEM:
                 return "PLAY_HISTORY_ITEM";
            case USER_LOGGED:
                 return "USER_LOGGED";
            case USER_DELETE:
                return "USER_DELETE";
            case APP_EXIT:
                return "APP_EXIT";
            default:
                return "UNKNOWN";
        }
    }
}
