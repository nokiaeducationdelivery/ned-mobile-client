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
package org.ned.client.utils;

import com.sun.lwuit.Image;
import org.ned.client.NedConsts.NedContentType;
import org.ned.client.NedMidlet;


public class MediaTypeResolver {

    private static Image video;
    private static Image audio;
    private static Image picture;
    private static Image text;
    private static Image unknown;


    static {
        audio = NedMidlet.getRes().getImage( "Audio2" );
        video = NedMidlet.getRes().getImage( "Video2" );
        picture = NedMidlet.getRes().getImage( "Image2" );
        text = NedMidlet.getRes().getImage( "Text2" );
        unknown = NedMidlet.getRes().getImage( "UnknownType" );
    }

    public static boolean isVideo( String aFileName ) {
        return aFileName.toUpperCase().endsWith("MP4")
              || aFileName.toUpperCase().endsWith("MPG");
    }

    public static boolean isAudio( String aFileName ) {
        return aFileName.toUpperCase().endsWith("MP3");
    }

    public static boolean isImage( String aFileName ) {
        return aFileName.toUpperCase().endsWith("JPG")
              || aFileName.toUpperCase().endsWith("PNG")
              || aFileName.toUpperCase().endsWith("JPEG");
    }

    public static boolean isText( String aFileName ) {
         return aFileName.toUpperCase().endsWith("TXT")
               || aFileName.toUpperCase().endsWith("HTM")
               || aFileName.toUpperCase().endsWith("HTML")
               || aFileName.toUpperCase().endsWith("RTF");
    }

    public static boolean isOldMedia( String aFileName ) {
        return isVideo(aFileName) || isAudio(aFileName) || isImage(aFileName);
    }

    public static Image getTypeIcon( String aType ) {
        if ( NedContentType.AUDIO.equals( aType ) ) {
            return audio;
        } else if ( NedContentType.VIDEO.equals( aType ) ) {
            return video;
        } else if ( NedContentType.TEXT.equals( aType ) ) {
            return text;
        } else if ( NedContentType.IMAGE.equals( aType ) ) {
            return picture;
        } else {
            return unknown;
        }
    }
}
