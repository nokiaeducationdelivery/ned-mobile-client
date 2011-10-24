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
