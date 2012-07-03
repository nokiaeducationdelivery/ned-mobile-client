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
package org.ned.client.transfer;

import com.sun.lwuit.Display;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.NedMidlet;
import org.ned.client.library.NedLibrary;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.ContentNotExistException;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.UnauthorizedLibraryUsageException;

public class DownloadManager implements IDownloadTaskManager {

    public static final int MAX_DOWNLOADS = 2;
    private NedMidlet midlet = null;
    private Vector vectorDownloadTasks = null;
    private IMediaItemListUpdater mediaListUpdater = null;
    private IDownloadListUpdater downloadListUpdater = null;

    public void setMediaListUpdater( IMediaItemListUpdater mediaListUpdater ) {
        this.mediaListUpdater = mediaListUpdater;
    }

    public DownloadManager( NedMidlet _midlet ) {
        midlet = _midlet;
        vectorDownloadTasks = new Vector();
        midlet.getXmlManager().readDownloads( this );

    }

    public Vector getMainDownloadQueue() {
        return vectorDownloadTasks;
    }

    public boolean downloadsExist() {
        if ( vectorDownloadTasks.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTransferExist( String localPath ) {
        DownloadTask tf = null;
        boolean exists = false;

        for ( int i = 0; i < vectorDownloadTasks.size(); i++ ) {
            tf = (DownloadTask) vectorDownloadTasks.elementAt( i );
            if ( tf.getFile().equals( localPath ) ) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public DownloadTask getTransfer( String localFile ) {
        DownloadTask tf = null;
        DownloadTask ret = null;

        for ( int i = 0; i < vectorDownloadTasks.size(); i++ ) {
            tf = (DownloadTask) vectorDownloadTasks.elementAt( i );
            if ( tf.getFile().equals( localFile ) ) {
                ret = tf;
                break;
            }
        }
        return ret;
    }

    public void removeFromQueue( DownloadTask tf ) {
        midlet.getXmlManager().removeDownloadsEntry( tf.getFile() );

        vectorDownloadTasks.removeElement( tf );

        System.out.println( "Download Queue Size: " + vectorDownloadTasks.size() );

        if ( NedMidlet.getSettingsManager().getDlAutomatic() ) {
            startDownloads();
        }
    }

    private void startInstantDownloads() {
        DownloadTask tf;
        for ( int i = 0; i < vectorDownloadTasks.size(); i++ ) {
            if ( countActiveDownload() >= MAX_DOWNLOADS ) {
                return;
            }
            tf = (DownloadTask) vectorDownloadTasks.elementAt( i );
            if ( !tf.isDownloading() && tf.getInstantDownload() ) {
                tf.startDownload();
            }
        }
    }

    public void startDownloads() {
        DownloadTask tf;
        for ( int i = 0; i < vectorDownloadTasks.size(); i++ ) {
            if ( countActiveDownload() >= MAX_DOWNLOADS ) {
                return;
            }
            tf = (DownloadTask) vectorDownloadTasks.elementAt( i );
            if ( !tf.isDownloading() ) {
                tf.startDownload();
            }
        }
    }

    public int countActiveDownload() {
        int activeDownloads = 0;
        for ( int i = 0; i < vectorDownloadTasks.size(); i++ ) {
            if ( ((DownloadTask) vectorDownloadTasks.elementAt( i )).isDownloading() ) {
                activeDownloads++;
            }
        }
        return activeDownloads;
    }

    public void addDownloadToQueue( String file, String url, String title, boolean instantDownload ) {
        if ( file == null ) {
            //todo add message
            return;
        }
        if ( isTransferExist( file ) ) {
            if ( instantDownload && !getTransfer( file ).isDownloading() ) {
                getTransfer( file ).startDownload();
            }
            return;
        }
        DownloadTask tf = new DownloadTask( this, file, url, title );

        tf.setInstantDownload( instantDownload );

        midlet.getXmlManager().addDownloadsEntry( tf );

        if ( midlet.getDownloadState() == NedMidlet.DOWNLOAD_AUTOMATIC || instantDownload ) {
            tf.startDownload();
        }
    }

    public boolean getViaServlet( String url, NedLibrary library ) throws SecurityException, UnauthorizedLibraryUsageException, ContentNotExistException {
        HttpConnection hc = null;
        InputStream is = null;
        FileConnection fc = null;
        DataOutputStream dos = null;

        boolean downloaded = false;

        try {

            if ( !NedIOUtils.fileExists( library.getDirUri() ) ) {
                NedIOUtils.createDirectory( library.getDirUri() );
            }
            if ( !NedIOUtils.fileExists( library.getDirUri() + "/" + NedLocalConst.VIDEOSDIR ) ) {
                NedIOUtils.createDirectory( library.getDirUri() + "/" + NedLocalConst.VIDEOSDIR );
            }

            hc = (HttpConnection) Connector.open( url );
            hc.setRequestMethod( HttpConnection.GET );
            hc.setRequestProperty( "id", library.getId() );
            NedConnectionUtils.addCredentialsToConnection( hc,
                                                           NedMidlet.getAccountManager().getCurrentUser().login,
                                                           NedMidlet.getAccountManager().getCurrentUser().password );
            is = hc.openInputStream();

            if ( hc.getResponseCode() == HttpConnection.HTTP_OK ) {
                String version = hc.getHeaderField( "Version" );

                fc = (FileConnection) Connector.open( library.getFileUri(), Connector.READ_WRITE );
                if ( !fc.exists() ) {
                    fc.create();
                } else {
                    fc.truncate( 0 );
                    //check to do partial download or
                    //check to write over file with yes/no dialog
                }
                dos = fc.openDataOutputStream();

                int bytesread = 0;
                byte[] databyte = new byte[NedConnectionUtils.MTU];

                try {
                    Thread.sleep( 500 );
                } catch ( Exception ex ) {
                }

                while ( (bytesread = is.read( databyte, 0, NedConnectionUtils.MTU )) != -1 ) {
                    dos.write( databyte, 0, bytesread );
                }
                if ( bytesread == -1 ) {
                    downloaded = true;
                    library.setVersion( version );
                }
            } else if ( hc.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED ) {
                throw new UnauthorizedLibraryUsageException();
            } else if ( hc.getResponseCode() == HttpConnection.HTTP_NO_CONTENT ) {
                throw new ContentNotExistException();
            }
        } catch ( IOException ioe ) {
        } finally {
            try {
                if ( dos != null ) {
                    dos.close();
                }
                if ( fc != null ) {
                    fc.close();
                }
                if ( is != null ) {
                    is.close();
                }
                if ( hc != null ) {
                    hc.close();
                }
            } catch ( IOException ioe ) {
                ioe.printStackTrace();
            }
        }
        return downloaded;
    }

    public void taskCompleted( DownloadTask transfer ) {
        NedMidlet.getInstance().getXmlManager().removeDownloadsEntry( transfer.getFile() );

        vectorDownloadTasks.removeElement( transfer );
        if ( mediaListUpdater != null ) {
            mediaListUpdater.updateMediaList();
        }
        if ( downloadListUpdater != null ) {
            downloadListUpdater.sourceDestroyed( transfer );
        }
        Display.getInstance().getCurrent().repaint();
        StatisticsManager.logEvent( StatType.DOWNLOAD_COMPLETED, "Url=" + transfer.getUrlPath()
                                                                 + "Progress=" + transfer.getPercentDownloaded()
                                                                 + ";Status=" + transfer.getStatus() + ";" );
        if ( midlet.getSettingsManager().getDlAutomatic() ) {
            startDownloads();
        } else {
            startInstantDownloads();
        }
    }

    public void addNewDownloadTask( DownloadTask newTask ) {
        vectorDownloadTasks.addElement( newTask );
    }

    public void progresUpdate( DownloadTask source ) {
        if ( downloadListUpdater != null ) {
            downloadListUpdater.dataChanged( source );
        }
    }

    public void setObserver( IDownloadListUpdater downloadListUpdater ) {
        this.downloadListUpdater = downloadListUpdater;
    }

    public void statusChanged( DownloadTask source ) {
        if ( downloadListUpdater != null ) {
            downloadListUpdater.dataChanged( source );
        }
    }

    public synchronized void taskCancelled( DownloadTask taskCancelled ) {
        NedMidlet.getInstance().getXmlManager().removeDownloadsEntry( taskCancelled.getFile() );

        vectorDownloadTasks.removeElement( taskCancelled );
        if ( downloadListUpdater != null ) {
            downloadListUpdater.sourceDestroyed( taskCancelled );
        }
        Display.getInstance().getCurrent().repaint();
        if ( midlet.getSettingsManager().getDlAutomatic() ) {
            startDownloads();
        } else {
            startInstantDownloads();
        }
    }
}
