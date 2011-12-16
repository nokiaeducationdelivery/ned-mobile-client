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
package org.ned.client.statistics;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;


public class StatEntry {

    private int mEventType;
    private Date mTime;
    private String mDetails;

    public StatEntry( int aEvent, String aDetails ) {
        mTime = new Date();
        mEventType = aEvent;
        mDetails = aDetails;
    }

    public void save( PrintStream aWriter ) throws IOException {
        aWriter.println( StatType.type2String(mEventType) + "," + mDetails + "," + mTime.toString() );
    }
}
