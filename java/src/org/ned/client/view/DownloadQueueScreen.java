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
package org.ned.client.view;

import com.sun.lwuit.Command;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.BackDownloadCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.transfer.DownloadManager;
import org.ned.client.transfer.DownloadTask;
import org.ned.client.view.customComponents.DownloadList;

public class DownloadQueueScreen extends NedFormBase implements ActionListener, SelectionListener, DataChangedListener {

    private final Command mStartCommnad = new Command( NedResources.START );
    private final Command mPauseCommand = new Command( NedResources.PAUSE );
    private final Command mRemoveCommand = new Command( NedResources.REMOVE );
    private final Command mRemoveAllCommand = new Command( NedResources.REMOVEALL );

    private DownloadList mTransfersList;
    private Label mNoDowloadsLabel;
    private Object invoker;

    public DownloadQueueScreen() {
        super();
        setNedTitle( NedResources.MID_DOWNLOADS );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );

        mNoDowloadsLabel = new Label( NedResources.NO_DOWNLOADS );
        mNoDowloadsLabel.setAlignment(CENTER);
        DownloadManager downloadManager = NedMidlet.getInstance().getDownloadManager();
        mTransfersList = DownloadList.getDownloadList(downloadManager);
        downloadManager.setObserver(mTransfersList);


        addCommand( BackDownloadCommand.getInstance().getCommand() );
        addCommand(HelpCommand.getInstance().getCommand() );
        if( mTransfersList.size() > 0 ) {
            addComponent( mTransfersList );
            addCommand( mRemoveCommand );
            addCommand( mRemoveAllCommand );
        } else {
            addComponent( mNoDowloadsLabel );
        }

        addCommandListener( this );
        mTransfersList.addActionListener( this );
        mTransfersList.addSelectionListener( this );
        mTransfersList.getModel().addDataChangedListener( this );
    }

    public DownloadQueueScreen(Object invoker)
    {
        this();
        this.invoker = invoker;
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if ( src == BackDownloadCommand.getInstance().getCommand() ) {
            NedMidlet.getInstance().getDownloadManager().setObserver(null);
            BackDownloadCommand.getInstance().execute(invoker);
        } else if ( src == mRemoveCommand ) {
           if ( mTransfersList.getSelectedIndex() >= 0 ) {
                if (GeneralAlert.showQuestion(NedResources.TRA_REMOVE_DOWNLOAD_DIALOG) == GeneralAlert.RESULT_YES) {
                    DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( mTransfersList.getSelectedIndex() );
                    if( tr != null ) {
                        tr.CancelAndRemove();
                        StatisticsManager.logEvent( StatType.DOWNLOAD_REMOVE, "Url=" + tr.getUrlPath()
                                                                            + "Progress=" + tr.getPercentDownloaded() + ";");
                    }
                }
            }
        } else if ( src == mRemoveAllCommand ) {
            if (GeneralAlert.showQuestion(NedResources.REMOVEALL_DOWNLOAD_DIALOG) == GeneralAlert.RESULT_YES) {
                int size = mTransfersList.getModel().getSize();
                for( int i = size - 1; i >= 0; i-- ) {
                    DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( i );
                    if( tr != null ) {
                        tr.CancelAndRemove();
                        StatisticsManager.logEvent( StatType.DOWNLOAD_REMOVE, "Url=" + tr.getUrlPath()
                                                                        + "Progress=" + tr.getPercentDownloaded() + ";");
                    }
                }
            }
        } else if ( src == mStartCommnad ) {
           if ( mTransfersList.getSelectedIndex() >= 0 ) {
                DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( mTransfersList.getSelectedIndex() );
                if( tr != null ) {
                    if( tr.startDownload() ) {
                        StatisticsManager.logEvent(StatType.DOWNLOAD_START, "Url=" + tr.getUrlPath()
                                + "Progress=" + tr.getPercentDownloaded() + ";");
                    } else {
                        GeneralAlert.show(NedResources.TOO_MANY_DOWNLOADS, GeneralAlert.INFO);
                    }
                }
            }
        } else if ( src == mPauseCommand ) {
            if ( mTransfersList.getSelectedIndex() >= 0 ) {
                DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( mTransfersList.getSelectedIndex() );
                if( tr != null ) {
                    tr.stopDownload();
                    StatisticsManager.logEvent( StatType.DOWNLOAD_END, "Url=" + tr.getUrlPath()
                                                                      + "Progress=" + tr.getPercentDownloaded() + ";" );
                }
            }
        } else if ( src instanceof List ) {
            if ( mTransfersList.getSelectedIndex() >= 0 ) {
                DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( mTransfersList.getSelectedIndex() );
                if( tr != null && tr.isDownloading() ) {
                    tr.stopDownload();
                    StatisticsManager.logEvent( StatType.DOWNLOAD_END, "Url=" + tr.getUrlPath()
                                                                     + "Progress=" + tr.getPercentDownloaded() + ";" );
                } else {
                    if( tr.startDownload() ) {
                        StatisticsManager.logEvent(StatType.DOWNLOAD_START, "Url=" + tr.getUrlPath()
                                + "Progress=" + tr.getPercentDownloaded() + ";");
                    } else {
                        GeneralAlert.show(NedResources.TOO_MANY_DOWNLOADS, GeneralAlert.INFO);
                    }
                }
            }
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }

    public void selectionChanged( int i, int i1 ) {
        removeAllCommands();
        addCommand( BackDownloadCommand.getInstance().getCommand() );
        addCommand(HelpCommand.getInstance().getCommand() );
        if ( mTransfersList.size() > 0 && mTransfersList.getSelectedIndex() >= 0 ) {
            DownloadTask tr = (DownloadTask)mTransfersList.getModel().getItemAt( mTransfersList.getSelectedIndex() );
            if( tr != null && tr.isDownloading() ) {
                addCommand( mRemoveCommand );
                addCommand( mRemoveAllCommand );
                addCommand( mPauseCommand );
            } else {
                addCommand( mRemoveCommand );
                addCommand( mRemoveAllCommand );
                addCommand( mStartCommnad );
            }
        } else if ( mTransfersList.size() == 0 ) {
            removeComponent( mTransfersList );
            if( !contains( mNoDowloadsLabel ) ) {
                addComponent( mNoDowloadsLabel );
            }
        }
    }

    public void dataChanged(int i, int i1) {
        selectionChanged(i, i1);//to refresh menu action list
    }
}

