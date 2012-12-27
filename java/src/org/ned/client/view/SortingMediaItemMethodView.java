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
package org.ned.client.view;

import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.BackDownloadOptionsCommand;
import org.ned.client.command.BackStatisticsOptionsCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.view.customComponents.RadioButton;

public class SortingMediaItemMethodView extends NedFormBase implements ActionListener {

    private RadioButton mAsDefinedRB;
    private RadioButton mByNameRB;
    private RadioButton mByTypeRB;
    private RadioButton mByNameAndType;

    public SortingMediaItemMethodView() {
        super();
        setNedTitle( NedResources.SORTING_OPTION );

        addComponent( createGeneral() );
        addCommand( BackDownloadOptionsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    private Container createGeneral() {
        Container general = new Container();
        general.setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        Label sortByLabel = new Label( NedResources.SORT_BY );
        mAsDefinedRB = new RadioButton( NedResources.SORT_NONE );
        mAsDefinedRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mAsDefinedRB.setTickerEnabled( false );
        mAsDefinedRB.addActionListener( this );
        mByNameRB = new RadioButton( NedResources.SORY_BY_NAME );
        mByNameRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mByNameRB.setTickerEnabled( false );
        mByNameRB.addActionListener( this );
        mByTypeRB = new RadioButton( NedResources.SORT_BY_TYPE );
        mByTypeRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mByTypeRB.setTickerEnabled( false );
        mByTypeRB.addActionListener( this );
        mByNameAndType = new RadioButton( NedResources.SORY_BY_NAME + "&" + NedResources.SORT_BY_TYPE );
        mByNameAndType.setPreferredW( Display.getInstance().getDisplayWidth() );
        mByNameAndType.setTickerEnabled( false );
        mByNameAndType.addActionListener( this );
        ButtonGroup sortingMethodGroup = new ButtonGroup();
        sortingMethodGroup.add( mAsDefinedRB );
        sortingMethodGroup.add( mByNameRB );
        sortingMethodGroup.add( mByTypeRB );
        sortingMethodGroup.add( mByNameAndType );

        switch ( NedMidlet.getSettingsManager().getSortBy() ) {
            case NedConsts.SortOrder.BY_NAME:
                sortingMethodGroup.setSelected( mByNameRB );
                break;
            case NedConsts.SortOrder.BY_TYPE_AND_NAME:
                sortingMethodGroup.setSelected( mByNameAndType );
                break;
            case NedConsts.SortOrder.BY_TYPE:
                sortingMethodGroup.setSelected( mByTypeRB );
            case NedConsts.SortOrder.NONE:
            default:
                sortingMethodGroup.setSelected( NedConsts.SortOrder.NONE );
                break;
        }

        general.addComponent( sortByLabel );
        general.addComponent( mAsDefinedRB );
        general.addComponent( mByNameRB );
        general.addComponent( mByTypeRB );
        general.addComponent( mByNameAndType );

        return general;
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == BackDownloadOptionsCommand.getInstance().getCommand() ) {
            BackStatisticsOptionsCommand.getInstance().execute( null );
        } else if ( src == mAsDefinedRB ) {
            NedMidlet.getSettingsManager().setSortBy( NedConsts.SortOrder.NONE );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == mByNameRB ) {
            NedMidlet.getSettingsManager().setSortBy( NedConsts.SortOrder.BY_NAME );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == mByTypeRB ) {
            NedMidlet.getSettingsManager().setSortBy( NedConsts.SortOrder.BY_TYPE );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == mByNameAndType ) {
            NedMidlet.getSettingsManager().setSortBy( NedConsts.SortOrder.BY_TYPE_AND_NAME );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }
}
