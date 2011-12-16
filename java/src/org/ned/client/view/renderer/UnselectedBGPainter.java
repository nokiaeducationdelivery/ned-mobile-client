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

public class UnselectedBGPainter implements Painter {

    public void paint( Graphics g, Rectangle rect ) {
        int height = rect.getSize().getHeight();
        int width = rect.getSize().getWidth();
        int x = rect.getX();
        int y = rect.getY();

        g.setColor( NEDStyleToolbox.MAIN_BG_COLOR );
        g.fillRect( rect.getX(), rect.getY(), rect.getSize().getWidth(), height );

        //separator line
        g.setColor( 0xCDCDCD );
        g.drawLine( x ,y + height - 2, x + width, y + height - 2 );
        g.setColor( 0xEAEAEA );
        g.drawLine( x, y + height - 1, x + width, y + height - 1 );
    }
}