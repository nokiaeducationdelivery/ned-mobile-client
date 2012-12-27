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
package org.ned.client.library.advanced;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedMidlet;
import org.ned.client.utils.Utils;

public class LibraryChanges {

    private String mDeltaFile;
    private Vector/*<String>*/ mIds;

    public LibraryChanges( String aDeltaFile ) {
        mDeltaFile = aDeltaFile;
    }

    public static void persistChangesInfo( Vector aMediaChanged, String aLibraryFile, boolean aOverride ) {

        String deltaFile = NedMidlet.getSettingsManager().getLibraryManager().
                getCurrentLibrary().getFileUri() + ".delta";
        FileConnection fc = null;
        OutputStream os = null;
        try {
            fc = (FileConnection)Connector.open( deltaFile, Connector.READ_WRITE );

            if ( !fc.exists() ) {
                fc.create();
            } else if ( aOverride ) {
                fc.delete();
                fc.create();
            }
            os = fc.openOutputStream( fc.fileSize() );

            Enumeration en = aMediaChanged.elements();
            while ( en.hasMoreElements() ) {
                Object object = en.nextElement();
                if ( object instanceof LibraryElement ) {
                    LibraryElement item = (LibraryElement)object;
                    os.write( item.getId().getBytes( "UTF-8" ) );
                } else {
                    os.write( ((String)object).getBytes( "UTF-8" ) );
                }
                os.write( "\n".getBytes( "UTF-8" ) );
            }
            os.flush();

        } catch ( IOException ioex ) {
        } finally {
            if ( os != null ) {
                try {
                    os.close();
                } catch ( IOException ex ) {
                }
            }
            if ( fc != null ) {
                try {
                    fc.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }

    public void load() {
        FileConnection fc = null;
        InputStream is = null;

        try {

            fc = (FileConnection)Connector.open( mDeltaFile, Connector.READ );
            if ( fc.exists() ) {
                StringBuffer builder = new StringBuffer( (int)fc.fileSize() );
                is = fc.openInputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ( (bytesRead = is.read( buffer )) > 0 ) {
                    builder.append( new String( buffer, 0, bytesRead, "UTF-8" ) );
                }
                mIds = Utils.split( builder.toString(), "\n" );
            } else {
                mIds = new Vector( 0, 0 );
            }
        } catch ( IOException ioex ) {
        } finally {
            if ( is != null ) {
                try {
                    is.close();
                } catch ( IOException ex ) {
                }
            }
            if ( fc != null ) {
                try {
                    fc.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }

    /**
     * @return the mIds
     */
    public Vector getNewItemIds() {
        return mIds;
    }

    public boolean removeItem( String aId ) {
        return mIds.removeElement( aId );
    }
}
