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
