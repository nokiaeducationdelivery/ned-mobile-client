package org.ned.client.view;

import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import org.ned.client.NedResources;
import org.ned.client.command.BackSettingsCommand;
import org.ned.client.command.DownloadOptionsCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.LoginViewCommand;
import org.ned.client.command.NedCommand;
import org.ned.client.command.StatisticsOptionsCommand;
import org.ned.client.view.renderer.SimpleListCellRenderer;


public class SettingsScreen extends NedFormBase implements ActionListener{

    private List mCommands;

    public SettingsScreen() {
        super();
        setNedTitle( NedResources.MID_SETTINGS_TITLE );
        mCommands = new List( new Object[]{ StatisticsOptionsCommand.getInstance(),
                                            DownloadOptionsCommand.getInstance(),
                                            LoginViewCommand.getInstance() } );
        mCommands.setListCellRenderer( new SimpleListCellRenderer() );
        mCommands.addActionListener( this );
        addComponent( mCommands );
        addCommand( BackSettingsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    public void actionPerformed( ActionEvent evt ) {
        Object src = evt.getSource();
        if ( src == BackSettingsCommand.getInstance().getCommand() ) {
            BackSettingsCommand.getInstance().execute( null );
        } else if ( src == mCommands && mCommands.getSelectedIndex() >= 0 ) {
            ((NedCommand)mCommands.getSelectedItem()).execute( null );
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }
}
