package org.ned.client.transfer;


public interface IDownloadListUpdater {
    public void dataChanged( DownloadTask aSource );
    public void sourceDestroyed( DownloadTask aSource );
}