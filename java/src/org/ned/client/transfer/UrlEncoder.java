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
package org.ned.client.transfer;

public class UrlEncoder {

    public static String URLEncoder(String str) {
        if (str == null) return null;

        StringBuffer resultStr = new StringBuffer( str.length() );
        char tmpChar;

        for( int i=0; i<str.length(); i++ ) {
            tmpChar = str.charAt(i);
            switch( tmpChar ) {
            case ' ':
                resultStr.append( "%20" );
                break;
            case '-':
                resultStr.append( "%2D" );
                break;
            case '/':
                resultStr.append( "%2F" );
                break;
            case ':':
                resultStr.append( "%3A" );
                break;
            case '=':
                resultStr.append( "%3D" );
                break;
            case '?':
                resultStr.append( "%3F" );
                break;
            case '#':
                resultStr.append( "%23" );
                break;
            case '\r':
                resultStr.append( "%0D" );
                break;
            case '\n':
                resultStr.append( "%0A" );
                break;
            default:
                resultStr.append( tmpChar );
                break;
            }
        }
    return resultStr.toString();
    }
}
