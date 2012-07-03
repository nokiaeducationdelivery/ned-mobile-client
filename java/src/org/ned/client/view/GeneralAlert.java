/*******************************************************************************
* Copyright (c) 2011-2012 Nokia Corporation
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
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.view.renderer.DialogTitlePainter;


public class GeneralAlert {
    private static Dialog messageDialog;
    private static TextArea mMessageTextArea;
    private static boolean isReversed = false;

    private static DialogTitlePainter mDialogTitlePainer = new DialogTitlePainter();

    public static final int RESULT_YES = 1;
    public static final int RESULT_NO = 0;

    public static final int ERROR = 1001;
    public static final int QUESTION = 1002;
    public static final int INFO = 1003;
    public static final int WARNING = 1004;
    public static final int ALARM = 1005;
    public static final int TEXT = 1006;

    private static final int DIALOG_FADE_TIME = 1000;//1s

    private static final Command okCommand = new Command(NedResources.MID_OK_COMMAND);
    private static final Command yesCommand = new Command(NedResources.MID_ANSWER_YES);
    private static final Command noCommand = new Command(NedResources.MID_ANSWER_NO);


    public static void show( String aMessage, int aAlertType ) {
        initDialog( aMessage, aAlertType );
        messageDialog.addCommand( okCommand );

        WaitingScreen.dispose();
        showDialog();
    }

    public static int showQuestion( String message ) {
        initDialog( message, QUESTION );
        messageDialog.addCommand( yesCommand );
        messageDialog.addCommand( noCommand );
        return showDialog() == yesCommand ? GeneralAlert.RESULT_YES : GeneralAlert.RESULT_NO;
    }

    private static Command showDialog() {
        Container cont1 = messageDialog.getContentPane();
        int hi = 0;
        int wi = cont1.getPreferredW() + 2*5;
        int wi2 = messageDialog.getTitleStyle().getFont().stringWidth(  mDialogTitlePainer.getTitle() ) + 2*5 + DialogTitlePainter.TITLE_LEFT_MARGIN;
        wi = Math.max(wi, wi2);

        for( int i = 0 ; i< messageDialog.getComponentCount() ; i ++ ){
            hi += messageDialog.getComponentAt(i).getPreferredH();
        }

        int disH = Display.getInstance().getDisplayHeight();
        int disW = Display.getInstance().getDisplayWidth();

        int H_Margin = hi < disH ? (disH - hi)/2 : 0;
        int V_Margin =  wi < disW ? (disW - wi)/2 : 0;
        Command tmp = messageDialog.show( H_Margin, H_Margin, V_Margin, V_Margin, true);
        messageDialog.dispose();
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons(isReversed);
        return tmp;
    }

    private static void initDialog( String aMessage, int aAlertType ) {
        isReversed = UIManager.getInstance().getLookAndFeel().isReverseSoftButtons();
        UIManager.getInstance().getLookAndFeel().setReverseSoftButtons( false );
        messageDialog = new Dialog( " " );
        messageDialog.getTitleComponent().setPreferredH( mDialogTitlePainer.getFontSize()
                                                       + messageDialog.getTitleStyle().getPadding( Component.TOP )
                                                       + messageDialog.getTitleStyle().getPadding( Component.BOTTOM )
                                                       + messageDialog.getTitleStyle().getMargin( Component.TOP )
                                                       + messageDialog.getTitleStyle().getMargin( Component.BOTTOM ) );
        messageDialog.setScrollableX(false);
        messageDialog.setLayout( new BorderLayout() );
        messageDialog.setTransitionInAnimator( CommonTransitions.createFade( DIALOG_FADE_TIME ) );
        messageDialog.setTransitionOutAnimator( CommonTransitions.createFade( DIALOG_FADE_TIME ) );

        mDialogTitlePainer.setTitle( alertType2String( aAlertType ) );
        messageDialog.getTitleStyle().setBgPainter( mDialogTitlePainer );
        messageDialog.setLayout( new BorderLayout() );

        Container c = new Container( new FlowLayout() );
        Image img = getIcon( aAlertType );
        Label imgLabel= new Label( img );
        imgLabel.setAlignment( Label.LEFT );
        c.addComponent( imgLabel );
        messageDialog.addComponent(BorderLayout.NORTH, c);
        c.setScrollable(false);

        Container c2 = new Container( new BoxLayout( BoxLayout.Y_AXIS ) );

        int displayW = Display.getInstance().getDisplayWidth() - 10; // magic number, it should rather be set to width of margins+borders+padding
        int displayH = Display.getInstance().getDisplayHeight();

        mMessageTextArea = new TextArea( aMessage );
        mMessageTextArea.setSelectedStyle( mMessageTextArea.getUnselectedStyle() );

        int textWidth = mMessageTextArea.getSelectedStyle().getFont().stringWidth( aMessage );
        int lineHeight = mMessageTextArea.getSelectedStyle().getFont().getHeight() + mMessageTextArea.getRowsGap();
        mMessageTextArea.setIsScrollVisible(false);
        mMessageTextArea.setEditable(false);

        if ( textWidth >= displayW )
        {
            mMessageTextArea.setPreferredW( textWidth / ( textWidth/displayW + 1) + 2 );//to fill equally to all lines
            mMessageTextArea.setRows(textWidth / mMessageTextArea.getPreferredW() + 2);
            int preferredH = lineHeight * mMessageTextArea.getRows() + 2;
            if ( preferredH > ( displayH * 0.5 ) ) {
                mMessageTextArea.setIsScrollVisible(true);
                preferredH = (int) ( displayH * 0.5);
            }
            mMessageTextArea.setPreferredH(preferredH);
            mMessageTextArea.setGrowByContent(true);
        }
        else
        {
            mMessageTextArea.setGrowByContent(false);
            mMessageTextArea.setRows(1);
            mMessageTextArea.setPreferredW( textWidth );
            mMessageTextArea.setPreferredH( lineHeight );
        }
        // WARN: the following line is important!
        // Setting it to false caused hard to track OutOfMemoryException
        messageDialog.setScrollable(true);
        c2.addComponent(mMessageTextArea);
        messageDialog.addComponent(BorderLayout.CENTER,c2);

    }

    private static Image getIcon( int aAlertType ) {

        switch ( aAlertType) {
            case ERROR:
                return NedMidlet.getRes().getImage("error");
            case QUESTION:
                return NedMidlet.getRes().getImage("question");
            case WARNING:
                return NedMidlet.getRes().getImage("warning");
            case TEXT:
                return null;
            case INFO:
            default:
                return NedMidlet.getRes().getImage("info");
        }
    }

    private static String alertType2String( int aAlertType ) {

        switch ( aAlertType ) {
            case ERROR:
                return NedResources.ERROR;
            case QUESTION:
                return NedResources.QUESTION;
            case WARNING:
                return NedResources.WARNING;
            case TEXT:
            case INFO:
            default:
                return NedResources.INFO;
        }
    }
}
