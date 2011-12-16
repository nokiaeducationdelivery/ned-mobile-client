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
package org.ned.client.command;

import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.UIManager;


public abstract class BackNedCommand extends NedCommand {

    public void execute( Object aParam ) {
        doBefore();
        doLog( aParam );
        doAction( aParam );
        doAfter();
    }

    public void doBefore() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 500));
    }

    public void doAfter() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
    }

}
