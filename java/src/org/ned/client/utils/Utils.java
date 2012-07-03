/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.utils;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class Utils {

    private static Random rng = new Random();

    public static String generateId() {
        int id = 1000000 + rng.nextInt( 8999999 );
        String sid = String.valueOf( id );
        return ("id" + sid);
    }

    public static Vector split( String sb, String splitter ) {
        Vector strings = new Vector( 4, 4 );
        int splitterLength = splitter.length();
        int initialIndex = 0;
        int indexOfSplitter = sb.indexOf( splitter, initialIndex );
        if ( -1 == indexOfSplitter ) {
            strings.addElement( sb.trim() );
            return strings;
        }
        while ( -1 != indexOfSplitter ) {
            String substring = sb.substring( initialIndex, indexOfSplitter );
            initialIndex = indexOfSplitter + splitterLength;
            indexOfSplitter = sb.indexOf( splitter, indexOfSplitter + 1 );
            strings.addElement( substring.trim() );
        }

        if ( initialIndex + splitterLength <= sb.length() ) {
            String substring = sb.substring( initialIndex, sb.length() );
            strings.addElement( substring.trim() );
        }
        return strings;
    }

    public static Version versionParser( String aVersionStr ) {
        Vector versionNumbers = split( aVersionStr, "." );
        Enumeration en = versionNumbers.elements();

        Version version = new Version();
        version.Major = en.hasMoreElements() ? getNumber( (String)en.nextElement() ) : 0;
        version.Minor = en.hasMoreElements() ? getNumber( (String)en.nextElement() ) : 0;
        version.Build = en.hasMoreElements() ? getNumber( (String)en.nextElement() ) : 0;

        return version;
    }

    private static int getNumber( String numberStr ) {
        if ( numberStr == null || numberStr.length() == 0 ) {
            return 0;
        } else {
            try {
                return Integer.parseInt( numberStr );
            } catch ( NumberFormatException nfex ) {
                return 0;
            }
        }
    }
}
