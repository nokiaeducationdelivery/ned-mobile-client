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
