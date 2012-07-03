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

import org.ned.client.view.renderer.MediaItemsListCellRenderer;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import org.ned.client.Content;
import org.ned.client.command.BackMediaItemsCommand;
import org.ned.client.command.AddToDownloadQueueCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.DownloadAllMediaItemsScreenCommand;
import org.ned.client.command.GoToStartCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.InstantDownloadCommand;
import org.ned.client.command.PlayMediaCommand;
import org.ned.client.command.SearchDialogCommand;
import org.ned.client.command.ShowDetailsCommand;
import org.ned.client.command.ShowLinksCommand;

public class MediaItemsScreen extends NedFormBase implements ActionListener, SelectionListener {

    private List mMediaList;

    public MediaItemsScreen(String id) {
        super(id);
        setNedTitle(currentElement.getText());

        listModel.sortByType();

        mMediaList = new List(listModel);
        mMediaList.setFixedSelection(List.FIXED_NONE);
        mMediaList.setSelectedIndex(0);
        mMediaList.setListCellRenderer(new MediaItemsListCellRenderer());
        mMediaList.setPreferredW(Display.getInstance().getDisplayWidth());
        mMediaList.addSelectionListener(this);
        addComponent(mMediaList);

        addGameKeyListener(Display.GAME_LEFT, this);
        addGameKeyListener(Display.GAME_FIRE, this);
        mMediaList.addActionListener(this);
        addCommandListener(this);

    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if (src == BackMediaItemsCommand.getInstance().getCommand()
                || evt.getKeyEvent() == Display.GAME_LEFT) {
            BackMediaItemsCommand.getInstance().execute(currentElement.getParentId());
        } else if ( src == ShowDetailsCommand.getInstance().getCommand() ) {
            ShowDetailsCommand.getInstance().execute(mMediaList.getSelectedItem());
        } else if ( src == DeleteContentCommand.getInstance().getCommand() ) {
            DeleteContentCommand.getInstance().execute(mMediaList);
        } else if ( src == InstantDownloadCommand.getInstance().getCommand() ) {
            InstantDownloadCommand.getInstance().execute(mMediaList.getSelectedItem());
            new DownloadQueueScreen(mMediaList.getSelectedItem()).show();
        } else if ( src == GoToStartCommand.getInstance().getCommand() ) {
            GoToStartCommand.getInstance().execute(null);
        } else if ( src == AddToDownloadQueueCommand.getInstance().getCommand() ) {
            Content content = (Content) mMediaList.getSelectedItem();
            AddToDownloadQueueCommand.getInstance().execute(content);
//            String state = NedResources.MID_MANUAL;
//            if (NedMidlet.getInstance().getDownloadState() == NedMidlet.DOWNLOAD_AUTOMATIC) {
//                state = NedResources.MID_AUTOMATIC;
//            }
//            Object[] params = {content.getText(), state};
//            String s = Localization.getMessage(NedResources.MID_ADDED_DOWNLOAD_MESSAGE, params);
//            GeneralAlert.show( s, GeneralAlert.INFO );
            new DownloadQueueScreen(mMediaList.getSelectedItem()).show();
        } else if ( src == PlayMediaCommand.getInstance().getCommand() ) {
            Content content = (Content) mMediaList.getSelectedItem();
            PlayMediaCommand.getInstance().execute(content);
        } else if ( src == DownloadAllMediaItemsScreenCommand.getInstance().getCommand()) {
            DownloadAllMediaItemsScreenCommand.getInstance().execute(listModel);
        } else if ( src == SearchDialogCommand.getInstance().getCommand()) {
            Content content = (Content) mMediaList.getSelectedItem();
            SearchDialogCommand.getInstance().execute(content.getParentId());
        } else if ( src instanceof List) {
            MediaItemContextMenu menu = null;
            if( mMediaList.getSelectedItem() != null ) {
                if ( ((Content)mMediaList.getSelectedItem()).isDownloaded() ){
                    menu = new MediaItemContextMenu( mMediaList , MediaItemContextMenu.PLAY );
                } else {
                    menu = new MediaItemContextMenu( mMediaList , MediaItemContextMenu.DOWNLOAD );
                }
                menu.show();
            }
        } else if ( src == ShowLinksCommand.getInstance().getCommand()) {
            Content content = (Content) mMediaList.getSelectedItem();
            ShowLinksCommand.getInstance().execute(content);
        } else if ( src == showFreeMem) {
            GeneralAlert.show(String.valueOf(Runtime.getRuntime().freeMemory()), GeneralAlert.INFO );
        }  else if ( src == HelpCommand.getInstance().getCommand() ) {
            Object[] params = {this.getClass(), currentElement.getId() };
            HelpCommand.getInstance().execute( params );
        }
    }

    public void selectionChanged(int oldSel, int newSel) {
        Content content = (Content) mMediaList.getModel().getItemAt(newSel);
        removeAllCommands();
        addCommand(BackMediaItemsCommand.getInstance().getCommand());
        addCommand( HelpCommand.getInstance().getCommand() );

        if( content!= null) {
            addCommand(DownloadAllMediaItemsScreenCommand.getInstance().getCommand());
            addCommand(SearchDialogCommand.getInstance().getCommand());
            if (content != null && content.isDownloaded()) {
                addCommand(DeleteContentCommand.getInstance().getCommand());
                addCommand(ShowLinksCommand.getInstance().getCommand());
                addCommand(ShowDetailsCommand.getInstance().getCommand());
                addCommand(PlayMediaCommand.getInstance().getCommand());
            } else {
                addCommand(DeleteContentCommand.getInstance().getCommand());
                addCommand(AddToDownloadQueueCommand.getInstance().getCommand());
                addCommand(InstantDownloadCommand.getInstance().getCommand());
                addCommand(ShowLinksCommand.getInstance().getCommand());
                addCommand(ShowDetailsCommand.getInstance().getCommand());
            }
        }
        addCommand(GoToStartCommand.getInstance().getCommand());
    }
}
