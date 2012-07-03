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

import com.sun.lwuit.List;
import com.sun.lwuit.events.SelectionListener;
import java.util.Vector;
import org.ned.client.view.renderer.ItemAnimatedListCellRenderer;

/**
 *
 * @author damian.janicki
 */
public class AnimatedList extends List implements SelectionListener {

    private static final int ANIMATION_DELAY = 300;
    private static final int ANIMATION_START_DELAY = 1000;
    private long tickTime = System.currentTimeMillis();
    private int lastSelection = -1;
    private ItemAnimatedListCellRenderer renderer = null;
    private ListAnimation la;

    public AnimatedList( Vector list ) {
        super( list );
        renderer = new ItemAnimatedListCellRenderer();
        setRenderer( renderer );
        addSelectionListener( this );
    }

    public void startAnimation() {
        la = new ListAnimation();
        la.setList( this );
        Thread tAnimate = new Thread( la );
        renderer.resetPosition();
        tickTime = System.currentTimeMillis();
        tAnimate.start();
    }

    public void stopAnimation() {
        if ( la != null ) {
            la.stop();
        }
    }

    public boolean animate() {
        boolean val = super.animate();
        if ( hasFocus() ) {
            long currentTime = System.currentTimeMillis();
            if ( currentTime - tickTime > ANIMATION_START_DELAY ) { // index!=0 to avoid a hw bug
                if ( lastSelection == getSelectedIndex() ) {
                    renderer.incrementPosition();
                    repaint();
                } else {
                    lastSelection = getSelectedIndex();
                    renderer.resetPosition();
                }
                val = true;
            }
        } else {
            renderer.resetPosition();
            stopAnimation();
        }
        return val;
    }

    static class ListAnimation implements Runnable {

        private AnimatedList list;
        private boolean stop = false;

        public void setList( AnimatedList _list ) {
            list = _list;
        }

        public void stop() {
            stop = true;
        }

        public void run() {
            while ( !stop ) {
                try {
                    list.animate();
                    Thread.sleep( ANIMATION_DELAY );
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void selectionChanged( int i, int i1 ) {
        stopAnimation();
        startAnimation();
        lastSelection = getSelectedIndex();
    }
}
