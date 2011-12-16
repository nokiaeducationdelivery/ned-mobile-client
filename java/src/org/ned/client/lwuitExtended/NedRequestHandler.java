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
package org.ned.client.lwuitExtended;

import com.sun.lwuit.html.DocumentInfo;
import com.sun.lwuit.html.DocumentRequestHandler;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;


public class NedRequestHandler implements DocumentRequestHandler {

    public InputStream resourceRequested(DocumentInfo docInfo) {

        String fileName = docInfo.getUrl();
        FileConnection fc = null;
        InputStream is = null;
        try {
           String path = NedMidlet.getSettingsManager().getLibraryManager().getCurrentLibrary().getDirUri()
                       + "/"
                       + NedConsts.NedLocalConst.VIDEOSDIR
                       + fileName;
            fc = (FileConnection)Connector.open(path, Connector.READ);
            if (fc.exists()) {
                is = fc.openInputStream();
            }
        }
        catch (Exception e) {
        } finally {
            try {
                fc.close();
            } catch (Exception ex) {
            }
        }
        return is;
    }
}

