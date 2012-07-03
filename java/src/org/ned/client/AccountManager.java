/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.ned.client.NedConsts.LoginError;
import org.ned.client.statistics.StatisticsManager;
import org.ned.client.utils.*;
import org.ned.client.view.GeneralAlert;
import org.ned.client.view.WaitingScreen;

public class AccountManager {

    private String serverAddress = null;
    private Vector users = new Vector();
    private UserInfo currentUser = null;
    private LanguageInfo currentLanguage;

    public void setServer( String url ) {
        serverAddress = url;
        saveSetup();
    }

    private int TryLoginToServer( String login, String password, int retval ) {
        WaitingScreen.show( NedResources.CONNECTING );
        retval = loginToServer( login, password );
        WaitingScreen.dispose();
        if ( retval != LoginError.SUCCESS ) {
            ErrorConnectionMessageResolver.showErrorMessage( retval );
        }
        return retval;
    }

    public int getUsersCount() {
        return users.size();
    }

    public class UserInfo {

        public UserInfo( String login, String password, boolean save ) {
            this.login = login;
            this.password = password;
            this.isPassSaved = save;
        }
        public String login = null;
        public String password = null;
        public boolean isPassSaved = false;
    }

    public AccountManager() {
        readSetup();
    }

    public String getServerUrl() {
        return serverAddress;
    }

    public String getContentServletUri() {
        return serverAddress + NedConsts.NedRemotePath.BASEPATH
                + NedConsts.NedRemotePath.GETXMLSERVLET;
    }

    public String getMotdServletUri() {
        return serverAddress + NedConsts.NedRemotePath.BASEPATH
                + NedConsts.NedRemotePath.MOTDSERVLET;
    }

    public String getStatisticsServletUri() {
        return serverAddress + NedConsts.NedRemotePath.BASEPATH
                + NedConsts.NedRemotePath.STAT_UPLOAD;
    }

    public String getLoginServletUri() {
        return serverAddress + NedConsts.NedRemotePath.BASEPATH
                + NedConsts.NedRemotePath.LOGIN;
    }

    public String getLocalizationUri() {
        return serverAddress + NedConsts.NedRemotePath.BASEPATH
                + NedConsts.NedRemotePath.LOCALIZATION;
    }

    private void readSetup() {
        if ( NedIOUtils.fileExists( NedIOUtils.getAccountsFile() ) ) {
            Element rootElement = null;
            try {
                Document doc = NedXmlUtils.getDocFile( NedIOUtils.
                        getAccountsFile() );
                if ( doc == null ) {
                    NedIOUtils.removeFile( NedIOUtils.getAccountsFile() );
                    return;
                } else {
                    rootElement = doc.getRootElement();
                }
            } catch ( Exception ex ) {
                GeneralAlert.show( "load accounts error: " + ex.getMessage(), GeneralAlert.ERROR );
            }
            for ( int i = 0; i < rootElement.getChildCount(); i++ ) {
                if ( rootElement.getType( i ) == Node.ELEMENT ) {
                    Element element = rootElement.getElement( i );
                    if ( element.getName().equals( NedConsts.NedXmlTag.USER ) ) {
                        String username = element.getAttributeValue( "", NedConsts.NedXmlAttribute.LOGIN );
                        String password = element.getAttributeValue( "", NedConsts.NedXmlAttribute.PASSWORD );
                        String strSave = element.getAttributeValue( "", NedConsts.NedXmlAttribute.SAVE_PASS );
                        boolean bSave = false;
                        if ( strSave != null && strSave.equals( "yes" ) ) {
                            bSave = true;
                        }
                        users.addElement( new UserInfo( username, password, bSave ) );
                    } else if ( element.getName().equals( NedConsts.NedXmlTag.SERVER ) ) {
                        serverAddress = element.getText( 0 );
                    } else if ( (element.getName().equals( NedConsts.NedXmlTag.LAST_USER )) ) {
                        currentUser = findUser( element.getAttributeValue( "", NedConsts.NedXmlAttribute.LOGIN ) );
                    } else if ( element.getName().equals( "Language" ) ) {
                        currentLanguage = new LanguageInfo( element.
                                getAttributeValue( "", "langName" ),
                                                            element.
                                getAttributeValue( "", "locale" ), true );
                    }
                }
            }
        }
    }

    public void saveSetup() {
        Document doc = new Document();
        Element accountSetup = doc.createElement( "", "AccountSetup" );

        Element server = doc.createElement( "", NedConsts.NedXmlTag.SERVER );
        server.addChild( Node.TEXT, serverAddress );

        accountSetup.addChild( Node.ELEMENT, server );

        for ( int i = 0; i < users.size(); i++ ) {
            Element user = accountSetup.createElement( "", NedConsts.NedXmlTag.USER );
            user.setAttribute( "", NedConsts.NedXmlAttribute.LOGIN, ((UserInfo)users.
                    elementAt( i )).login );
            user.setAttribute( "", NedConsts.NedXmlAttribute.PASSWORD, ((UserInfo)users.
                    elementAt( i )).password );
            user.setAttribute( "", NedConsts.NedXmlAttribute.PASSWORD, ((UserInfo)users.
                    elementAt( i )).password );
            user.setAttribute( "", NedConsts.NedXmlAttribute.SAVE_PASS, ((UserInfo)users.
                    elementAt( i )).isPassSaved ? "yes" : "no" );
            accountSetup.addChild( Node.ELEMENT, user );
        }
        doc.addChild( Node.ELEMENT, accountSetup );
        if ( currentUser != null ) {
            Element lastUser = accountSetup.createElement( "", NedConsts.NedXmlTag.LAST_USER );
            lastUser.setAttribute( "", NedConsts.NedXmlAttribute.LOGIN, currentUser.login );
            accountSetup.addChild( Node.ELEMENT, lastUser );
        }

        if ( currentLanguage != null ) {
            Element language = doc.createElement( "", "Language" );
            language.setAttribute( "", "langName", currentLanguage.getLangName() );
            language.setAttribute( "", "locale", currentLanguage.getLocale() );
            accountSetup.addChild( Node.ELEMENT, language );
        }

        NedXmlUtils.writeXmlFile( NedIOUtils.getAccountsFile(), doc );
    }

    public int login( String login, String password ) {
        int retval = LoginError.UNKNOWN;
        UserInfo user = findUser( login );
        if ( user == null ) {
            retval = TryLoginToServer( login, password, retval );
        } else if ( user.password.equals( password ) ) {
            currentUser = user;
            saveSetup();
            retval = LoginError.SUCCESS;
        } else {
            if ( GeneralAlert.showQuestion( NedResources.LOGIN_ONLINE )
                    == GeneralAlert.RESULT_YES ) {
                try {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex ) {
                    ex.printStackTrace();
                }
                retval = TryLoginToServer( login, password, retval );
            } else {
                retval = LoginError.ABORTED;// for concurrency purpose
            }
        }
        return retval;
    }

    public void savePassword( String userName, boolean bSave ) {
        UserInfo user = findUser( userName );
        user.isPassSaved = bSave;
        saveSetup();
    }

    public int loginToServer( String login, String password ) {
        int retval = LoginError.UNKNOWN;
        DataOutputStream httpOutput = null;
        HttpConnection httpConn = null;
        try {
            httpConn = (HttpConnection)Connector.open( getLoginServletUri() );
            httpConn.setRequestMethod( HttpConnection.GET );
            httpConn.setRequestProperty( NedConsts.HttpHeader.CACHECONTROL, NedConsts.HttpHeaderValue.NOCACHE );
            NedConnectionUtils.addCredentialsToConnection( httpConn, login, password );
            httpOutput = httpConn.openDataOutputStream();
            int rspCode = httpConn.getResponseCode();

            if ( rspCode == HttpConnection.HTTP_OK ) {
                // exchange old user for new if exists
                UserInfo user = findUser( login );
                if ( user != null ) {
                    users.removeElement( user );
                    saveSetup();
                }
                user = new UserInfo( login, password, false );
                users.addElement( user );
                currentUser = user;
                NedIOUtils.createDirectory( NedIOUtils.getUserRootDirectory() );
                saveSetup();
                retval = LoginError.SUCCESS;
                if ( NedMidlet.getSettingsManager().getAutoStatSend() ) {
                    StatisticsManager.uploadStats( true );
                }
                MotdManager.getInstance().updateMotd();
            } else if ( rspCode == HttpConnection.HTTP_UNAUTHORIZED ) {
                retval = LoginError.UNAUTHORIZED;
            }
        } catch ( ConnectionNotFoundException cex ) {
            retval = LoginError.CONNECTIONERROR;
        } catch ( IOException ioex ) {
            if ( "-3".equals( ioex.getMessage() ) ) {
                retval = LoginError.ABORTED;
            } else {
                retval = LoginError.OTHERCONNECTIONPROBLEM;
            }
            ioex.printStackTrace();
        } catch ( SecurityException sec ) {
            retval = LoginError.LOCALSECURITY;
            sec.printStackTrace();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            retval = LoginError.UNKNOWN;
        } finally {
            try {
                if ( httpOutput != null ) {
                    httpOutput.close();
                }
                if ( httpConn != null ) {
                    httpConn.close();
                }
            } catch ( IOException ex ) {
            }
        }
        return retval;
    }

    public void changePassword( String newPasword ) {
        String currentUser = NedMidlet.getSettingsManager().getUser();
        UserInfo checkUser = findUser( currentUser );

        if ( checkUser != null ) {
            users.removeElement( checkUser );
            users.addElement( new UserInfo( checkUser.login, newPasword, false ) );
            saveSetup();
            //todo push to server
        }
    }

    public void removeUser( String user ) {
        if ( user != null ) {
            UserInfo removeUser = findUser( user );
            users.removeElement( removeUser );
            saveSetup();
            //todo push to server
        }
    }

    public UserInfo findUser( String login ) {
        UserInfo retval = null;
        for ( int i = 0; i
                < users.size(); i++ ) {
            if ( ((UserInfo)users.elementAt( i )).login.equals( login ) ) {
                retval = (UserInfo)users.elementAt( i );
            }
        }
        return retval;
    }

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public LanguageInfo getLanguage() {
        return currentLanguage;
    }

    public void setLanguage( LanguageInfo language ) {
        currentLanguage = language;
    }
}
