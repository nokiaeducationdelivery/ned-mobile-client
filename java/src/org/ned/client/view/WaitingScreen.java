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

import com.sun.lwuit.Dialog;
import com.sun.lwuit.Label;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.UIManager;
import org.ned.client.NedConsts;

public class WaitingScreen {

    private static WaitingScreen instance;
    private Dialog dialog;

    public static void show( String status ) {
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn( null );
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut( null );
        instance = new WaitingScreen( status );
        instance.showModeless();
    }

    public static boolean isShowed() {
        if ( instance != null ) {
            return instance.dialog.isVisible();
        } else {
            return false;
        }
    }

    public static void dispose() {
        if ( instance != null && instance.dialog != null ) {
            instance.dialog.setVisible( false );
            instance.dialog.dispose();
        }
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn( CommonTransitions.createSlide(
                CommonTransitions.SLIDE_VERTICAL, false, NedConsts.NedTransitions.TRANSITION_TIME ) );
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut( CommonTransitions.createSlide(
                CommonTransitions.SLIDE_VERTICAL, true, NedConsts.NedTransitions.TRANSITION_TIME ) );
    }

    private void showModeless() {
        dialog.showPacked( BorderLayout.CENTER, false );
    }

    private WaitingScreen( String status ) {
        dialog = new Dialog();
        dialog.setFocusable( false );
        dialog.setLayout( new BorderLayout() );

        Label msg = new Label();
        msg.setText( status );

        dialog.addComponent( BorderLayout.CENTER, msg );
    }
}
