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

import java.util.Vector;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.NedConsts.NedContentType;
import org.ned.client.NedConsts.NedXmlTag;
import org.ned.client.transfer.DownloadTask;
import org.ned.client.transfer.IDownloadTaskManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.NedXmlUtils;
import org.ned.client.utils.StringRepository;

public class XmlManager {

    private NedMidlet midlet = null;
//    private String currentVideo = "";
//    private String currentSummary = "";

    public XmlManager( NedMidlet _midlet ) {
        this.midlet = _midlet;
    }

    public static void parseLibrary( Document doc, Vector outLibrary ) {
        Content libraryItem = null;

        Element rootElement = doc.getRootElement();

        for ( int i = 0; i < rootElement.getChildCount(); i++ ) {

            if ( rootElement.getType( i ) != Node.ELEMENT ) {
                continue;
            }

            Element element = rootElement.getElement( i );

            if ( element.getName().equals( "entry" ) ) {

                String title = null;
                String id = null;

                id = element.getAttributeValue( "", "id" );

                for ( int j = 0; j < element.getChildCount(); j++ ) {

                    if ( element.getType( j ) != Node.ELEMENT ) {
                        continue;
                    }

                    Element entryElement = element.getElement( j );
                    if ( entryElement.getName().equals( "title" ) ) {
                        title = entryElement.getText( 0 );
                        continue;
                    }

                }
                libraryItem = new Content( title, id );
                outLibrary.addElement( libraryItem );
            }
        }
    }

    public static Vector getContentChilds( String contentId, String libFile ) {
        Vector retval = new Vector();

        Element element = findElement( contentId, libFile );
        if ( element != null ) {
            Vector childElementVector = findChilds( element, StringRepository.TAG_CHILDS );
            if ( childElementVector.size() == 1 ) {
                Vector childNodes = findChilds( (Element)childElementVector.
                        elementAt( 0 ), StringRepository.TAG_NODE );
                for ( int i = 0; i < childNodes.size(); i++ ) {
                    retval.addElement( parseContent( (Element)childNodes.
                            elementAt( i ) ) );
                }
            }
        }
        return retval;
    }

    /*new*/
    public static Vector getContentChilds( Element aContentElement ) {
        Vector retval = new Vector();

        if ( aContentElement != null ) {
            Vector childElementVector = findChilds( aContentElement, StringRepository.TAG_CHILDS );
            if ( childElementVector.size() == 1 ) {
                Vector childNodes = findChilds( (Element)childElementVector.
                        elementAt( 0 ), StringRepository.TAG_NODE );
                for ( int i = 0; i < childNodes.size(); i++ ) {
                    retval.addElement( parseContent( (Element)childNodes.
                            elementAt( i ) ) );
                }
            }
        }
        return retval;
    }

    public static void removeContentChild( String contentId ) {
        String libUri = NedMidlet.getSettingsManager().getLibraryManager().
                getCurrentLibrary().getFileUri();
        Document doc = NedXmlUtils.getDocFile( libUri );

        if ( doc == null ) {
            return;
        }

        Element element = findElement( doc.getRootElement(), contentId );

        if ( element != null ) {
            Node parent = element.getParent();

            for ( int idx = 0; idx < parent.getChildCount(); idx++ ) {
                if ( parent.getChild( idx ) == element ) {
                    parent.removeChild( idx );
                    NedXmlUtils.writeXmlFile( libUri, doc );
                    NedXmlUtils.touch();
                    break;
                }
            }
        }
    }

    public static Vector getContentAllFiles( String contentId ) {
        Vector fileList = new Vector();

        String libUri = NedMidlet.getSettingsManager().getLibraryManager().
                getCurrentLibrary().getFileUri();
        Document doc = NedXmlUtils.getDocFile( libUri );

        Element element = findElement( doc.getRootElement(), contentId );

        if ( element != null ) {
            searchContentFiles( element, fileList );
        }
        return fileList;
    }

    private static void searchContentFiles( Element element, Vector fileList ) {

        if ( element.getName().equals( StringRepository.TAG_NODE ) ) {
            Content content = parseContent( element );

            String type = content.getType();
            if ( type.equals( NedContentType.VIDEO )
                 || type.equals( NedContentType.IMAGE )
                 || type.equals( NedContentType.AUDIO )
                 || type.equals( NedContentType.TEXT ) ) {
                if ( content.getData() != null && content.getData().length() > 0 ) {
                    fileList.addElement( content.getMediaFile() );
                }
            } else {
                Vector childElementVector = findChilds( element, StringRepository.TAG_CHILDS );
                if ( childElementVector.size() == 1 ) {
                    Vector childNodes = findChilds( (Element)childElementVector.
                            elementAt( 0 ), StringRepository.TAG_NODE );
                    for ( int idx = 0; idx < childNodes.size(); idx++ ) {
                        searchContentFiles( (Element)childNodes.elementAt( idx ), fileList );
                    }
                }
            }
        }
    }

    public static Vector getContentAllChilds( String contentId ) {
        Vector itemList = new Vector();

        String libUri = NedMidlet.getSettingsManager().getLibraryManager().
                getCurrentLibrary().getFileUri();
        Document doc = NedXmlUtils.getDocFile( libUri );

        Element element = findElement( doc.getRootElement(), contentId );

        if ( element != null ) {
            searchContentAllChilds( element, itemList );
        }
        return itemList;
    }

    private static void searchContentAllChilds( Element element, Vector itemList ) {

        if ( element.getName().equals( StringRepository.TAG_NODE ) ) {
            Content content = parseContent( element );

            String type = content.getType();
            itemList.addElement( content );

            if ( type.equals( NedContentType.VIDEO )
                 || type.equals( NedContentType.IMAGE )
                 || type.equals( NedContentType.AUDIO ) ) {
            } else {
                Vector childElementVector = findChilds( element, StringRepository.TAG_CHILDS );
                if ( childElementVector.size() == 1 ) {
                    Vector childNodes = findChilds( (Element)childElementVector.
                            elementAt( 0 ), StringRepository.TAG_NODE );
                    for ( int idx = 0; idx < childNodes.size(); idx++ ) {
                        searchContentAllChilds( (Element)childNodes.elementAt( idx ), itemList );
                    }
                }
            }
        }
    }

    private static Vector findChilds( Element parent, String tagName ) {
        Vector retVal = new Vector();
        for ( int j = 0; j < parent.getChildCount(); j++ ) {
            if ( parent.getType( j ) == Node.ELEMENT ) {
                Element entryElement = parent.getElement( j );

                if ( entryElement.getName().equals( tagName ) ) {
                    retVal.addElement( entryElement );
                }
            }
        }
        return retVal;
    }

    public static Content getContentData( String contentId ) {
        Content result = null;

        Element element = findElement( contentId, null );
        if ( element != null ) {
            result = parseContent( element );
        }
        return result;
    }

    /*new*/
    public static Content getContentData( Element aContentElem ) {
        Content result = null;

        if ( aContentElem != null ) {
            result = parseContent( aContentElem );
        }
        return result;
    }

    private static Content parseContent( Element element ) {
        String title = null;
        String description = null;
        String parentId = element.getAttributeValue( "", StringRepository.ATTRIBUTE_PARENT );
        String type = element.getAttributeValue( "", StringRepository.ATTRIBUTE_TYPE ).
                intern();
        String data = element.getAttributeValue( "", StringRepository.ATTRIBUTE_DATA );
        String id = element.getAttributeValue( "", StringRepository.ATTRIBUTE_ID );
        String version = element.getAttributeValue( "", StringRepository.ATTRIBUTE_VERSION );

        Vector keywords = null;
        Vector externalLinks = null;
        for ( int j = 0; j < element.getChildCount(); j++ ) {
            if ( element.getType( j ) == Node.ELEMENT ) {
                Element entryElement = element.getElement( j );

                if ( entryElement.getName().equals( StringRepository.TAG_TITLE ) ) {
                    try {
                        title = entryElement.getText( 0 );
                    } catch ( Exception ex ) {
                        title = "";
                    }
                } else if ( entryElement.getName().equals( StringRepository.TAG_DESCRIPTION ) ) {
                    description = entryElement.getText( 0 );
                } else if ( entryElement.getName().equals( StringRepository.TAG_KEYWORDS ) ) {
                    if ( keywords == null ) {
                        keywords = new Vector( 4, 4 );
                    }
                    keywords.addElement( entryElement.getText( 0 ) );
                } else if ( entryElement.getName().equals( StringRepository.TAG_EXTERNAL_LINKS ) ) {
                    if ( externalLinks == null ) {
                        externalLinks = new Vector( 4, 4 );
                    }
                    externalLinks.addElement( entryElement.getText( 0 ) );
                }
            }
        }
        Content parsed = new Content( title, id );
        parsed.setParentId( parentId );
        parsed.setData( data );
        parsed.setDescription( description == null ? "" : description );
        parsed.setType( type );
        parsed.setKeywords( keywords );
        parsed.setExternalLinks( externalLinks );
        parsed.setVersion( version == null ? "" : version );
        return parsed;
    }

    /*private*/
    public static Element findElement( String contentId, String libFile ) {
        Element retVal = null;
        String libfile = (libFile == null)
                         ? NedMidlet.getSettingsManager().getLibraryManager().
                getCurrentLibrary().getFileUri()
                         : libFile;
        Document doc = NedXmlUtils.getDocFile( libfile );
        if ( doc != null ) {
            Element rootElement = doc.getRootElement();
            if ( rootElement != null ) {
                retVal = findElement( rootElement, contentId );
            }
        }
        return retVal;
    }

    private static Element findElement( Element rootElement, String contentId ) {
        Element retVal = null;
        String idValue = rootElement.getAttributeValue( "", "id" );
        if ( idValue != null && idValue.equals( contentId ) ) {
            retVal = rootElement;
        } else {
            for ( int i = 0; i < rootElement.getChildCount() && retVal == null; i++ ) {
                if ( rootElement.getElement( i ) != null ) {
                    retVal = findElement( rootElement.getElement( i ), contentId );
                }
            }
        }
        return retVal;
    }

//    public static MediaItem parseMediaItem(Document doc) {
//        MediaItem item = new MediaItem();
//        Element rootElement = doc.getRootElement();
//
////        TextArea ta = new TextArea();
////        ta.setFocusable(false);
//
//        for (int i = 0; i < rootElement.getChildCount(); i++) {
//            if (rootElement.getType(i) != Node.ELEMENT) {
//                continue;
//            }
//            Element element = rootElement.getElement(i);
//
//            if (element.getName().equals("summary")) {
//                item.summary = element.getText(0);
//                continue;
//            } else if (element.getName().equals("file")) {
//                item.file = element.getText(0);
//                continue;
//            }
//        }
//        return item;
//    }
//    public void updateStatistics() {
//        StatisticsManager.updateStatistics(LibraryManager.getInstance().getCurrentLibrary().currentCatalog.currentCategory.currentMediaItem.content.getPath(),
//                LibraryManager.getInstance().getCurrentLibrary().currentCatalog.currentCategory.currentMediaItem.content.getText());
//    }
////    public static int removeEntry_(String filepath, String entryId) {
//        boolean removed = false;
//        String currentId = null;
//        int entryElementCount = 0;
//
//        Document doc = NedXmlUtils.getDocFile(filepath);
//
//        if (doc != null) {
//            Element rootElement = doc.getRootElement();
//
//            int count = rootElement.getChildCount();
//            int removeIndex = 0;
//
//            for (int i = 0; i < count; i++) {
//                if (rootElement.getType(i) != Node.ELEMENT) {
//                    continue;
//                }
//
//                Element element = rootElement.getElement(i);
//
//                if (element.getName().equals("entry")) {
//                    entryElementCount++;
//                    if (!removed) {
//                        currentId = element.getAttributeValue("", "id");
//                        if (currentId.equals(entryId)) {
//                            removeIndex = i;
//                            removed = true;
//                        }
//                    }
//                }
//            }
//
//            if (removed) {
//                rootElement.removeChild(removeIndex);
//                NedXmlUtils.writeXmlFile(filepath, doc);
//                entryElementCount--;
//            }
//        }
//
//        return entryElementCount;
//    }
//    public void resetLibrary() {
//        NedIOUtils.removeFile(midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.LIBRARYFILE);
//        NedIOUtils.removeFile(midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.LIBRARYDIR + '/');
//    }
//    public void updateLibrary() {
//        boolean newer = true;
//        String file = midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.LIBRARYFILE;
//        String newfile = file + "tmp";
//        try {
//            midlet.getDlManager().downloadLibraryTmp();
//            Document doc = NedXmlUtils.getDocFile(file);
//            String version = "0";
//            if (doc != null) {
//                Element rootElement = doc.getRootElement();
//                newer = false;
//                version = rootElement.getAttributeValue("", "version");
//            }
//            doc = NedXmlUtils.getDocFile(newfile);
//            String newversion = "0";
//            if (doc != null) {
//                Element rootElement = doc.getRootElement();
//                newer = true;
//                newversion = rootElement.getAttributeValue("", "version");
//            }
//            version = version.replace('.', '0');
//            newversion = newversion.replace('.', '0');
//            if (Integer.parseInt(newversion) <= Integer.parseInt(version)) {
//                newer = false;
//            }
//        } finally {
//            if (newer) {
//                resetLibrary();
//                try {
//                    FileConnection fc = (FileConnection) Connector.open(newfile,
//                            Connector.READ_WRITE);
//                    fc.rename(NedLocalConst.LIBRARYFILE);
//                } catch (IOException ex) {
//                }
//            } else {
//                try {
//                    FileConnection fc = (FileConnection) Connector.open(newfile,
//                            Connector.READ_WRITE);
//                    fc.delete();
//                } catch (IOException ex) {
//                }
//            }
//        }
//    }
    public void readDownloads( IDownloadTaskManager downloadManager ) {
        if ( NedIOUtils.fileExists( NedIOUtils.getDowloadsFile() ) ) {
            Document doc = NedXmlUtils.getDocFile( NedIOUtils.getDowloadsFile() );
            if ( doc == null ) {
                NedIOUtils.removeFile( NedIOUtils.getDowloadsFile() );
                return;
            }
            Element rootElement = doc.getRootElement();
            for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
                if ( rootElement.getType( i ) != Node.ELEMENT ) {
                    continue;
                }
                Element element = rootElement.getElement( i );
                if ( element.getName().equals( "entry" ) ) {
                    String localPath = element.getText( 0 );
                    String title = element.getAttributeValue( "", "title" );
                    String progress = element.getAttributeValue( "", "progress" );
                    if ( (localPath == null) || (title == null)
                         || (progress == null) ) {
                        continue;
                    }
                    String url = element.getAttributeValue( "", "url" );

                    progress = progress.replace( '%', '0' );
                    DownloadTask tf = new DownloadTask( downloadManager, localPath, url, title );
                    tf.setPercentDownloaded( progress );

                    long size = 0;
                    long pSize = 0;
                    String fileSize = element.getAttributeValue( "", "filesize" );
                    if ( fileSize != null ) {
                        size = Long.parseLong( fileSize );
                        pSize = size * Long.parseLong( progress ) / 1000;
                    }
                    if ( size > 0 ) {
                        tf.setDownloadLength( size );
                        tf.addTotalBytesDownloaded( pSize );
                        tf.setPercentDownloaded();
                    }
                    if ( midlet.getSettingsManager().getDlAutomatic() ) {
                        tf.startDownload();
                    }
                    continue;
                }
            }
        } else {
            Document doc = new Document();
            Element videos = doc.createElement( "", NedXmlTag.VIDEOS );
            doc.addChild( Node.ELEMENT, videos );
            NedXmlUtils.writeXmlFile( NedIOUtils.getDowloadsFile(), doc );
            return;
        }
    }

    public void setProgress( String videoFile, float progress, long fileSize ) {
        Document doc = NedXmlUtils.getDocFile( NedIOUtils.getDowloadsFile() );
        if ( doc == null ) {
            NedIOUtils.removeFile( NedIOUtils.getDowloadsFile() );
            return;
        }
        Element rootElement = doc.getRootElement();
        for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
            if ( rootElement.getType( i ) != Node.ELEMENT ) {
                continue;
            }
            Element element = rootElement.getElement( i );
            if ( element.getName().equals( "entry" ) ) {
                String video = element.getText( 0 );
                if ( video.equals( videoFile ) ) {
                    String pr = String.valueOf( (int)(progress) ) + "%";
                    element.setAttribute( "", "progress", pr );
                    element.setAttribute( "", "filesize",
                                          String.valueOf( fileSize ) );
                    NedXmlUtils.writeXmlFile( NedIOUtils.getDowloadsFile(), doc );
                    return;
                }
            }
        }
    }

//    public void addDownloadsEntry(String video, String title) {
//        String file = NedMidlet.getInstance().getLocalRoot()
//                + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                + NedLocalConst.DOWNLOADSFILE;
//        Document doc = NedXmlUtils.getDocFile(file);
//        if (doc != null) {
//            if (!videoEntryExists(video, doc)) {
//                Element entry = doc.createElement("", "entry");
//                entry.setAttribute("", "title", title);
//                entry.setAttribute("", "progress", "0%");
//                entry.addChild(Node.TEXT, video);
//                doc.getRootElement().addChild(Node.ELEMENT, entry);
//                NedXmlUtils.writeXmlFile(NedMidlet.getInstance().getLocalRoot()
//                        + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                        + NedLocalConst.DOWNLOADSFILE, doc);
//            }
//        } else {
//            NedIOUtils.removeFile(NedMidlet.getInstance().getLocalRoot()
//                    + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                    + NedLocalConst.DOWNLOADSFILE);
//            return;
//        }
//    }
    //newmethod
    public void addDownloadsEntry( DownloadTask transfer ) {

        Document doc = NedXmlUtils.getDocFile( NedIOUtils.getDowloadsFile() );
        if ( doc != null ) {
            if ( !videoEntryExists( transfer.getTitle(), doc ) ) {
                Element entry = doc.createElement( "", "entry" );
                entry.setAttribute( "", "title", transfer.getTitle() );
                entry.setAttribute( "", "progress", "0%" );
                entry.setAttribute( "", "url", transfer.getUrlPath() );
                entry.addChild( Node.TEXT, transfer.getFile() );
                doc.getRootElement().addChild( Node.ELEMENT, entry );
                NedXmlUtils.writeXmlFile( NedIOUtils.getDowloadsFile(), doc );
            }
        } else {
            NedIOUtils.removeFile( NedIOUtils.getDowloadsFile() );
            return;
        }
    }

//    public void addServerEntry(String video, String title) {
//        String file = NedMidlet.getInstance().getLocalRoot()
//                + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                + NedLocalConst.DOWNLOADSFILE;
//        Document doc = NedXmlUtils.getDocFile(file);
//        if (doc != null) {
//            if (!videoEntryExists(video, doc)) {
//                Element entry = doc.createElement("", "entry");
//                entry.setAttribute("", "title", title);
//                entry.setAttribute("", "progress", "0%");
//                entry.addChild(Node.TEXT, video);
//                doc.getRootElement().addChild(Node.ELEMENT, entry);
//                NedXmlUtils.writeXmlFile(NedMidlet.getInstance().getLocalRoot()
//                        + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                        + NedLocalConst.DOWNLOADSFILE, doc);
//            }
//        } else {
//            NedIOUtils.removeFile(NedMidlet.getInstance().getLocalRoot()
//                    + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName()
//                    + NedLocalConst.DOWNLOADSFILE);
//            return;
//        }
//    }
    public void removeDownloadsEntry( String video ) {
        Document doc = NedXmlUtils.getDocFile( NedIOUtils.getDowloadsFile() );
        if ( doc != null ) {
            if ( videoEntryExists( video, doc ) ) {
                //TODO remove entry
                Element rootElement = doc.getRootElement();
                int removeId = -1;
                for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
                    if ( rootElement.getType( i ) != Node.ELEMENT ) {
                        continue;
                    }
                    Element element = rootElement.getElement( i );
                    if ( element.getName().equals( "entry" ) ) {
                        String entryVideo = element.getText( 0 );
                        if ( (entryVideo != null)
                             && (entryVideo.equals( video )) ) {
                            removeId = i;
                        }
                    }
                }
                if ( removeId > -1 ) {
                    rootElement.removeChild( removeId );
                    NedXmlUtils.writeXmlFile( NedIOUtils.getDowloadsFile(), doc );
                }
            }
        } else {
            NedIOUtils.removeFile( NedIOUtils.getDowloadsFile() );
            return;
        }
    }

    public boolean videoEntryExists( String video, Document doc ) {
        boolean entryFound = false;
        Element rootElement = doc.getRootElement();
        for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
            if ( rootElement.getType( i ) != Node.ELEMENT ) {
                continue;
            }
            Element element = rootElement.getElement( i );
            if ( element.getName().equals( "entry" ) ) {
                String text = element.getText( 0 );
                if ( (text != null) && (text.equals( video )) ) {
                    entryFound = true;
                    break;
                }
            }
        }
        return entryFound;
    }
    //public boolean loadFileSystem() {
//        if (NedIOUtils.fileExists(midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.FILESYSTEMFILE)) {
//            Document doc = NedXmlUtils.getDocFile(midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.FILESYSTEMFILE);
//            if (doc == null) {
//                return false;
//            }
//            Element rootElement = doc.getRootElement();
//            for (int i = 0; i < rootElement.getChildCount(); i++) {
//                if (rootElement.getType(i) != Node.ELEMENT) {
//                    continue;
//                }
//                Element element = rootElement.getElement(i);
//                String url = "", path = "", title = "";
//                for (int j = 0; j < element.getChildCount(); j++) {
//                    Element entryProperty = element.getElement(j);
//                    if (entryProperty.getName().equals("title")) {
//                        title = entryProperty.getText(0);
//                        continue;
//                    }
//                    if (entryProperty.getName().equals("url")) {
//                        url = entryProperty.getText(0);
//                        continue;
//                    }
//                    if (entryProperty.getName().equals("path")) {
//                        path = entryProperty.getText(0);
//                        continue;
//                    }
//                }
//                NedLibrary currentLibrary = LibraryManager.getInstance().getCurrentLibrary();
//                if (currentLibrary != null && currentLibrary.getUrlRoot().equals(url)) {
//                    currentLibrary.setTitle(title); // for title update
//                } else {
//                    currentLibrary = new NedLibrary(url, title);
//                }
//                LibraryManager.getInstance().setCurrentLibrary(currentLibrary);
//                LibraryManager.getInstance().addLibrary(currentLibrary);
//                currentLibrary.setLibraryDirId(NedLocalConst.LIBRARYDIR + '/' + path);
//            }
//        } else {
//            Document doc = new Document();
//            Element libraries = doc.createElement("", "libraries");
//            Element entry = doc.createElement("", "entry");
//            Element title = doc.createElement("", "title");
//            Element url = doc.createElement("", "url");
//            Element path = doc.createElement("", "path");
//
//            title.addChild(Node.TEXT, LibraryManager.getInstance().getCurrentLibrary().getTitle());
//            url.addChild(Node.TEXT, LibraryManager.getInstance().getCurrentLibrary().getUrlRoot());
//            String dirPath = IdGenerator.getId();
//            path.addChild(Node.TEXT, dirPath);
//
//            entry.addChild(Node.ELEMENT, title);
//            entry.addChild(Node.ELEMENT, url);
//            entry.addChild(Node.ELEMENT, path);
//            libraries.addChild(Node.ELEMENT, entry);
//            doc.addChild(Node.ELEMENT, libraries);
//
//            LibraryManager.getInstance().getCurrentLibrary().setLibraryDirId(NedLocalConst.LIBRARYDIR + '/' + dirPath);//DF:is this needed??
//
//            NedXmlUtils.writeXmlFile(midlet.getLocalRoot() + LibraryManager.getInstance().getCurrentLibrary().getLocalDirName() + NedLocalConst.FILESYSTEMFILE, doc);
//        }
//        return true;
//    }
//        return false;
//}
}
