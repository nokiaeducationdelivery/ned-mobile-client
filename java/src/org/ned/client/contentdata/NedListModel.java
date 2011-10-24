package org.ned.client.contentdata;

import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.DefaultListModel;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.NedConsts;
import org.ned.client.XmlManager;

public class NedListModel extends DefaultListModel {

    private int selectedIndex = -1;
    private Vector browsedContent = new Vector();
    private Vector selectionListeners = new Vector();

    public Object getItemAt(int i) {
        Object retval = null;
        if(i >= 0 && i < browsedContent.size())
        {
            retval = browsedContent.elementAt(i);
        }
        return retval;
    }

    public int getSize() {
        return browsedContent.size();
    }


    public void sortByType(){
        Vector video = new Vector();
        Vector audio = new Vector();
        Vector picture = new Vector();
        Vector text = new Vector();
        Vector rest = new Vector();

        Content content = null;
        for (int i = 0; i < browsedContent.size(); i++) {
            content = (Content) browsedContent.elementAt(i);
            if (content.getType().equals(NedConsts.NedContentType.VIDEO)) {
                video.addElement(content);
            } else if (content.getType().equals(NedConsts.NedContentType.AUDIO)) {
                audio.addElement(content);
            } else if (content.getType().equals(NedConsts.NedContentType.IMAGE)) {
                picture.addElement(content);
            } else if (content.getType().equals(NedConsts.NedContentType.TEXT)) {
                text.addElement(content);
            } else {
                rest.addElement(content);
            }
        }
        for(int i = 0; i<video.size(); i++){
            rest.addElement(video.elementAt(i));
        }
        for(int i = 0; i<audio.size(); i++){
            rest.addElement(audio.elementAt(i));
        }
        for(int i = 0; i<picture.size(); i++){
            rest.addElement(picture.elementAt(i));
        }
        for(int i = 0; i<text.size(); i++){
            rest.addElement(text.elementAt(i));
        }

        browsedContent = rest;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int index) {
        for (int j=0; j<selectionListeners.size(); j++) {
            ((SelectionListener)selectionListeners.elementAt(j)).selectionChanged(selectedIndex, index);
        }
        this.selectedIndex = index;
    }

   public void addSelectionListener(SelectionListener sl) {
        selectionListeners.addElement(sl);
    }

    public void removeSelectionListener(SelectionListener sl) {
        selectionListeners.removeElement(sl);
    }

    public void addItem(Object newElement) {
        browsedContent.addElement(newElement);
    }

    public void removeItem(int index) {
        browsedContent.removeElementAt(index);
    }

    public void loadNode(String contentId) {
        browsedContent = XmlManager.getContentChilds(contentId, null);
    }

    public void loadFilteredAllChilds(String contentId, String filter) {
        Vector all = XmlManager.getContentAllChilds(contentId);

        Content tempContent = null;
        for (int i = 0; i < all.size(); i++) {
            tempContent = (Content) all.elementAt(i);
            if ( applyFilter(filter, tempContent) ) {
               browsedContent.addElement(tempContent);
            }
        }
    }

    private boolean applyFilter(String key, Content content) {
        boolean result = false;
        key = key.trim().toLowerCase();

        if (content.getText().toLowerCase().indexOf(key) >= 0) {
            result = true;
            return result;   // ugly but more efficient
        }
        Vector keyWords = content.getKeywords();
        if (keyWords != null) {
            for (int i = 0; i < keyWords.size(); i++) {
                if (((String) keyWords.elementAt(i)).toLowerCase().indexOf(key) >= 0) {
                    result = true;
                }
            }
        }
        return result;
    }
}
