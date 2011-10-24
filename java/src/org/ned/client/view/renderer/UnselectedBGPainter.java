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