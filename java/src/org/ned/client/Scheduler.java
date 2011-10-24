package org.ned.client;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import org.ned.client.view.GeneralAlert;

public class Scheduler {

    private NedMidlet midlet = null;
    private Timer timer = null;
    private boolean timerRunning;
    private Task task;
    private final long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    private class Task extends TimerTask {

        public void run() {
                System.out.println("Starting task!!!");
                midlet.getDownloadManager().startDownloads();
        }
    }

    public Scheduler(NedMidlet _midlet) {
        midlet = _midlet;
    }

    public void startTask() {
        //first cancel any running task
        stopTask();

        //compute data from settings     
        Calendar calendar = Calendar.getInstance();
        System.out.println("Current Time: " + calendar.getTime().toString());

        int hour12 = Integer.parseInt(midlet.getSettingsManager().getDlHour());
        int hour = hour12;

        //convert to 24 hour time
        if (midlet.getSettingsManager().getDlAmPm().equals("PM")) {
            if (hour < 12) {
                hour += 12;
            }
        } else {
            if (hour == 12) {
                hour = 0;
            }
        }

        int minute = Integer.parseInt(midlet.getSettingsManager().getDlMin());
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);

        System.out.println("Schedule Time: " + calendar.getTime());

        timer = new Timer();
        task = new Task();

        long delay = calendar.getTime().getTime() - Calendar.getInstance().getTime().getTime();
        if( delay >= 0 ) {
            timer.schedule(task, delay,ONCE_PER_DAY);
        } else {
            timer.schedule(task, Math.abs(delay), ONCE_PER_DAY);
        }


        timerRunning = true;
        String minutestring = String.valueOf(minute);
        if (minute < 10) {
            minutestring = "0" + minutestring;
        }
        String strHour12 = (hour12 < 10) ? "0" + String.valueOf(hour12) : String.valueOf(hour12);
        Object[] param = {strHour12, minutestring, midlet.getSettingsManager().getDlAmPm()};

        GeneralAlert.show( Localization.getMessage("SCH_MESSAGE", param), GeneralAlert.INFO );
        midlet.getSettingsManager().setDlState(true);
    }

    public void stopTask() {
        if (timerRunning) {
            task.cancel();
            timer.cancel();
            midlet.getSettingsManager().setDlState(false);
            timerRunning = false;
        }
    }
}
