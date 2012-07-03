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
package org.ned.client.command;

import com.sun.lwuit.Command;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedResources;

public class CheckServerUrlComand extends NedCommandAsync {

    private static CheckServerUrlComand instance;
    private final String serverPostfix = "/NEDCatalogTool2/";

    private CheckServerUrlComand() {
        command = new Command( NedResources.CHECK_SERVER );
    }

    public static CheckServerUrlComand getInstance() {
        if ( instance == null ) {
            instance = new CheckServerUrlComand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        String url = (String) param;
        if ( url.endsWith( "/" ) ) {
            url = url.substring( 0, url.length() - 1 );
        }
        String wwwUrl = url + serverPostfix;
        HttpConnection hc = null;
        try {
            hc = (HttpConnection) Connector.open( wwwUrl );
            hc.setRequestMethod( HttpConnection.GET );
            hc.setRequestProperty( NedConsts.HttpHeader.CACHECONTROL, NedConsts.HttpHeaderValue.NOCACHE );

            if ( hc.getResponseCode() != HttpConnection.HTTP_OK
                 && hc.getResponseCode() != HttpConnection.HTTP_UNAUTHORIZED ) {
                throw new AsyncException( " " + hc.getResponseCode() );
            }
        } catch ( Exception ex ) {
            throw new AsyncException( ex.getMessage() + " Url: " + wwwUrl );
        } finally {
            if ( hc != null ) {
                try {
                    hc.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }
}
