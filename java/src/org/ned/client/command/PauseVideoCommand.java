package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;

public class PauseVideoCommand extends NedCommand {

    private static PauseVideoCommand instance;

    private PauseVideoCommand() {
        command = new Command(NedResources.VC_PAUSE);
    }

    public static PauseVideoCommand getInstance() {
        if (instance == null) {
            instance = new PauseVideoCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        if (NedMidlet.getInstance().getVideoPlayer() != null) {
            NedMidlet.getInstance().getVideoPlayer().pause();
        }
    }
}
