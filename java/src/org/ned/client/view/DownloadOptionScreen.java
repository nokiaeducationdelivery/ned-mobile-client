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
import org.ned.client.command.HelpCommand;
import org.ned.client.view.customComponents.RadioButton;

public class DownloadOptionScreen extends NedFormBase implements ActionListener {

    private RadioButton mAutomaticRB;
    private RadioButton mManualRB;

    public DownloadOptionScreen() {
        super();
        setNedTitle( NedResources.DOWNLOAD_OPTIONS );

        addComponent( createGeneral() );
        addCommand( BackDownloadOptionsCommand.getInstance().getCommand() );
        addCommand( HelpCommand.getInstance().getCommand() );
        addCommandListener( this );
    }

    private Container createGeneral() {
        Container general = new Container();
        general.setLayout( new BoxLayout(BoxLayout.Y_AXIS ) );
        Label downloadStateLabel = new Label( NedResources.MID_DOWNLOAD_STATE_SETTINGS );
        mAutomaticRB = new RadioButton( NedResources.MID_AUTOMATIC_SETTINGS );
        mAutomaticRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mAutomaticRB.setTickerEnabled( false );
        mAutomaticRB.addActionListener( this );
        mManualRB = new RadioButton( NedResources.MID_MANUAL_SETTINGS );
        mManualRB.setPreferredW( Display.getInstance().getDisplayWidth() );
        mManualRB.setTickerEnabled( false );
        mManualRB.addActionListener( this );
        ButtonGroup downloadStateBG = new ButtonGroup();
        downloadStateBG.add( mAutomaticRB );
        downloadStateBG.add( mManualRB );

        if ( NedMidlet.getSettingsManager().getDlAutomatic() ) {
            downloadStateBG.setSelected( mAutomaticRB );
            NedMidlet.getInstance().setDownloadState( NedMidlet.DOWNLOAD_AUTOMATIC );
        } else {
            downloadStateBG.setSelected( mManualRB );
            NedMidlet.getInstance().setDownloadState( NedMidlet.DOWNLOAD_MANUAL );
        }

        general.addComponent( downloadStateLabel );
        general.addComponent( mAutomaticRB );
        general.addComponent( mManualRB );

        return general;
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if ( src == BackDownloadOptionsCommand.getInstance().getCommand() ) {
            BackDownloadOptionsCommand.getInstance().execute( null );
        } else if (src == mAutomaticRB) {
            NedMidlet.getInstance().setDownloadState(NedMidlet.DOWNLOAD_AUTOMATIC);
            NedMidlet.getSettingsManager().setDlAutomatic(true);
            NedMidlet.getSettingsManager().saveSettings();
        } else if (src == mManualRB) {
            NedMidlet.getInstance().setDownloadState(NedMidlet.DOWNLOAD_MANUAL);
            NedMidlet.getSettingsManager().setDlAutomatic(false);
            NedMidlet.getSettingsManager().saveSettings();
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }
}


