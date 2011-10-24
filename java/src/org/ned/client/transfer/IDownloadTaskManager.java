package org.ned.client.transfer;


public interface IDownloadTaskManager {
    public void taskCompleted( DownloadTask taskCompleted );
    public void taskCancelled( DownloadTask taskCancelled);
    public void addNewDownloadTask( DownloadTask taskNew) ;
    public void progresUpdate ( DownloadTask source);
    public void statusChanged( DownloadTask source);
}
