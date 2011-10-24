package org.ned.client.view;

import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.AccountManager.UserInfo;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.ExitCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.LoginOnStartCommand;
import org.ned.client.command.ShowAboutCommand;
import org.ned.client.command.RemoveUserCommand;
import org.ned.client.view.customComponents.ClearTextField;
import org.ned.client.command.ResetFactorySettingsCommand;
import org.ned.client.view.customComponents.CheckBox;

public class LoginScreen extends NedFormBase implements ActionListener, FocusListener {

    private ClearTextField textAreaUser;
    private ClearTextField textAreaPassword;
    private CheckBox rememberCheckBox;

    public LoginScreen() {
        setNedTitle( NedResources.USER_AUTHENTICATION );
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        initForm();
        addCommand(ExitCommand.getInstance().getCommand());
        addCommand(ShowAboutCommand.getInstance().getCommand());
        addCommand(HelpCommand.getInstance().getCommand());
        addCommand(ResetFactorySettingsCommand.getInstance().getCommand());
        addCommand(RemoveUserCommand.getInstance().getCommand());
        addCommand(LoginOnStartCommand.getInstance().getCommand());
        addCommandListener(this);
    }

    private void initForm() {

        addServerLabel();
        addUserLabel();
        addUserTextArea();
        addPasswordLabel();
        addPasswordTextArea();
        addRememberCheckBox();

        setLastUser();

        textAreaUser.setFocus(true);
    }

    private void setLastUser(){
        UserInfo currentUser = NedMidlet.getAccountManager().getCurrentUser();
        if (currentUser != null) {
            textAreaUser.setText(currentUser.login);
            if(currentUser.isPassSaved){
                textAreaPassword.setText(currentUser.password);
            }
            rememberCheckBox.setSelected(currentUser.isPassSaved);
        }

    }

    private void addRememberCheckBox(){
        rememberCheckBox = new CheckBox( NedResources.REMEMBERME );
        rememberCheckBox.setFocusable(true);

        addComponent(rememberCheckBox);
    }

    private void addServerLabel(){
        Label labelServer = new Label( NedMidlet.getAccountManager().getServerUrl());
        labelServer.setFocusable(false);
        labelServer.setAlignment(CENTER);

        addComponent(labelServer);
    }

    private void addPasswordLabel() {
        Label labelPassword = new Label( NedResources.PASSWORD );
        labelPassword.setFocusable(false);
        addComponent(labelPassword);
    }

    private void addUserTextArea() {
        textAreaUser = new ClearTextField();

        textAreaUser.addActionListener(this);
        textAreaUser.setInputMode("abc");
        textAreaUser.addFocusListener(this);
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
        textAreaPassword.addActionListener(this);
        textAreaPassword.setInputMode("abc");
        addComponent(textAreaPassword);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if ( src == LoginOnStartCommand.getInstance().getCommand() ) {
            Object[] login = new Object[]{ textAreaUser.getText(), textAreaPassword.getText(), ( rememberCheckBox.isSelected() ? Boolean.TRUE : Boolean.FALSE ) };
            LoginOnStartCommand.getInstance().execute(login);
        } else if ( src == ExitCommand.getInstance().getCommand() ) {
            ExitCommand.getInstance().execute(null);
        } else if ( src == RemoveUserCommand.getInstance().getCommand()) {
            if( textAreaUser.getText() != null && !textAreaUser.getText().equals("") ) {
                Object[] login = new Object[]{ textAreaUser.getText(), textAreaPassword.getText() };
                RemoveUserCommand.getInstance().execute( login );
                textAreaUser.clear();
                textAreaPassword.clear();
            }
        } else if (src == ResetFactorySettingsCommand.getInstance().getCommand()) {
            if (textAreaUser.getText() != null && !textAreaUser.getText().equals("")) {
                Object[] login = new Object[]{textAreaUser.getText(), textAreaPassword.getText()};
                ResetFactorySettingsCommand.getInstance().execute(login);
            }
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        } else if ( src == ShowAboutCommand.getInstance().getCommand()) {
            ShowAboutCommand.getInstance().execute(null);
        }
    }

    public void focusGained( Component cmpnt ) {
    }

    public void focusLost( Component cmpnt ) {
        if ( cmpnt == textAreaUser ) {
            UserInfo user = NedMidlet.getAccountManager().findUser( textAreaUser.getText() );
            if ( user != null ) {
                if( user.isPassSaved ){
                    textAreaPassword.setText( user.password );
                }
                rememberCheckBox.setSelected( user.isPassSaved );
            } else {
                textAreaPassword.clear();
                rememberCheckBox.setSelected( false );
            }
        }
    }
}
