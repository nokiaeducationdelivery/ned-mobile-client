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
package org.ned.client.library.advanced;

import java.util.Enumeration;
import java.util.Vector;

public class KeywordFilter implements IFilter {

    private String mKeyword;

    public KeywordFilter( String aKeyword ) {
        mKeyword = aKeyword.trim().toLowerCase();
    }

    public boolean isMatch( LibraryElement aElement ) {
        if ( aElement.getName().toLowerCase().indexOf( mKeyword ) >= 0 ) {
            return true;// ugly but more efficient
        }

        Vector keyWords = aElement.getDetails().getKeywords();
        if ( keyWords != null ) {
            Enumeration en = keyWords.elements();
            while ( en.hasMoreElements() ) {
                if ( ((String)en.nextElement()).toLowerCase().indexOf( mKeyword )
                        >= 0 ) {
                    return true;
                }
            }
        }
        return false;
    }
}
