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
package org.ned.client.command;

import com.sun.lwuit.Command;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.LoginScreen;

public class CheckServerUrlComand extends NedCommand {

    private static CheckServerUrlComand instance;
    private final String serverPostfix = "/NEDCatalogTool2/";

    private CheckServerUrlComand() {
        command = new Command( NedResources.CHECK_SERVER );
    }

    public static CheckServerUrlComand getInstance() {
        if (instance == null) {
            instance = new CheckServerUrlComand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        String url = (String) param;
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        String wwwUrl = url + serverPostfix;
        HttpConnection hc = null;
        try {
            hc = (HttpConnection) Connector.open(wwwUrl);
            hc.setRequestMethod(HttpConnection.GET);
            if (hc.getResponseCode() == HttpConnection.HTTP_OK ||
                hc.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED ) {
                NedMidlet.getAccountManager().setServer(url);
                new LoginScreen().show();
            } else {
                GeneralAlert.show(" " + hc.getResponseCode() , GeneralAlert.WARNING );//todo better message needed
            }
        } catch (Exception ex) {
            GeneralAlert.show(ex.getMessage() + " Url: " + wwwUrl, GeneralAlert.ERROR);
            ex.printStackTrace();
        } finally {
            if (hc != null) {
                try {
                    hc.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
