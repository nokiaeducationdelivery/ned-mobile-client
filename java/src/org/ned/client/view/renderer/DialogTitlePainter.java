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

import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.plaf.UIManager;
import org.ned.client.view.style.NEDStyleToolbox;


public class DialogTitlePainter implements Painter {

    static final public int TITLE_LEFT_MARGIN = 5;
    static final public int TITLE_TOP_MARGIN = 5;
    static final private Font mFont = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM );

    private String mTitle = "";

    public void setTitle( String aTitle ) {
        mTitle = aTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getFontSize() {
        return mFont.getHeight();
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();

        int bgColor = NEDStyleToolbox.WHITE;//Dialog BG color
        g.setColor(bgColor);
        g.fillRect(rect.getX(), rect.getY(), width, height);

        int endColor = NEDStyleToolbox.BACKGROUND_END_COLOR;
        int startColor = NEDStyleToolbox.BACKGROUND_START_COLOR;

        g.fillLinearGradient(startColor, endColor, rect.getX()+1, rect.getY()+1, width-2, height, false);

        // curve left side
        g.fillRect(rect.getX()+1, rect.getY()+1, 1, 1);

        // curve right side
        g.fillRect(rect.getX()+width-2, rect.getY()+1, 1, 1);

        int tintColor = UIManager.getInstance().getLookAndFeel().getDefaultFormTintColor();
        g.setColor(tintColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));
        g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1, (byte) ((tintColor >> 24) & 0xff));

        g.setFont(mFont);
        g.setColor( NEDStyleToolbox.WHITE );
        g.drawString( mTitle, rect.getX() + TITLE_LEFT_MARGIN, rect.getY() + TITLE_TOP_MARGIN );
    }
}
