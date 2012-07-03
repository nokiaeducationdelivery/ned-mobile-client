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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import org.ned.client.NedConsts;
import org.ned.client.NedMidlet;
import org.ned.client.NedResources;
import org.ned.client.utils.NedConnectionUtils;
import org.ned.client.utils.UnauthorizedLibraryUsageException;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.LoginOnLineScreen;
import org.ned.client.view.StatisticsScreen;
import org.ned.client.view.WaitingScreen;

public class StatisticsUploader implements Runnable {

    private static boolean mIsSending = false;

    private String urlServlet = null;
    private HttpConnection httpConn = null;
    private DataOutputStream httpOutput = null;
    private DataInputStream httpInput = null;
    private boolean stop = false;
    private String mStatFile;
    private boolean mSupressLogin;

    public StatisticsUploader( String aPath, boolean supressLogin ) {
        urlServlet = NedMidlet.getAccountManager().getStatisticsServletUri();
        mStatFile = aPath;
        mSupressLogin = supressLogin;
    }

    public void cancel() {
        stop = true;
    }

    public void submitToServer( ) throws IOException, SecurityException, UnauthorizedLibraryUsageException {
        if ( !stop && mStatFile!= null && mStatFile.length() > 0 )
        {
            httpConn = (HttpConnection)Connector.open( urlServlet );
            httpConn.setRequestMethod( HttpConnection.POST );
            httpConn.setRequestProperty( "Username", NedMidlet.getAccountManager().getCurrentUser().login );
            httpConn.setRequestProperty( "DeviceId", NedMidlet.getInstance().getImei() );
            NedConnectionUtils.addCredentialsToConnection( httpConn,
                                                           NedMidlet.getAccountManager().getCurrentUser().login,
                                                           NedMidlet.getAccountManager().getCurrentUser().password );
            
            httpOutput = httpConn.openDataOutputStream();

            String buffer = loadFile( mStatFile );
           
            if ( !stop && buffer != null ) {
                submitFile( buffer );

                if ( httpConn.getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED ) {
                    throw new UnauthorizedLibraryUsageException();
                }

                httpInput = new DataInputStream( httpConn.openDataInputStream() );
                if( httpInput != null ) {
                    int status = httpInput.readInt();
                    if ( status == NedConsts.NedStatUploadConsts.NEWSTATS
                      || status == NedConsts.NedStatUploadConsts.STATSUPDATED ) {
                        deleteLocalStats();
                    }
                }
            }
        }
    }

    private void finalizeTransmission() {
        if (httpOutput != null) {
            try {
                httpOutput.close();
            } catch (IOException ex) {
            }
        }
        if (httpInput != null) {
            try {
                httpInput.close();
            } catch (IOException ex) {
            }
        }
        if (httpConn != null) {
            try {
                httpConn.close();
            } catch (IOException ex) {
            }
        }
        mIsSending = false;
    }

    private boolean submitFile(String buffer)  {
        boolean success= false;
        try {
            byte[] byteBuf = buffer.getBytes();
            httpOutput.write(byteBuf, 0, byteBuf.length);
            httpOutput.flush();
            success = true;
        } catch (IOException ioe) {
            success = false;
        }
        return success;
    }

    private String loadFile(String _filename) {
        ByteArrayOutputStream baos = null;
        FileConnection fc = null;
        DataInputStream dos = null;
        String result = null;

        try {
            fc = (FileConnection) Connector.open(_filename);
            dos = fc.openDataInputStream();
            baos = new ByteArrayOutputStream();
            int data = dos.read();

            while (data != -1) {
                baos.write((byte) data);
                data = dos.read();
            }

            result = baos.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex) {
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                }
            }
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException ex) {
                }
            }
        }
        return result;
    }

    public void start() {
        if( !mIsSending ) {
            mIsSending = true;
            try {
                Thread t = new Thread( this );
                t.setPriority( Thread.MIN_PRIORITY );
                t.start();
            } catch ( IllegalThreadStateException ex ) {
                mIsSending = false;
            } catch ( SecurityException ex ) {
              //  GeneralAlert.show( ex.getMessage(), GeneralAlert.ERROR ); TODO this may mess watiting screen
            }
        }
    }

    public void run() {
        try {
            submitToServer();
        } catch ( UnauthorizedLibraryUsageException ex ) {
            WaitingScreen.dispose();
            if ( !mSupressLogin
                && GeneralAlert.showQuestion(NedResources.LOGIN_AGAIN ) == GeneralAlert.RESULT_YES ) {
                    new LoginOnLineScreen( StatisticsScreen.class ).show();
            }
        } catch (Exception ex) {
        } finally {
            finalizeTransmission();
        }
    }

    private void deleteLocalStats() {
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open( mStatFile );
            fc.delete();
        } catch ( Exception ex ) {
        } finally {
            if( fc != null ) {
                try {
                    fc.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }
}
