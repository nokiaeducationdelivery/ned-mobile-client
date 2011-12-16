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
package org.ned.client.view.customComponents;

import org.ned.client.view.renderer.SelectedBGPainter;
import org.ned.client.view.style.NEDStyleToolbox;


public class RadioButton extends com.sun.lwuit.RadioButton {

    private static SelectedBGPainter selectedPainter = new SelectedBGPainter( NEDStyleToolbox.MAIN_BG_COLOR );

    public RadioButton() {
        super();
        initStyle();
    }

    public RadioButton( String aText ) {
        super( aText );
        initStyle();
    }

    private void initStyle() {
        getSelectedStyle().setBgPainter( selectedPainter );
    }
}
