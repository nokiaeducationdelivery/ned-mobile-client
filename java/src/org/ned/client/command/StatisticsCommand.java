package org.ned.client.command;

import com.sun.lwuit.Command;
import org.ned.client.NedResources;
import org.ned.client.view.StatisticsScreen;

public class StatisticsCommand extends NedCommand{

    private static StatisticsCommand exitCommand;

    private StatisticsCommand() {
        command = new Command(NedResources.MID_STATISTICS_COMMAND);
    }

    public static StatisticsCommand getInstance() {
        if( exitCommand == null) {
           exitCommand = new StatisticsCommand();
        }
        return exitCommand;
    }

    protected void doAction(Object param) {
        new StatisticsScreen().show();
    }
}
