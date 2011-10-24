package org.ned.client.view;

import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.html.HTMLComponent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedResources;
import org.ned.client.command.BackHelpCommand;
import org.ned.client.lwuitExtended.NedRequestHandler;

public class HelpView extends NedFormBase implements ActionListener {

    private HTMLComponent comp = null;
    private Class caller = null;
    private Object params = null;

    private final static Hashtable classNames = new Hashtable();
    static {
        classNames.put(CategoryScreen.class, "CategoryScreen");
        classNames.put(CatalogScreen.class, "CatalogScreen");
        classNames.put(DownloadQueueScreen.class, "DownloadQueueScreen");
        classNames.put(DownloadOptionScreen.class, "DownloadOptionScreen");
        classNames.put(StatisticsOptionScreen.class, "StatisticsOptionScreen");
        classNames.put(StatisticsScreen.class, "StatisticsScreen");
        classNames.put(WelcomeScreen.class, "WelcomeScreen");
        classNames.put(LoginScreen.class, "LoginScreen");
        classNames.put(MediaItemsScreen.class, "MediaItemsScreen");
        classNames.put(SettingsScreen.class, "SettingsScreen");
        classNames.put(HistoryScreen.class, "HistoryScreen");
        classNames.put(LibraryManagerScreen.class, "LibraryManagerScreen");
        classNames.put(MainScreen.class, "MainScreen");
    }

    public HelpView(Object aParam) {

        if (aParam instanceof Class) {
            caller = (Class) aParam;
        } else {
            Object[] params = (Object[]) aParam;
            caller = (Class) params[0];
        }
        params = aParam;

        String viewName = getViewNameByClass(caller);
        String text = getHelpText(viewName);

        setLayout(new BorderLayout());
        setNedTitle( "Help for " + viewName );
        setScrollable(false);

        comp = new HTMLComponent(new NedRequestHandler());
        comp.setShowImages(true);
        comp.setScrollableX(false);
        comp.setScrollableY(true);
        comp.setBodyText(text);

        addComponent(BorderLayout.CENTER, comp);
        addCommand(BackHelpCommand.getInstance().getCommand());
        addCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == BackHelpCommand.getInstance().getCommand()) {
            comp.cancel();
            BackHelpCommand.getInstance().execute(params);
        }
    }

    private String getHelpText(String caller) {
        FileConnection fc = null;
        InputStream is = null;
        StringBuffer sb = null;

        String fileName = "/org/ned/client/help/".concat(caller);
        Class clazz = Runtime.getRuntime().getClass();

        try {
            is = clazz.getResourceAsStream(fileName);
            sb = new StringBuffer();
            int chars = 0;
            while ((chars = is.read()) != -1) {
                sb.append((char) chars);
            }
        } catch (IOException ex) {
            ex.printStackTrace();  // TODO?
        } catch (OutOfMemoryError ex) {
            GeneralAlert.show(NedResources.MEMORY_OUT, GeneralAlert.WARNING);
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
            try {
                fc.close();
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }

    private String getViewNameByClass(Class caller) {
            return (String)classNames.get( caller );
    }
}
