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
package org.ned.client.view;

import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedResources;
import org.ned.client.command.BackStatisiticsCommand;
import org.ned.client.command.HelpCommand;
import org.ned.client.command.UploadStatisticsCommand;
import org.ned.client.statistics.StatType;
import org.ned.client.utils.MediaTypeResolver;
import org.ned.client.utils.NedIOUtils;


public class StatisticsScreen extends NedFormBase implements ActionListener{

    private static final int ICON_FIT_SIZE = 36;

    private Container mCenterContainer;

    public StatisticsScreen() {
        super();

        setNedTitle(NedResources.STAT_TITLE);
        setLayout(new BorderLayout());
        mCenterContainer = new Container( new BoxLayout( BoxLayout.Y_AXIS ) );
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open( NedIOUtils.getUserRootDirectory() + NedConsts.NedLocalConst.STATS_FILE,
                                                  Connector.READ );
            if( fc.exists() )  {
                updateStatistics( fc );
            } else {
                
            }
        } catch ( IOException ex ) {

        } finally {
            if ( fc != null ) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }

        addCommand(BackStatisiticsCommand.getInstance().getCommand());
        //addCommand(HelpCommand.getInstance().getCommand());
        addCommand(UploadStatisticsCommand.getInstance().getCommand());

        addComponent( BorderLayout.CENTER, mCenterContainer );
        addCommandListener(this);
    }

    private void updateStatistics( FileConnection aFc ) {
        DataInputStream dos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int data;
        try {
            dos = aFc.openDataInputStream();
            data = dos.read();
            while (data != -1) {
                baos.write((byte) data);
                data = dos.read();
            }
            parseStatistics( baos.toString() );

        } catch (IOException ex) {
        } finally {
            try {
                baos.close();
            } catch (IOException ex) {
            }
            try {
                dos.close();
            } catch (IOException ex) {
            }
        }
    }

    private void parseStatistics( String aStats )
    {
        if ( aStats == null ) {
            return;
        }

        int start = 0;
        int end = -1;
        end = aStats.indexOf( "\n" );

        String entry = null;
        while( end != -1 ) {
            entry = aStats.substring(start, end );
            start = end + 1;
            if ( entry.indexOf( StatType.type2String( StatType.PLAY_ITEM_START ) ) > -1 ) {
                int dataStart = entry.indexOf(",");
                int dateEnd = entry.indexOf( ",", dataStart + 1 );
                parseEntry( entry.substring( dataStart +1 ,dateEnd ) );
            }
            end = aStats.indexOf("\n", end + 1);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();

        if( src == BackStatisiticsCommand.getInstance().getCommand() ) {
            BackStatisiticsCommand.getInstance().execute(null);
        } else if( src == UploadStatisticsCommand.getInstance().getCommand() ) {
            UploadStatisticsCommand.getInstance().execute(null);
        } else if ( src == HelpCommand.getInstance().getCommand() ) {
            HelpCommand.getInstance().execute( this.getClass() );
        }
    }

    private void parseEntry( String aEntry ) {
        int idEnd = aEntry.indexOf(";");
        String id = aEntry.substring(0, idEnd );
        int indexEq = id.indexOf("=");
        id = id.substring( indexEq + 1, idEnd );

        int typeEnd = aEntry.indexOf( ";", idEnd + 1 );
        String type = aEntry.substring( idEnd + 1, typeEnd );
        indexEq = type.indexOf("=");
        type = type.substring( indexEq + 1 );

        int titleEnd = aEntry.indexOf( ";", typeEnd + 1 );
        String title = aEntry.substring( typeEnd + 1, titleEnd );
        indexEq = title.indexOf("=");
        title = title.substring( indexEq + 1 );

        Container con = new Container( new BoxLayout(BoxLayout.X_AXIS) );
        con.setPreferredH( ICON_FIT_SIZE );

        con.addComponent( new Label( MediaTypeResolver.getTypeIcon(type) ) );
        con.addComponent( new Label( title ) );
        con.addComponent( new Label( "[" + id+ "]"));

        mCenterContainer.addComponent( con );
    }
}
