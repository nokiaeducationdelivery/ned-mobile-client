/*******************************************************************************
 * Copyright (c) 2011 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.utils.LanguageInfo;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.NedXmlUtils;

public class LanguageManager {

    Vector/*<LanguageInfo>*/ mLanguages = null;

    public LanguageManager() {
        mLanguages = new Vector();
        readSetup();
    }

    private void readSetup() {
        mLanguages.removeAllElements();
        if ( NedIOUtils.fileExists( NedIOUtils.getLanguageFile() ) ) {
            Element rootElement = null;
            try {
                Document doc = NedXmlUtils.getDocFile( NedIOUtils.
                        getLanguageFile(), true );
                if ( doc == null ) {
                    NedIOUtils.removeFile( NedIOUtils.getLanguageFile() );
                    return;
                } else {
                    rootElement = doc.getRootElement();
                }
            } catch ( Exception ex ) {
                NedIOUtils.removeFile( NedIOUtils.getLanguageFile() );
                return;
            }

            for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
                if ( rootElement.getType( i ) != Node.ELEMENT ) {
                    continue;
                }
                Element element = rootElement.getElement( i );
                if ( element.getName().equals( NedConsts.NedXmlTag.LANGUAGE ) ) {
                    String name = element.getAttributeValue( "", NedConsts.NedXmlAttribute.NAME );
                    String locale = element.getAttributeValue( "", NedConsts.NedXmlAttribute.LOCALE );
                    String remoteName = element.getAttributeValue( "", NedConsts.NedXmlAttribute.REMOTENAME );
                    boolean isLocal = NedIOUtils.fileExists( NedIOUtils.
                            getLocalRoot() + "messages_" + locale
                            + ".properties" );
                    mLanguages.addElement( new LanguageInfo( name, locale, isLocal, remoteName ) );
                }
            }
        } else {
            createDefault();
        }
    }

    public void saveSetup() {
        Document doc = new Document();
        Element rootElm = doc.createElement( "", NedConsts.NedXmlTag.LANGUAGES );

        for ( int i = 0; i < mLanguages.size(); i++ ) {
            Element lang = rootElm.createElement( "", NedConsts.NedXmlTag.LANGUAGE );
            lang.setAttribute( "", NedConsts.NedXmlAttribute.NAME, ((LanguageInfo)mLanguages.
                    elementAt( i )).getLangName() );
            lang.setAttribute( "", NedConsts.NedXmlAttribute.LOCALE, ((LanguageInfo)mLanguages.
                    elementAt( i )).getLocale() );
            lang.setAttribute( "", NedConsts.NedXmlAttribute.REMOTENAME, ((LanguageInfo)mLanguages.
                    elementAt( i )).getFile() );
            rootElm.addChild( Node.ELEMENT, lang );
        }
        doc.addChild( Node.ELEMENT, rootElm );

        NedXmlUtils.writeXmlFile( NedIOUtils.getLanguageFile(), doc );
    }

    public Vector getLanguages() {
        return mLanguages;
    }

    private void createDefault() {
        FileConnection fc = null;
        InputStream in = null;
        OutputStream os = null;
        try {
            mLanguages.addElement( new LanguageInfo( "Default (English)", "en-GB", true, "messages_en-GB.properties" ) );
            in = Runtime.getRuntime().getClass().getResourceAsStream( Localization._MESSAGES_BUNDLE );
            fc = (FileConnection)Connector.open( NedIOUtils.getLocalRoot()
                    + "messages_en-GB.properties", Connector.READ_WRITE );
            if ( fc.exists() ) {
                fc.delete();
            }
            fc.create();
            os = fc.openOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = -1;

            while ( (bytesRead = in.read( buffer )) != -1 ) {
                os.write( buffer, 0, bytesRead );
            }

            saveSetup();
            readSetup();
        } catch ( IOException ex ) {
        } finally {
            if ( os != null ) {
                try {
                    os.close();
                } catch ( IOException ex ) {
                    ex.printStackTrace();
                }
            }
            if ( in != null ) {
                try {
                    in.close();
                } catch ( IOException ex ) {
                    ex.printStackTrace();
                }
            }
            if ( fc != null ) {
                try {
                    fc.close();
                } catch ( IOException ex ) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
