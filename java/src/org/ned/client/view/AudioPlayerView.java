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

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import org.ned.client.IContent;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.BackAudioCommand;
import org.ned.client.command.PauseAudioCommand;

/**
 * @author community  this class it to be thrown
 */
public class AudioPlayerView extends NedFormBase implements ActionListener, PlayerListener, Runnable {

    private static final int INIT_VOLUME_LEVEL = 100;
    private static int currentVolume = -1;

    private VolumeControl volume = null;
    private String audioFile;
    private Player player;
    private Image musicImage = NedMidlet.getRes().getImage( "AudioBig" );
    private IContent mContent;

    public AudioPlayerView(IContent content) {
        try {
            mContent = content;
            audioFile = mContent.getMediaFile();
            setLayout(new BorderLayout());
            setNedTitle( mContent.getText() );

            Label musicLabel = new Label(musicImage);
            musicLabel.setAlignment(Label.CENTER);

            addComponent(BorderLayout.CENTER, musicLabel);

            addCommand(BackAudioCommand.getInstance().getCommand());
            addCommand(PauseAudioCommand.getInstance().getCommand());
            addGameKeyListener(Display.GAME_UP, this);
            addGameKeyListener(Display.GAME_DOWN, this);
            addGameKeyListener(Display.GAME_FIRE, this);
            addCommandListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == BackAudioCommand.getInstance().getCommand()) {
            BackAudioCommand.getInstance().execute( mContent.getParentId() );
        } else if (source == PauseAudioCommand.getInstance().getCommand()) {
            PauseAudioCommand.getInstance().execute(null);
        } else {
            int eventCode = evt.getKeyEvent();
            switch (eventCode) {
                case (Display.GAME_FIRE):
                    pause();
                    break;
                case (Display.GAME_DOWN):
                    if (volume != null) {
                        currentVolume = volume.getLevel() - 5;
                        volume.setLevel( currentVolume );
                    }
                    break;
                case (Display.GAME_UP):
                    if (volume != null) {
                        currentVolume = volume.getLevel() + 5;
                        volume.setLevel( currentVolume );
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void pause() {
        if (player != null) {
            if (player.getState() == Player.STARTED) {
                try {
                    long mt = player.getMediaTime();
                    player.stop();
                    player.setMediaTime(mt);
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    player.start();
                } catch (MediaException ex) {
                    start();
                } catch ( IllegalStateException isex ) {
                    start();
                }
            }
        }
    }

    public void stopPlayer() {
        try {
            if ( player != null ) {
                if ( player.getState() == Player.STARTED ) {
                    player.stop();
                }
                if ( player.getState() == Player.PREFETCHED ) {
                    player.deallocate();
                }
                if ( player.getState() == Player.REALIZED
                  || player.getState() == Player.UNREALIZED ) {
                    player.close();
                }
            }
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.setPriority( Thread.MIN_PRIORITY );
        t.start();
    }

    public void playerUpdate(Player player, String event, Object eventData) {
        if (event.equals( PlayerListener.END_OF_MEDIA)) {
            stopPlayer();
            PauseAudioCommand.getInstance().getCommand().setCommandName( NedResources.AC_PLAY );
            removeCommand( PauseAudioCommand.getInstance().getCommand() );
            addCommand( PauseAudioCommand.getInstance().getCommand() );
        } else if ( event.equals( PlayerListener.STARTED ) ) {
            PauseAudioCommand.getInstance().getCommand().setCommandName( NedResources.AC_PAUSE );
            removeCommand( PauseAudioCommand.getInstance().getCommand() );
            addCommand( PauseAudioCommand.getInstance().getCommand() );
        } else if ( event.equals( PlayerListener.STOPPED )
                 || event.equals( PlayerListener.STOPPED_AT_TIME ) ) {
            PauseAudioCommand.getInstance().getCommand().setCommandName( NedResources.AC_PLAY );
            removeCommand( PauseAudioCommand.getInstance().getCommand() );
            addCommand( PauseAudioCommand.getInstance().getCommand() );
        }
        repaint();
    }

    public void run() {
        init();
    }

    void init() {
        try {
            player = Manager.createPlayer( audioFile );
            player.addPlayerListener( this );
            player.realize();
            volume = (VolumeControl)player.getControl( "VolumeControl" );
            if (volume != null) {
                volume.setLevel( currentVolume == -1 ? INIT_VOLUME_LEVEL : currentVolume );
            }
            player.prefetch();
            player.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            GeneralAlert.show( NedResources.UNSUPPORTED_MEDIA_FORMAT, GeneralAlert.WARNING );
        }
    }
}
