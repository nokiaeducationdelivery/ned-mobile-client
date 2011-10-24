package org.ned.client;

import java.util.Vector;
import org.ned.client.utils.NedIOUtils;

public class Content implements IContent {

    private String text;
    private String entryId;
    private String parentId = null;
    private String data = null;
    private String description = null;
    private String type = null;
    private Vector keywords = null;
    private Vector externalLinks = null;
    private String version = null;
    private String videoFile = null;
    private String videoPath = null;

    public Content(String _text, String _id) {
        text = _text;
        entryId = _id;
        description = "";
    }

    public Content(String _oldFilePath) {
        description = "";
        videoFile = _oldFilePath;
    }

    public String getMediaFile() {

        if (videoFile == null && data != null && data.length() > 0) {
            videoFile = getMediaFilePath() + data;
        }
        return videoFile;
    }

    public String getId() {
        return entryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public boolean isDownloaded() {
        boolean result = false;
        if (data != null && !data.equals("")) {
            result = NedIOUtils.fileExists(getMediaFile());
        }
        return result;
    }

    public boolean isDownloaded(Vector directory) {
        boolean result = false;
        for ( int i = 0; i < directory.size() && !result; i++ ) {
            if (data != null && data.equals(directory.elementAt(i))) {
                result = true;
            }
        }
        return result;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String aText) {
        text = aText;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Vector getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(Vector externalLinks) {
        this.externalLinks = externalLinks;
    }

    public Vector getKeywords() {
        return keywords;
    }

    public void setKeywords(Vector keywords) {
        this.keywords = keywords;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getMediaFilePath() {
        if (videoPath == null) {
            videoPath = NedMidlet.getSettingsManager().getLibraryManager().
                    getCurrentLibrary().getDirUri() + "/"
                    + NedConsts.NedLocalConst.VIDEOSDIR;
        }
        return videoPath;
    }

    public void setVideoPath(String vPath) {
        videoPath = vPath;
    }

}
