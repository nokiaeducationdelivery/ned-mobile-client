package org.ned.client.view.renderer;

import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import org.ned.client.view.style.NEDStyleToolbox;


public class ListSelectedBGPainter implements Painter {

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();
        int x = rect.getX();
        int y = rect.getY();
        g.fillLinearGradient( NEDStyleToolbox.BACKGROUND_START_COLOR, NEDStyleToolbox.BACKGROUND_END_COLOR,
                              x, y, width, height - 3, false );

        //round corners
        g.setColor( NEDStyleToolbox.MAIN_BG_COLOR );
        g.fillRect( x, y, 1, 1 );
        g.fillRect( x + width - 1, y, 1, 1);
        g.fillRect( x, y+ height - 4, 1, 1 );
        g.fillRect( x + width - 1, y + height -4 , 1, 1 );

        //separator line
        g.setColor( 0xCDCDCD );
        g.drawLine( x ,y + height - 2, x + width, y + height - 2 );
        g.setColor( 0xEAEAEA );
        g.drawLine( x, y + height - 1, x + width, y + height - 1 );
    }
}
