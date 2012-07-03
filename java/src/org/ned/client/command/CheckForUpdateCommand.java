/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client.command;

import com.sun.lwuit.Command;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import org.ned.client.Localization;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.Utils;
import org.ned.client.utils.Version;
import org.ned.client.view.GeneralAlert;

public class CheckForUpdateCommand extends NedCommandAsync {

    private static CheckForUpdateCommand instance;

    private CheckForUpdateCommand() {
        command = new Command( NedResources.CHECK_FOR_UPDATE );
    }

    public static CheckForUpdateCommand getInstance() {
        if ( instance == null ) {
            instance = new CheckForUpdateCommand();
        }
        return instance;
    }

    protected void doAction( Object param ) {
        Hashtable headers = new Hashtable( 1 );
        headers.put( NedConsts.HttpHeader.CACHECONTROL, NedConsts.HttpHeaderValue.NOCACHE );

        String rsp = NedConnectionUtils.httpGet( NedConsts.NedUpdateAddress.CHECKFORUPDATEURL, headers ).
                trim();

        if ( rsp == null || rsp.length() == 0 ) {
            throw new AsyncException( NedResources.TRYAGAINLATER );
        } else {
            processUpdate( rsp );
        }
    }

    private void processUpdate( String rsp ) {
        Vector params = Utils.split( rsp, "\n" );

        Enumeration en = params.elements();
        String version = "";
        String url = "";
        while ( en.hasMoreElements() ) {
            String value = (String)en.nextElement();
            if ( value.startsWith( NedConsts.NedUpdateInfo.VERSION ) ) {
                version = getParamValue( value, NedConsts.NedUpdateInfo.VERSION );
            } else if ( value.startsWith( NedConsts.NedUpdateInfo.OVISTORE ) ) {
                url = getParamValue( value, NedConsts.NedUpdateInfo.OVISTORE );
            }
        }

        if ( isNewerVersion( version ) ) {
            String message = Localization.getMessage( NedResources.NEWVERSIONAVAILABLE,
                                                      new Object[]{ version } );
            if ( GeneralAlert.RESULT_YES == GeneralAlert.showQuestion( message ) ) {
                try {
                    NedMidlet.getInstance().platformRequest( NedConsts.NedUpdateAddress.INSTALATIONFILE );
                } catch ( ConnectionNotFoundException ex ) {
                    throw new AsyncException( ex.getMessage()
                                              + NedResources.TRYAGAINLATER );
                }
            }
        } else {
            throw new AsyncException( NedResources.NEWESTVERSION );
        }
    }

    private String getParamValue( String aInput, String aKeyName ) {
        if ( aKeyName == null && aKeyName.length() == 0 ) {
            return aInput;
        } else {
            return aInput.substring( aKeyName.length() + 1 );//+1 is for colon
        }
    }

    private boolean isNewerVersion( String aNewVersion ) {
        Version currentVer =
                Utils.versionParser( NedMidlet.getInstance().getVersion() );

        Version availableVer =
                Utils.versionParser( aNewVersion );

        if ( currentVer.Major < availableVer.Major ) {
            return true;
        } else if ( currentVer.Minor < availableVer.Minor ) {
            return true;
        } else if ( currentVer.Build < availableVer.Build ) {
            return true;
        }

        return false;
    }
}
