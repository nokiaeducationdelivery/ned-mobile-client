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
package org.ned.client.command;

import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import org.ned.client.IContent;
import org.ned.client.NedResources;
import org.ned.client.statistics.StatType;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.GeneralAlert;

/**
 *
 * @author damian.janicki
 */
public class DeleteHistoryFileCommand extends NedCommand{

    private static DeleteHistoryFileCommand instance;

    private DeleteHistoryFileCommand(){
        command = new Command( NedResources.DELETE );
    }

    public static DeleteHistoryFileCommand getInstance(){
        if(instance == null){
            instance = new DeleteHistoryFileCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( NedResources.QUESTION_REMOVE_CONTENT ) ) {
            List uiList = (List)param;

            IContent content = (IContent)uiList.getSelectedItem();
            if(content != null){
                NedIOUtils.removeFile(content.getMediaFile());

                uiList.getModel().removeItem(uiList.getSelectedIndex());
                uiList.getModel().setSelectedIndex(0);
                uiList.repaint();
                StatisticsManager.logEvent( StatType.DELETE_HISTORY_ITEM, "Id=" + content.getMediaFile() + ";" );
            }
        }
    }
}
