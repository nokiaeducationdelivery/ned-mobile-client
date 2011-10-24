package org.ned.client.view.customComponents;

import org.ned.client.view.style.NEDStyleToolbox;

/**
 *
 * @author damian.janicki
 */
public class CheckBox extends com.sun.lwuit.CheckBox{
        
    public CheckBox(){
        super();
        initStyle();
    }
    
    public CheckBox(String text){
        super(text);
        initStyle();
    }
    
    private void initStyle() {        
        getStyle().setFgColor(NEDStyleToolbox.MAIN_FONT_COLOR);
    }   
}
