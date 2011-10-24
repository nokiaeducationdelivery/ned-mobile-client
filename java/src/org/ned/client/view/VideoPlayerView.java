package org.ned.client.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.MediaComponent;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.ned.client.NedResources;
import org.ned.client.command.BackVideoCommand;
import org.ned.client.command.PauseVideoCommand;

public class VideoPlayerView extends NedFormBase implements PlayerListener, ActionListener, Runnable {

    private static final int INIT_VOLUME_LEVEL = 80;
    private VolumeControl volume = null;
    private MediaComponent mediaComponent;
    private Player player;
    private FramePositioningControl frame = null;
    private String videoFile;
    private KeyListetener mKeyListener;

    public VideoPlayerView(IContent content) {
        currentElement = content;
        videoFile = currentElement.getMediaFile();
        setLayout(new BorderLayout());
        setNedTitle(currentElement.getText());

        mKeyListener = new KeyListetener();
        addGameKeyListener(Display.GAME_UP, mKeyListener);
        addGameKeyListener(Display.GAME_DOWN, mKeyListener);
        addGameKeyListener(Display.GAME_FIRE, mKeyListener);
        addGameKeyListener(Display.GAME_LEFT, mKeyListener);
        addGameKeyListener(Display.GAME_RIGHT, mKeyListener);

        addCommand(BackVideoCommand.getInstance().getCommand());
        addCommand(PauseVideoCommand.getInstance().getCommand());
        addCommandListener(this);
        addPointerReleasedListener(new ActionListener() {

            public void actionPerformed( ActionEvent evt ) {
                if( player != null && player.getState() == Player.STARTED ) {
                    PauseVideoCommand.getInstance().execute(null);
                }
            }
        });
    }

    public void prepareToPlay() {
        start();
    }

    /**
     * Reads the content from the specified HTTP URL and returns InputStream
     * where the contents are read.
     * 
     * @return InputStream
     * @throws IOException
     */
    private InputStream urlToStream(String url) throws IOException {
        // Open connection to the http url...
        HttpConnection connection = null;
        DataInputStream dataIn = null;
        ByteArrayOutputStream byteout = null;
        try {
            connection = (HttpConnection) Connector.open(url);
            dataIn = connection.openDataInputStream();
            byte[] buffer = new byte[1000];
            int read = -1;
            // Read the content from url.
            byteout = new ByteArrayOutputStream();
            while ((read = dataIn.read(buffer)) >= 0) {
                byteout.write(buffer, 0, read);
            }

        } catch (IOException ex) {
            if (dataIn != null) {
                dataIn.close();
            }
            if (connection != null) {
                connection.close();
            }
            throw ex;
        }
        // Fill InputStream to return with content read from the URL.
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteout.toByteArray());
        return byteIn;
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == BackVideoCommand.getInstance().getCommand()) {
            BackVideoCommand.getInstance().execute(currentElement.getParentId());
        } else if (source == PauseVideoCommand.getInstance().getCommand()) {
            PauseVideoCommand.getInstance().execute(null);
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
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            } else if (player.getState() == Player.PREFETCHED) {
                try {
                    player.start();
                } catch (MediaException ex) {
                    start();
                } catch (IllegalStateException isex) {
                    start();
                }
            }
        }
    }

    public void stopPlayer() {
        try {
            if (player != null && player.getState() != Player.CLOSED) {
                if (player.getState() == Player.STARTED) {
                    player.stop();
                }
                if (player.getState() == Player.PREFETCHED) {
                    player.deallocate();
                }
                if (player.getState() == Player.REALIZED
                        || player.getState() == Player.UNREALIZED) {
                    player.close();
                }
                player = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        Thread t = new Thread(this);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public void playerUpdate(final Player p, final String event,
            final Object eventData) {
        // queue a call to updateEvent in the user interface event queue
        Display display = Display.getInstance();
        display.callSerially(new Runnable() {

            public void run() {
                playerUpdate2(p, event, eventData);
            }
        });
    }

    public synchronized void playerUpdate2(Player p, String event, Object eventData) {
        if (p.getState() == Player.CLOSED) {
            return;
        }
        if (event.equals(PlayerListener.END_OF_MEDIA)) {
            mediaComponent.setFullScreen(false);
            PauseVideoCommand.getInstance().getCommand().setCommandName(NedResources.AC_PLAY);
            removeAllCommands();
            addCommand(BackVideoCommand.getInstance().getCommand());
            addCommand(PauseVideoCommand.getInstance().getCommand());
        } else if (event.equals(PlayerListener.STARTED)) {
            mediaComponent.setFullScreen(true);
            removeAllCommands();
        } else if (event.equals(PlayerListener.STOPPED)) {
            mediaComponent.setFullScreen(false);
            PauseVideoCommand.getInstance().getCommand().setCommandName(NedResources.AC_PLAY);
            removeAllCommands();
            addCommand(BackVideoCommand.getInstance().getCommand());
            addCommand(PauseVideoCommand.getInstance().getCommand());
        }
    }

    public void run() {
        init();
    }

    void init() {
        try {
            if (mediaComponent != null) {
                mediaComponent.setVisible(false);
                removeComponent(mediaComponent);
            }

            boolean fromHttp = videoFile.startsWith("http://");
            if (fromHttp) {
                InputStream is = urlToStream(videoFile);
                player = Manager.createPlayer(is, "video/3gpp");
            } else {
                player = Manager.createPlayer(videoFile);
            }
            player.realize();
            player.prefetch();
            mediaComponent = new MediaComponent(player);
            mediaComponent.setPreferredH(getContentPane().getHeight());
            mediaComponent.getStyle().setMargin(0, 0, 0, 0);
            mediaComponent.setFullScreen(true);
            mediaComponent.setVisible(true);

            addComponent(BorderLayout.CENTER, mediaComponent);
            player.addPlayerListener(this);

            Control[] cs = player.getControls();
            for (int i = 0; i < cs.length; i++) {
                if (cs[i] instanceof VolumeControl) {
                    volume = (VolumeControl) cs[i];
                    volume.setLevel(INIT_VOLUME_LEVEL);
                } else if (cs[i] instanceof FramePositioningControl) {
                    frame = (FramePositioningControl) cs[i];
                }
            }
            player.start();
            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            GeneralAlert.show(NedResources.UNSUPPORTED_MEDIA_FORMAT, GeneralAlert.WARNING);
        }
    }

    private class KeyListetener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            int keyCode = evt.getKeyEvent();
            switch (keyCode) {
                case Display.GAME_FIRE:
                    pause();
                    break;
                case Display.GAME_UP:
                    if (volume != null) {
                        volume.setLevel(volume.getLevel() + 5);
                    }
                    break;
                case Display.GAME_DOWN:
                    if (volume != null) {
                        volume.setLevel(volume.getLevel() - 5);
                    }
                    break;
                case Display.GAME_LEFT:
                    if (frame != null) {
                        frame.skip(-25);  //rewind
                    }
                    break;
                case Display.GAME_RIGHT:
                    if (frame != null) {
                        frame.skip(25);  //fast forward
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
