package org.ned.client.library;

import org.ned.client.NedConsts.NedLocalConst;
import org.ned.client.XmlManager;
import org.ned.client.utils.NedIOUtils;
import org.kxml2.kdom.Element;

public class NedLibrary {

    private String title;
    private String id;
    private boolean visible = true;
    private boolean downloaded = false;
    public String version = null;
    private int catalogCount = 0;


    public NedLibrary(String id, String title, String version) {
        this.id = id;
        this.title = title;
        this.version = version;
    }

    public NedLibrary(String id, String title, boolean visible) {
        this.id = id;
        this.title = title;
        this.visible = visible;
    }

    public NedLibrary(Element element) {
        for (int j = 0; j < element.getChildCount(); j++) {
            Element pelement = element.getElement(j);
            if(pelement == null){
                continue;
            }
            if (pelement.getName().equals("title")) {
                this.title = pelement.getText(0);
            }
            if (pelement.getName().equals("id")) {
                this.id = pelement.getText(0);
            }
            if (pelement.getName().equals("visible")) {
                if (pelement.getText(0).compareTo("0") == 0) {
                    this.visible = false;
                }
            }
            if(pelement.getName().equals("version")){
                this.version = pelement.getText(0);
            }


            setCatalogCount();
        }
    }

    public void setDownloaded(boolean val){
        downloaded = val;
    }

    public boolean isDownloaded(){
        return downloaded;
    }


    public int getCatalogCount() {
        return catalogCount;
    }

    public void setCatalogCount() {
        if(NedIOUtils.fileExists( getFileUri())){
            downloaded = true;
            catalogCount = (XmlManager.getContentChilds( getId(), getFileUri() ) ).size();
        }
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        return id.equals(((NedLibrary) obj).id);
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    public String getId() {
        return id;
    }

    public final String getFileUri() {
        String file = NedIOUtils.getUserRootDirectory()
                + id
                + "/"
                + NedLocalConst.LIBRARYFILE;
        return file;
    }

        public String getDirUri() {
        String dir = NedIOUtils.getUserRootDirectory()
                + id;
        return dir;
    }


    public int getVersionInt(){
        int retVal = 0;
        if(version != null){
            retVal = Integer.parseInt(version);
        }
        return retVal;
    }

    public void setVersion(String version){
        this.version = version;
    }

    public String getVersion(){
        return version;
    }
}
