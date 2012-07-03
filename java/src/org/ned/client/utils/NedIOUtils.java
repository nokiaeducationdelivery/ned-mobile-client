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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.NedMidlet;

/**
 *
 * @author pawel.polanski
 */
public class NedIOUtils {

    private static String externalMemory = "file:///E:/";
    private static String localData = "file:///E:/Data/";
    private static String localRoot = "file:///E:/Data/NokiaECD.2java/";

    public static void setupRootDir() {
        boolean sunWTKEmulator = false;
        Enumeration e = FileSystemRegistry.listRoots();
        while (e.hasMoreElements()) {
            String drive = (String) e.nextElement();
            if (drive.equals("root1/")) {
                sunWTKEmulator = true;
                break;
            }
        }
        if (sunWTKEmulator) {
            externalMemory = "file:///root1/";
            localData = "file:///root1/Data/";
            localRoot = "file:///root1/Data/NokiaECD.2java/";
        }
    }

    public static String loadFile(String file) {
        FileConnection fc = null;
        InputStream is = null;
        String content = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ);

            if (fc.exists()) {
                is = fc.openInputStream();
                StringBuffer sb = new StringBuffer();

                int chars = 0;

                while ((chars = is.read()) != -1) {
                    sb.append((char) chars);
                }
                content = sb.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return content;
    }

    public static InputStream loadFileAsStream(String file) {
        FileConnection fc = null;
        InputStream is = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ);

            if ( fc.exists()) {
                is = fc.openInputStream();
            }
        } catch (IOException ex) {
           is = null;
        }
        return is;
    }

    public static void saveFile( String aFile, String aContent ) {
        FileConnection fc = null;
        PrintStream os = null;
        try {
            fc = (FileConnection)Connector.open( aFile, Connector.READ_WRITE );

            if ( fc.exists() ) {
                fc.delete();
            }
            fc.create();

            os = new PrintStream( fc.openOutputStream() );
            os.print( aContent == null ? "" : aContent );
            os.flush();
        } catch ( IOException ex ) {
        } finally {
            if (os != null) {
                os.close();
            }
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static boolean removeFile(String file) {
        boolean deleted = false;
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ_WRITE);
            if (fc.exists()) {
                //recursivly delete
                if (fc.isDirectory()) {
                    Enumeration e = fc.list("*", true);
                    String f = null;
                    while (e.hasMoreElements()) {
                        f = (String) e.nextElement();
                        removeFile(file + f);
                    }
                }

                fc.setWritable(true);
                fc.delete();
                deleted = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return deleted;
    }

    public static void createDirectory(String path) {
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.mkdir();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fc.close();
            } catch (IOException ex) {
            }
        }
    }

    public static boolean fileExists(String file) {
        FileConnection fc = null;
        boolean exists = false;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ);
            if (fc.exists()) {
                exists = true;
            } else {
                exists = false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
        return exists;
    }

    public static Vector directoryListing(String path) {
        Vector fileList = new Vector();
        FileConnection directoryConnection = null;
        try {
            directoryConnection = (FileConnection) Connector.open(path);
            Enumeration directoryListing = directoryConnection.list();
            while (directoryListing.hasMoreElements()) {
                fileList.addElement(directoryListing.nextElement());
            }

        } catch (IOException ex) {
        } finally {
            if (directoryConnection != null) {
                try {
                    directoryConnection.close();
                } catch (IOException ignore) {
                }
            }
        }
        return fileList;
    }

    public static String getLocalData() {
        return localData;
    }

    public static String getLocalRoot() {
        return localRoot;
    }

    public static String getStorage() {
        return externalMemory;
    }

    public static String getUserRootDirectory() {
        String retval = null;
        String username = NedMidlet.getAccountManager().getCurrentUser().login;
        if (username != null) {
            retval = localRoot + username + "/";
        }
        return retval;
    }

    public static String getUserHistoryDirectory() {
        return getUserRootDirectory();
    }

    public static String getSettingsFile() {
        return getUserRootDirectory() + NedLocalConst.SETTINGSFILE;
    }

    public static String getAccountsFile() {
        return getLocalRoot() + NedLocalConst.ACCOUNTSFILE;
    }

    public static String getDowloadsFile() {
        return getUserRootDirectory() + NedLocalConst.DOWNLOADSFILE;
    }

    public static String getLanguageFile() {
        return getLocalRoot() + NedLocalConst.LANGUAGESFILE;
    }

    public static void moveFile(String oldPath, String newPath) {
        FileConnection oldConn = null;
        FileConnection newConn = null;

        InputStream oldFileInput = null;
        OutputStream newFileOutput = null;


        //TODO duplicated files?
        try {
            oldConn = (FileConnection) Connector.open(oldPath, Connector.READ_WRITE);

            if (oldConn.exists()) {
                newConn = (FileConnection) Connector.open(newPath, Connector.WRITE);
                newConn.create();

                oldFileInput = oldConn.openInputStream();
                newFileOutput = newConn.openOutputStream();

                int len = 0;
                byte[] buf = new byte[1024];

                while ((len = oldFileInput.read(buf)) > 0) {
                    newFileOutput.write(buf, 0, len);
                }
                oldConn.delete();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (oldFileInput != null) {
                    oldFileInput.close();
                }
                if (oldConn != null) {
                    oldConn.close();
                }
                if (newFileOutput != null) {
                    newFileOutput.close();
                }
                if (newConn != null) {
                    newConn.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static byte[] bytesFromFile(String file) throws IOException {

        byte[] bytes = null;
        InputStream in = null;
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(file, Connector.READ_WRITE);
            if (fc.exists()) {
                in = fc.openInputStream();
                long length = fc.fileSize();

                bytes = new byte[(int) length];
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = in.read(bytes,
                        offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
            }
        } catch (IOException ex) {
        } finally {
            if (in != null) {
                in.close();
            }
            if (fc != null) {
                fc.close();
            }
        }
        return bytes;
    }
}
