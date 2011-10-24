package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;

public class PauseAudioCommand extends NedCommand {

    private static PauseAudioCommand instance;

    private PauseAudioCommand() {
        command = new Command(NedResources.AC_PAUSE);
    }

    public static PauseAudioCommand getInstance() {
        if (instance == null) {
            instance = new PauseAudioCommand();
        }
        return instance;
    }

    protected void doAction(Object param) {
        if (NedMidlet.getInstance().getAudioPlayer() != null) {
            NedMidlet.getInstance().getAudioPlayer().pause();
        }
    }
}
