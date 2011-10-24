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
