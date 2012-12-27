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

    public XmlManager() {
    }

    public static Vector getContentChilds( String contentId, String libFile ) {
        Vector retval = new Vector();

        Element element = findElement( contentId, libFile );
        if ( element != null ) {
            Vector childElementVector = findChilds( element, StringRepository.TAG_CHILDS );
            if ( childElementVector.size() == 1 ) {
                Vector childNodes = findChilds( (Element)childElementVector.elementAt( 0 ), StringRepository.TAG_NODE );
                for ( int i = 0; i < childNodes.size(); i++ ) {
                    retval.addElement( parseContent( (Element)childNodes.elementAt( i ) ) );
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
                    Vector childNodes = findChilds( (Element)childElementVector.elementAt( 0 ), StringRepository.TAG_NODE );
                    for ( int idx = 0; idx < childNodes.size(); idx++ ) {
                        searchContentFiles( (Element)childNodes.elementAt( idx ), fileList );
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
                    String keyword;
                    try {
                        keyword = entryElement.getText( 0 );
                        keywords.addElement( keyword );
                    } catch ( Exception ex ) {
                        //no keywords links - ignore
                    }
                } else if ( entryElement.getName().equals( StringRepository.TAG_EXTERNAL_LINKS ) ) {
                    if ( externalLinks == null ) {
                        externalLinks = new Vector( 4, 4 );
                    }
                    String link = null;
                    try {
                        entryElement.getText( 0 );
                        externalLinks.addElement( link );
                    } catch ( Exception ex ) {
                        //no external links - ignore
                    }
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
                    if ( NedMidlet.getSettingsManager().getDlAutomatic() ) {
                        tf.startDownload( false );
                    }
                    continue;
                }
            }
        } else {
            Document doc = new Document();
            Element videos = doc.createElement( "", NedXmlTag.VIDEOS );
            doc.addChild( Node.ELEMENT, videos );
            NedXmlUtils.writeXmlFile( NedIOUtils.getDowloadsFile(), doc );
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
        }
    }

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
}
