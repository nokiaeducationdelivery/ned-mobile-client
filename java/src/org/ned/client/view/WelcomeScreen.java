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

import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.AsyncCompletedCallback;
import org.ned.client.command.CheckServerUrlComand;
import org.ned.client.command.ExitCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.ShowAboutCommand;
import org.ned.client.view.customComponents.ClearTextField;


public class WelcomeScreen extends NedFormBase implements ActionListener, AsyncCompletedCallback  {

    private ClearTextField serverUrlTextArea;
    private final String defaultInput = "http://";

    public WelcomeScreen(){
        setNedTitle( NedResources.SERVER_WIZARD );
        setLayout( new BoxLayout( BoxLayout.Y_AXIS) );

        initForm();

        addCommand(ExitCommand.getInstance().getCommand());
        addCommand(ShowAboutCommand.getInstance().getCommand());
        addCommand(HelpCommand.getInstance().getCommand());
        addCommand(CheckServerUrlComand.getInstance().getCommand());
        addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == ExitCommand.getInstance().getCommand()) {
            ExitCommand.getInstance().execute(null);
        } else if ( src == CheckServerUrlComand.getInstance().getCommand() ) {
            String newAddress = serverUrlTextArea.getText().trim();
            CheckServerUrlComand.getInstance().beginAsync(newAddress, this, true);
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        } else if ( src == ShowAboutCommand.getInstance().getCommand()) {
            ShowAboutCommand.getInstance().execute(null);
        }
    }

    private void initForm() {
        addUrlLabel();
        addUrlTextArea();
        serverUrlTextArea.setFocus(true);
    }

    private void addUrlTextArea() {
        serverUrlTextArea = new ClearTextField();
        serverUrlTextArea.setText(defaultInput);
        serverUrlTextArea.addActionListener(this);
        serverUrlTextArea.setInputMode("Abc");
        addComponent(serverUrlTextArea);
    }

    private void addUrlLabel() {
        Label serverUrlLabel = new Label( NedResources.ENTER_SERVER_ADDRESS );
        serverUrlLabel.setAlignment(Label.CENTER);
        serverUrlLabel.getStyle().setPadding(10, 10, 1, 1);
        addComponent(serverUrlLabel);
    }

    //for check url command
    public void onSuccess() {
        NedMidlet.getAccountManager().setServer(serverUrlTextArea.getText().trim());
        new LoginScreen().show();
    }

    //for check url command
    public void onFailure(String error) {
        GeneralAlert.show(error , GeneralAlert.WARNING );//todo better message needed
    }
}
