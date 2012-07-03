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
package org.ned.client.view.renderer;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Vector;
import org.ned.client.Content;
import org.ned.client.IContent;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.transfer.IMediaItemListUpdater;
import org.ned.client.utils.MediaTypeResolver;
import org.ned.client.utils.NedIOUtils;
import org.ned.client.view.style.NEDStyleToolbox;

public class MediaItemsListCellRenderer extends ListCellRendererBase implements IMediaItemListUpdater{

    private static final int ICON_FIT_SIZE = 36;
    private static final int ICON_WIDTH = 32;
    private Label mFlag;
    private Label mMediaType;
    private int displayW;
    private static Image local;
    private static Image remote;
    private Vector fileLists = null;

    public MediaItemsListCellRenderer() {
        super();
        NedMidlet.getInstance().getDownloadManager().setMediaListUpdater(this);
        if (com.sun.lwuit.Display.getInstance().isTouchScreenDevice()) {
            getStyle().setPadding(10, 10, 2, 2);
            getSelectedStyle().setPadding(10, 10, 2, 2);
        }
        if (getPreferredH() < ICON_FIT_SIZE) {
            setPreferredH(ICON_FIT_SIZE);
        }
        displayW = Display.getInstance().getDisplayWidth();

        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        setWidth(displayW);
        setFocusable(true);
        mMediaType = new Label(" ");//must set some text to render properly
        mMediaType.getStyle().setPadding(0, 0, 0, 0);
        mMediaType.getStyle().setMargin(0, 0, 0, 0);
        mMediaType.getSelectedStyle().setPadding(0, 0, 0, 0);
        mMediaType.getSelectedStyle().setMargin(0, 0, 0, 0);
        mMediaType.setAlignment(Label.LEFT);
        mMediaType.getStyle().setBgTransparency(0);
        mMediaType.setPreferredW(ICON_WIDTH);
        mMediaType.setCellRenderer(true);

        mTitle.setPreferredW(displayW - 3 * ICON_WIDTH);
        removeComponent(mTitle);

        mFlag = new Label();
        mFlag.getStyle().setPadding(0, 0, 0, 0);
        mFlag.getStyle().setMargin(0, 0, 0, 0);
        mFlag.getSelectedStyle().setPadding(0, 0, 0, 0);
        mFlag.getSelectedStyle().setMargin(0, 0, 0, 0);
        mFlag.setAlignment(Label.RIGHT);
        mFlag.getStyle().setBgTransparency(0);
        mFlag.setPreferredW(ICON_WIDTH);
        mFlag.setCellRenderer(true);

        addComponent(mMediaType);
        addComponent(mTitle);
        addComponent(mFlag);

        local = NedMidlet.getRes().getImage("Local");
        remote = NedMidlet.getRes().getImage("Remote");
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        IContent content = (IContent) value;

        mMediaType.setText("");
        mMediaType.setIcon(null);
        mMediaType.setIcon(MediaTypeResolver.getTypeIcon(content.getType()));

        mTitle.setText(content.getText());

        if (content instanceof Content) {

            if (fileLists == null) {
                fileLists = NedIOUtils.directoryListing(((Content) content).getMediaFilePath());
            }

            if ( fileLists != null && ((Content) content).isDownloaded(fileLists)) {
                mFlag.setIcon(local);
            } else {
                mFlag.setIcon(remote);
            }
        }

        if (isSelected) {
            setFocus(true);
            mTitle.getStyle().setFgColor(NEDStyleToolbox.WHITE);
            getStyle().setBgPainter(mSelectedPainter);
        } else {
            setFocus(false);
            mTitle.getStyle().setFgColor(NEDStyleToolbox.MAIN_FONT_COLOR);
            getStyle().setBgPainter(mUnselectedPainter);
        }
        return this;
    }

    public void updateMediaList() {
        fileLists = null;
    }
}
