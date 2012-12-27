/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.utils;

import java.util.Vector;
import org.ned.client.library.advanced.LibraryElement;

public class Sort {

    public static void sort( Vector/*<LibraryElement>*/ list ) {
        quicksort( list, 0, list.size() - 1 );
    }

    private static void quicksort( Vector list, int p, int r ) {
        if ( p < r ) {
            int q = qpartition( list, p, r );
            if ( q == r ) {
                q--;
            }
            quicksort( list, p, q );
            quicksort( list, q + 1, r );
        }
    }

    private static int qpartition( Vector list, int p, int r ) {
        String itemName = ((LibraryElement)list.elementAt( p )).getName();
        int lo = p;
        int hi = r;

        while ( true ) {
            while ( ((LibraryElement)list.elementAt( hi )).getName().compareTo( itemName ) >= 0 && lo < hi ) {
                hi--;
            }
            while ( ((LibraryElement)list.elementAt( lo )).getName().compareTo( itemName ) < 0 && lo < hi ) {
                lo++;
            }
            if ( lo < hi ) {
                Object toBeMoved = list.elementAt( lo );
                list.setElementAt( list.elementAt( hi ), lo );
                list.setElementAt( toBeMoved, hi );
            } else {
                return hi;
            }
        }
    }
}
