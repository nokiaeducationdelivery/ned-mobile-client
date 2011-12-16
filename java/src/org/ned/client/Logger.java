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
package org.ned.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class Logger {

    private static Logger instance = new Logger();
    private FileConnection logFile = null;
    private OutputStream output;
    //private String localRoot = "file:///root1/Data/NokiaECD/";
    private String localRoot = "file:///E:/Data/NokiaECD.2java/";

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    private void openLogFile() throws IOException {
        if (logFile == null) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String strDay = (day < 10 ? "0" + Integer.toString(day) : Integer.toString(day));
            int month = cal.get(Calendar.MONTH) + 1;
            String strMonth = (month < 10 ? "0" + Integer.toString(month) : Integer.toString(month));
            int year = cal.get(Calendar.YEAR);
            long time = cal.getTime().getTime();

            logFile = (FileConnection) Connector.open(localRoot + "logging_" + year + strMonth + strDay + time + ".txt");
            if (!logFile.exists()) {
                logFile.create();
            }
            output = logFile.openOutputStream(logFile.fileSize());
        }
    }

    public void log(double n) {
        log(String.valueOf(n), false);
    }

    public void log(String s) {
        log(s, false);
    }

    public void log(String a, String b) {
        log(a);
        log(b);
        log("\r\n");
    }

    public void logException(String s) {
        logException(s, false);
    }

    private void log(String s, boolean close) {
        boolean logSupport = true;

        if (logSupport) {
            try {
                openLogFile();
                output.write((s + "\r\n").getBytes("UTF-8"));
                output.flush();

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                clearConnections(close);
            }
        }
    }

    private void logException(String s, boolean close) {
        try {
            openLogFile();
            output.write((s + "\r\n").getBytes("UTF-8"));
            output.flush();



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            clearConnections(close);
        }
    }

    private void clearConnections(boolean close) {
        if (close) {
            try {
                output.close();
                logFile.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void emul(String message, Object o) {
        System.out.println(message + o);
    }

    public void emul(String message, double n) {
        System.out.println(message + n);
    }
}
