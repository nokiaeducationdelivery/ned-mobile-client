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

import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import java.io.IOException;
import org.ned.client.view.style.NEDStyleToolbox;

public class TitleBarPainter implements Painter {

    private static final int textPadding = 2;
    private static final Font mSmallFont = Font.createSystemFont( Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL );
    private static final Font mFont = Font.createSystemFont( Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL );
    private static Image mLogo;
    private static int mLogoHeight = 0;
    private String mTitle1;
    private String mTitle2;

    public TitleBarPainter( String title1, String title2 ) {
        mTitle1 = title1;
        mTitle2 = title2;

        try {
            mLogo = Image.createImage( "/org/ned/client/ned_titlebar.png" );
            mLogoHeight = mLogo.getHeight();
        } catch ( IOException ioe ) {
            //do nothing
        }
    }

    public int getPrefferedH() {
        if ( mTitle1.length() == 0 && mTitle2.length() == 0 ) {
            return 0;
        }
        int fontsHight = 3 * textPadding + mSmallFont.getHeight() + mFont.getHeight();
        int logoHight = 2 * textPadding + mLogoHeight;
        return fontsHight < logoHight ? logoHight : fontsHight;
    }

    public void paint( Graphics g, Rectangle rect ) {
        Dimension d = rect.getSize();
        final int height = d.getHeight();
        final int width = d.getWidth();
        final int displayWidth = Display.getInstance().getDisplayWidth();
        g.fillLinearGradient( NEDStyleToolbox.WHITE, 0xe1e1e1, 0, 0, width, height - 3, false );

        g.setColor( 0xcdcdcd );
        g.drawLine( 0,
                    height - 2,
                    displayWidth,
                    height - 2 );

        g.setColor( 0xeaeaea );
        g.drawLine( 0,
                    height - 1,
                    displayWidth,
                    height - 1 );

        if ( mLogo != null ) {
            g.drawImage( mLogo, 20, textPadding );
        }

        g.setFont( mSmallFont );
        g.setColor( NEDStyleToolbox.MAIN_FONT_COLOR );
        g.drawString( mTitle1, 65, textPadding );
        g.setFont( mFont );
        g.drawString( mTitle2, 65, 2 * textPadding + mSmallFont.getHeight() );
    }

    public void setTitle1( String title1 ) {
        mTitle1 = title1;
    }

    public void setTitle2( String title2 ) {
        mTitle2 = title2;
    }
}
