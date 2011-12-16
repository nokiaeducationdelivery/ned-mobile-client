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

import java.util.Vector;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.library.NedLibrary;
import org.ned.client.library.LibraryManager;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.utils.NedXmlUtils;
import org.ned.client.view.GeneralAlert;

public class SettingsManager {

    private NedMidlet midlet = null;
    private String userName = null;
    private String dlTime = "06:30AM";
    private boolean dlOn = false;
    private boolean dlAutomatic = false;
    private boolean autoStatSend = true;
    private LibraryManager libraryManager = null;
    int oldUrlSelection = 0;

    public SettingsManager(NedMidlet _midlet) {
        midlet = _midlet;
        libraryManager = new LibraryManager();
    }

    public void setDlTime(String time) {
        if (time.length() >= dlTime.length()) {
            dlTime = time;
        } else {
            dlTime = time + dlTime.substring(time.length());
        }
    }

    public String getDlTime() {
        return dlTime;
    }

    public void setDlHour(String hour) {
        try {
            int tmp = Integer.parseInt(hour);
            if (tmp < 10) {
                setDlTime("0" + String.valueOf(tmp) + ":" + getDlMin() + getDlAmPm());
            } else if (hour.length() == 2) {
                setDlTime(hour + ":" + getDlMin() + getDlAmPm());
            }
        } catch (NumberFormatException nfe) {
        }
    }

    public void setDlMin(String min) {
        try {
            int tmp = Integer.parseInt(min);
            if (tmp < 10) {
                setDlTime(getDlHour() + ":0" + String.valueOf(tmp) + getDlAmPm());
            } else if (min.length() == 2) {
                setDlTime(getDlHour() + ":" + min + getDlAmPm());
            }
        } catch (NumberFormatException nfe) {
        }
    }

    public void setDlAmPm(String ampm) {
        if (ampm.length() == 2) {
            setDlTime(getDlHour() + ":" + getDlMin() + ampm);
        }
    }

    public String getDlHour() {
        return dlTime.substring(0, 2);
    }

    public String getDlMin() {
        return dlTime.substring(3, 5);
    }

    public String getDlAmPm() {
        return dlTime.substring(5, 7);
    }

    public boolean getDlAutomatic() {
        return dlAutomatic;
    }

    public void setDlAutomatic(boolean auto) {
        dlAutomatic = auto;
    }

    public boolean getDlState() {
        return dlOn;
    }

    public boolean isSchedulerOn() {
        return dlOn;
    }

    public void setDlState(boolean state) {
        dlOn = state;
    }

    public String getUser() {
        return userName;
    }

    public void setUser(String newUser) {
        userName = newUser;
    }

    public void resetServer() {
        loadSettings();
    }

    public boolean loadSettings() {
        if (NedIOUtils.fileExists(NedIOUtils.getSettingsFile()) ) {
            Element rootElement = null;
            try {
                Document doc = NedXmlUtils.getDocFile(NedIOUtils.getSettingsFile());
                if (doc == null) {
                    NedIOUtils.removeFile(NedIOUtils.getSettingsFile());
                    return saveSettings();
                } else {
                    rootElement = doc.getRootElement();
                }
            } catch (Exception ex) {
                GeneralAlert.show( "load settings err: " + ex.getMessage(), GeneralAlert.ERROR );
            }
            for (int i = 0; i < rootElement.getChildCount(); i++) {
                if (rootElement.getType(i) != Node.ELEMENT) {
                    continue;
                }
                Element element = rootElement.getElement(i);
                if (element.getName().equals("Library")) {
                    libraryManager.addLibrary(new NedLibrary(element));
                }

                if (element.getName().equals("Schedule")) {
                    setDlTime(element.getText(0));
                    if (element.getAttributeValue("", "state").equals("on")) {
                        setDlState(true);
                    } else {
                        setDlState(false);
                    }
                    String auto = element.getAttributeValue("", "automatic");
                    if ((auto != null) && (auto.equals("yes"))) {
                        setDlAutomatic(true);
                    } else {
                        setDlAutomatic(false);
                    }
                    continue;
                }
                if( element.getName().equals("Statistics") ) {
                    if ( element.getAttributeValue("", "auto") != null
                      && element.getAttributeValue("", "auto").equals("no")) {
                        setAutoStatSend(false);
                    } else {
                        setAutoStatSend(true);
                    }
                }
            }
        } else {
            return saveSettings();
        }
        return true;
    }

    public boolean saveSettings() {
        Document doc = new Document();
        Element settings = doc.createElement("", "Settings");
        Element schedule = doc.createElement("", "Schedule");
        Element stats = doc.createElement("", "Statistics");

        // Element thisLibrary = doc.createElement("", "ActualLibrary");

        String time = getDlTime();

        schedule.addChild(Node.TEXT, time);
        String state = "off";
        if (getDlState()) {
            state = "on";
        }
        String automatic = "no";
        if (getDlAutomatic()) {
            automatic = "yes";
        }
        schedule.setAttribute("", "state", state);
        schedule.setAttribute("", "automatic", automatic);

        String autoSend ="yes";
        if ( !getAutoStatSend() ) {
            autoSend = "no";
        }
        stats.setAttribute("", "auto", autoSend );

        Element[] libraries = createLibraryXml(doc);
        for (int i = 0; i < libraries.length; i++) {
            settings.addChild(Node.ELEMENT, libraries[i]);
        }
        settings.addChild(Node.ELEMENT, schedule);
        settings.addChild(Node.ELEMENT, stats);
        doc.addChild(Node.ELEMENT, settings);
        NedXmlUtils.writeXmlFile(NedIOUtils.getSettingsFile(), doc);

        return true;
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    private Element[] createLibraryXml(Document doc) {
        Vector librariesList = libraryManager.getLibrariesList();
        Element[] retval = new Element[librariesList.size()];
        for (int i = 0; i < librariesList.size(); i++) {
            Element pLibrary = doc.createElement("", "Library");
            Element pElement = null;

            pElement = doc.createElement("", "title");
            pElement.addChild(Node.TEXT, ((NedLibrary) librariesList.elementAt(i)).getTitle());
            pLibrary.addChild(Node.ELEMENT, pElement);

            pElement = doc.createElement("", "id");
            pElement.addChild(Node.TEXT,
            ((NedLibrary) librariesList.elementAt(i)).getId() );
            pLibrary.addChild(Node.ELEMENT, pElement);

            pElement = doc.createElement("", "visible");
            if(((NedLibrary) librariesList.elementAt(i)).getVisible() == true) {
                pElement.addChild(Node.TEXT, "1");
            } else {
                pElement.addChild(Node.TEXT, "0");
            }
            pLibrary.addChild(Node.ELEMENT, pElement);

            pElement = doc.createElement("", "version");
            String version = ((NedLibrary) librariesList.elementAt(i)).getVersion();
            if(version == null || version.equals(""))
            {
                pElement.addChild(Node.TEXT, "0");
            }else{
                pElement.addChild(Node.TEXT, version);
            }
            pLibrary.addChild(Node.ELEMENT, pElement);
            
            retval[i] = pLibrary;
        }
        return retval;
    }

    public boolean getAutoStatSend() {
        return autoStatSend;
    }

    public void setAutoStatSend(boolean state) {
        autoStatSend = state;
    }
}
