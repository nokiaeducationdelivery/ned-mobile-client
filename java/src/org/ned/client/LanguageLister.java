/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
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
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.utils.LanguageInfo;
import org.ned.client.utils.LanguageInfoRemote;
import org.xmlpull.v1.XmlPullParserException;

public class LanguageLister {

    private String mListUrl;
    private Vector/*<Languages>*/ mLanguages;

    public LanguageLister( String url ) {
        if ( url.endsWith( "/" ) ) {
            url = url.substring( 0, url.length() - 1 );
        }
        mListUrl = url + "?action=list";
        mLanguages = new Vector();
    }

    public void parseList( InputStream aStream ) throws UnsupportedEncodingException, XmlPullParserException, IOException {
        mLanguages.removeAllElements();
        KXmlParser parser = new KXmlParser();
        parser.setInput( aStream, "UTF-8" );
        Document doc = new Document();
        doc.parse( parser );
        Element rootElement = doc.getRootElement();

        for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
            if ( rootElement.getType( i ) == Node.ELEMENT ) {
                Element languageElement = rootElement.getElement( i );
                LanguageInfoRemote el = new LanguageInfoRemote();
                for ( int j = 0; j < languageElement.getChildCount(); j++ ) {
                    if ( languageElement.getType( j ) == Node.ELEMENT ) {
                        parseLanguage( el, languageElement.getElement( j ) );
                    }
                }
                if ( el.getLocale() != null && el.getFile() != null ) {
                    mLanguages.addElement( el );
                }
            }
        }
    }

    private void parseLanguage( LanguageInfoRemote el, Element element ) {
        String elementName = element.getName();
        String elementValue = element.getText( 0 );

        if ( elementName.equals( NedConsts.NedXmlTag.ID ) ) {
            el.setId( elementValue );
        } else if ( elementName.equals( NedConsts.NedXmlTag.LOCALE ) ) {
            el.setLocale( elementValue );
        } else if ( elementName.equals( NedConsts.NedXmlTag.FILE ) ) {
            el.setFile( elementValue );
        } else if ( elementName.equals( NedConsts.NedXmlTag.NAME ) ) {
            el.setName( elementValue );
        }
    }

    public String checkUrl() {
        return mListUrl;
    }

    public Vector getNew() {
        Vector newLang = new Vector();
        Vector local = new LanguageManager().getLanguages();
        for ( int i = 0; i < mLanguages.size(); i++ ) {
            String loc = ((LanguageInfoRemote) mLanguages.elementAt( i )).getLocale();
            if ( !isOnList( local, loc ) ) {
                newLang.addElement( mLanguages.elementAt( i ) );
            }
        }
        return newLang;
    }

    private boolean isOnList( Vector local, String loc ) {
        Enumeration iter = local.elements();

        while ( iter.hasMoreElements() ) {
            if ( ((LanguageInfo) iter.nextElement()).getLocale().equalsIgnoreCase( loc ) ) {
                return true;
            }
        }
        return false;
    }
}
