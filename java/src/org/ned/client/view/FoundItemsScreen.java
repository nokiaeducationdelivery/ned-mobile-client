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
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.Content;
import org.ned.client.Localization;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.XmlManager;
import org.ned.client.command.BackGenericCommand;
import org.ned.client.command.AddToDownloadQueueCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.InstantDownloadCommand;
import org.ned.client.command.PlayMediaCommand;
import org.ned.client.command.SearchDialogCommand;
import org.ned.client.command.ShowDetailsCommand;
import org.ned.client.contentdata.NedListModel;

public class FoundItemsScreen extends NedFormBase implements ActionListener, SelectionListener {

    private List itemsList;

    public FoundItemsScreen(String keyWord, String contentId) {

        setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        currentElement = XmlManager.getContentData(contentId);
        setNedTitle(currentElement.getText());

        listModel = new NedListModel();
        listModel.loadFilteredAllChilds(contentId, keyWord);

        if (listModel.getSize() > 0) {
            itemsList = new List(listModel);
            itemsList.setSelectedIndex(0);
            itemsList.setListCellRenderer(new MediaItemsListCellRenderer());
            itemsList.setPreferredW(Display.getInstance().getDisplayWidth());
            itemsList.addSelectionListener(this);
            addComponent(itemsList);
            addGameKeyListener(Display.GAME_FIRE, this);
            itemsList.addActionListener(this);
        } else {
            noItemsFound();
        }
        addCommand(BackGenericCommand.getInstance().getCommand());
        addCommand(SearchDialogCommand.getInstance().getCommand());
        addGameKeyListener(Display.GAME_LEFT, this);
        addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if (src == BackGenericCommand.getInstance().getCommand()
                || evt.getKeyEvent() == Display.GAME_LEFT) {
            BackGenericCommand.getInstance().execute(currentElement.getParentId());
        } else if (src == ShowDetailsCommand.getInstance().getCommand()) {
            ShowDetailsCommand.getInstance().execute(itemsList.getSelectedItem());
        } else if (src == DeleteContentCommand.getInstance().getCommand()) {
            DeleteContentCommand.getInstance().execute(itemsList);
        } else if (src == InstantDownloadCommand.getInstance().getCommand()) {
            InstantDownloadCommand.getInstance().execute(itemsList.getSelectedItem());
        } else if (src instanceof List && listModel.getSize() > 0 ) {
            Content content = (Content) itemsList.getSelectedItem();
            if (content.isDownloaded()) {
                PlayMediaCommand.getInstance().execute(content);
            } else {
                AddToDownloadQueueCommand.getInstance().execute(content);
                String state = NedResources.MID_MANUAL;
                if (NedMidlet.getInstance().getDownloadState() == NedMidlet.DOWNLOAD_AUTOMATIC) {
                    state = NedResources.MID_AUTOMATIC;
                }
                Object[] params = {content.getText(), state};
                String s = Localization.getMessage(NedResources.MID_ADDED_DOWNLOAD_MESSAGE, params);
                GeneralAlert.show( s, GeneralAlert.INFO );
            }
        } else if(src == SearchDialogCommand.getInstance().getCommand() ) {
            SearchDialogCommand.getInstance().execute( currentElement.getId() );
        } else if ( src == AddToDownloadQueueCommand.getInstance().getCommand() ) {
            Content content = (Content) itemsList.getSelectedItem();
            AddToDownloadQueueCommand.getInstance().execute( content );
        }
        else if (src == showFreeMem) {
            //GeneralAlert.show(String.valueOf(Runtime.getRuntime().freeMemory()));
        }
    }

    public void selectionChanged(int oldSel, int newSel) {
        Content content = (Content) itemsList.getModel().getItemAt(newSel);
        removeAllCommands();
        addCommand(BackGenericCommand.getInstance().getCommand());

        if (content != null && content.isDownloaded()) {
            addCommand(ShowDetailsCommand.getInstance().getCommand());
            addCommand(DeleteContentCommand.getInstance().getCommand());
            addCommand(PlayMediaCommand.getInstance().getCommand());
        } else {
            addCommand(ShowDetailsCommand.getInstance().getCommand());
            addCommand(AddToDownloadQueueCommand.getInstance().getCommand());
            addCommand(InstantDownloadCommand.getInstance().getCommand());
        }
    }

    private void noItemsFound() {
        Label noItems = new Label( NedResources.NO_ITEMS_FOUND );
        noItems.setAlignment(CENTER);
        addComponent(noItems);
    }
}
