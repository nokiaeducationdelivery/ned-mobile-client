/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client;

public class NedConsts {

    public class HttpHeader {

        public static final String CACHECONTROL = "Cache-Control";
    }

    public class HttpHeaderValue {

        public static final String NOCACHE = "no-cache";
    }

    public class NedUpdateAddress {

        public static final String CHECKFORUPDATEURL = "http://nokiacsr.com:8080/ned.ota/version.txt";
        public static final String INSTALATIONFILE = "http://nokiacsr.com:8080/ned.ota/NED.jad";//case sensitive URL
    }

    public class NedDemo {

        public static final String DEMOURL = "http://217.74.73.16:8083";
        public static final String DEMOUSERNAME = "guest";
        public static final String DEMOPASSWORD = "guest";
        public static final String DEMOLIBID = "khan";
    }

    public class NedUpdateInfo {

        public static final String VERSION = "Version";
        public static final String OVISTORE = "OVIStore";
    }

    public class NedLocalConst {

        public static final String SETTINGSFILE = "settings.xml";
        public static final String STATS_FILE = "statistics.xml";
        public static final String LIBRARYFILE = "library.xml";
        public static final String ACCOUNTSFILE = "accounts.xml";
        public static final String LIBRARYDIR = "library";
        public static final String FILESYSTEMFILE = "filesystem.xml";
        public static final String FILESYSTEMDIR = "filesystem/";
        public static final String VIDEOSDIR = "videos/";
        public static final String MOTDFILE = "motd.txt";
        public static final String DOWNLOADSFILE = "downloads.xml";
        public static final String TMP = "tmp";
        public static final String HISTORYDIR = "history";
        public static final String LANGUAGESFILE = "languages.xml";
    }

    public class NedXmlTag {

        public static final String VIDEOS = "videos";
        public static final String ENTRY = "entry";
        public static final String VIEWS = "views";
        public static final String NAME = "name";
        public static final String MODIFIED = "modified";
        public static final String USER = "user";
        public static final String LAST_USER = "lastuser";
        public static final String SERVER = "server";
        public static final String LANGUAGE = "language";
        public static final String LANGUAGES = "languages";
        public static final String ID = "id";
        public static final String FILE = "file";
        public static final String LOCALE = "locale";
    }

    public class NedContentType {

        public static final String VIDEO = "Video";
        public static final String AUDIO = "Audio";
        public static final String IMAGE = "Picture";
        public static final String TEXT = "Text";
        public static final String LIBRARY = "Library";
        public static final String CATEGORY = "Category";
        public static final String CATALOG = "Catalog";
        public static final String UNDEFINED = "Undefined";
    }

    public class NedXmlAttribute {

        public static final String MEDIA_ID = "mediaid";
        public static final String STAT_TYPE = "stattype";
        public static final String UPLOADED = "uploaded";
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
        public static final String SAVE_PASS = "save";
        public static final String NAME = "name";
        public static final String LOCALE = "locale";
        public static final String REMOTENAME = "remote";
    }

    //variables and values must be in-sync with server values in org.nedcatalogtool.server.UploadStatServlet class
    public class NedStatUploadConsts {

        public static final int STATSUPDATED = 0;
        public static final int NEWSTATS = 1;
        public static final int MISSINGIMEI = -1;
        public static final int SERVERINTERNALERROR = -2;
        public static final int UNKNOWN = -9999;
    }

    public class NedRemotePath {

        public static final String BASEPATH = "/NEDCatalogTool2";
        public static final String STAT_UPLOAD = "/UploadStatServlet";
        public static final String LOGIN = "/LoginServlet";
        public static final String ROOTDIR = "nokiaecd/";
        public static final String LIBRARYDIR = "library/";
        public static final String VIDEOSDIR = "videos/";
        public static final String GETXMLSERVLET = "/XmlContentServlet";
        public static final String MOTDSERVLET = "/MotdUpdateServlet";
        public static final String LOCALIZATION = "/Localizations";
        public static final String LOCALESURL = "/locales";
    }

    public class NedTransitions {

        public static final int TRANSITION_TIME = 500;
    }

    public class LoginError {

        public static final int SUCCESS = 0;
        public static final int UNAUTHORIZED = -1;
        public static final int LOCALSECURITY = -2;
        public static final int CONNECTIONERROR = -3;
        public static final int ABORTED = -4;
        public static final int OTHERCONNECTIONPROBLEM = -5;
        public static final int UNKNOWN = -9999;
    }

    public class SortOrder {

        public static final int NONE = 0;
        public static final int BY_NAME = 1;
        public static final int BY_TYPE = 2;
        public static final int BY_TYPE_AND_NAME = 3;
    }
}
