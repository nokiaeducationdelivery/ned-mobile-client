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

import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import org.ned.client.view.style.NEDStyleToolbox;

public class SelectedBGPainter implements Painter {

    private final int mBgColor;

    public SelectedBGPainter( int aBgColor ) {
        mBgColor = aBgColor;
    }

    public void paint( Graphics g, Rectangle rect ) {
        final int height = rect.getSize().getHeight();
        final int width = rect.getSize().getWidth();
        final int x = rect.getX();
        final int y = rect.getY();
        g.fillLinearGradient( NEDStyleToolbox.BACKGROUND_START_COLOR, NEDStyleToolbox.BACKGROUND_END_COLOR,
                              x, y,
                              width, height,
                              false );

        g.setColor( mBgColor );
        g.fillRect( x, y, 1, 1 );
        g.fillRect( x + width - 1, y, 1, 1 );
        g.fillRect( x, y + height - 1, 1, 1 );
        g.fillRect( x + width - 1, y + height - 1, 1, 1 );
    }
}
