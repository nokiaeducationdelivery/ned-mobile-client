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

public interface IDownloadTaskManager {
    public void taskCompleted( DownloadTask taskCompleted );
    public void taskCancelled( DownloadTask taskCancelled);
    public void addNewDownloadTask( DownloadTask taskNew) ;
    public void progresUpdate ( DownloadTask source);
    public void statusChanged( DownloadTask source);
}
