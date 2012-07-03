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

import com.sun.lwuit.*;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.FramePositioningControl;
import javax.microedition.media.control.VolumeControl;
import org.ned.client.IContent;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.command.BackVideoCommand;

public class VideoPlayerView extends NedFormBase implements PlayerListener, ActionListener, Runnable {

    private static final int INIT_VOLUME_LEVEL = 80;
    private static final Image playIcon = NedMidlet.getRes().getImage( "Play" );
    private static final Image pauseIcon = NedMidlet.getRes().getImage( "Pause" );
    private static final Image ffIcon = NedMidlet.getRes().getImage( "FF" );
    private static final Image rewIcon = NedMidlet.getRes().getImage( "Rew" );
    private static final Image fullIcon = NedMidlet.getRes().getImage( "Fullscreen" );
    private static final Image exitIcon = NedMidlet.getRes().getImage( "BackIcon" );
    private static int currentVolume = -1;
    private VolumeControl volume = null;
    private MediaComponent mediaComponent;
    private Player player;
    private FramePositioningControl frame = null;
    private String videoFile;
    private KeyListetener mKeyListener;
    private Container controlUI = null;
    private Slider progress = null;
    private boolean ignoreEvent = false;
    private boolean isPortrait;
    private Button rewindButton;
    private Button playButton;
    private Button fastForwardButton;
    private Button fullScreenButton;
    private Button backButton;
    private UpdateProgessbarTimerTask updateProgressBar;
    private static Command playCommand = new Command( "Play" );
    private static Command fastForwardCommand = new Command( "FF" );
    private static Command rewindCommanf = new Command( "Rew" );
    private static Command fullScreenCommand = new Command( "Full" );
    private static Command exitPlayerCommand = new Command( "Exit" );

    public VideoPlayerView( IContent content ) {
        currentElement = content;
        videoFile = currentElement.getMediaFile();
        setLayout( new BorderLayout() );
        setNedTitle( currentElement.getText() );
        isPortrait = Display.getInstance().isPortrait();

        mKeyListener = new KeyListetener();
        addGameKeyListener( Display.GAME_UP, mKeyListener );
        addGameKeyListener( Display.GAME_DOWN, mKeyListener );
        addGameKeyListener( Display.GAME_FIRE, mKeyListener );

        addCommandListener( this );

        addPointerReleasedListener( new ActionListener() {

            public void actionPerformed( ActionEvent evt ) {
                if ( player != null && mediaComponent != null && mediaComponent.isFullScreen() ) {
                    showControlPanel();
                }
            }
        } );
        initControlUI();
        updateProgressBar = new UpdateProgessbarTimerTask();
    }

    public void prepareToPlay() {
        start();
    }

    private void showControlPanel() {
        if ( controlUI != null && contains( controlUI ) ) {
            removeComponent( controlUI );
            controlUI = null;
        }
        initControlUI();
        mediaComponent.setFullScreen( false );
        addComponent( BorderLayout.SOUTH, controlUI );
        removeGameKeyListener( Display.GAME_FIRE, mKeyListener );

        controlUI.requestFocus();
        repaint();
        updateProgressBar();
        updateProgressBar.startTimer();
    }

    private synchronized void updateProgressBar() {
        if ( player != null && progress.isRenderPercentageOnTop() ) {
            long time = player.getMediaTime();
            long duration = player.getDuration();
            if ( time != Player.TIME_UNKNOWN && duration != Player.TIME_UNKNOWN && duration > 0 ) {
                progress.setProgress( (int) (100 * time / duration) );
            }
        }
    }

    /**
     * Reads the content from the specified HTTP URL and returns InputStream
     * where the contents are read.
     *
     * @return InputStream
     * @throws IOException
     */
    private InputStream urlToStream( String url ) throws IOException {
        // Open connection to the http url...
        HttpConnection connection = null;
        DataInputStream dataIn = null;
        ByteArrayOutputStream byteout = null;
        try {
            connection = (HttpConnection) Connector.open( url );
            dataIn = connection.openDataInputStream();
            byte[] buffer = new byte[1000];
            int read = -1;
            // Read the content from url.
            byteout = new ByteArrayOutputStream();
            while ( (read = dataIn.read( buffer )) >= 0 ) {
                byteout.write( buffer, 0, read );
            }

        } catch ( IOException ex ) {
            if ( dataIn != null ) {
                dataIn.close();
            }
            if ( connection != null ) {
                connection.close();
            }
            throw ex;
        }
        // Fill InputStream to return with content read from the URL.
        ByteArrayInputStream byteIn = new ByteArrayInputStream( byteout.toByteArray() );
        return byteIn;
    }

    public synchronized void actionPerformed( ActionEvent evt ) {
        Object source = evt.getSource();

        if ( source == BackVideoCommand.getInstance().getCommand() ) {
            BackVideoCommand.getInstance().execute( currentElement.getParentId() );
        } else if ( source == fastForwardCommand || source == rewindCommanf ) {
            if ( frame != null ) {
                frame.skip( 25 * (source == fastForwardCommand ? 1 : -1) );//fast forward or rew
                updateProgressBar();
            } else {
                NotSupportedMediaContolAction();
            }
        } else if ( source == playCommand ) {
            pause();
        } else if ( source == fullScreenCommand ) {
            evt.consume();
            removeComponent( controlUI );
            ignoreEvent = true;
            addGameKeyListener( Display.GAME_FIRE, mKeyListener );
            mediaComponent.setFullScreen( true );
            repaint();
            updateProgressBar.cancelTimer();
        } else if ( source == exitPlayerCommand ) {
            updateProgressBar.cancelTimer();
            BackVideoCommand.getInstance().execute( currentElement.getParentId() );
        }
    }

    public void pause() {
        if ( player != null ) {
            if ( player.getState() == Player.STARTED ) {
                try {
                    long mt = player.getMediaTime();
                    player.stop();
                    player.setMediaTime( mt );
                } catch ( MediaException ex ) {
                    ex.printStackTrace();
                } catch ( IllegalStateException ex ) {
                    ex.printStackTrace();
                }
            } else if ( player.getState() == Player.PREFETCHED ) {
                try {
                    player.start();
                } catch ( MediaException ex ) {
                    start();
                } catch ( IllegalStateException isex ) {
                    start();
                }
            }
        }
    }

    public void stopPlayer() {
        try {
            if ( player != null && player.getState() != Player.CLOSED ) {
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
                player = null;
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

    public void start() {
        Thread t = new Thread( this );
        t.setPriority( Thread.MIN_PRIORITY );
        t.start();
    }

    public void playerUpdate( final Player p, final String event,
                              final Object eventData ) {
        // queue a call to updateEvent in the user interface event queue
        Display display = Display.getInstance();
        display.callSerially( new Runnable() {

            public void run() {
                playerUpdate2( p, event, eventData );
            }
        } );
    }

    public synchronized void playerUpdate2( Player p, String event, Object eventData ) {
        if ( p.getState() == Player.CLOSED ) {
            return;
        }
        if ( event.equals( PlayerListener.END_OF_MEDIA ) ) {
            playButton.setIcon( playIcon );
            playButton.repaint();
            showControlPanel();
        } else if ( event.equals( PlayerListener.STARTED ) ) {
            addGameKeyListener( Display.GAME_FIRE, mKeyListener );
            playButton.setIcon( pauseIcon );
            playButton.repaint();
        } else if ( event.equals( PlayerListener.STOPPED ) ) {
            playButton.setIcon( playIcon );
            playButton.repaint();
            showControlPanel();
        }
    }

    public void run() {
        init();
    }

    void init() {
        try {
            if ( mediaComponent != null ) {
                mediaComponent.setVisible( false );
                removeComponent( mediaComponent );
            }

            boolean fromHttp = videoFile.startsWith( "http://" );
            if ( fromHttp ) {
                InputStream is = urlToStream( videoFile );
                player = Manager.createPlayer( is, "video/3gpp" );
            } else {
                player = Manager.createPlayer( videoFile );
            }
            player.realize();
            player.prefetch();
            mediaComponent = new MediaComponent( player );
            mediaComponent.setPreferredH( getContentPane().getHeight() - 3 * Font.getDefaultFont().getHeight() );
            mediaComponent.getStyle().setMargin( 0, 0, 0, 0 );
            mediaComponent.setFullScreen( true );
            mediaComponent.setVisible( true );

            addComponent( BorderLayout.CENTER, mediaComponent );

            player.addPlayerListener( this );

            Control[] cs = player.getControls();
            for ( int i = 0; i < cs.length; i++ ) {
                if ( cs[i] instanceof VolumeControl ) {
                    volume = (VolumeControl) cs[i];
                    volume.setLevel( currentVolume == -1 ? INIT_VOLUME_LEVEL : currentVolume );
                } else if ( cs[i] instanceof FramePositioningControl ) {
                    frame = (FramePositioningControl) cs[i];
                }
            }
            player.start();
            repaint();

            progress.setProgress( 0 );
        } catch ( IOException ex ) {
            ex.printStackTrace();
        } catch ( MediaException ex ) {
            GeneralAlert.show( NedResources.UNSUPPORTED_MEDIA_FORMAT, GeneralAlert.WARNING );
        }
    }

    private void initControlUI() {
        if ( controlUI == null ) {
            controlUI = new Container( new BoxLayout( (BoxLayout.Y_AXIS) ) );
            Container controlUIButton = new Container( new BoxLayout( (BoxLayout.X_AXIS) ) );
            int prefH = Font.getDefaultFont().getHeight();

            rewindButton = new Button( rewindCommanf );
            rewindButton.setIcon( rewIcon );
            rewindButton.setText( "" );
            int prefW = (Display.getInstance().getDisplayWidth() - 10 * rewindButton.getStyle().getMargin( Component.LEFT )) / 5;
            rewindButton.setPreferredW( prefW );
            rewindButton.setAlignment( Component.CENTER );
            rewindButton.setPreferredH( 2 * prefH );

            playButton = new Button( playCommand );
            playButton.setIcon( (player != null && player.getState() == Player.STARTED) ? pauseIcon : playIcon );
            playButton.setText( "" );
            playButton.setPreferredW( prefW );
            playButton.setPreferredH( 2 * prefH );
            playButton.setAlignment( Component.CENTER );

            fastForwardButton = new Button( fastForwardCommand );
            fastForwardButton.setIcon( ffIcon );
            fastForwardButton.setText( "" );
            fastForwardButton.setPreferredW( prefW );
            fastForwardButton.setPreferredH( 2 * prefH );
            fastForwardButton.setAlignment( Component.CENTER );

            fullScreenButton = new Button( fullScreenCommand );
            fullScreenButton.setIcon( fullIcon );
            fullScreenButton.setText( "" );
            fullScreenButton.setPreferredW( prefW );
            fullScreenButton.setPreferredH( 2 * prefH );
            fullScreenButton.setAlignment( Component.CENTER );

            backButton = new Button( exitPlayerCommand );
            backButton.setIcon( exitIcon );
            backButton.setText( "" );
            backButton.setPreferredW( prefW );
            backButton.setPreferredH( 2 * prefH );
            backButton.setAlignment( Component.CENTER );

            progress = new Slider();
            progress.setThumbImage( pauseIcon.subImage( 7, 0, 8, pauseIcon.getHeight(), false ) );
            progress.setMinValue( 0 );
            progress.setMaxValue( 100 );
            progress.setFocusable( true );
            progress.setRenderPercentageOnTop( true );
            progress.setIncrements( 1 );
            progress.setEditable( true );
            progress.setPreferredH( Font.getDefaultFont().getHeight() );
            progress.addDataChangedListener( new RewFFVideo() );
            controlUIButton.addComponent( rewindButton );
            controlUIButton.addComponent( playButton );
            controlUIButton.addComponent( fastForwardButton );
            controlUIButton.addComponent( fullScreenButton );
            controlUIButton.addComponent( backButton );
            controlUI.addComponent( progress );
            controlUI.addComponent( controlUIButton );
            controlUI.setPreferredH( 3 * prefH );
        }
    }

    protected void sizeChanged( int w, int h ) {
        boolean isPortraitNow = Display.getInstance().isPortrait();
        if ( isPortrait != isPortraitNow ) {
            if ( contains( controlUI ) ) {
                removeComponent( controlUI );
            }
            controlUI = null;
            initControlUI();
            isPortrait = isPortraitNow;

            if ( mediaComponent != null ) {
                removeComponent( mediaComponent );
                if ( isPortraitNow ) {
                    mediaComponent.setPreferredH( getContentPane().getHeight() - 3 * Font.getDefaultFont().getHeight() );
                } else {
                    mediaComponent.setPreferredH( 100 );
                }
                addComponent( BorderLayout.CENTER, mediaComponent );

            }
            addComponent( BorderLayout.SOUTH, controlUI );
            controlUI.requestFocus();
            repaint();
        }
    }

    private class UpdateProgessbarTimerTask {

        private static final long SEC = 1000;
        private Timer timer;
        private TimerTask task;

        public void startTimer() {
            cancelTimer();
            timer = new Timer();
            task = new TimerTask() {

                public void run() {
                    try {
                        updateProgressBar();
                    } catch ( Exception ex ) {
                    }
                }
            };
            timer.schedule( task, SEC, SEC );
        }

        public void cancelTimer() {
            if ( task != null ) {
                task.cancel();
                task = null;
            }
            if ( timer != null ) {
                timer.cancel();
                timer = null;
            }
        }

        protected void finalize() throws Throwable {
            cancelTimer();
        }
    }

    private class KeyListetener implements ActionListener {

        public void actionPerformed( ActionEvent evt ) {
            int keyCode = evt.getKeyEvent();
            switch ( keyCode ) {
                case Display.GAME_FIRE:
                    if ( mediaComponent.isFullScreen() && !ignoreEvent ) {
                        showControlPanel();
                    } else {
                        ignoreEvent = false;
                    }
                    break;
                case Display.GAME_UP:
                    if ( volume != null ) {
                        currentVolume = volume.getLevel() + 5;
                        volume.setLevel( currentVolume );
                    }
                    break;
                case Display.GAME_DOWN:
                    if ( volume != null ) {
                        currentVolume = volume.getLevel() - 5;
                        volume.setLevel( currentVolume );
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void NotSupportedMediaContolAction() {
        progress.setRenderPercentageOnTop( false );
        progress.setText( NedResources.NOT_SUPPORTED );
        Timer timer = new Timer();
        timer.schedule( new FadingNotSupportedLabelTask( timer ), 1000 );
    }

    private class RewFFVideo implements DataChangedListener {

        public RewFFVideo() {
        }

        public synchronized void dataChanged( int i, int i1 ) {
            if ( player != null ) {
                try {
                    player.setMediaTime( i1 * player.getDuration() / 100 );
                } catch ( MediaException ex ) {
                    NotSupportedMediaContolAction();
                }
                updateProgressBar();
            } else {
                NotSupportedMediaContolAction();
            }
        }
    }

    private class FadingNotSupportedLabelTask extends TimerTask {

        private Timer mTimer;

        public FadingNotSupportedLabelTask( Timer aTimer ) {
            mTimer = aTimer;
        }

        public void run() {
            progress.setRenderPercentageOnTop( true );
            mTimer.cancel();
        }
    }
}
