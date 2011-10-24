package org.ned.client.view.customComponents;

import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.geom.Dimension;
import org.ned.client.view.style.NEDStyleToolbox;


public class ProgressBar extends Component {

    private int percent;

    public ProgressBar() {
    }

    public void setProgress(int aPercent) {
        percent = aPercent;
        repaint();
    }

    public String getUIID() {
        return "ProgressBar";
    }

    protected Dimension calcPreferredSize() {
        return new Dimension(Display.getInstance().getDisplayWidth(),
                Font.getDefaultFont().getHeight());
    }

    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int progressCompletedWidth = (int) ((((float) percent) / 100.0f) * width );
        int x = getX();
        int y = getY();

        int curve = getHeight() / 2 - 1;
        g.setColor( NEDStyleToolbox.DARK_GREY );//border
        g.drawRoundRect(x, y, width-1 , height-1 , curve, curve);

        g.setColor( NEDStyleToolbox.WHITE );//0% progress
        g.fillRoundRect( x+1, y+1, width-2, height-2, curve, curve );

        g.clipRect(x+1, y+1, progressCompletedWidth - 2, height - 2);
        g.setColor(getStyle().getFgColor());//progress color
        g.fillRoundRect( x+1 , y+1, width , height - 2, curve, curve);
    }
}
