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
package org.ned.client.view;

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import org.ned.client.NedMidlet;
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
            splashImg = NedMidlet.getRes().getImage(imgName);
        } else {
            if (Display.getInstance().getDisplayHeight() > Display.getInstance().getDisplayWidth()) {
                imgName = splashes[3];
            } else {
                imgName = splashes[2];
            }
            splashImg = NedMidlet.getRes().getImage(imgName).scaled(
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
