package org.ned.client.view;

import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import org.ned.client.NedResources;
import org.ned.client.command.CheckServerUrlComand;
import org.ned.client.command.ExitCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.ShowAboutCommand;
import org.ned.client.view.customComponents.ClearTextField;


public class WelcomeScreen extends NedFormBase implements ActionListener  {

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
            WaitingScreen.show( NedResources.CONNECTING );
            CheckServerUrlComand.getInstance().execute(newAddress);
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
}
