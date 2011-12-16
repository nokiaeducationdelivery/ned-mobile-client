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
package org.ned.client;

import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public interface IContent {
    String getMediaFile();
    String getText();
    String getParentId();
    String getType();
    String getId();
    String getDescription();
    Vector getExternalLinks();
}
