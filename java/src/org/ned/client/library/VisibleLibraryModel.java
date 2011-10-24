package org.ned.client.library;

import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;


public class VisibleLibraryModel implements ListModel {

    private LibraryManager libraryManager;
    private Vector currentLibraries = new Vector();
    private Vector selectionListeners = new Vector();
    private Vector dataChangeListeners = new Vector();
    private int selectedIndex = -1;

    public VisibleLibraryModel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        currentLibraries = libraryManager.getVisibleLibrariesList();
    }

    public Object getItemAt(int i) {
        return (i < currentLibraries.size() && i >= 0)
                ? currentLibraries.elementAt(i)
                : null;
    }

    public int getSize() {
        return currentLibraries.size();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public NedLibrary getCurrentLibrary() {
        return (NedLibrary) currentLibraries.elementAt(selectedIndex);
    }

    public void setSelectedIndex(int i) {
        selectedIndex = i;
        if(selectedIndex < currentLibraries.size() && selectedIndex >= 0)
        {
            NedLibrary selected = (NedLibrary) currentLibraries.elementAt(selectedIndex);
            libraryManager.selectLibrary(selected.getId());
        }

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
        //this should never be called
        throw new RuntimeException();
    }

    public void removeItem(int i) {
        //this should never be called
        throw new RuntimeException();
    }
}
