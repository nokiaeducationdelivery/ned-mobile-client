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

    public void paint(Graphics g, Rectangle rect) {
        int height = rect.getSize().getHeight();
        int width = rect.getSize().getWidth();
        int x = rect.getX();
        int y = rect.getY();
        g.fillLinearGradient( NEDStyleToolbox.BACKGROUND_START_COLOR, NEDStyleToolbox.BACKGROUND_END_COLOR,
                              x, y,
                              width, height,
                              false);

        g.setColor( mBgColor );
        g.fillRect( x, y, 1, 1 );
        g.fillRect( x + width - 1, y, 1, 1 );
        g.fillRect( x, y + height - 1, 1, 1 );
        g.fillRect( x + width - 1, y + height - 1, 1, 1 );
    }
}
