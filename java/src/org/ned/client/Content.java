/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client;

import java.util.Enumeration;
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

    public Content( String _text, String _id ) {
        text = _text;
        entryId = _id;
        description = "";
    }

    public Content( String _oldFilePath ) {
        description = "";
        videoFile = _oldFilePath;
    }

    public String getMediaFile() {

        if ( videoFile == null && data != null && data.length() > 0 ) {
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

    public void setParentId( String parentId ) {
        this.parentId = parentId;
    }

    public void setData( String data ) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public boolean isDownloaded() {
        boolean result = false;
        if ( data != null && !data.equals( "" ) ) {
            result = NedIOUtils.fileExists( getMediaFile() );
        }
        return result;
    }

    public boolean isDownloaded( Vector directory ) {
        Enumeration en = directory.elements();
        final String dataL = this.data;
        while ( en.hasMoreElements() ) {
            String el = (String)en.nextElement();
            if ( dataL != null && dataL.equals( el ) ) {
                return true;
            }
        }
        return false;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText( String aText ) {
        text = aText;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Vector getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks( Vector externalLinks ) {
        this.externalLinks = externalLinks;
    }

    public Vector getKeywords() {
        return keywords;
    }

    public void setKeywords( Vector keywords ) {
        this.keywords = keywords;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getMediaFilePath() {
        if ( videoPath == null ) {
            videoPath = NedMidlet.getSettingsManager().getLibraryManager().
                    getCurrentLibrary().getDirUri() + "/"
                    + NedConsts.NedLocalConst.VIDEOSDIR;
        }
        return videoPath;
    }

    public void setVideoPath( String vPath ) {
        videoPath = vPath;
    }
}
