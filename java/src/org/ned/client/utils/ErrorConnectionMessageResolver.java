package org.ned.client.utils;

import org.ned.client.NedConsts.LoginError;
import org.ned.client.NedResources;
import org.ned.client.view.GeneralAlert;


public class ErrorConnectionMessageResolver {

    public static void showErrorMessage( int error ) {
        switch ( error ) {
            case LoginError.CONNECTIONERROR:
                GeneralAlert.show( NedResources.CONNECTIONERROR, GeneralAlert.WARNING );
                break;
            case LoginError.LOCALSECURITY:
                GeneralAlert.show( NedResources.LOCALSECURITY, GeneralAlert.WARNING );
                break;
            case LoginError.UNAUTHORIZED:
                GeneralAlert.show( NedResources.BAD_LOGIN, GeneralAlert.WARNING );
                break;
            case LoginError.ABORTED:
                GeneralAlert.show( NedResources.ABORTED, GeneralAlert.WARNING );
                break;
            case LoginError.OTHERCONNECTIONPROBLEM:
                GeneralAlert.show( NedResources.OTHERCONNECTIONPROBLEM, GeneralAlert.WARNING );
                break;
            case LoginError.UNKNOWN:
            default:
                GeneralAlert.show( NedResources.UNKNOWN, GeneralAlert.WARNING );
                break;
        }
    }
}
