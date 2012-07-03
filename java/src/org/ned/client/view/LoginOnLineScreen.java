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
import org.ned.client.AccountManager.UserInfo;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.AsyncCompletedCallback;
import org.ned.client.command.ExitCommand;
import org.ned.client.command.LoginToServerCommand;
import org.ned.client.utils.ErrorConnectionMessageResolver;
import org.ned.client.view.customComponents.ClearTextField;

public class LoginOnLineScreen extends NedFormBase implements ActionListener, AsyncCompletedCallback {

    private ClearTextField textAreaUser;
    private ClearTextField textAreaPassword;
    private Class mPreviousForm;

    public LoginOnLineScreen( Class aPreviousForm ) {
        setNedTitle( NedResources.USER_AUTHENTICATION );
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        initForm();
        addCommand(ExitCommand.getInstance().getCommand());
        addCommand(LoginToServerCommand.getInstance().getCommand());
        addCommandListener(this);
        mPreviousForm = aPreviousForm;
    }

    private void initForm() {
        addUserLabel();
        addUserTextArea();
        addPasswordLabel();
        addPasswordTextArea();
        textAreaUser.setFocus(true);
    }

    private void addPasswordLabel() {
        Label labelPassword = new Label( NedResources.PASSWORD );
        labelPassword.setFocusable(false);
        addComponent(labelPassword);
    }

    private void addUserTextArea() {
        textAreaUser = new ClearTextField();
        UserInfo currentUser = NedMidlet.getAccountManager().getCurrentUser();
        if (currentUser != null) {
            textAreaUser.setText(currentUser.login);
        }
        textAreaUser.setEditable(false);
        textAreaUser.setInputMode("abc");
        addComponent(textAreaUser);
    }

    private void addUserLabel() {
        Label labelUser = new Label( NedResources.USER_NAME );
        labelUser.setFocusable(false);
        addComponent(labelUser);
    }

    private void addPasswordTextArea() {
        textAreaPassword = new ClearTextField();
        textAreaPassword.setConstraint(ClearTextField.PASSWORD);
        textAreaPassword.setInputMode("abc");
        addComponent(textAreaPassword);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if ( src == LoginToServerCommand.getInstance().getCommand() ) {
            Object[] credentials = new Object[]{ textAreaUser.getText(), textAreaPassword.getText()};
            LoginToServerCommand.getInstance().beginAsync(credentials, this, true );
        } else if ( src == ExitCommand.getInstance().getCommand() ) {
            ExitCommand.getInstance().execute(null);
        }
    }

    public void onSuccess() {
                try {
            ((NedFormBase) mPreviousForm.newInstance()).show();
        } catch (Exception ex) {
            new MainScreen().show();
        }
    }

    public void onFailure(String error) {
        ErrorConnectionMessageResolver.showErrorMessage( Integer.parseInt(error) );
    }
}
