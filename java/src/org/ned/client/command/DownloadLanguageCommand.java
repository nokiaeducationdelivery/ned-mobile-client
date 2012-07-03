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
package org.ned.client.command;

import com.sun.lwuit.Command;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.LanguageInfo;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedIOUtils;

public class DownloadLanguageCommand extends NedCommandAsync {

    private static DownloadLanguageCommand instance;

    public DownloadLanguageCommand() {
        command = new Command( NedResources.MID_OK_COMMAND );
    }

    public static DownloadLanguageCommand getInstance() {
        if ( instance == null ) {
            instance = new DownloadLanguageCommand();
        }
        return instance;
    }

    protected void doAction( Object aParam ) {
        LanguageInfo lang = (LanguageInfo) param;

        HttpConnection hc = null;
        FileConnection fc = null;
        InputStream is = null;
        OutputStream os = null;
        String url = NedMidlet.getAccountManager().getServerUrl() + NedConsts.NedRemotePath.LOCALESURL;

        url = url + "/" + lang.getFile();
        try {

            hc = (HttpConnection) Connector.open( url );
            NedConnectionUtils.addCredentialsToConnection( hc,
                                                           NedMidlet.getAccountManager().getCurrentUser().login,
                                                           NedMidlet.getAccountManager().getCurrentUser().password );
            hc.setRequestMethod( HttpConnection.GET );
            hc.setRequestProperty( NedConsts.HttpHeader.CACHECONTROL, NedConsts.HttpHeaderValue.NOCACHE );

            if ( hc.getResponseCode() != HttpConnection.HTTP_OK
                 && hc.getResponseCode() != HttpConnection.HTTP_UNAUTHORIZED ) {
                throw new AsyncException( " " + hc.getResponseCode() );
            } else if ( hc.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED ) {
                throw new AsyncException( NedResources.UNAUTHORIZED_ACCESS );
            }
            is = hc.openInputStream();

            fc = (FileConnection) Connector.open( NedIOUtils.getLocalRoot() + "/messages_" + lang.getLocale() + ".properties", Connector.READ_WRITE );
            if ( fc.exists() ) {
                fc.delete();
            }
            fc.create();
            os = fc.openOutputStream();

            byte[] databyte = new byte[NedConnectionUtils.MTU];
            int bytesread = -1;
            try {
                Thread.sleep( 200 );
            } catch ( IllegalStateException ex ) {
            }

            while ( (bytesread = is.read( databyte, 0, NedConnectionUtils.MTU )) != -1 ) {
                os.write( databyte, 0, bytesread );
            }
            lang.setLocal();
        } catch ( ConnectionNotFoundException cnfex ) {
            throw new AsyncException( NedResources.CONNECTION_ERROR );
        } catch ( UnsupportedEncodingException ueex ) {
            throw new AsyncException( NedResources.INVALID_ENCODING );
        } catch ( IOException ioex ) {
            throw new AsyncException( NedResources.CORRUPTED_DOCUMENT );
        } catch ( Exception ex ) {
            throw new AsyncException( ex.getMessage() + " Url: " + url );
        } finally {
            if ( os != null ) {
                try {
                    os.close();
                } catch ( IOException ex ) {
                }
            }
            if ( is != null ) {
                try {
                    is.close();
                } catch ( IOException ex ) {
                }
            }
            if ( hc != null ) {
                try {
                    hc.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }
}
