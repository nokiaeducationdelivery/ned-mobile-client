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
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.BackDownloadOptionsCommand;
import org.ned.client.command.BackStatisticsOptionsCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.view.customComponents.RadioButton;

public class ShowTipsConfigView extends NedFormBase implements ActionListener {

    private RadioButton mEnableRB;
    private RadioButton mDisableRB;

    public ShowTipsConfigView() {
        super();
        setNedTitle( NedResources.TIPS_TRICKS );

        addComponent( createGeneral() );
        addCommand( BackDownloadOptionsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    private Container createGeneral() {
        Container general = new Container();
        general.setLayout( new BoxLayout( BoxLayout.Y_AXIS ) );
        Label sortByLabel = new Label( NedResources.TIPS_ON_STARTUP );
        mEnableRB = new RadioButton( NedResources.MID_ON_SETTINGS );
        mEnableRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mEnableRB.setTickerEnabled( false );
        mEnableRB.addActionListener( this );
        mDisableRB = new RadioButton( NedResources.MID_OFF_SETTINGS );
        mDisableRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mDisableRB.setTickerEnabled( false );
        mDisableRB.addActionListener( this );
        ButtonGroup group = new ButtonGroup();
        group.add( mEnableRB );
        group.add( mDisableRB );

        if ( NedMidlet.getSettingsManager().getShowTips() ) {
            group.setSelected( mEnableRB );
        } else {
            group.setSelected( mDisableRB );
        }

        general.addComponent( sortByLabel );
        general.addComponent( mEnableRB );
        general.addComponent( mDisableRB );

        return general;
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == BackDownloadOptionsCommand.getInstance().getCommand() ) {
            BackStatisticsOptionsCommand.getInstance().execute( null );
        } else if ( src == mEnableRB ) {
            NedMidlet.getSettingsManager().setShowTips( true );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == mDisableRB ) {
            NedMidlet.getSettingsManager().setShowTips( false );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }
}
