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

import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.Content;
import org.ned.client.NedResources;
import org.ned.client.command.BackToMainScreenCommand;
import org.ned.client.command.DeleteContentCommand;
import org.ned.client.command.DeleteHistoryFileCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.PlayMediaCommand;
import org.ned.client.utils.NedHistoryContent;
import org.ned.client.view.customComponents.AnimatedList;
import org.ned.client.view.renderer.MediaItemsListCellRenderer;

/**
 *
 * @author damian.janicki
 */
public class HistoryScreen extends NedFormBase implements ActionListener, SelectionListener{

    private List mHistoryList = null;
    private Label mNoHistoryLabel;

    public HistoryScreen(){
        super();

        setNedTitle( NedResources.HISTORY );
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        mNoHistoryLabel = new Label( NedResources.NO_HISTORY );
        mNoHistoryLabel.setAlignment(CENTER);
        prepareHistoryList();

        if( mHistoryList.size() > 0 ) {
            addComponent( mHistoryList );
        } else {
            addComponent( mNoHistoryLabel );
        }
        addCommand(BackToMainScreenCommand.getInstance().getCommand());
        addCommand(HelpCommand.getInstance().getCommand() );
        addCommand(DeleteContentCommand.getInstance().getCommand());
        addCommand(PlayMediaCommand.getInstance().getCommand());
        
        addGameKeyListener(Display.GAME_FIRE, this);
        addCommandListener(this);
    }

    private void prepareHistoryList(){
        mHistoryList = new AnimatedList(NedHistoryContent.getHistoryContent());
        mHistoryList.setSelectedIndex(0);
//        mHistoryList.setListCellRenderer(new MediaItemsListCellRenderer());
        mHistoryList.setPreferredW(Display.getInstance().getDisplayWidth());
        mHistoryList.addSelectionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();

        if (src == BackToMainScreenCommand.getInstance().getCommand()) {
            BackToMainScreenCommand.getInstance().execute(null);
        } else if(src == DeleteHistoryFileCommand.getInstance().getCommand()) {
            DeleteHistoryFileCommand.getInstance().execute(mHistoryList);
        } else if ( src == PlayMediaCommand.getInstance().getCommand()) {
            Content content = (Content) mHistoryList.getSelectedItem();
            PlayMediaCommand.getInstance().execute(content);
        } else if ( ( src instanceof List || ae.getKeyEvent() == Display.GAME_FIRE )
                    && mHistoryList.size() > 0 ) {
            OldMediaItemContextMenu menu = null;
            menu = new OldMediaItemContextMenu( mHistoryList );
            menu.show();
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }

    public void selectionChanged(int oldSel, int newSel) {
        removeAllCommands();
        addCommand(BackToMainScreenCommand.getInstance().getCommand());
        addCommand(HelpCommand.getInstance().getCommand() );
        if ( mHistoryList.size() > 0 && mHistoryList.getSelectedIndex() >= 0 ) {
                addCommand(DeleteHistoryFileCommand.getInstance().getCommand());
                addCommand(PlayMediaCommand.getInstance().getCommand());
        } else if ( mHistoryList.size() == 0 ) {
            removeComponent( mHistoryList );
            if( !contains( mNoHistoryLabel ) ) {
                addComponent( mNoHistoryLabel );
            }
        }
    }
}
