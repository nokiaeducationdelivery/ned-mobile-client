package org.ned.client.library;

import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;

public class LibraryManager implements ListModel {

    private Vector librariesList = new Vector();
    private Vector selectionListeners = new Vector();
    private Vector dataChangeListeners = new Vector();
    private int selectedIndex = -1;

    public LibraryManager() {
    }

    public boolean addLibrary(NedLibrary lib) {
        boolean retval = false;
        if (findLibrary(lib.getId()) == null) {
            addItem(lib);
            retval = true;
        }
        //todo check duplicates;
        return retval;
    }

    public NedLibrary getLibraryByUrl(String url) {
        //find libraries
        return null;
    }

    public NedLibrary findLibrary(String id) {
        for (int i = 0; i < librariesList.size(); i++) {
            if (((NedLibrary) librariesList.elementAt(i)).getId().equals(id)) {
                return (NedLibrary) librariesList.elementAt(i);
            }
        }
        return null;
    }

    void selectLibrary(String id) {
        for (int i = 0; i < librariesList.size(); i++) {
            if (((NedLibrary) librariesList.elementAt(i)).getId().equals(id)) {
                setSelectedIndex(i);
            }
        }
    }

    public Vector getLibrariesList() {
        return librariesList;
    }

    public Vector getVisibleLibrariesList() {
        Vector vLibraries = new Vector();
        for (int i = 0; i < librariesList.size(); i++) {
            if (((NedLibrary) librariesList.elementAt(i)).getVisible() == true) {
                vLibraries.addElement((NedLibrary) librariesList.elementAt(i));
            }
        }
        return vLibraries;
    }

    public Object getItemAt(int i) {
        return (i < librariesList.size() && i >= 0)
                ? librariesList.elementAt(i)
                : null;
    }

    public int getSize() {
        return librariesList.size();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public NedLibrary getCurrentLibrary() {
        if (librariesList.size() > 0) {
            return (NedLibrary) librariesList.elementAt(selectedIndex);
        } else {
            return null;
        }
    }

    public void setSelectedIndex(int i) {
        for (int j=0; j<selectionListeners.size(); j++) {
            ((SelectionListener)selectionListeners.elementAt(j)).selectionChanged(selectedIndex, i);
        }
        selectedIndex = i;
    }

    public void addDataChangedListener(DataChangedListener dl) {
        dataChangeListeners.addElement(dl);

    }

    public void removeDataChangedListener(DataChangedListener dl) {
        dataChangeListeners.removeElement(dl);
    }

    public void addSelectionListener(SelectionListener sl) {
        selectionListeners.addElement(sl);
    }

    public void removeSelectionListener(SelectionListener sl) {
        selectionListeners.removeElement(sl);
    }

    public void addItem(Object o) {
        librariesList.addElement(o);
        for (int i = 0; i < dataChangeListeners.size(); i++) {
            ((DataChangedListener) dataChangeListeners.elementAt(i)).dataChanged(DataChangedListener.ADDED, librariesList.size() - 1);
        }
    }

    public void removeItem(int index) {
        if(index >= 0 && index < librariesList.size() )
        {
        librariesList.removeElementAt(index);
        for (int i = 0; i < dataChangeListeners.size(); i++) {
            ((DataChangedListener) dataChangeListeners.elementAt(i)).dataChanged(DataChangedListener.REMOVED, index);
        }
        }
    }
}
