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


public class NedCatalog {

    public Content contents;
    public String catalogDir;

    public NedCategory currentCategory;

    public NedCatalog( Content con ) {
        contents = con;
    }
}
