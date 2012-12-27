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

import com.sun.lwuit.Command;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.customComponents.CheckBox;

public class ShowTipsView extends NedFormBase implements ActionListener {

    private final Command showOnStartupCommand = new Command( NedResources.TIPS_ON_STARTUP );
    private final Command nextTip = new Command( NedResources.NEXT );
    private final Command hide = new Command( NedResources.HIDE );
    private CheckBox showOnStartupCheckBox;
    private TextArea tipsText;


    public ShowTipsView() {
        super();
        setLayout( new BorderLayout() );
        setScrollable( false );
        setNedTitle( NedResources.TIPS_TRICKS );
        addTipsTextComponent();

        showOnStartupCheckBox = new CheckBox( NedResources.TIPS_ON_STARTUP );
        showOnStartupCheckBox.setCommand( showOnStartupCommand );
        showOnStartupCheckBox.setFocusable( true );
        showOnStartupCheckBox.setSelected( NedMidlet.getSettingsManager().getShowTips() );

        addCommand( nextTip );
        addCommand( hide );
        addComponent( BorderLayout.SOUTH, showOnStartupCheckBox );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();

        if ( src == showOnStartupCommand ) {
            NedMidlet.getSettingsManager().setShowTips( !NedMidlet.getSettingsManager().getShowTips() );
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == nextTip ) {
            tipsText.setText( NedResources.getInstance().getTip() );
        } else if ( src == hide ) {
            NedMidlet.getInstance().showFirstView();
        }
    }

    private void addTipsTextComponent() {
        tipsText = new TextArea( NedResources.getInstance().getTip() );
        tipsText.setSelectedStyle( tipsText.getUnselectedStyle() );
        tipsText.setGrowByContent( true );
        tipsText.setScrollVisible( true );
        tipsText.setEditable( false );
        addComponent( BorderLayout.CENTER, tipsText );
    }
}
