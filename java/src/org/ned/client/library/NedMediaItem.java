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
package org.ned.client.library;

import org.ned.client.Content;
import org.ned.client.MediaItem;


public class NedMediaItem {
    public Content content;

    public MediaItem item;
    
    public NedMediaItem( Content newContent ) {
        content = newContent;
    }
}
