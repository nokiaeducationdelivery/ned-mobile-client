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
package org.ned.client.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import org.ned.client.Content;
import org.ned.client.NedConsts.NedContentType;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

/**
 *
 * @author pawel.polanski
 */
public class NedHistoryContent {

    private static String localOldRoot = "file:///E:/Data/NokiaECD.java/";

    public static Vector getHistoryContent() {
        Enumeration e = FileSystemRegistry.listRoots();
        while (e.hasMoreElements()) {
            String drive = (String) e.nextElement();
            if (drive.equals("root1/")) {
                localOldRoot = "file:///root1/Data/NokiaECD.java/";
                break;
            }
        }
        Vector vect = new Vector();
        FileConnection histDirConn = null;
        try {
            histDirConn = (FileConnection) Connector.open(localOldRoot, Connector.READ);
            if (histDirConn.exists()) {
                Enumeration fileList = histDirConn.list();
                histDirConn.close();
                histDirConn = null;
                String fileName = null;
                while (fileList.hasMoreElements()) {
                    fileName = (String) fileList.nextElement();
                    if (fileName.startsWith("srv_")) {
                        handleOldPath(vect, localOldRoot + fileName);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (histDirConn != null) {
                try {
                    histDirConn.close();
                } catch (IOException ex) {
                }
            }
        }
        return vect;
    }

    protected static boolean handleOldPath(Vector vect, String path) {
        boolean result = true;
        String fileName = null;
        FileConnection connection = null;

        try {
            connection = (FileConnection) Connector.open(path, Connector.READ);
            if (connection.exists()) {
                if (path.endsWith("/")) {  // isDirctory does not work
                    Enumeration e = connection.list();
                    Content content = null;
                    while (e.hasMoreElements()) {
                        try {
                            fileName = e.nextElement().toString();
                            if ( fileName.endsWith("/") ) {
                                handleOldPath(vect, path + fileName);
                            } else if (MediaTypeResolver.isOldMedia(fileName)) {
                                content = new Content(path + fileName);
                                content.setVideoPath(path);
                                String title = getOldContentTitle(path);
                                if (title != null && !title.equals("")) {
                                    content.setText( title );
                                } else {
                                    content.setText(fileName);
                                }
                                content.setData(fileName);
                                if (MediaTypeResolver.isAudio(fileName)) {
                                    content.setType(NedContentType.AUDIO);
                                } else if (MediaTypeResolver.isVideo(fileName)) {
                                    content.setType(NedContentType.VIDEO);
                                } else if (MediaTypeResolver.isImage(fileName)) {
                                    content.setType(NedContentType.IMAGE);
                                }
                                vect.addElement(content);
                            }
                        } catch (SecurityException ex) {
                            // not accessible file or dir is not displayed
                            GeneralAlert.show(NedResources.ACCESS_DENIED, GeneralAlert.WARNING);
                        } catch (Exception ex) {
                            GeneralAlert.show(NedResources.MEMORY_OUT, GeneralAlert.WARNING);
                        } finally {

                        }
                    }
                }
            } else {
                result = false;
            }
        } catch (SecurityException ex) {
            //acces denied
            GeneralAlert.show(NedResources.ACCESS_DENIED, GeneralAlert.WARNING);
            result = false;
        } catch (OutOfMemoryError ex) {
            GeneralAlert.show(NedResources.MEMORY_OUT, GeneralAlert.WARNING);
            result = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }  finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException ex) {
                }
            }
        }
        return result;
    }

    public static String getOldContentTitle(String path) {
        try{
            String title = null;
            String fileName = path.substring(0, path.lastIndexOf('/'));
            fileName += ".xml";
            Document doc = NedXmlUtils.getDocFile(fileName);
            Element category = doc.getElement(null, "category");
            Element entry = category.getElement(null, "entry");
            Element titleEl = entry.getElement(null, "title");
            title = titleEl.getText(0);
            return title;
        }catch(Exception ex){
            return "";
        }
    }
}
