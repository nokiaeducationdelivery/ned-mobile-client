package org.ned.client.statistics;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.transfer.StatisticsUploader;

public class StatisticsManager {

    public static final int COMMIT_TRIGER = 10;

    private static StatisticsManager mInstance;

    private Vector/*<StatEntry>*/ mStats;
    private String mUserCatalog;

    private StatisticsManager( String aUserCatalog ) {
        mUserCatalog = aUserCatalog;
        mStats = new Vector();
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open( aUserCatalog + NedConsts.NedLocalConst.STATS_FILE,
                                                  Connector.READ_WRITE );
            if ( !fc.exists() ) {
                fc.create();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if( fc != null ) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void init( String aUserCatalog ) {
        if( mInstance != null ) {
            mInstance.save();
            mInstance = null;
        }
        mInstance =  new StatisticsManager( aUserCatalog );
    }

    protected void finalize () {
        if( mInstance != null ) {
            mInstance.save();
            mInstance = null;
        }
    }

    public static void logEvent( int aEvent, String aDetails ) {
        if ( mInstance != null ) {
            mInstance.mStats.addElement( new StatEntry( aEvent, aDetails ) );
            if ( mInstance.mStats.size() >= COMMIT_TRIGER ) {
                mInstance.save();
            }
        }
    }

    public static void dispose() {
        if( mInstance != null ) {
            mInstance.save();
            mInstance = null;
        }
    }

    public static void uploadStats( boolean aSilent ) {
        if( StatisticsManager.isStatsChanged() ) {
            StatisticsUploader uploader = new StatisticsUploader( mInstance.mUserCatalog + NedConsts.NedLocalConst.STATS_FILE, aSilent );
            uploader.start();
        }
    }

    public static boolean isStatsChanged() {
        if( mInstance == null ) {
            return false;
        }
        mInstance.save();
        boolean changed = false;
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open( mInstance.mUserCatalog + NedConsts.NedLocalConst.STATS_FILE,
                                                  Connector.READ_WRITE );
            if ( fc.exists() && fc.fileSize() > 0 ) {
                changed = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if( fc != null ) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
        return changed;
    }

    private void save() {
        FileConnection fc = null;
        PrintStream writer = null;
        try {
            fc = (FileConnection) Connector.open( mUserCatalog + NedConsts.NedLocalConst.STATS_FILE,
                                                  Connector.READ_WRITE );
            if ( !fc.exists() ) {
                fc.create();
            }
            writer = new PrintStream(  fc.openOutputStream( fc.fileSize() ) );
            write( writer );
            writer.flush();
            mInstance.mStats.removeAllElements();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if ( writer != null ) {
                writer.close();
            }
            if ( fc != null ) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void write( PrintStream aWriter ) throws IOException {
        for ( int i =0; i< mStats.size(); i++ ) {
            StatEntry entry = (StatEntry)mStats.elementAt(i);
            entry.save( aWriter );
        }
    }
}
