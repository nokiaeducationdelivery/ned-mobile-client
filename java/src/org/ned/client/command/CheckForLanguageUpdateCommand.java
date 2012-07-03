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
import java.io.UnsupportedEncodingException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.ned.client.LanguageLister;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.NedConnectionUtils;
import org.xmlpull.v1.XmlPullParserException;

public class CheckForLanguageUpdateCommand extends NedCommandAsync {

    private static CheckForLanguageUpdateCommand instance;

    private CheckForLanguageUpdateCommand() {
        command = new Command( NedResources.CHECK_FOR_NEW_LANGUAGES );
    }

    public static CheckForLanguageUpdateCommand getInstance() {
        if ( instance == null ) {
            instance = new CheckForLanguageUpdateCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        LanguageLister lister = (LanguageLister) param;

        HttpConnection hc = null;
        InputStream is = null;
        try {

            hc = (HttpConnection) Connector.open( lister.checkUrl() );
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

            lister.parseList( is );
        } catch ( ConnectionNotFoundException cnfex ) {
            throw new AsyncException( NedResources.CONNECTION_ERROR );
        } catch ( UnsupportedEncodingException ueex ) {
            throw new AsyncException( NedResources.INVALID_ENCODING );
        } catch ( IOException ioex ) {
            throw new AsyncException( NedResources.CORRUPTED_DOCUMENT );
        } catch ( XmlPullParserException ppex ) {
            throw new AsyncException( NedResources.CORRUPTED_DOCUMENT );
        } catch ( Exception ex ) {
            throw new AsyncException( ex.getMessage() + " Url: " + lister.checkUrl() );
        } finally {
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
