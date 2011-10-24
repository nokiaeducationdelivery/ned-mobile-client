package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.StatisticsOptionScreen;


public class StatisticsOptionsCommand extends NedCommand {
    private static StatisticsOptionsCommand instance;

    private StatisticsOptionsCommand() {
        command = new Command( NedResources.STATISTICS_OPTIONS );
    }

    public static StatisticsOptionsCommand getInstance() {
        if( instance == null ) {
            instance = new StatisticsOptionsCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        new StatisticsOptionScreen().show();
    }
}
