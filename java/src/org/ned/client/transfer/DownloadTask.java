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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import org.ned.client.MotdManager;
import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.NedIOUtils;

public class DownloadTask implements Runnable {

    private IDownloadTaskManager downloadTaskManager;
    private Thread mTransferThread = null;
    private boolean stopped = false;
    private String title = null;
    private String filename = null;
    private String urlPath = null;
    private long bytesRead = 0;
    private long totalBytesDownloaded = 0;
    private float percentDownloaded = 0;
    private long downloadLength = 0;
    private boolean instantDownload = false;
    private final int counterUpdateViewInterval = 100000 / NedConnectionUtils.MTU;
    private int counterUpdateView = counterUpdateViewInterval;
    private String status = NedResources.TRA_WAITING_STATUS;
    private String percentDownloadItemLabel = "0";
    private InputStream activeConnection;

    public DownloadTask( IDownloadTaskManager downloadTaskManager, String filename, String url, String title ) {
        this.downloadTaskManager = downloadTaskManager;
        downloadTaskManager.addNewDownloadTask( this );
        this.filename = filename;
        this.urlPath = url;
        this.title = title;
    }

    public boolean isDownloading() {
        return (mTransferThread != null && mTransferThread.isAlive());
    }

    public boolean getInstantDownload() {
        return instantDownload;
    }

    public void setInstantDownload( boolean _instantDownload ) {
        instantDownload = _instantDownload;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return filename;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setStatus( String _status ) {
        status = _status;
        downloadTaskManager.statusChanged( this );
    }

    public String getStatus() {
        return status;
    }

    public void setBytesRead( long _bytesRead ) {
        bytesRead = _bytesRead;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getTotalBytesDownloaded() {
        return totalBytesDownloaded;
    }

    public void addTotalBytesDownloaded( long morebytes ) {
        totalBytesDownloaded += morebytes;
    }

    public void setDownloadLength( long _downloadLength ) {
        downloadLength = _downloadLength;
    }

    public void setPercentDownloaded( String initValue ) {
        percentDownloadItemLabel = initValue;
    }

    public String getPercentDownloaded() {
        return percentDownloadItemLabel;
    }

    public int getPercentDownloadedInt() {
        float percent = ((float) totalBytesDownloaded / (float) downloadLength) * 100;
        return downloadLength != 0 ? (int) percent : 0;
    }

    public void setPercentDownloaded() {
        if ( counterUpdateView < 0 || totalBytesDownloaded == downloadLength ) {
            counterUpdateView = counterUpdateViewInterval;
            percentDownloaded = ((float) totalBytesDownloaded / (float) downloadLength) * 100;
            int percent = (int) percentDownloaded;
            int percentDot = (int) (percentDownloaded * 100) - 100 * percent;
            String afterDot = String.valueOf( percentDot );
            if ( percentDot < 10 ) {
                afterDot = "0" + afterDot;
            }
            percentDownloadItemLabel = String.valueOf( percent ) + "." + afterDot;
            downloadTaskManager.progresUpdate( this );
        } else {
            counterUpdateView--;
        }
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void run() {
        try {
            download();
            if ( NedMidlet.getSettingsManager().getAutoStatSend() ) {
                StatisticsManager.uploadStats( true );
            }
            MotdManager.getInstance().updateMotd();
        } catch ( IOException ex ) {
            ex.printStackTrace();
        } finally {
            if ( status.equals( NedResources.TRA_COMPLETED_STATUS ) ) {
                if ( downloadTaskManager != null ) {
                    downloadTaskManager.taskCompleted( this );
                    downloadTaskManager = null;
                }
            } else if ( status.equals( NedResources.TRA_CANCELLING_STATUS ) ) {
                removeFile();
            }
        }
    }

    private void download() throws IOException {
        HttpConnection hc = null;
        InputStream ic = null;
        FileConnection fc = null;
        OutputStream oc = null;

        long offset = 0;

        try {
            setStatus( NedResources.TRA_CHECKING_STATUS );

            if ( NedIOUtils.fileExists( filename ) ) {
                setStatus( NedResources.TRA_COMPLETED_STATUS );
                return;
            }

            fc = (FileConnection) Connector.open( filename + NedLocalConst.TMP, Connector.READ_WRITE );
            if ( fc.exists() ) {
                offset = fc.fileSize();
            } else {
                fc.create();
            }

            setStatus( NedResources.TRA_CONNECTING_STATUS );

            hc = (HttpConnection) Connector.open( urlPath );
            hc.setRequestMethod( HttpConnection.GET );
            hc.setRequestProperty( "Range", "bytes=" + String.valueOf( offset ) + "-" );

            ic = hc.openDataInputStream();
            activeConnection = ic;

            int responseCode = hc.getResponseCode();

            if ( responseCode == HttpConnection.HTTP_PARTIAL
                 || responseCode == HttpConnection.HTTP_OK ) {

                setStatus( NedResources.TRA_CONNECTED_STATUS );

                long length = hc.getLength();
                setBytesRead( 0 );
                totalBytesDownloaded = offset;

                setDownloadLength( length + offset );  //set length of download

                byte[] databyte = new byte[NedConnectionUtils.MTU];

                oc = fc.openOutputStream( fc.fileSize() );

                while ( true ) {
                    if ( stopped ) {
                        break;
                    }
                    setBytesRead( ic.read( databyte, 0, NedConnectionUtils.MTU ) );
                    if ( bytesRead == -1 ) {
                        break;//transfer completed - end of file reached
                    }
                    addTotalBytesDownloaded( bytesRead );
                    setPercentDownloaded();
                    oc.write( databyte, 0, (int) bytesRead );
                }
            } else {
                if ( fc != null && fc.exists() ) {
                    fc.delete();
                }
            }

            activeConnection = null;

            if ( stopped ) {
                NedMidlet.getInstance().getXmlManager().setProgress( filename, percentDownloaded, downloadLength );
            } else {
                setStatus( NedResources.TRA_COMPLETED_STATUS );
                String name = filename.substring( filename.lastIndexOf( '/' ) + 1 );
                fc.rename( name );
            }
        } catch ( ConnectionNotFoundException cnex ) {
            //TODO - add message
        } catch ( IOException ioe ) {
        } catch ( IllegalArgumentException iex ) {
            //TODO - add message
            //NedMidlet.getInstance().showMessageDialog();
        } catch ( SecurityException sex ) {
            //TODO - add message
        } finally {
            if ( ic != null ) {
                ic.close();
            }
            if ( hc != null ) {
                hc.close();
            }
            if ( oc != null ) {
                oc.close();
            }
            if ( fc != null ) {
                fc.close();
            }
        }
    }

    public boolean startDownload() {
        boolean result = false;
        if ( NedMidlet.getInstance().getDownloadManager().countActiveDownload() < DownloadManager.MAX_DOWNLOADS ) {
            mTransferThread = new Thread( this );
            mTransferThread.setPriority( Thread.MIN_PRIORITY );
            stopped = false;
            mTransferThread.start();
            result = true;
        }
        return result;
    }

    public void stopDownload() {
        status = NedResources.TRA_WAITING_STATUS;
        stopped = true;
    }

    public void CancelAndRemove() {

        if ( activeConnection != null ) {
            try {
                activeConnection.close();
                activeConnection = null;
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        }

        setStatus( NedResources.TRA_CANCELLING_STATUS );
        stopped = true;

        if ( mTransferThread == null || !mTransferThread.isAlive() ) {
            removeFile();
        }
    }

    private void removeFile() {
        if ( downloadTaskManager != null ) {
            downloadTaskManager.taskCancelled( this );
            downloadTaskManager = null;
        }
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open( filename + NedLocalConst.TMP, Connector.READ_WRITE );
            if ( fc.exists() ) {
                fc.delete();
            }
        } catch ( IOException ex ) {
        } finally {
            if ( fc != null ) {
                try {
                    fc.close();
                } catch ( Exception ex ) {
                }
            }
        }
    }
}
