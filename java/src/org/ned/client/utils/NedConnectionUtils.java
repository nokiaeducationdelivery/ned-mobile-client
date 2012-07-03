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
package org.ned.client.utils;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.library.NedLibrary;

public class NedConnectionUtils {

    public static final int MTU = 1024;

    public static String GetMainXmlUri() {
        return "";
    }

    public static NedLibrary getLibraryInfo( String libraryId ) throws UnauthorizedLibraryUsageException, ContentNotExistException {
        NedLibrary retval = null;

        HttpConnection hc = null;
        InputStream is = null;

        try {
            String uri = NedMidlet.getAccountManager().getContentServletUri();
            hc = (HttpConnection) Connector.open( uri );
            hc.setRequestMethod( HttpConnection.GET );
            hc.setRequestProperty( "id", libraryId );
            hc.setRequestProperty( StringRepository.HEADER_NONRECURSIVE, "" );
            hc.setRequestProperty( NedConsts.HttpHeader.CACHECONTROL, NedConsts.HttpHeaderValue.NOCACHE );

            addCredentialsToConnection( hc,
                                        NedMidlet.getAccountManager().getCurrentUser().login,
                                        NedMidlet.getAccountManager().getCurrentUser().password );
            is = hc.openInputStream();
            int rc = hc.getResponseCode();

            if ( rc == HttpConnection.HTTP_OK ) {
                String title = hc.getHeaderField( "Title" );
                String type = hc.getHeaderField( "Type" );
                String version = hc.getHeaderField( "Version" );

                if ( title != null && !title.equals( "" ) && type.equals( "Library" ) ) {
                    retval = new NedLibrary( libraryId, title, version );
                } else {
                    throw new ContentNotExistException();
                }
            } else if ( rc == HttpConnection.HTTP_UNAUTHORIZED ) {
                throw new UnauthorizedLibraryUsageException();
            } else if ( rc == HttpConnection.HTTP_NO_CONTENT ) {
                throw new ContentNotExistException();
            }

        } catch ( IOException ioe ) {
        } finally {
            try {
                if ( is != null ) {
                    is.close();
                }
                if ( hc != null ) {
                    hc.close();
                }
            } catch ( IOException ioe ) {
            }
        }
        return retval;
    }

    public static void addCredentialsToConnection( HttpConnection hc, String user, String password ) throws IOException {
        hc.setRequestProperty( "Authorization", "Basic " + BasicAuth.encode( user, password ) );
    }

    public static String httpGet( String aUrl ) throws SecurityException {
        HttpConnection hc = null;
        InputStream ic = null;
        StringBuffer buffer = new StringBuffer();
        try {
            hc = (HttpConnection) Connector.open( aUrl );
            hc.setRequestMethod( HttpConnection.GET );
            addCredentialsToConnection( hc,
                                        NedMidlet.getAccountManager().getCurrentUser().login,
                                        NedMidlet.getAccountManager().getCurrentUser().password );

            ic = hc.openInputStream();

            int responseCode = hc.getResponseCode();

            if ( responseCode == HttpConnection.HTTP_PARTIAL
                 || responseCode == HttpConnection.HTTP_OK ) {

                byte[] databyte = new byte[MTU];

                int bytesRead = 0;
                while ( true ) {

                    bytesRead = ic.read( databyte, 0, MTU );
                    if ( bytesRead == -1 ) {
                        break;//transfer completed - end of file reached
                    }
                    buffer.append( new String( databyte, 0, bytesRead ) );
                }
            }
        } catch ( IOException ex ) {
        } finally {
            try {
                if ( ic != null ) {
                    ic.close();
                }
                if ( hc != null ) {
                    hc.close();
                }
            } catch ( IOException ex ) {
            }
        }
        return buffer.toString();
    }
}
