package org.ned.client.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import org.ned.client.NedResources;


public class SplashScreen extends NedFormBase {

    private Image splashImg;
    private String[] splashes = new String[] {
        "splash_screen_320x240",
        "splash_screen_240x320",
        "splash_screen_640x360",
        "splash_screen_360x640" };



    public SplashScreen() {
        super();
        String imgName = null;
        switch (Display.getInstance().getDisplayHeight()) {
            case 240:
                imgName = splashes[0];
                break;
            case 320:
                imgName = splashes[1];
                break;
            case 360:
                imgName = splashes[2];
                break;
            case 640:
                imgName = splashes[3];
                break;
            default:
                imgName = null;
                break;
        }
        
        if (imgName != null) {
            splashImg = NedResources.getRes().getImage(imgName);
        } else {
            if (Display.getInstance().getDisplayHeight() > Display.getInstance().getDisplayWidth()) {
                imgName = splashes[3];
            } else {
                imgName = splashes[2];
            }
            splashImg = NedResources.getRes().getImage(imgName).scaled(
                    Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        }
        fitImageIntoScreen();
        getStyle().setBgColor(0xdddbdb);
        
    }

    private void fitImageIntoScreen() {
        setFocusable(false);
        setLayout( new BorderLayout() );
        Label label = new Label( splashImg );
        label.setAlignment(Label.CENTER);
        setScrollable(false);
        label.getStyle().setMargin(0, 0, 0, 0);
        label.getStyle().setPadding(0, 0, 0, 0);
        addComponent(BorderLayout.CENTER, label );
    }
}
